package com.beside.whatmeal.domain

import android.content.Context
import androidx.annotation.AnyThread
import com.beside.whatmeal.domain.domainmodel.*
import com.beside.whatmeal.domain.interactor.WhatMealRepositoryInterface
import com.beside.whatmeal.domain.task.*
import com.beside.whatmeal.servicelocator.InstantiationFactory
import com.beside.whatmeal.servicelocator.getInstance

class WhatMealBo private constructor(private val whatMealRepository: WhatMealRepositoryInterface) {

    @AnyThread
    fun isTutorialShown(): Result<Boolean> =
        GetIsTutorialShownTask(whatMealRepository).execute()

    @AnyThread
    fun setTutorialShown(value: Boolean): Result<Unit> =
        SetTutorialShownTask(whatMealRepository, value).execute()

    @AnyThread
    suspend fun registerTrackingId(
        age: Age,
        mealTime: MealTime,
        standard1: Standard,
        standard2: Standard
    ): Result<Unit> =
        RegisterTrackingIdTask(whatMealRepository, age, mealTime, standard1, standard2).executeOnCoroutine()

    @AnyThread
    suspend fun loadFoodListFirstPage(
        basics: List<Basic>,
        soup: Soup,
        cooks: List<Cook>,
        ingredients: List<Ingredient>,
        states: List<State>
    ): Result<PagedFoodListModel> =
        LoadFoodListFirstPageTask(
            whatMealRepository,
            basics,
            soup,
            cooks,
            ingredients,
            states
        ).executeOnCoroutine()

    @AnyThread
    suspend fun loadFoodListNextPage(
        pagingModel: FoodListPagingModel
    ): Result<PagedFoodListModel> =
        LoadFoodListNextPageTask(whatMealRepository, pagingModel).executeOnCoroutine()

    @AnyThread
    suspend fun loadMapUri(
        latitude: String,
        longitude: String,
        foodName: String
    ): Result<String> =
        LoadMapUrlTask(whatMealRepository, latitude, longitude, foodName).executeOnCoroutine()



    companion object : InstantiationFactory<WhatMealBo>() {
        override fun createInstance(context: Context): WhatMealBo {
            val whatMealRepository = context.getInstance(WhatMealRepositoryInterface)
            return WhatMealBo(whatMealRepository)
        }
    }
}