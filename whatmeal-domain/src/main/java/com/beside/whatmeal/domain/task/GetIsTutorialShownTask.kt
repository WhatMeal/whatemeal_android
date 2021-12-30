package com.beside.whatmeal.domain.task

import androidx.annotation.AnyThread
import com.beside.whatmeal.domain.interactor.WhatMealRepositoryInterface

class GetIsTutorialShownTask(
    private val repository: WhatMealRepositoryInterface
) : Task<Result<Boolean>>() {

    @AnyThread
    public override fun execute(): Result<Boolean> = runCatching { repository.isTutorialShown() }
}