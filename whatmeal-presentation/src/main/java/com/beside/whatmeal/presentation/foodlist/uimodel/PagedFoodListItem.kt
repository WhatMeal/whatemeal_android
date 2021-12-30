package com.beside.whatmeal.presentation.foodlist.uimodel

data class PagedFoodListItem (
    val foodList: List<FoodItem>,
    val pagingItem: FoodListPagingItem
)