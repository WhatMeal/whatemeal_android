package com.beside.whatmeal.domain.domainmodel

data class FoodListPagingModel(
    val basics: String?,
    val soup: String?,
    val cooks: String?,
    val ingredients: String?,
    val states: String?,
    val page: Int?,
    val hasNext: Boolean
)