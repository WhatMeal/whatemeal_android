package com.beside.whatmeal.foodlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.beside.whatmeal.common.progress.CommonProgressScreen
import com.beside.whatmeal.foodlist.uimodel.FoodListFirstLoadingState
import com.beside.whatmeal.foodlist.view.FoodListScreen
import com.beside.whatmeal.foodlist.viewmodel.FoodListViewModel
import com.beside.whatmeal.utils.observeAsNotNullState

class FoodListActivity : ComponentActivity() {
    private val foodListViewModel: FoodListViewModel by viewModels {
        FoodListViewModel.Factory(FoodListViewActionHandler(this), this, intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val firstLoadingState
                    by foodListViewModel.foodListFirstLoadingState.observeAsNotNullState()
            when (firstLoadingState) {
                FoodListFirstLoadingState.LOADING -> CommonProgressScreen(foodListViewModel)
                FoodListFirstLoadingState.DONE -> FoodListScreen(foodListViewModel)
            }
        }
    }
}