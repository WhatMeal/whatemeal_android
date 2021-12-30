package com.beside.whatmeal.data.remote

import com.beside.whatmeal.data.remote.remotemodel.request.LoadFoodListRequest
import com.beside.whatmeal.data.remote.remotemodel.response.LoadFoodListResponse
import retrofit2.Call
import retrofit2.http.*

interface WhatMealRetrofitService {
    // @TODO: Not implemented yet.
    @POST("infos/onboarding")
    fun postOnBoarding(
        @Field("age") age: String,
        @Field("mealTime") mealTime: String,
        @Field("standard1") standard1: String,
        @Field("Standard2") standard2: String
    )

    @GET("foods")
    fun getFoodList(
        @Query("basics") basics: String,
        @Query("soup") soup: String,
        @Query("cooks") cooks: String,
        @Query("ingredients") ingredients: String,
        @Query("states") states: String,
        @Query("pages") pages: Int
    ): Call<LoadFoodListResponse>

    // @TODO: Not implemented yet.
    @PUT("infos/finalfood")
    fun putFinalFood(
        @Field("id") id: Int,
        @Field("foodName") foodName: String
    )
}

fun WhatMealRetrofitService.getFoodList(request: LoadFoodListRequest) = getFoodList(
    basics = request.basics,
    soup = request.soup,
    cooks = request.cooks,
    ingredients = request.ingredients,
    states = request.states,
    pages = request.pages
)