package com.beside.whatmeal.presentation.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import com.beside.whatmeal.presentation.common.getValue
import com.beside.whatmeal.presentation.common.view.progress.CommonProgressScreen
import com.beside.whatmeal.presentation.foodlist.viewmodel.FoodListViewModel
import com.beside.whatmeal.presentation.main.uimodel.MainIdRequestState
import com.beside.whatmeal.presentation.main.uimodel.MainItem
import com.beside.whatmeal.presentation.main.uimodel.MainRoundState
import com.beside.whatmeal.presentation.main.uimodel.MainViewAction
import com.beside.whatmeal.presentation.main.viewmodel.MainViewModel
import com.beside.whatmeal.presentation.common.observeAsNotNullState
import com.beside.whatmeal.servicelocator.getInstance

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        val whatMealBo = getInstance(WhatMealBoDelegator)
        MainViewModel.Factory(whatMealBo, this, intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val idRequestState by mainViewModel.mainIdRequestState.observeAsNotNullState()
            when (idRequestState) {
                MainIdRequestState.IN_PROGRESS -> CommonProgressScreen(mainViewModel)
                MainIdRequestState.DONE -> ShowMainScreen()
            }
        }

        mainViewModel.mainViewAction.subscribe(this, this::handleViewAction)
    }

    @Composable
    private fun ShowMainScreen() {
        val roundState: MainRoundState by mainViewModel.mainRoundState.observeAsNotNullState()
        val allItems: List<MainItem> by mainViewModel.allItems.observeAsNotNullState()
        val selectedItems: List<MainItem> by mainViewModel.selectedItems.observeAsNotNullState()

        MainScreen(
            roundState,
            allItems,
            selectedItems,
            mainViewModel::onUpButtonClick,
            mainViewModel::onOptionSelect,
            mainViewModel::onNextClick
        )
    }

    private fun handleViewAction(viewAction: MainViewAction) = when (viewAction) {
        is MainViewAction.StartFoodListScreen -> {
            val intent = FoodListViewModel.createIntent(
                context = this,
                viewAction.basics,
                viewAction.soup,
                viewAction.cooks,
                viewAction.ingredients,
                viewAction.states
            )
            startActivity(intent)
        }
        is MainViewAction.FailToRegisterTrackingId -> finish()
    }
}