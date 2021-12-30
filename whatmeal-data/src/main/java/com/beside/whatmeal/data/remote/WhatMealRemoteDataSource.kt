package com.beside.whatmeal.data.remote

import androidx.annotation.WorkerThread
import com.beside.whatmeal.data.remote.remotemodel.request.LoadFoodListRequest
import com.beside.whatmeal.data.remote.remotemodel.request.LoadMapUrlRequest
import com.beside.whatmeal.data.remote.remotemodel.request.RegisterTrackingIdRequest
import com.beside.whatmeal.data.remote.remotemodel.response.LoadFoodListResponse
import com.beside.whatmeal.data.remote.remotemodel.response.LoadMapUrlResponse
import com.beside.whatmeal.data.remote.remotemodel.response.RegisterTrackingIdResponse

interface WhatMealRemoteDataSource {
    @WorkerThread
    fun registerTrackingId(request: RegisterTrackingIdRequest): Result<RegisterTrackingIdResponse>

    @WorkerThread
    fun loadFoodList(request: LoadFoodListRequest): Result<LoadFoodListResponse>

    @WorkerThread
    fun loadMapUrl(request: LoadMapUrlRequest): Result<LoadMapUrlResponse>
}