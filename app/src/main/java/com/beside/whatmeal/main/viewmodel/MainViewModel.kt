package com.beside.whatmeal.main.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.beside.whatmeal.common.progress.CommonProgressViewModel
import com.beside.whatmeal.foodlist.uimodel.FoodListFirstLoadingState
import com.beside.whatmeal.main.MainActivity
import com.beside.whatmeal.main.MainViewActionHandler
import com.beside.whatmeal.main.uimodel.*
import kotlinx.coroutines.*

class MainViewModel(
    savedState: SavedStateHandle,
    private val actionHandler: MainViewActionHandler
) : CommonProgressViewModel(), MainViewModelInterface {
    private val coroutineScope: CoroutineScope = viewModelScope
    private val mutableMainIdRequestState: MediatorLiveData<MainIdRequestState> =
        MediatorLiveData<MainIdRequestState>().apply {
            addSource(progressFinishEvent) { value = getMainIdRegistrationState() }
        }
    val mainIdRequestState: LiveData<MainIdRequestState> =
        mutableMainIdRequestState

    private fun getMainIdRegistrationState(): MainIdRequestState =
        if (progressFinishEvent.value != null) {
            MainIdRequestState.DONE
        } else {
            MainIdRequestState.IN_PROGRESS
        }

    init {
        requestIdToRemoteBy(savedState)
    }

    private fun requestIdToRemoteBy(savedState: SavedStateHandle) = coroutineScope.launch {
        mutableMainIdRequestState.value = MainIdRequestState.IN_PROGRESS
        startAutoIncrementProgress(1000L, ::onProgressFinished)

        // @TODO: Not implemented yet.
        withContext(Dispatchers.IO) {
            delay((Math.random() * 2000).toLong())
            mutableIsTaskFinished.postValue(true)
        }
    }

    private fun onProgressFinished() {
        mutableMainIdRequestState.value = MainIdRequestState.DONE
    }

    private val mutableMainRoundState: MutableLiveData<MainRoundState> =
        MutableLiveData(MainRoundState.BASIC)
    override val mainRoundState: LiveData<MainRoundState> = mutableMainRoundState

    private val mutableAllItems: MediatorLiveData<List<MainItem>> =
        MediatorLiveData<List<MainItem>>().apply {
            addSource(mainRoundState) {
                value = getAllItemsBy(mainRoundState.value ?: return@addSource)
            }
        }
    override val allItems: LiveData<List<MainItem>> = mutableAllItems

    private val stateSelectedItemsMap: MutableMap<MainRoundState, MutableList<MainItem>> =
        mutableMapOf<MainRoundState, MutableList<MainItem>>().apply {
            MainRoundState.values().forEach { this[it] = mutableListOf() }
        }
    private val mutableSelectedItems: MutableLiveData<List<MainItem>> = MutableLiveData(listOf())
    override val selectedItems: LiveData<List<MainItem>> = mutableSelectedItems

    override fun onNextClick() {
        val currentRoundState = mainRoundState.value ?: return
        val lastPageOrder = MainRoundState.values().size
        val currentPageOrder = currentRoundState.pageOrder
        if (currentPageOrder < lastPageOrder) {
            changeRoundState(currentPageOrder + 1)
        } else if (currentPageOrder == lastPageOrder) {
            actionHandler.postMainViewAction(stateSelectedItemsMap.toStartFoodListScreenAction())
        }
    }

    override fun onBackPressed(runOSOnBackPressed: () -> Unit) {
        val pageOrder = mainRoundState.value?.pageOrder
        when {
            pageOrder != 1 -> onUpButtonClick()
            pageOrder == 1 -> runOSOnBackPressed()
        }
    }

    override fun onUpButtonClick() {
        val currentRoundState = mainRoundState.value ?: return
        val currentPageOrder = currentRoundState.pageOrder
        val lastPageOrder = MainRoundState.values().size
        if (currentPageOrder in 2..lastPageOrder) {
            changeRoundState(currentPageOrder - 1)
        } else {
            return
        }
    }

    override fun onOptionSelect(mainItem: MainItem) {
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

    private fun getAllItemsBy(roundState: MainRoundState): List<MainItem> = when (roundState) {
        MainRoundState.BASIC -> Basic.values()
        MainRoundState.SOUP -> Soup.values()
        MainRoundState.COOK -> Cook.values()
        MainRoundState.INGREDIENT -> Ingredient.values()
        MainRoundState.STATE -> State.values()
    }.toList()

    private fun MutableMap<MainRoundState, MutableList<MainItem>>.toStartFoodListScreenAction()
            : MainViewAction.StartFoodListScreenAction = MainViewAction.StartFoodListScreenAction(
        basics = this[MainRoundState.BASIC]?.joinToString(",") { it.id.toString() } ?: "",
        soup = this[MainRoundState.SOUP]?.firstOrNull()?.toString() ?: "",
        cooks = this[MainRoundState.COOK]?.joinToString(",") { it.id.toString() } ?: "",
        ingredients = this[MainRoundState.INGREDIENT]?.joinToString(",") { it.id.toString() } ?: "",
        states = this[MainRoundState.STATE]?.joinToString(",") { it.id.toString() } ?: ""
    )

    class Factory(
        private val actionHandler: MainViewActionHandler,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T = MainViewModel(handle, actionHandler) as T
    }

    companion object {
        private const val TAG = "MainViewModel"

        private const val INTENT_PARAM_AGE = "age"
        private const val INTENT_PARAM_MEAL_TILE = "meal_time"
        private const val INTENT_PARAM_STANDARD = "standards"

        fun createIntent(
            context: Context,
            age: String,
            mealTime: String,
            standards: String
        ): Intent = Intent(context, MainActivity::class.java).putExtras(
            bundleOf(
                INTENT_PARAM_AGE to age,
                INTENT_PARAM_MEAL_TILE to mealTime,
                INTENT_PARAM_STANDARD to standards
            ).also { Log.i(TAG, "createIntent bundle: $it") }
        )
    }
}