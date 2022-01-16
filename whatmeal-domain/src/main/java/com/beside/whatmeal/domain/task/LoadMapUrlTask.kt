package com.beside.whatmeal.domain.task

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.beside.whatmeal.domain.common.TrackingManager
import com.beside.whatmeal.domain.interactor.WhatMealRepositoryInterface

class LoadMapUrlTask(
    private val repository: WhatMealRepositoryInterface,
    private val latitude: String,
    private val longitude: String,
    private val foodName: String
) : Task<Result<String>>() {
    private val trackingManager: TrackingManager = TrackingManager

    @WorkerThread
    override fun execute(): Result<String> =
        repository.loadMapUrl(trackingManager.getTrackingId(), latitude, longitude, foodName)

    @AnyThread
    public override suspend fun executeOnCoroutine() = super.executeOnCoroutine()
}