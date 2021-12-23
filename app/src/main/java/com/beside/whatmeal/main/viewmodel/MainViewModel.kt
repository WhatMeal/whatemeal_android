package com.beside.whatmeal.main.viewmodel

import androidx.lifecycle.*
import com.beside.whatmeal.common.progress.CommonProgressViewModel
import com.beside.whatmeal.main.uimodel.MainItem
import com.beside.whatmeal.main.uimodel.MainRoundState
import com.beside.whatmeal.main.uimodel.MainViewState
import kotlinx.coroutines.*

class MainViewModel : CommonProgressViewModel() {
    private val coroutineScope: CoroutineScope = viewModelScope

    private val mutableMainViewState: MutableLiveData<MainViewState> =
        MutableLiveData(MainViewState.ROUND)
    val mainViewState: LiveData<MainViewState> = mutableMainViewState

    private val mutableMainRoundState: MutableLiveData<MainRoundState> =
        MutableLiveData(MainRoundState.BASIC)
    val mainRoundState: LiveData<MainRoundState> = mutableMainRoundState

    private val stateSelectedItemsMap: MutableMap<MainRoundState, MutableList<MainItem>> =
        mutableMapOf<MainRoundState, MutableList<MainItem>>().apply {
            MainRoundState.values().forEach { this[it] = mutableListOf() }
        }
    private val mutableSelectedItems: MutableLiveData<List<MainItem>> = MutableLiveData(listOf())
    val selectedItems: LiveData<List<MainItem>> = mutableSelectedItems

    private val mutableNextButtonEnabled: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            value = false
            addSource(mainRoundState) { value = isNextButtonEnabled() }
            addSource(selectedItems) { value = isNextButtonEnabled() }
        }
    val nextButtonEnabled: LiveData<Boolean> = mutableNextButtonEnabled

    fun onNextClick() {
        val currentRoundState = mainRoundState.value ?: return
        val lastPageOrder = MainRoundState.values().size
        val currentPageOrder = currentRoundState.pageOrder
        if (currentPageOrder < lastPageOrder) {
            val nextRoundState = MainRoundState.of(currentPageOrder + 1)
            mutableMainRoundState.value = nextRoundState
            mutableSelectedItems.value = stateSelectedItemsMap[nextRoundState]
        } else if (currentPageOrder == lastPageOrder) {
            mutableMainViewState.value = MainViewState.PROGRESS
            postSelectedItemsToRemote()
        }
    }

    fun onBackPressed(runOSOnBackPressed: () -> Unit) {
        when {
            mainViewState.value == MainViewState.ROUND && mainRoundState.value?.pageOrder != 1 -> {
                onUpButtonClick()
            }
            mainViewState.value == MainViewState.ROUND && mainRoundState.value?.pageOrder == 1 -> {
                runOSOnBackPressed()
            }
            mainViewState.value != MainViewState.ROUND -> {
                /* Do nothing */
            }
        }
    }

    fun onUpButtonClick() {
        val currentRoundState = mainRoundState.value ?: return
        val lastPageOrder = MainRoundState.values().size
        if (currentRoundState.pageOrder in 2..lastPageOrder) {
            val previousRoundState = MainRoundState.of(currentRoundState.pageOrder - 1)
            mutableMainRoundState.value = previousRoundState
            mutableSelectedItems.value = stateSelectedItemsMap[previousRoundState]
        } else {
            return
        }
    }

    fun onOptionSelect(mainItem: MainItem) {
        val currentRoundState = mainRoundState.value ?: return
        val selectedItems = stateSelectedItemsMap[currentRoundState] ?: return
        when {
            selectedItems.contains(mainItem) -> {
                selectedItems.remove(mainItem)
            }
            selectedItems.size < currentRoundState.selectableCount -> {
                selectedItems.add(mainItem)
            }
            selectedItems.size == currentRoundState.selectableCount -> {
                selectedItems.removeLastOrNull()
                selectedItems.add(mainItem)
            }
            else -> return
        }
        mutableSelectedItems.value = selectedItems
    }

    private fun postSelectedItemsToRemote() = coroutineScope.launch {
        withContext(Dispatchers.IO) {
            // @TODO: Not implemented yet.
            delay((Math.random() * 2000).toLong())
            mutableIsTaskFinished.postValue(true)
        }
    }

    private fun isNextButtonEnabled(): Boolean {
        val selectableCount = mainRoundState.value?.selectableCount ?: return false
        val selectedCount = selectedItems.value?.size ?: return false
        return selectedCount in 1..selectableCount
    }
}