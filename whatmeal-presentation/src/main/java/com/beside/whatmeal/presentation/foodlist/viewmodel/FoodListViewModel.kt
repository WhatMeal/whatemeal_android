package com.beside.whatmeal.presentation.foodlist.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.beside.whatmeal.presentation.common.view.progress.CommonProgressViewModel
import com.beside.whatmeal.presentation.common.MutableOneShotEvent
import com.beside.whatmeal.presentation.common.OneShotEvent
import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import com.beside.whatmeal.presentation.common.getOrThrow
import com.beside.whatmeal.presentation.foodlist.uimodel.*
import com.beside.whatmeal.presentation.foodlist.view.FoodListActivity
import com.beside.whatmeal.presentation.main.uimodel.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

// @TODO: Please add unit test for it.
@SuppressLint("NullSafeMutableLiveData")
class FoodListViewModel(
    private val whatMealBo: WhatMealBoDelegator,
    savedStateHandle: SavedStateHandle
) : CommonProgressViewModel() {
    private val coroutineScope: CoroutineScope = viewModelScope

    private val mutableFoodListViewAction: MutableOneShotEvent<FoodListViewAction> =
        MutableOneShotEvent()
    val foodListViewAction: OneShotEvent<FoodListViewAction> = mutableFoodListViewAction

    private val mutableFoodListFirstLoadingState: MediatorLiveData<FoodListFirstLoadingState> =
        MediatorLiveData<FoodListFirstLoadingState>().apply {
            addSource(progressFinishEvent) { value = getFoodListFirstLoadingState() }
        }
    val foodListFirstLoadingState: LiveData<FoodListFirstLoadingState> =
        mutableFoodListFirstLoadingState

    private val mutableFoodItemList: MutableLiveData<List<FoodItem>> = MutableLiveData(listOf())
    val foodItemList: LiveData<List<FoodItem>> = mutableFoodItemList

    private val mutablePagingState: MutableLiveData<FoodListPagingState> =
        MutableLiveData(FoodListPagingState.LOADING)
    val pagingState: LiveData<FoodListPagingState> = mutablePagingState

    private val mutableSelectedFoodItem: MutableLiveData<FoodItem> = MutableLiveData()
    val selectedFoodItem: LiveData<FoodItem> = mutableSelectedFoodItem

    private lateinit var pagingItem: FoodListPagingItem

    init {
        loadFoodListFromRemote(savedStateHandle)
    }

    fun onRefreshClick() {
        if (pagingState.value == FoodListPagingState.LOADING) {
            return
        }
        mutablePagingState.value = FoodListPagingState.LOADING
        mutableSelectedFoodItem.value = null
        loadNextPageOfFoodListFromRemote()
    }

    fun onFoodSelect(foodItem: FoodItem) {
        if (selectedFoodItem.value == foodItem) {
            mutableSelectedFoodItem.value = null
        } else {
            mutableSelectedFoodItem.value = foodItem
        }
    }

    fun onNextClick() {
        mutableFoodListViewAction.post(
            FoodListViewAction.StartMapScreen(
                selectedFoodItem.value?.name
                    ?: throw IllegalStateException("selectedFood can't not be null in this time")
            )
        )
    }

    private fun getFoodListFirstLoadingState(): FoodListFirstLoadingState =
        if (progressFinishEvent.value != null) {
            FoodListFirstLoadingState.DONE
        } else {
            FoodListFirstLoadingState.LOADING
        }

    private fun loadFoodListFromRemote(savedStateHandle: SavedStateHandle) = coroutineScope.launch {
        mutableFoodListFirstLoadingState.value = FoodListFirstLoadingState.LOADING
        startAutoIncrementProgress(timeMillis = 1000L, onFinished = ::onProgressFinished)

        whatMealBo.loadFoodListFirstPage(
            savedStateHandle.getOrThrow(INTENT_PARAM_BASICS),
            savedStateHandle.getOrThrow(INTENT_PARAM_SOUP),
            savedStateHandle.getOrThrow(INTENT_PARAM_COOKS),
            savedStateHandle.getOrThrow(INTENT_PARAM_INGREDIENTS),
            savedStateHandle.getOrThrow(INTENT_PARAM_STATES)
        ).onSuccess {
            Log.d(TAG, "Success to loadFoodListFromRemote")
            mutableFoodItemList.value = it.foodList
            mutablePagingState.value = if (it.pagingItem.hasNext) {
                FoodListPagingState.HAS_NEXT
            } else {
                FoodListPagingState.NO_NEXT
            }
            pagingItem = it.pagingItem
            mutableIsTaskFinished.value = true
        }.onFailure {
            // @TODO: We should properly inform the user of the error situation.
            Log.e(TAG, "Fail to loadFoodListFromRemote", it)
            mutableFoodListViewAction.post(FoodListViewAction.FailToLoadFoodListOnFirst)
            stopAutoIncrementProgress()
        }
    }

    private fun onProgressFinished() {
        mutableFoodListFirstLoadingState.value = FoodListFirstLoadingState.DONE
    }

    private fun loadNextPageOfFoodListFromRemote() = coroutineScope.launch {
        if (!this@FoodListViewModel::pagingItem.isInitialized) {
            mutablePagingState.value = FoodListPagingState.NO_NEXT
            return@launch
        }

        whatMealBo.loadFoodListNextPage(pagingItem)
            .onSuccess {
                Log.d(TAG, "Success to loadNextPageOfFoodListFromRemote")
                mutableFoodItemList.value = it.foodList
                mutablePagingState.value = if (it.pagingItem.hasNext) {
                    FoodListPagingState.HAS_NEXT
                } else {
                    FoodListPagingState.NO_NEXT
                }
                pagingItem = it.pagingItem
            }.onFailure {
                // @TODO: We should properly inform the user of the error situation.
                Log.e(TAG, "Fail to loadNextPageOfFoodListFromRemote", it)
                mutablePagingState.value = FoodListPagingState.HAS_NEXT
            }
    }

    class Factory(
        private val whatMealBo: WhatMealBoDelegator,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T = FoodListViewModel(whatMealBo, handle) as T
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
            basics: List<Basic>,
            soup: Soup,
            cooks: List<Cook>,
            ingredients: List<Ingredient>,
            states: List<State>
        ): Intent = Intent(context, FoodListActivity::class.java).putExtras(
            bundleOf(
                INTENT_PARAM_BASICS to basics,
                INTENT_PARAM_SOUP to soup,
                INTENT_PARAM_COOKS to cooks,
                INTENT_PARAM_INGREDIENTS to ingredients,
                INTENT_PARAM_STATES to states
            ).also { Log.i(TAG, "createIntent bundle: $it") }
        )
    }
}