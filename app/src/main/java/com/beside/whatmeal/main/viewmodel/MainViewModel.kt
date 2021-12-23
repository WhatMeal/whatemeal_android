package com.beside.whatmeal.main.viewmodel

import androidx.lifecycle.*
import com.beside.whatmeal.common.progress.CommonProgressViewModel
import com.beside.whatmeal.main.uimodel.*
import com.beside.whatmeal.survey.uimodel.SurveyItem
import kotlinx.coroutines.*

class MainViewModel : CommonProgressViewModel() {
    private val coroutineScope: CoroutineScope = viewModelScope

    private val mutableMainViewState: MutableLiveData<MainViewState> =
        MutableLiveData(MainViewState.ROUND)
    val mainViewState: LiveData<MainViewState> = mutableMainViewState

    private val mutableMainRoundState: MutableLiveData<MainRoundState> =
        MutableLiveData(MainRoundState.BASIC)
    val mainRoundState: LiveData<MainRoundState> = mutableMainRoundState

    private val mutableAllItems: MediatorLiveData<List<MainItem>> =
        MediatorLiveData<List<MainItem>>().apply {
            addSource(mainRoundState) {
                value = getAllItemsBy(mainRoundState.value ?: return@addSource)
            }
        }
    val allItems: LiveData<List<MainItem>> = mutableAllItems

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
            changeRoundState(currentPageOrder + 1)
        } else if (currentPageOrder == lastPageOrder) {
            mutableMainViewState.value = MainViewState.PROGRESS
            postSelectedItemsToRemote()
        }
    }

    fun onBackPressed(runOSOnBackPressed: () -> Unit) {
        val viewState = mainViewState.value
        val pageOrder = mainRoundState.value?.pageOrder
        when {
            viewState == MainViewState.ROUND && pageOrder != 1 -> onUpButtonClick()
            viewState == MainViewState.ROUND && pageOrder == 1 -> runOSOnBackPressed()
            viewState != MainViewState.ROUND -> Unit
        }
    }

    fun onUpButtonClick() {
        val currentRoundState = mainRoundState.value ?: return
        val currentPageOrder = currentRoundState.pageOrder
        val lastPageOrder = MainRoundState.values().size
        if (currentPageOrder in 2..lastPageOrder) {
            changeRoundState(currentPageOrder - 1)
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

    private fun changeRoundState(nextPageOrder: Int) {
        val nextRoundState = MainRoundState.of(nextPageOrder)
        mutableMainRoundState.value = nextRoundState
        mutableSelectedItems.value = stateSelectedItemsMap[nextRoundState]
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

    private fun getAllItemsBy(roundState: MainRoundState): List<MainItem> = when (roundState) {
        MainRoundState.BASIC -> Basic.values()
        MainRoundState.SOUP -> Soup.values()
        MainRoundState.COOK -> Cook.values()
        MainRoundState.INGREDIENT -> Ingredient.values()
        MainRoundState.STATE -> State.values()
    }.toList()
}