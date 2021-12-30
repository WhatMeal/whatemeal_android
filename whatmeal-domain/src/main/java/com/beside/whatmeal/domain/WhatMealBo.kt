package com.beside.whatmeal.domain

import android.content.Context
import androidx.annotation.AnyThread
import com.beside.whatmeal.domain.domainmodel.*
import com.beside.whatmeal.domain.interactor.WhatMealRepositoryInterface
import com.beside.whatmeal.domain.task.*
import com.linecorp.lich.component.ComponentFactory
import com.linecorp.lich.component.getComponent

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
        standards: List<Standard>
    ): Result<Unit> =
        RegisterTrackingIdTask(whatMealRepository, age, mealTime, standards).executeOnCoroutine()

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
        latitude: Double,
        longitude: Double,
        foodName: String
    ): Result<String> =
        LoadMapUrlTask(whatMealRepository, latitude, longitude, foodName).executeOnCoroutine()

    companion object : ComponentFactory<WhatMealBo>() {
        override fun createComponent(context: Context): WhatMealBo {
            val whatMealRepository = context.getComponent(WhatMealRepositoryInterface)
            return WhatMealBo(whatMealRepository)
        }
    }
}