package com.beside.whatmeal.presentation.foodlist.uimodel

data class FoodListPagingItem(
    val basics: String?,
    val soup: String?,
    val cooks: String?,
    val ingredients: String?,
    val states: String?,
    val page: Int?,
    val hasNext: Boolean
)