package com.beside.whatmeal.data.remote.model.response

import com.beside.whatmeal.data.remote.model.Food
import com.google.gson.annotations.SerializedName

data class GetFoodListResponse(
    val food: List<Food>,
    val page: Int,
    @SerializedName("has_next") val hasNext: Boolean
)