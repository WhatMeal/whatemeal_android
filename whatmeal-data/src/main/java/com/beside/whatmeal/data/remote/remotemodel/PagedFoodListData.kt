package com.beside.whatmeal.data.remote.remotemodel

data class PagedFoodListData (
    val foodList: List<FoodData>,
    val pagingData: FoodListPagingData
)