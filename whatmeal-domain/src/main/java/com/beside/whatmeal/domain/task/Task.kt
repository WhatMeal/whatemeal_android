package com.beside.whatmeal.domain.task

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class Task<Result> {
    protected abstract fun execute(): Result

    protected open suspend fun executeOnCoroutine(): Result = withContext(Dispatchers.IO) {
        execute()
    }
}