package com.beside.whatmeal.domain.task

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.beside.whatmeal.domain.domainmodel.*
import com.beside.whatmeal.domain.interactor.WhatMealRepositoryInterface

class LoadFoodListFirstPageTask(
    private val repository: WhatMealRepositoryInterface,
    private val basics: List<Basic>,
    private val soup: Soup,
    private val cooks: List<Cook>,
    private val ingredients: List<Ingredient>,
    private val states: List<State>
) : Task<Result<PagedFoodListModel>>() {

    @WorkerThread
    override fun execute(): Result<PagedFoodListModel> =
        repository.loadFoodList(basics, soup, cooks, ingredients, states)

    @AnyThread
    public override suspend fun executeOnCoroutine() = super.executeOnCoroutine()
}