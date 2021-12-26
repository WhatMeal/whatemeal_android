package com.beside.whatmeal.foodlist.viewmodel

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.beside.whatmeal.data.remote.model.Food
import com.beside.whatmeal.foodlist.uimodel.FoodListPagingState
import com.beside.whatmeal.foodlist.viewmodel.FoodListViewModel.Companion.DUMMY_FOOD_LIST

class FoodListViewModelPreviewParameterProvider :
    PreviewParameterProvider<FoodListViewModelInterface> {
    override val values: Sequence<FoodListViewModelInterface> = sequenceOf(
        LOADING, HAS_NEXT, NO_NEXT
    )

    companion object {
        val HAS_NEXT = object : FoodListViewModelInterface {
            override val foodList: LiveData<List<Food>> = MutableLiveData(DUMMY_FOOD_LIST[0])
            override val selectedFood: LiveData<Food> = MutableLiveData(DUMMY_FOOD_LIST[0][0])
            override val pagingState: LiveData<FoodListPagingState> =
                MutableLiveData(FoodListPagingState.HAS_NEXT)

            override fun onRefreshClick() = Unit
            override fun onFoodSelect(food: Food) = Unit
            override fun onNextClick() = Unit
        }

        val NO_NEXT = object : FoodListViewModelInterface {
            override val foodList: LiveData<List<Food>> = MutableLiveData(DUMMY_FOOD_LIST[0])
            override val selectedFood: LiveData<Food> = MutableLiveData()
            override val pagingState: LiveData<FoodListPagingState> =
                MutableLiveData(FoodListPagingState.NO_NEXT)

            override fun onRefreshClick() = Unit
            override fun onFoodSelect(food: Food) = Unit
            override fun onNextClick() = Unit
        }

        val LOADING = object : FoodListViewModelInterface {
            override val foodList: LiveData<List<Food>> = MutableLiveData(DUMMY_FOOD_LIST[0])
            override val selectedFood: LiveData<Food> = MutableLiveData()
            override val pagingState: LiveData<FoodListPagingState> =
                MutableLiveData(FoodListPagingState.LOADING)

            override fun onRefreshClick() = Unit
            override fun onFoodSelect(food: Food) = Unit
            override fun onNextClick() = Unit
        }
    }
}