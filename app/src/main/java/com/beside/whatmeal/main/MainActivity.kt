package com.beside.whatmeal.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.beside.whatmeal.Food.FoodListActivity
import com.beside.whatmeal.utils.observeAsNotNullState

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewState by mainViewModel.mainViewState.observeAsNotNullState()
            when (viewState) {
                MainViewState.ROUND -> MainScreen(viewModel = mainViewModel)
                MainViewState.LOADING,
                MainViewState.FINISH -> {
                    startActivity(Intent(this, FoodListActivity::class.java))
                    finish()
                }
            }
        }
    }
}