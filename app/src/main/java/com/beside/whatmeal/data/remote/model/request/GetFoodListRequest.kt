package com.beside.whatmeal.data.remote.model.request

data class GetFoodListRequest(
    val basics: String,
    val soup: String,
    val cooks: String,
    val ingredients: String,
    val states: String,
    val pages: Int = 1
)