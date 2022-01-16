package com.beside.whatmeal.data

import android.content.Context
import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.beside.whatmeal.data.mapper.toDataModel
import com.beside.whatmeal.data.mapper.toDomainModel
import com.beside.whatmeal.domain.domainmodel.*
import com.beside.whatmeal.domain.interactor.WhatMealRepositoryInterface
import com.beside.whatmeal.servicelocator.InstantiationFactory
import com.beside.whatmeal.servicelocator.getInstance

class WhatMealRepositoryDelegator(
    private val dataRepository: WhatMealRepository
) : WhatMealRepositoryInterface {
    @AnyThread
    override fun isTutorialShown(): Boolean = dataRepository.isTutorialShown()

    @AnyThread
    override fun setTutorialShown(value: Boolean) = dataRepository.setTutorialShown(value)

    @WorkerThread
    override fun registerTrackingId(
        age: Age,
        mealTime: MealTime,
        standard1: Standard,
        standard2: Standard
    ): Result<Int> = dataRepository.registerTrackingId(
        age.toDataModel(),
        mealTime.toDataModel(),
        standard1.toDataModel(),
        standard2.toDataModel()
    )

    @WorkerThread
    override fun loadFoodList(
        basics: List<Basic>,
        soup: Soup,
        cooks: List<Cook>,
        ingredients: List<Ingredient>,
        states: List<State>,
    ): Result<PagedFoodListModel> =
        dataRepository.loadFoodList(
            basics.map { it.toDataModel() },
            soup.toDataModel(),
            cooks.map { it.toDataModel() },
            ingredients.map { it.toDataModel() },
            states.map { it.toDataModel() }
        ).map { it.toDomainModel() }

    @WorkerThread
    override fun loadNextPageFoodList(pagingData: FoodListPagingModel): Result<PagedFoodListModel> =
        dataRepository.loadNextPageFoodList(pagingData.toDataModel()).map { it.toDomainModel() }

    @WorkerThread
    override fun loadMapUrl(
        trackingId: Int,
        latitude: String,
        longitude: String,
        foodName: String
    ): Result<String> = dataRepository.loadMapUrl(trackingId, latitude, longitude, foodName)
}

class WhatMealRepositoryDelegatorFactory : InstantiationFactory<WhatMealRepositoryDelegator>() {
    override fun createInstance(context: Context): WhatMealRepositoryDelegator {
        val dataRepository = context.getInstance(WhatMealRepository)
        return WhatMealRepositoryDelegator(dataRepository)
    }
}