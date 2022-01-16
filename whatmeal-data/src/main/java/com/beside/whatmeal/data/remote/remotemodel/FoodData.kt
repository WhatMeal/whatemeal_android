package com.beside.whatmeal.data.remote.remotemodel

import com.google.gson.annotations.SerializedName

data class FoodData(
    @SerializedName("food") val name: String,
    @SerializedName("imgSrc") val imageUrl: String
)