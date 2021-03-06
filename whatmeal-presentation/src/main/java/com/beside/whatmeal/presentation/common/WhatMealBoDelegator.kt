package com.beside.whatmeal.presentation.common

import android.content.Context
import androidx.annotation.AnyThread
import com.beside.whatmeal.domain.WhatMealBo
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodListPagingItem
import com.beside.whatmeal.presentation.foodlist.uimodel.PagedFoodListItem
import com.beside.whatmeal.presentation.main.uimodel.*
import com.beside.whatmeal.presentation.mapper.toDomainModel
import com.beside.whatmeal.presentation.mapper.toUiModel
import com.beside.whatmeal.presentation.survey.uimodel.Age
import com.beside.whatmeal.presentation.survey.uimodel.MealTime
import com.beside.whatmeal.presentation.survey.uimodel.Standard
import com.beside.whatmeal.servicelocator.InstantiationFactory
import com.beside.whatmeal.servicelocator.getInstance

class WhatMealBoDelegator private constructor(private val domainBo: WhatMealBo) {

    @AnyThread
    fun isTutorialShown(): Result<Boolean> = domainBo.isTutorialShown()

    @AnyThread
    fun setTutorialShown(value: Boolean): Result<Unit> = domainBo.setTutorialShown(value)

    @AnyThread
    suspend fun registerTrackingId(
        age: Age,
        mealTime: MealTime,
        standard1: Standard,
        standard2: Standard
    ): Result<Unit> = domainBo.registerTrackingId(
        age.toDomainModel(),
        mealTime.toDomainModel(),
        standard1.toDomainModel(),
        standard2.toDomainModel()
    )

    @AnyThread
    suspend fun loadFoodListFirstPage(
        basics: List<Basic>,
        soup: Soup,
        cooks: List<Cook>,
        ingredients: List<Ingredient>,
        states: List<State>
    ): Result<PagedFoodListItem> = domainBo.loadFoodListFirstPage(
        basics.map { it.toDomainModel() },
        soup.toDomainModel(),
        cooks.map { it.toDomainModel() },
        ingredients.map { it.toDomainModel() },
        states.map { it.toDomainModel() }
    ).map { it.toUiModel() }

    @AnyThread
    suspend fun loadFoodListNextPage(
        pagingModel: FoodListPagingItem
    ): Result<PagedFoodListItem> = domainBo.loadFoodListNextPage(
        pagingModel.toDomainModel()
    ).map { it.toUiModel() }

    @AnyThread
    suspend fun loadMapUri(
        latitude: String,
        longitude: String,
        foodName: String
    ): Result<String> = domainBo.loadMapUri(latitude, longitude, foodName)

    companion object: InstantiationFactory<WhatMealBoDelegator>() {
        override fun createInstance(context: Context): WhatMealBoDelegator {
            val domainBo = context.getInstance(WhatMealBo)
            return WhatMealBoDelegator(domainBo)
        }
    }
}