package com.beside.whatmeal.domain.interactor

import android.content.Context
import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.beside.whatmeal.domain.domainmodel.*
import com.linecorp.lich.component.ComponentFactory

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

    companion object : ComponentFactory<WhatMealRepositoryInterface>() {
        override fun createComponent(context: Context): WhatMealRepositoryInterface =
            delegateCreation(
                context,
                "com.beside.whatmeal.data.WhatMealRepositoryDelegatorFactory"
            )
    }
}