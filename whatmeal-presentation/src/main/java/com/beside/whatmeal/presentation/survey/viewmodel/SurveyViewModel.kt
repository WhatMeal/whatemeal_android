package com.beside.whatmeal.presentation.survey.viewmodel

import androidx.lifecycle.*
import com.beside.whatmeal.presentation.survey.uimodel.*
import com.beside.whatmeal.presentation.common.MutableOneShotEvent
import com.beside.whatmeal.presentation.common.OneShotEvent
import com.beside.whatmeal.presentation.common.firstOrThrow
import com.beside.whatmeal.presentation.common.getOrThrow
import kotlinx.coroutines.*

// @TODO: Please add unit test for it.
class SurveyViewModel : ViewModel() {
    private val mutableSurveyViewAction: MutableOneShotEvent<SurveyViewAction> =
        MutableOneShotEvent()
    val surveyViewAction: OneShotEvent<SurveyViewAction> = mutableSurveyViewAction

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

    private val mutableSelectedItems: MutableLiveData<List<SurveyItem>> = MutableLiveData(listOf())
    val selectedItems: LiveData<List<SurveyItem>> = mutableSelectedItems

    private val stateSelectedItemsMap: MutableMap<SurveyRoundState, MutableList<SurveyItem>> =
        mutableMapOf<SurveyRoundState, MutableList<SurveyItem>>().apply {
            SurveyRoundState.values().forEach { this[it] = mutableListOf() }
        }

    fun onNextClick() {
        val currentRoundState = surveyRoundState.value ?: return
        val lastPageOrder = SurveyRoundState.values().size
        val currentPageOrder = currentRoundState.pageOrder
        if (currentPageOrder < lastPageOrder) {
            changeRoundState(currentPageOrder + 1)
        } else if (currentPageOrder == lastPageOrder) {
            val action = stateSelectedItemsMap.toStartMainScreenAction()
            mutableSurveyViewAction.post(action)
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

    private fun getAllItemsBy(roundState: SurveyRoundState): List<SurveyItem> = when (roundState) {
        SurveyRoundState.AGE -> Age.values()
        SurveyRoundState.MEAL_TIME -> MealTime.values()
        SurveyRoundState.STANDARD -> Standard.values()
    }.toList()

    private fun MutableMap<SurveyRoundState, MutableList<SurveyItem>>.toStartMainScreenAction()
            : SurveyViewAction.StartMainScreen {
        val standards: List<Standard> = this.getOrThrow(SurveyRoundState.STANDARD)
        return SurveyViewAction.StartMainScreen(
            age = this[SurveyRoundState.AGE].firstOrThrow(),
            mealTime = this[SurveyRoundState.MEAL_TIME].firstOrThrow(),
            standard1 = standards[0],
            standard2 = standards[1]
        )
    }
}