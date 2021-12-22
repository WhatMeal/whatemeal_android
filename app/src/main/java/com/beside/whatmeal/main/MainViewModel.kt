package com.beside.whatmeal.main

import androidx.lifecycle.*
import com.beside.whatmeal.common.progress.CommonProgressViewModelInterface
import com.beside.whatmeal.data.model.*
import kotlinx.coroutines.*

class MainViewModel : ViewModel(), CommonProgressViewModelInterface {
    private val coroutineScope: CoroutineScope = viewModelScope

    private val mutableMainRoundType: MutableLiveData<MainRoundType> =
        MutableLiveData(MainRoundType.BASIC)
    val mainRoundType: LiveData<MainRoundType> = mutableMainRoundType

    private val stateSelectedItemsMap: MutableMap<MainRoundType, MutableList<MainItem>> =
        mutableMapOf<MainRoundType, MutableList<MainItem>>().apply {
            MainRoundType.values().forEach { this[it] = mutableListOf() }
        }
    private val mutableSelectedItems: MutableLiveData<List<MainItem>> = MutableLiveData(listOf())
    val selectedItems: LiveData<List<MainItem>> = mutableSelectedItems

    private val mutableNextButtonEnabled: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            value = false
            addSource(mainRoundType) { value = isNextButtonEnabled() }
            addSource(selectedItems) { value = isNextButtonEnabled() }
        }
    val nextButtonEnabled: LiveData<Boolean> = mutableNextButtonEnabled

    private val mutableAutoIncrementProgress: MutableLiveData<Float> =
        MutableLiveData(START_PROGRESS)
    override val autoIncrementProgress: LiveData<Float> = mutableAutoIncrementProgress

    private val mutableLoadingFinish: MutableLiveData<Boolean> = MutableLiveData(false)
    override val loadingFinished: LiveData<Boolean> = mutableLoadingFinish

    private val mutableMainViewState: MediatorLiveData<MainViewState> =
        MediatorLiveData<MainViewState>().apply {
            value = MainViewState.ROUND
            addSource(loadingFinished) { value = getMainViewState() }
            addSource(autoIncrementProgress) { value = getMainViewState() }
        }
    val mainViewState: LiveData<MainViewState> = mutableMainViewState

    fun onNextClick() {
        val currentRoundType = mainRoundType.value ?: return
        val lastPageOrder = MainRoundType.values().size
        val currentPageOrder = currentRoundType.pageOrder
        if (currentPageOrder < lastPageOrder) {
            val nextRoundType = MainRoundType.of(currentPageOrder + 1)
            mutableMainRoundType.value = nextRoundType
            mutableSelectedItems.value = stateSelectedItemsMap[nextRoundType]
        } else if (currentPageOrder == lastPageOrder) {
            mutableMainViewState.value = MainViewState.LOADING
            postSelectedItemsToRemote()
        }
    }

    fun onBackPressed(runOSOnBackPressed: () -> Unit) {
        when {
            mainViewState.value == MainViewState.ROUND && mainRoundType.value?.pageOrder != 1 -> {
                onUpButtonClick()
            }
            mainViewState.value == MainViewState.ROUND && mainRoundType.value?.pageOrder == 1 -> {
                runOSOnBackPressed()
            }
            mainViewState.value != MainViewState.ROUND -> {
                /* Do nothing */
            }
        }
    }

    fun onUpButtonClick() {
        val currentRoundType = mainRoundType.value ?: return
        val lastPageOrder = MainRoundType.values().size
        if (currentRoundType.pageOrder in 2..lastPageOrder) {
            val previousRoundType = MainRoundType.of(currentRoundType.pageOrder - 1)
            mutableMainRoundType.value = previousRoundType
            mutableSelectedItems.value = stateSelectedItemsMap[previousRoundType]
        } else {
            return
        }
    }

    fun onOptionSelect(mainItem: MainItem) {
        val currentRoundType = mainRoundType.value ?: return
        val selectedItems = stateSelectedItemsMap[currentRoundType] ?: return
        when {
            selectedItems.contains(mainItem) -> {
                selectedItems.remove(mainItem)
            }
            selectedItems.size < currentRoundType.selectableCount -> {
                selectedItems.add(mainItem)
            }
            selectedItems.size == currentRoundType.selectableCount -> {
                selectedItems.removeLastOrNull()
                selectedItems.add(mainItem)
            }
            else -> return
        }
        mutableSelectedItems.value = selectedItems
    }

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

    private fun postSelectedItemsToRemote() = coroutineScope.launch {
        withContext(Dispatchers.IO) {
            // @TODO: Not implemented yet.
            delay((Math.random() * 1000).toLong())
            mutableLoadingFinish.postValue(true)
        }
    }

    private fun getMainViewState(): MainViewState = when {
        mainViewState.value == MainViewState.ROUND -> MainViewState.ROUND
        loadingFinished.value == true && autoIncrementProgress.value == MAX_PROGRESS -> {
            MainViewState.FINISH
        }
        else -> MainViewState.LOADING
    }

    private fun isNextButtonEnabled(): Boolean {
        val selectableCount = mainRoundType.value?.selectableCount ?: return false
        val selectedCount = selectedItems.value?.size ?: return false
        return selectedCount in 1..selectableCount
    }

    companion object {
        const val START_PROGRESS = 0f
        const val MAX_PROGRESS = 0.99f

        const val DELAY_TIME_MILLIS = 50L
    }
}