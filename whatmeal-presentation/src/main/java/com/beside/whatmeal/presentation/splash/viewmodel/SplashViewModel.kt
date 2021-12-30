package com.beside.whatmeal.presentation.splash.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import kotlinx.coroutines.*

class SplashViewModel(private val whatMealBo: WhatMealBoDelegator) : ViewModel() {
    private val coroutineScope: CoroutineScope = viewModelScope

    @VisibleForTesting
    val mutablePastMinimumTime: MutableLiveData<Boolean> = MutableLiveData(false)
    private val mutableTutorialShownOrNull: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            addSource(mutablePastMinimumTime) { value = isTutorialShownOrNull() }
        }
    val tutorialShownOrNull: LiveData<Boolean> = mutableTutorialShownOrNull

    init {
        waitMinimumTime()
    }

    private fun waitMinimumTime() = coroutineScope.launch {
        withContext(Dispatchers.IO) {
            delay(MINIMUM_TIME)
            mutablePastMinimumTime.postValue(true)
        }
    }

    private fun isTutorialShownOrNull(): Boolean? =
        if (mutablePastMinimumTime.value == true) {
            whatMealBo.isTutorialShown().getOrNull()
        } else {
            null
        }

    companion object {
        private const val MINIMUM_TIME: Long = 1000L
    }

    class Factory(
        private val whatMealBo: WhatMealBoDelegator
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SplashViewModel(whatMealBo) as T
        }
    }
}