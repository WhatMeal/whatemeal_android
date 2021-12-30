package com.beside.whatmeal.presentation.main.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.beside.whatmeal.presentation.common.*
import com.beside.whatmeal.presentation.common.view.progress.CommonProgressViewModel
import com.beside.whatmeal.presentation.main.uimodel.*
import com.beside.whatmeal.presentation.main.view.MainActivity
import com.beside.whatmeal.presentation.survey.uimodel.Age
import com.beside.whatmeal.presentation.survey.uimodel.MealTime
import com.beside.whatmeal.presentation.survey.uimodel.Standard
import kotlinx.coroutines.*

// @TODO: Please add unit test for it.
class MainViewModel(
    private val whatMealBo: WhatMealBoDelegator,
    savedState: SavedStateHandle
) : CommonProgressViewModel() {
    private val coroutineScope: CoroutineScope = viewModelScope

    private val mutableMainViewAction: MutableOneShotEvent<MainViewAction> = MutableOneShotEvent()
    val mainViewAction: OneShotEvent<MainViewAction> = mutableMainViewAction

    private val mutableMainIdRequestState: MediatorLiveData<MainIdRequestState> =
        MediatorLiveData<MainIdRequestState>().apply {
            addSource(progressFinishEvent) { value = getMainIdRegistrationState() }
        }
    val mainIdRequestState: LiveData<MainIdRequestState> = mutableMainIdRequestState

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

    private val mutableSelectedItems: MutableLiveData<List<MainItem>> = MutableLiveData(listOf())
    val selectedItems: LiveData<List<MainItem>> = mutableSelectedItems

    private val stateSelectedItemsMap: MutableMap<MainRoundState, MutableList<MainItem>> =
        mutableMapOf<MainRoundState, MutableList<MainItem>>().apply {
            MainRoundState.values().forEach { this[it] = mutableListOf() }
        }

    init {
        registerTrackingIdBy(savedState)
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

    fun onNextClick() {
        val currentRoundState = mainRoundState.value ?: return
        val lastPageOrder = MainRoundState.values().size
        val currentPageOrder = currentRoundState.pageOrder
        if (currentPageOrder < lastPageOrder) {
            changeRoundState(currentPageOrder + 1)
        } else if (currentPageOrder == lastPageOrder) {
            mutableMainViewAction.post(stateSelectedItemsMap.toStartFoodListScreenAction())
        }
    }

    private fun getMainIdRegistrationState(): MainIdRequestState =
        if (progressFinishEvent.value != null) {
            MainIdRequestState.DONE
        } else {
            MainIdRequestState.IN_PROGRESS
        }

    // TODO: Please move it to survey screen.
    private fun registerTrackingIdBy(savedState: SavedStateHandle) = coroutineScope.launch {
        mutableMainIdRequestState.value = MainIdRequestState.IN_PROGRESS
        startAutoIncrementProgress(1000L, ::onProgressFinished)

        whatMealBo.registerTrackingId(
            savedState.getOrThrow(INTENT_PARAM_AGE),
            savedState.getOrThrow(INTENT_PARAM_MEAL_TILE),
            savedState.getOrThrow(INTENT_PARAM_STANDARD)
        ).onSuccess {
            mutableIsTaskFinished.value = true
        }.onFailure {
            // TODO: Not implemented yet.
        }
    }

    private fun onProgressFinished() {
        mutableMainIdRequestState.value = MainIdRequestState.DONE
    }

    private fun changeRoundState(nextPageOrder: Int) {
        val nextRoundState = MainRoundState.of(nextPageOrder)
        mutableMainRoundState.value = nextRoundState
        mutableSelectedItems.value = stateSelectedItemsMap[nextRoundState]
    }

    private fun getAllItemsBy(roundState: MainRoundState): List<MainItem> = when (roundState) {
        MainRoundState.BASIC -> Basic.values()
        MainRoundState.SOUP -> Soup.values()
        MainRoundState.COOK -> Cook.values()
        MainRoundState.INGREDIENT -> Ingredient.values()
        MainRoundState.STATE -> State.values()
    }.toList()

    private fun MutableMap<MainRoundState, MutableList<MainItem>>.toStartFoodListScreenAction()
            : MainViewAction.StartFoodListScreen = MainViewAction.StartFoodListScreen(
        basics = getOrThrow(MainRoundState.BASIC),
        soup = this[MainRoundState.SOUP].firstOrThrow(),
        cooks = getOrThrow(MainRoundState.COOK),
        ingredients = getOrThrow(MainRoundState.INGREDIENT),
        states = getOrThrow(MainRoundState.STATE)
    )

    class Factory(
        private val whatMealBo: WhatMealBoDelegator,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T = MainViewModel(whatMealBo, handle) as T
    }

    companion object {
        private const val TAG = "MainViewModel"

        private const val INTENT_PARAM_AGE = "age"
        private const val INTENT_PARAM_MEAL_TILE = "meal_time"
        private const val INTENT_PARAM_STANDARD = "standards"

        fun createIntent(
            context: Context,
            age: Age,
            mealTime: MealTime,
            standards: List<Standard>
        ): Intent = Intent(context, MainActivity::class.java).putExtras(
            bundleOf(
                INTENT_PARAM_AGE to age,
                INTENT_PARAM_MEAL_TILE to mealTime,
                INTENT_PARAM_STANDARD to standards
            )
        )
    }
}