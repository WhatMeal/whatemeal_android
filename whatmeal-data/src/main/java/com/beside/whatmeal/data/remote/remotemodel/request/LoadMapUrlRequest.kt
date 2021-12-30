package com.beside.whatmeal.data.remote.remotemodel.request

data class LoadMapUrlRequest(
    val trackingId: String,
    val latitude: Double,
    val longitude: Double,
    val foodName: String
)