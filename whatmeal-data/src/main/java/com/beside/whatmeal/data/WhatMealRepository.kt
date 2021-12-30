package com.beside.whatmeal.data

import android.content.Context
import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.beside.whatmeal.data.local.WhatMealLocalDataSource
import com.beside.whatmeal.data.remote.WhatMealRemoteDataSource
import com.beside.whatmeal.data.remote.WhatMealRemoteDataSourceImpl
import com.beside.whatmeal.data.remote.remotemodel.*
import com.beside.whatmeal.data.remote.remotemodel.request.LoadFoodListRequest
import com.beside.whatmeal.data.remote.remotemodel.request.LoadMapUrlRequest
import com.beside.whatmeal.data.remote.remotemodel.request.RegisterTrackingIdRequest
import com.linecorp.lich.component.ComponentFactory
import com.linecorp.lich.component.getComponent

class WhatMealRepository private constructor(
    private val localDataSource: WhatMealLocalDataSource,
    private val remoteDataSource: WhatMealRemoteDataSource
) {
    @AnyThread
    fun isTutorialShown(): Boolean = localDataSource.isTutorialShown()

    @AnyThread
    fun setTutorialShown(value: Boolean) = localDataSource.setTutorialShown(value)

    @WorkerThread
    fun registerTrackingId(
        age: Age,
        mealTime: MealTime,
        standards: List<Standard>
    ): Result<String> {
        val request = RegisterTrackingIdRequest(age, mealTime, standards)
        return remoteDataSource.registerTrackingId(request).map { it.id }
    }

    @WorkerThread
    fun loadFoodList(
        basics: List<Basic>,
        soup: Soup,
        cooks: List<Cook>,
        ingredients: List<Ingredient>,
        states: List<State>
    ): Result<PagedFoodListData> {
        val request = LoadFoodListRequest(
            basics.joinToString(",") { it.id.toString() },
            soup.id.toString(),
            cooks.joinToString(",") { it.id.toString() },
            ingredients.joinToString(",") { it.id.toString() },
            states.joinToString(",") { it.id.toString() }
        )
        return remoteDataSource.loadFoodList(request).map {
            val pagingData = FoodListPagingData.createBy(request, it)
            PagedFoodListData(it.food, pagingData)
        }
    }

    @WorkerThread
    fun loadNextPageFoodList(pagingData: FoodListPagingData): Result<PagedFoodListData> {
        val (nextRequest, hasNext) = pagingData
        if (nextRequest == null || !hasNext) {
            return Result.failure(IllegalStateException())
        }

        return remoteDataSource.loadFoodList(nextRequest).map {
            val nextPagingData = pagingData.copyBy(it)
            PagedFoodListData(it.food, nextPagingData)
        }
    }

    @WorkerThread
    fun loadMapUrl(
        trackingId: String,
        latitude: Double,
        longitude: Double,
        foodName: String
    ): Result<String> {
        val request = LoadMapUrlRequest(trackingId, latitude, longitude, foodName)
        return remoteDataSource.loadMapUrl(request).map { it.url }
    }

    companion object : ComponentFactory<WhatMealRepository>() {
        override fun createComponent(context: Context): WhatMealRepository =
            WhatMealRepository(
                context.getComponent(WhatMealLocalDataSource),
                WhatMealRemoteDataSourceImpl
            )
    }
}