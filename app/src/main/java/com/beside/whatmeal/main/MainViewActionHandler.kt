package com.beside.whatmeal.main

import com.beside.whatmeal.foodlist.viewmodel.FoodListViewModel
import com.beside.whatmeal.main.uimodel.MainViewAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewActionHandler constructor(
    private val activity: MainActivity
) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    fun postMainViewAction(action: MainViewAction) = coroutineScope.launch {
        when (action) {
            is MainViewAction.StartFoodListScreenAction -> startFoodListActivity(action)
        }
    }

    private fun startFoodListActivity(action: MainViewAction.StartFoodListScreenAction) {
        val intent = FoodListViewModel.createIntent(
            context = activity,
            action.basics,
            action.soup,
            action.cooks,
            action.ingredients,
            action.states
        )
        activity.startActivity(intent)
    }
}