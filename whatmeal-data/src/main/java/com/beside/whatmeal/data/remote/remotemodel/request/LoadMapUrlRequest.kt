package com.beside.whatmeal.data.remote.remotemodel.request

import com.google.gson.annotations.SerializedName

data class LoadMapUrlRequest(
    @SerializedName("id") val trackingId: Int,
    @SerializedName("y") val latitude: String,
    @SerializedName("x") val longitude: String,
    val foodName: String
)