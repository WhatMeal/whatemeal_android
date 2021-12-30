package com.beside.whatmeal.domain.task

import androidx.annotation.AnyThread
import com.beside.whatmeal.domain.interactor.WhatMealRepositoryInterface

class SetTutorialShownTask(
    private val repository: WhatMealRepositoryInterface,
    private val value: Boolean
) : Task<Result<Unit>>() {

    @AnyThread
    public override fun execute(): Result<Unit> = runCatching { repository.setTutorialShown(value) }
}