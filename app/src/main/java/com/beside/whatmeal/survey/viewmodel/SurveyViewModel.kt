package com.beside.whatmeal.survey.viewmodel

import androidx.lifecycle.*
import com.beside.whatmeal.survey.SurveyViewActionHandler
import com.beside.whatmeal.survey.uimodel.*
import kotlinx.coroutines.*

// @TODO: Please add unit test for it.
class SurveyViewModel(
    private val actionHandler: SurveyViewActionHandler
) : ViewModel(), SurveyViewModelInterface {
    private val mutableSurveyRoundState: MutableLiveData<SurveyRoundState> =
        MutableLiveData(SurveyRoundState.AGE)
    override val surveyRoundState: LiveData<SurveyRoundState> = mutableSurveyRoundState

    private val mutableAllItems: MediatorLiveData<List<SurveyItem>> =
        MediatorLiveData<List<SurveyItem>>().apply {
            addSource(surveyRoundState) {
                value = getAllItemsBy(surveyRoundState.value ?: return@addSource)
            }
        }
    override val allItems: LiveData<List<SurveyItem>> = mutableAllItems

    private val stateSelectedItemsMap: MutableMap<SurveyRoundState, MutableList<SurveyItem>> =
        mutableMapOf<SurveyRoundState, MutableList<SurveyItem>>().apply {
            SurveyRoundState.values().forEach { this[it] = mutableListOf() }
        }
    private val mutableSelectedItems: MutableLiveData<List<SurveyItem>> = MutableLiveData(listOf())
    override val selectedItems: LiveData<List<SurveyItem>> = mutableSelectedItems

    override fun onNextClick() {
        val currentRoundState = surveyRoundState.value ?: return
        val lastPageOrder = SurveyRoundState.values().size
        val currentPageOrder = currentRoundState.pageOrder
        if (currentPageOrder < lastPageOrder) {
            changeRoundState(currentPageOrder + 1)
        } else if (currentPageOrder == lastPageOrder) {
            val action = stateSelectedItemsMap.toStartMainScreenAction()
            actionHandler.postSurveyViewAction(action)
        }
    }

    override fun onBackPressed(runOSOnBackPressed: () -> Unit) {
        val pageOrder = surveyRoundState.value?.pageOrder
        when {
            pageOrder != 1 -> onUpButtonClick()
            pageOrder == 1 -> runOSOnBackPressed()
        }
    }

    override fun onUpButtonClick() {
        val currentRoundState = surveyRoundState.value ?: return
        val currentPageOrder = currentRoundState.pageOrder
        val lastPageOrder = SurveyRoundState.values().size
        if (currentRoundState.pageOrder in 2..lastPageOrder) {
            changeRoundState(currentPageOrder - 1)
        } else {
            return
        }
    }

    override fun onOptionSelect(surveyItem: SurveyItem) {
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
            : SurveyViewAction.StartMainScreenAction = SurveyViewAction.StartMainScreenAction(
        age = this[SurveyRoundState.AGE]?.firstOrNull()?.toString() ?: "",
        mealTime = this[SurveyRoundState.MEAL_TIME]?.firstOrNull()?.toString() ?: "",
        standards = this[SurveyRoundState.STANDARD]?.joinToString(",") { it.id.toString() } ?: ""
    )

    class Factory(
        private val actionHandler: SurveyViewActionHandler
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            SurveyViewModel(actionHandler) as T
    }
}