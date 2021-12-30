package com.beside.whatmeal.data.remote.remotemodel.request

data class LoadFoodListRequest(
    val basics: String,
    val soup: String,
    val cooks: String,
    val ingredients: String,
    val states: String,
    val pages: Int = 1
)