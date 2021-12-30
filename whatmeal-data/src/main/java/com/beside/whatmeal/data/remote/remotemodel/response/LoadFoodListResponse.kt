package com.beside.whatmeal.data.remote.remotemodel.response

import com.beside.whatmeal.data.remote.remotemodel.FoodData
import com.google.gson.annotations.SerializedName

data class LoadFoodListResponse(
    val food: List<FoodData>,
    val page: Int,
    @SerializedName("has_next") val hasNext: Boolean
)