package com.beside.whatmeal.foodlist

import com.beside.whatmeal.foodlist.uimodel.FoodListViewAction
import com.beside.whatmeal.map.MapActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// @TODO: Please modify it so that the ViewModel doesn't know the Activity.
class FoodListViewActionHandler(private val activity: FoodListActivity) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    fun postFoodListViewAction(action: FoodListViewAction) = coroutineScope.launch {
        when (action) {
            is FoodListViewAction.FailToLoadFoodListOnFirst -> activity.finish()
            is FoodListViewAction.StartMapScreenAction -> startMapActivity(action)
        }
    }

    private fun startMapActivity(action: FoodListViewAction.StartMapScreenAction) {
        val intent = MapActivity.createIntent(context = activity, foodName = action.foodName)
        activity.startActivity(intent)
    }
}