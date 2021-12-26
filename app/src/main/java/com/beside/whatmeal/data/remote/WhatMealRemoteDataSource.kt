package com.beside.whatmeal.data.remote

import com.beside.whatmeal.data.remote.model.request.GetFoodListRequest
import com.beside.whatmeal.data.remote.model.response.GetFoodListResponse

interface WhatMealRemoteDataSource {
    suspend fun getFirstFoodList(request: GetFoodListRequest): Result<GetFoodListResponse>

    fun hasNextFoodList(): Boolean

    suspend fun getNextFoodList(): Result<GetFoodListResponse>
}