package com.beside.whatmeal.domain.interactor

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.beside.whatmeal.domain.domainmodel.*
import com.beside.whatmeal.servicelocator.ClassLoaderFactory

interface WhatMealRepositoryInterface {
    @AnyThread
    fun isTutorialShown(): Boolean

    @AnyThread
    fun setTutorialShown(value: Boolean)

    @WorkerThread
    fun registerTrackingId(
        age: Age,
        mealTime: MealTime,
        standard1: Standard,
        standard2: Standard
    ): Result<Int>

    @WorkerThread
    fun loadFoodList(
        basics: List<Basic>,
        soup: Soup,
        cooks: List<Cook>,
        ingredients: List<Ingredient>,
        states: List<State>,
    ): Result<PagedFoodListModel>

    @WorkerThread
    fun loadNextPageFoodList(pagingData: FoodListPagingModel): Result<PagedFoodListModel>

    @WorkerThread
    fun loadMapUrl(
        trackingId: Int,
        latitude: String,
        longitude: String,
        foodName: String
    ): Result<String>

    companion object : ClassLoaderFactory<WhatMealRepositoryInterface>() {
        override fun getInstantiationFactoryClassName(): String =
            "com.beside.whatmeal.data.WhatMealRepositoryDelegatorFactory"
    }
}