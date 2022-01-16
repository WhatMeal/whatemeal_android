package com.beside.whatmeal.data.remote

import com.beside.whatmeal.data.remote.remotemodel.WhatMealRemoteException
import com.beside.whatmeal.data.remote.remotemodel.request.LoadFoodListRequest
import com.beside.whatmeal.data.remote.remotemodel.request.LoadMapUrlRequest
import com.beside.whatmeal.data.remote.remotemodel.request.RegisterTrackingIdRequest
import com.beside.whatmeal.data.remote.remotemodel.response.LoadFoodListResponse
import com.beside.whatmeal.data.remote.remotemodel.response.LoadMapUrlResponse
import com.beside.whatmeal.data.remote.remotemodel.response.RegisterTrackingIdResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

// @TODO: Please add unit test for it.
object WhatMealRemoteDataSourceImpl : WhatMealRemoteDataSource {
    private const val WHAT_MEAL_URL = "https://whatmeal.herokuapp.com"

    // @TODO: Change logging level before release.
    private val loggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(WHAT_MEAL_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val whatMealService: WhatMealRetrofitService =
        retrofit.create(WhatMealRetrofitService::class.java)

    override fun registerTrackingId(
        request: RegisterTrackingIdRequest
    ): Result<RegisterTrackingIdResponse> = runCatchIOException {
        Thread.sleep((Math.random() * 2000).toLong())
        Result.success(WhatMealDummyService.requestTrackingId())
//        val retrofitResponse = whatMealService.postOnBoarding(request).execute()
//        val body = retrofitResponse.body()
//        return@runCatchIOException if (retrofitResponse.isSuccessful && body != null) {
//            Result.success(body)
//        } else {
//            Result.failure(WhatMealRemoteException())
//        }
    }

    override fun loadFoodList(request: LoadFoodListRequest): Result<LoadFoodListResponse> =
        runCatchIOException {
            val retrofitResponse = whatMealService.getFoodList(request).execute()
            val body = retrofitResponse.body()
            return@runCatchIOException if (retrofitResponse.isSuccessful && body != null) {
//                Result.success(body)
                Result.success(WhatMealDummyService.getFoodList(request.pages))
            } else {
                Result.failure(WhatMealRemoteException())
            }
        }

    override fun loadMapUrl(request: LoadMapUrlRequest): Result<LoadMapUrlResponse> =
        runCatchIOException {
            Thread.sleep((Math.random() * 1500).toLong())
            Result.success(
                WhatMealDummyService.loadMapUrlBy(
                    request.foodName,
                    request.latitude,
                    request.longitude
                )
            )
//            val retrofitResponse = whatMealService.putFinalFood(request).execute()
//            val body = retrofitResponse.body()
//            return@runCatchIOException if (retrofitResponse.isSuccessful && body != null) {
//                Result.success(body)
//            } else {
//                Result.failure(WhatMealRemoteException())
//            }
        }

    private fun <T> runCatchIOException(block: () -> Result<T>): Result<T> = try {
        block()
    } catch (e: IOException) {
        Result.failure(WhatMealRemoteException())
    }
}