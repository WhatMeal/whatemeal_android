package com.beside.whatmeal.foodlist.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.beside.whatmeal.common.progress.CommonProgressViewModel
import com.beside.whatmeal.data.remote.WhatMealRemoteDataSourceImpl
import com.beside.whatmeal.data.remote.model.Food
import com.beside.whatmeal.data.remote.model.request.GetFoodListRequest
import com.beside.whatmeal.foodlist.FoodListActivity
import com.beside.whatmeal.foodlist.FoodListViewActionHandler
import com.beside.whatmeal.foodlist.uimodel.FoodListFirstLoadingState
import com.beside.whatmeal.foodlist.uimodel.FoodListPagingState
import com.beside.whatmeal.foodlist.uimodel.FoodListViewAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

// @TODO: Please add unit test for it.
@SuppressLint("NullSafeMutableLiveData")
class FoodListViewModel(
    savedStateHandle: SavedStateHandle,
    private val actionHandler: FoodListViewActionHandler
) : CommonProgressViewModel(), FoodListViewModelInterface {
    private val coroutineScope: CoroutineScope = viewModelScope

    private val mutableFoodListFirstLoadingState: MediatorLiveData<FoodListFirstLoadingState> =
        MediatorLiveData<FoodListFirstLoadingState>().apply {
            addSource(progressFinishEvent) { value = getFoodListFirstLoadingState() }
        }
    val foodListFirstLoadingState: LiveData<FoodListFirstLoadingState> =
        mutableFoodListFirstLoadingState

    private val mutableFoodList: MutableLiveData<List<Food>> = MutableLiveData(listOf())
    override val foodList: LiveData<List<Food>> = mutableFoodList

    private val mutablePagingState: MutableLiveData<FoodListPagingState> =
        MutableLiveData(FoodListPagingState.LOADING)
    override val pagingState: LiveData<FoodListPagingState> = mutablePagingState

    private val mutableSelectedFood: MutableLiveData<Food> = MutableLiveData()
    override val selectedFood: LiveData<Food> = mutableSelectedFood

    // @TODO: Remove it.
    private var dummyIndex: Int = 0

    private fun getFoodListFirstLoadingState(): FoodListFirstLoadingState =
        if (progressFinishEvent.value != null) {
            FoodListFirstLoadingState.DONE
        } else {
            FoodListFirstLoadingState.LOADING
        }

    init {
        loadFoodListFromRemote(savedStateHandle)
    }

    private fun loadFoodListFromRemote(savedStateHandle: SavedStateHandle) = coroutineScope.launch {
        mutableFoodListFirstLoadingState.value = FoodListFirstLoadingState.LOADING
        startAutoIncrementProgress(timeMillis = 1000L, onFinished = ::onProgressFinished)

        val request = createGetFoodListRequestBy(savedStateHandle)
        WhatMealRemoteDataSourceImpl.getFirstFoodList(request).onSuccess {
            // @TODO: Not implemented yet.
            //      mutableFoodList.value = it.food
            //      mutablePagingState.value =
            //          if (it.hasNext) FoodListPagingState.HAS_NEXT else FoodListPagingState.NO_NEXT

            Log.d(TAG, "Success to loadFoodListFromRemote")

            mutableFoodList.value = DUMMY_FOOD_LIST[dummyIndex++]
            mutablePagingState.value = FoodListPagingState.HAS_NEXT
            mutableIsTaskFinished.value = true
        }.onFailure {
            Log.e(TAG, "Fail to loadFoodListFromRemote", it)
            actionHandler.postFoodListViewAction(FoodListViewAction.FailToLoadFoodListOnFirst)
            stopAutoIncrementProgress()
        }
    }

    private fun onProgressFinished() {
        mutableFoodListFirstLoadingState.value = FoodListFirstLoadingState.DONE
    }

    override fun onRefreshClick() {
        if (pagingState.value != FoodListPagingState.HAS_NEXT) {
            return
        }
        mutablePagingState.value = FoodListPagingState.LOADING
        loadNextPageOfFoodListFromRemote()
    }

    // @TODO: Please consider whether this is presenting logic.
    private fun loadNextPageOfFoodListFromRemote() = coroutineScope.launch {
        WhatMealRemoteDataSourceImpl.getNextFoodList().onSuccess {
            // @TODO: Not implemented yet.
            //      mutableFoodList.value = it.food
            //      mutablePagingState.value =
            //          if (it.hasNext) FoodListPagingState.HAS_NEXT else FoodListPagingState.NO_NEXT

            Log.d(TAG, "Success to loadNextPageOfFoodListFromRemote")

            mutableFoodList.value = DUMMY_FOOD_LIST[dummyIndex++]
            mutablePagingState.value = if (dummyIndex == DUMMY_FOOD_LIST.size) {
                FoodListPagingState.NO_NEXT
            } else {
                FoodListPagingState.HAS_NEXT
            }
            mutableSelectedFood.value = null
        }.onFailure {
            Log.e(TAG, "Fail to loadNextPageOfFoodListFromRemote", it)
            mutablePagingState.value = FoodListPagingState.HAS_NEXT
        }
    }

    override fun onFoodSelect(food: Food) {
        if (selectedFood.value == food) {
            mutableSelectedFood.value = null
        } else {
            mutableSelectedFood.value = food
        }
    }

    override fun onNextClick() {
        actionHandler.postFoodListViewAction(
            FoodListViewAction.StartMapScreenAction(
                selectedFood.value?.name
                    ?: throw IllegalStateException("selectedFood can't not be null in this time")
            )
        )
    }

    private fun createGetFoodListRequestBy(savedState: SavedStateHandle): GetFoodListRequest =
        GetFoodListRequest(
            savedState.get<String>(INTENT_PARAM_BASICS) ?: throw IllegalArgumentException(),
            savedState.get<String>(INTENT_PARAM_SOUP) ?: throw IllegalArgumentException(),
            savedState.get<String>(INTENT_PARAM_COOKS) ?: throw IllegalArgumentException(),
            savedState.get<String>(INTENT_PARAM_INGREDIENTS) ?: throw IllegalArgumentException(),
            savedState.get<String>(INTENT_PARAM_STATES) ?: throw IllegalArgumentException()
        )

    class Factory(
        private val actionHandler: FoodListViewActionHandler,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T = FoodListViewModel(handle, actionHandler) as T
    }

    companion object {
        private const val TAG = "FoodListViewModel"

        private const val INTENT_PARAM_BASICS = "basics"
        private const val INTENT_PARAM_SOUP = "soup"
        private const val INTENT_PARAM_COOKS = "cooks"
        private const val INTENT_PARAM_INGREDIENTS = "ingredients"
        private const val INTENT_PARAM_STATES = "states"

        fun createIntent(
            context: Context,
            basics: String,
            soup: String,
            cooks: String,
            ingredients: String,
            states: String
        ): Intent = Intent(context, FoodListActivity::class.java).putExtras(
            bundleOf(
                INTENT_PARAM_BASICS to basics,
                INTENT_PARAM_SOUP to soup,
                INTENT_PARAM_COOKS to cooks,
                INTENT_PARAM_INGREDIENTS to ingredients,
                INTENT_PARAM_STATES to states
            ).also { Log.i(TAG, "createIntent bundle: $it") }
        )

        val DUMMY_FOOD_LIST: List<List<Food>> = listOf(
            listOf(
                Food(
                    "치킨",
                    "https://cdn.eyesmag.com/content/uploads/posts/2021/03/10/main-aecd9fb4-f0b1-4115-94af-b87daaf34f33.jpg"
                ),
                Food(
                    "피자",
                    "https://img.hankyung.com/photo/202108/99.26501439.1-1200x.jpg"
                ),
                Food(
                    "족발",
                    "https://static.hubzum.zumst.com/hubzum/2019/07/26/11/8291a05e16b14e9b91eedc7a4375c934_780x585.jpg"
                ),
                Food(
                    "보쌈",
                    "https://recipe1.ezmember.co.kr/cache/recipe/2015/11/04/2822d0962875bb52265b4e90ca1d92ae1.jpg"
                ),
                Food(
                    "돼지고기",
                    "https://img.etoday.co.kr/pto_db/2019/07/600/20190726164728_1350748_727_485.jpg"
                )
            ),
            listOf(
                Food(
                    "짜장면",
                    "https://recipe1.ezmember.co.kr/cache/recipe/2020/06/04/d96e1e81ecc8d86c922d486ec6eec4da1.jpg"
                ),
                Food(
                    "짬뽕",
                    "https://recipe1.ezmember.co.kr/cache/recipe/2017/06/19/2756808e5603db7a18c4f5ee9a699ee41.jpg"
                ),
                Food(
                    "로제 떡볶이",
                    "http://storage.enuri.info/pic_upload/knowbox2/202107/050513263202107082eb27e43-aedc-4275-9af3-f0b50c6f30f8.JPEG"
                ),
                Food(
                    "파스타",
                    "https://blog.kakaocdn.net/dn/cK76SU/btqRCV7FVkE/wKt15KOhO4Mt1ibHq7j430/img.jpg"
                ),
                Food(
                    "라면",
                    "https://mblogthumb-phinf.pstatic.net/MjAyMDA1MjZfNDMg/MDAxNTkwNDgxOTc1OTE1.rpezhYUxGHG0X6-dmVwuACnqm7AugH9CUjxEatcVNsAg.C27glH_kXPk5zTORLyjUbU_yjkGDEbmwZXjaq_xGltIg.JPEG.naverschool/%ED%98%B8%EB%A1%9C%EB%A1%9C%EB%A1%9D.jpg?type=w800"
                )
            ),
            listOf(
                Food(
                    "탕수육",
                    "https://homecuisine.co.kr/files/attach/images/142/737/002/969e9f7dc60d42510c5c0353a58ba701.JPG"
                ),
                Food(
                    "커리",
                    "https://post-phinf.pstatic.net/MjAxNzAyMDhfMTk1/MDAxNDg2NTMxOTAzOTEw.OEsmL1LoHpngnkOBuIJYlg12M5TixHTj_ToPi9Ca-xog.Kan-5YVw8EuwUiLpIJ9MKAwmti8lO6GXeeHq7W0rj94g.JPEG/18.jpg?type=w1200"
                ),
                Food(
                    "쌀국수",
                    "https://ww.namu.la/s/6a370f38311ee224f8ad355ecda68a394b89b844c2c31ec7a6fc3b6dd98ad9094df30565ae25bc24b837aed11cbe92e05273d58a3bcf1c8eb6cf36644e6f8298978ec6ac96b0d1bfca26bded65df8402"
                ),
                Food(
                    "초밥",
                    "https://rimage.gnst.jp/livejapan.com/public/article/detail/a/00/00/a0000370/img/basic/a0000370_main.jpg?20201002142956&q=80&rw=750&rh=536"
                ),
                Food(
                    "찜닭",
                    "https://static.wtable.co.kr/image-resize/production/service/recipe/235/4x3/7c2b5692-bf30-474b-b056-48a2827cbded.jpg"
                )
            )
        )
    }
}