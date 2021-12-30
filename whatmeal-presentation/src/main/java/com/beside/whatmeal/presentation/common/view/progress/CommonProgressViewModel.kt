package com.beside.whatmeal.presentation.common.view.progress

import androidx.lifecycle.*
import kotlinx.coroutines.*

// @TODO: Please add unit test for it.
// @TODO: Please modify it to be an independent ViewModel by break the inheritance.
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

    private var isRunning: Boolean = false
    private var autoIncrementJob: Job? = null
    private var callback: ProgressFinishedCallback? = null

    fun startAutoIncrementProgress(timeMillis: Long, onFinished: (() -> Unit)) {
        val progressFinishedCallback = object : ProgressFinishedCallback {
            override fun onProgressFinished() = onFinished()
        }
        startAutoIncrementProgress(timeMillis, progressFinishedCallback)
    }

    fun startAutoIncrementProgress(
        timeMillis: Long,
        onFinishedCallBack: ProgressFinishedCallback? = null
    ) {
        if (isRunning) {
            return
        }
        init()
        isRunning = true
        this.callback = onFinishedCallBack

        autoIncrementJob = coroutineScope.launch {
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
                isRunning = false
            }
        }
    }

    fun stopAutoIncrementProgress() = init()

    private fun init() {
        autoIncrementJob?.cancel()
        autoIncrementJob = null
        isRunning = false
        callback = null
        mutableAutoIncrementProgress.value = START_PROGRESS
        mutableIsTaskFinished.value = false
    }

    private fun createProgressFinishEventOrNull(): ProgressFinishEvent? =
        if (isTaskFinished.value == true && autoIncrementProgress.value == MAX_PROGRESS) {
            callback?.onProgressFinished()
            ProgressFinishEvent()
        } else {
            null
        }

    companion object {
        private const val START_PROGRESS = 0f
        const val MAX_PROGRESS = 0.99f

        private const val DELAY_TIME_MILLIS = 17L
    }

    class ProgressFinishEvent
    interface ProgressFinishedCallback {
        fun onProgressFinished()
    }
}