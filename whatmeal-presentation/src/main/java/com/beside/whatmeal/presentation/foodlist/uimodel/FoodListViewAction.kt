package com.beside.whatmeal.presentation.foodlist.uimodel

sealed class FoodListViewAction {
    object FailToLoadFoodListOnFirst : FoodListViewAction()
    data class StartMapScreen(val foodName: String) : FoodListViewAction()
}