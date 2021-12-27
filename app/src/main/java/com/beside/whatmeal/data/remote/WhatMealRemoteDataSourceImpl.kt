package com.beside.whatmeal.data.remote

import com.beside.whatmeal.data.remote.model.request.GetFoodListRequest
import com.beside.whatmeal.data.remote.model.response.GetFoodListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

// @TODO: Please consider whether dealing with paging and id is the role of remote data.
// @TODO: Please add unit test for it.
object WhatMealRemoteDataSourceImpl : WhatMealRemoteDataSource {
    private const val WHAT_MEAL_URL = "https://whatmeal.herokuapp.com"

    // @TODO: Change logging level before release.
    private val loggingInterceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
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

    private var getFoodListPagingData: GetFoodListPagingData? = null

    override suspend fun getFirstFoodList(
        request: GetFoodListRequest
    ): Result<GetFoodListResponse> = withContext(Dispatchers.IO) {
        runCatchIOException {
            val retrofitResponse = whatMealService.getFoodList(request).execute()
            val body = retrofitResponse.body()
            return@runCatchIOException if (retrofitResponse.isSuccessful && body != null) {
                getFoodListPagingData = GetFoodListPagingData(request, body.page, body.hasNext)
                Result.success(body)
            } else {
                getFoodListPagingData = null
                Result.failure(WhatMealRemoteException())
            }
        }
    }

    override fun hasNextFoodList(): Boolean = getFoodListPagingData?.hasNext == true

    override suspend fun getNextFoodList(): Result<GetFoodListResponse> =
        withContext(Dispatchers.IO) {
            runCatchIOException {
                val (lastRequest, currentPage, hasNext) = getFoodListPagingData
                    ?: return@runCatchIOException Result.failure(WhatMealRemoteException())
                // @TODO: NOT implemented yet.
                //      if (!hasNext) {
                //          return@runCatchIOException Result.failure(WhatMealRemoteException())
                //      }

                val request = lastRequest.copy(pages = currentPage + 1)
                val retrofitResponse = whatMealService.getFoodList(request).execute()

                val body = retrofitResponse.body()
                return@runCatchIOException if (retrofitResponse.isSuccessful && body != null) {
                    getFoodListPagingData = GetFoodListPagingData(request, body.page, body.hasNext)
                    Result.success(body)
                } else {
                    getFoodListPagingData = null
                    Result.failure(WhatMealRemoteException())
                }
            }
        }

    private data class GetFoodListPagingData(
        val lastRequest: GetFoodListRequest,
        val currentPage: Int,
        val hasNext: Boolean
    )

    private class WhatMealRemoteException : Exception()

    private fun <T> runCatchIOException(block: () -> Result<T>): Result<T> = try {
        block()
    } catch (e: IOException) {
        Result.failure(WhatMealRemoteException())
    }
}