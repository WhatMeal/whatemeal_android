package com.beside.whatmeal.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.beside.whatmeal.common.progress.CommonProgressScreen
import com.beside.whatmeal.food.FoodListActivity
import com.beside.whatmeal.utils.observeAsNotNullState

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewState by mainViewModel.mainViewState.observeAsNotNullState()
            when (viewState) {
                MainViewState.ROUND -> {
                    MainScreen(mainViewModel)
                }
                MainViewState.LOADING -> {
                    CommonProgressScreen(mainViewModel)
                    mainViewModel.startAutoIncrement(1000L)
                }
                MainViewState.FINISH -> {
                    startActivity(Intent(this, FoodListActivity::class.java))
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() =
        mainViewModel.onBackPressed(runOSOnBackPressed = { super.onBackPressed() })
}