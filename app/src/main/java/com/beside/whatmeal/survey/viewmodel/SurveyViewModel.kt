package com.beside.whatmeal.survey.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.beside.whatmeal.common.progress.CommonProgressViewModel
import com.beside.whatmeal.survey.uimodel.*
import kotlinx.coroutines.*

class SurveyViewModel : CommonProgressViewModel() {
    private val coroutineScope: CoroutineScope = viewModelScope

    private val mutableSurveyViewState: MutableLiveData<SurveyViewState> =
        MutableLiveData(SurveyViewState.ROUND)
    val surveyViewState: LiveData<SurveyViewState> = mutableSurveyViewState

    private val mutableSurveyRoundState: MutableLiveData<SurveyRoundState> =
        MutableLiveData(SurveyRoundState.AGE)
    val surveyRoundState: LiveData<SurveyRoundState> = mutableSurveyRoundState

    private val mutableAllItems: MediatorLiveData<List<SurveyItem>> =
        MediatorLiveData<List<SurveyItem>>().apply {
            addSource(surveyRoundState) {
                value = getAllItemsBy(surveyRoundState.value ?: return@addSource)
            }
        }
    val allItems: LiveData<List<SurveyItem>> = mutableAllItems

    private val stateSelectedItemsMap: MutableMap<SurveyRoundState, MutableList<SurveyItem>> =
        mutableMapOf<SurveyRoundState, MutableList<SurveyItem>>().apply {
            SurveyRoundState.values().forEach { this[it] = mutableListOf() }
        }
    private val mutableSelectedItems: MutableLiveData<List<SurveyItem>> = MutableLiveData(listOf())
    val selectedItems: LiveData<List<SurveyItem>> = mutableSelectedItems

    private val mutableNextButtonEnabled: MediatorLiveData<Boolean> =
        MediatorLiveData<Boolean>().apply {
            value = false
            addSource(surveyRoundState) { value = isNextButtonEnabled() }
            addSource(selectedItems) { value = isNextButtonEnabled() }
        }
    val nextButtonEnabled: LiveData<Boolean> = mutableNextButtonEnabled

    fun onNextClick() {
        val currentRoundState = surveyRoundState.value ?: return
        val lastPageOrder = SurveyRoundState.values().size
        val currentPageOrder = currentRoundState.pageOrder
        if (currentPageOrder < lastPageOrder) {
            changeRoundState(currentPageOrder + 1)
        } else if (currentPageOrder == lastPageOrder) {
            mutableSurveyViewState.value = SurveyViewState.PROGRESS
            postSelectedItemsToRemote()
        }
    }

    fun onBackPressed(runOSOnBackPressed: () -> Unit) {
        val viewState = surveyViewState.value
        val pageOrder = surveyRoundState.value?.pageOrder
        when {
            viewState == SurveyViewState.ROUND && pageOrder != 1 -> onUpButtonClick()
            viewState == SurveyViewState.ROUND && pageOrder == 1 -> runOSOnBackPressed()
            viewState != SurveyViewState.ROUND -> Unit
        }
    }

    fun onUpButtonClick() {
        val currentRoundState = surveyRoundState.value ?: return
        val currentPageOrder = currentRoundState.pageOrder
        val lastPageOrder = SurveyRoundState.values().size
        if (currentRoundState.pageOrder in 2..lastPageOrder) {
            changeRoundState(currentPageOrder - 1)
        } else {
            return
        }
    }

    fun onOptionSelect(surveyItem: SurveyItem) {
        val currentRoundState = surveyRoundState.value ?: return
        val selectedItems = stateSelectedItemsMap[currentRoundState] ?: return
        when {
            selectedItems.contains(surveyItem) -> {
                selectedItems.remove(surveyItem)
            }
            selectedItems.size < currentRoundState.necessarySelectionCount -> {
                selectedItems.add(surveyItem)
            }
            selectedItems.size == currentRoundState.necessarySelectionCount -> {
                selectedItems.removeLastOrNull()
                selectedItems.add(surveyItem)
            }
            else -> return
        }
        mutableSelectedItems.value = selectedItems
    }

    private fun changeRoundState(nextPageOrder: Int) {
        val nextRoundState = SurveyRoundState.of(nextPageOrder)
        mutableSurveyRoundState.value = nextRoundState
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
        val necessarySelectionCount = surveyRoundState.value?.necessarySelectionCount ?: return false
        val selectedCount = selectedItems.value?.size ?: return false
        return selectedCount == necessarySelectionCount
    }

    private fun getAllItemsBy(roundState: SurveyRoundState): List<SurveyItem> = when (roundState) {
        SurveyRoundState.AGE -> Age.values()
        SurveyRoundState.MEAL_TIME -> MealTime.values()
        SurveyRoundState.STANDARD -> Standard.values()
    }.toList()
}