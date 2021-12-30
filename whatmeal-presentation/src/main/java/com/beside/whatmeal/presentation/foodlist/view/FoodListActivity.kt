package com.beside.whatmeal.presentation.foodlist.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import com.beside.whatmeal.presentation.common.view.progress.CommonProgressScreen
import com.beside.whatmeal.presentation.common.getValue
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodItem
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodListFirstLoadingState
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodListPagingState
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodListViewAction
import com.beside.whatmeal.presentation.foodlist.viewmodel.FoodListViewModel
import com.beside.whatmeal.presentation.map.MapViewModel
import com.beside.whatmeal.presentation.common.observeAsNotNullState
import com.linecorp.lich.component.getComponent

class FoodListActivity : ComponentActivity() {
    private val foodListViewModel: FoodListViewModel by viewModels {
        val whatMealBo = getComponent(WhatMealBoDelegator)
        FoodListViewModel.Factory(whatMealBo, this, intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val firstLoadingState
                    by foodListViewModel.foodListFirstLoadingState.observeAsNotNullState()
            when (firstLoadingState) {
                FoodListFirstLoadingState.LOADING -> CommonProgressScreen(foodListViewModel)
                FoodListFirstLoadingState.DONE -> ShowFoodListScreen()
            }
        }

        foodListViewModel.foodListViewAction.subscribe(this, this::handleViewAction)
    }

    @Composable
    private fun ShowFoodListScreen() {
        val foodDataList: List<FoodItem> by foodListViewModel.foodItemList.observeAsNotNullState()
        val selectedFoodData: FoodItem? by foodListViewModel.selectedFoodItem.observeAsState()
        val pagingState: FoodListPagingState
                by foodListViewModel.pagingState.observeAsNotNullState()

        FoodListScreen(
            foodDataList,
            selectedFoodData,
            pagingState,
            foodListViewModel::onFoodSelect,
            foodListViewModel::onRefreshClick,
            foodListViewModel::onNextClick
        )
    }

    private fun handleViewAction(viewAction: FoodListViewAction) = when (viewAction) {
        is FoodListViewAction.FailToLoadFoodListOnFirst -> finish()
        is FoodListViewAction.StartMapScreen -> {
            val intent = MapViewModel.createIntent(this, viewAction.foodName)
            startActivity(intent)
        }
    }
}