package com.beside.whatmeal.domain.task

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.beside.whatmeal.domain.domainmodel.FoodListPagingModel
import com.beside.whatmeal.domain.domainmodel.PagedFoodListModel
import com.beside.whatmeal.domain.interactor.WhatMealRepositoryInterface

class LoadFoodListNextPageTask(
    private val repository: WhatMealRepositoryInterface,
    private val pagingModel: FoodListPagingModel
) : Task<Result<PagedFoodListModel>>() {

    @WorkerThread
    override fun execute(): Result<PagedFoodListModel> =
        repository.loadNextPageFoodList(pagingModel)

    @AnyThread
    public override suspend fun executeOnCoroutine() = super.executeOnCoroutine()
}