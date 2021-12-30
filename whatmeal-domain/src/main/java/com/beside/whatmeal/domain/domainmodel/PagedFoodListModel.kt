package com.beside.whatmeal.domain.domainmodel

data class PagedFoodListModel (
    val foodList: List<FoodModel>,
    val pagingData: FoodListPagingModel
)