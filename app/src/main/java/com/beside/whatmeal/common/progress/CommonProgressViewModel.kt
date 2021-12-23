package com.beside.whatmeal.common.progress

import androidx.lifecycle.*
import kotlinx.coroutines.*

abstract class CommonProgressViewModel : ViewModel() {
    private val coroutineScope: CoroutineScope = viewModelScope

    private val mutableAutoIncrementProgress: MutableLiveData<Float> =
        MutableLiveData(START_PROGRESS)
    val autoIncrementProgress: LiveData<Float> = mutableAutoIncrementProgress

    protected val mutableIsTaskFinished: MutableLiveData<Boolean> = MutableLiveData(false)
    val isTaskFinished: LiveData<Boolean> = mutableIsTaskFinished

    private val mutableIsProgressFinished: MediatorLiveData<ProgressFinishEvent> =
        MediatorLiveData<ProgressFinishEvent>().apply {
            addSource(autoIncrementProgress) { value = createProgressFinishEventOrNull() }
            addSource(isTaskFinished) { value = createProgressFinishEventOrNull() }
        }
    val progressFinishEvent: LiveData<ProgressFinishEvent> = mutableIsProgressFinished

    fun startAutoIncrement(timeMillis: Long) = coroutineScope.launch {
        withContext(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            var currentTime = System.currentTimeMillis()
            while (currentTime - startTime < timeMillis) {
                mutableAutoIncrementProgress.postValue(
                    (currentTime - startTime) / timeMillis.toFloat()
                )
                delay(DELAY_TIME_MILLIS)
                currentTime = System.currentTimeMillis()
            }
            mutableAutoIncrementProgress.postValue(MAX_PROGRESS)
        }
    }

    private fun createProgressFinishEventOrNull(): ProgressFinishEvent? =
        if (isTaskFinished.value == true && autoIncrementProgress.value == MAX_PROGRESS) {
            ProgressFinishEvent()
        } else {
            null
        }

    companion object {
        private const val START_PROGRESS = 0f
        const val MAX_PROGRESS = 0.99f

        private const val DELAY_TIME_MILLIS = 50L
    }

    class ProgressFinishEvent
}