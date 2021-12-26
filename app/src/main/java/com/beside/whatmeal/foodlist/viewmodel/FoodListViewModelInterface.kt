package com.beside.whatmeal.foodlist.viewmodel

import androidx.lifecycle.LiveData
import com.beside.whatmeal.data.remote.model.Food
import com.beside.whatmeal.foodlist.uimodel.FoodListPagingState

interface FoodListViewModelInterface {
    val foodList: LiveData<List<Food>>
    val selectedFood: LiveData<Food>
    val pagingState: LiveData<FoodListPagingState>
    
    fun onRefreshClick()
    fun onFoodSelect(food: Food)
    fun onNextClick()
}