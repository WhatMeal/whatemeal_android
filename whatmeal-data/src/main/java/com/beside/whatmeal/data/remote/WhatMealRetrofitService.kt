package com.beside.whatmeal.data.remote

import com.beside.whatmeal.data.remote.remotemodel.request.LoadFoodListRequest
import com.beside.whatmeal.data.remote.remotemodel.request.LoadMapUrlRequest
import com.beside.whatmeal.data.remote.remotemodel.request.RegisterTrackingIdRequest
import com.beside.whatmeal.data.remote.remotemodel.response.LoadFoodListResponse
import com.beside.whatmeal.data.remote.remotemodel.response.LoadMapUrlResponse
import com.beside.whatmeal.data.remote.remotemodel.response.RegisterTrackingIdResponse
import retrofit2.Call
import retrofit2.http.*

interface WhatMealRetrofitService {
    @POST("infos/onboarding")
    fun postOnBoarding(@Body request: RegisterTrackingIdRequest): Call<RegisterTrackingIdResponse>

    @GET("foods")
    fun getFoodList(
        @Query("basics") basics: String,
        @Query("soup") soup: String,
        @Query("cooks") cooks: String,
        @Query("ingredients") ingredients: String,
        @Query("states") states: String,
        @Query("pages") pages: Int
    ): Call<LoadFoodListResponse>

    @PUT("infos/finalfood")
    fun putFinalFood(@Body request: LoadMapUrlRequest): Call<LoadMapUrlResponse>
}

fun WhatMealRetrofitService.getFoodList(request: LoadFoodListRequest) = getFoodList(
    basics = request.basics,
    soup = request.soup,
    cooks = request.cooks,
    ingredients = request.ingredients,
    states = request.states,
    pages = request.pages
)