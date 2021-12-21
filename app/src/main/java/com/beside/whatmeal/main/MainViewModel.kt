package com.beside.whatmeal.main

import androidx.lifecycle.*
import com.beside.whatmeal.data.model.*

class MainViewModel : ViewModel() {
    private val mutableMainViewState: MutableLiveData<MainViewState> =
        MutableLiveData(MainViewState.ROUND)
    val mainViewState: LiveData<MainViewState> = mutableMainViewState

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

    fun onNextClick() {
        val currentRoundType = mainRoundType.value ?: return
        val lastPageOrder = MainRoundType.values().size
        val currentPageOrder = currentRoundType.pageOrder
        if (currentPageOrder < lastPageOrder) {
            val nextRoundType = MainRoundType.of(currentPageOrder + 1)
            mutableMainRoundType.value = nextRoundType
            mutableSelectedItems.value = stateSelectedItemsMap[nextRoundType]
        } else if (currentPageOrder == lastPageOrder) {
            // @TODO: Not implemented yet.
            mutableMainViewState.value = MainViewState.FINISH
            return
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

    private fun isNextButtonEnabled(): Boolean {
        val selectableCount = mainRoundType.value?.selectableCount ?: return false
        val selectedCount = selectedItems.value?.size ?: return false
        return selectableCount == selectedCount
    }
}