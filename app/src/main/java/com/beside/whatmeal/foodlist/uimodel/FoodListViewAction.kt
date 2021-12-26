package com.beside.whatmeal.foodlist.uimodel

sealed class FoodListViewAction {
    object FailToLoadFoodListOnFirst : FoodListViewAction()
    data class StartMapScreenAction(val foodName: String) : FoodListViewAction()
}