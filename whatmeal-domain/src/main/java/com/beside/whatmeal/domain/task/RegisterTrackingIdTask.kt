package com.beside.whatmeal.domain.task

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.beside.whatmeal.domain.common.TrackingManager
import com.beside.whatmeal.domain.domainmodel.Age
import com.beside.whatmeal.domain.domainmodel.MealTime
import com.beside.whatmeal.domain.domainmodel.Standard
import com.beside.whatmeal.domain.interactor.WhatMealRepositoryInterface

class RegisterTrackingIdTask(
    private val repository: WhatMealRepositoryInterface,
    private val age: Age,
    private val mealTime: MealTime,
    private val standard1: Standard,
    private val standard2: Standard
) : Task<Result<Unit>>() {
    private val trackingManager = TrackingManager

    @WorkerThread
    override fun execute(): Result<Unit> =
        repository.registerTrackingId(
            age,
            mealTime,
            standard1,
            standard2
        ).map {
            trackingManager.holdTrackingId(it)
        }

    @AnyThread
    public override suspend fun executeOnCoroutine() = super.executeOnCoroutine()
}