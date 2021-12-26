package com.beside.whatmeal.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.beside.whatmeal.common.progress.CommonProgressScreen
import com.beside.whatmeal.main.uimodel.MainIdRequestState
import com.beside.whatmeal.main.view.MainScreen
import com.beside.whatmeal.main.viewmodel.MainViewModel
import com.beside.whatmeal.utils.observeAsNotNullState

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.Factory(MainViewActionHandler(this), this, intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val idRequestState by mainViewModel.mainIdRequestState.observeAsNotNullState()
            when (idRequestState) {
                MainIdRequestState.IN_PROGRESS -> CommonProgressScreen(mainViewModel)
                MainIdRequestState.DONE -> MainScreen(mainViewModel)
            }
        }
    }

    override fun onBackPressed() =
        mainViewModel.onBackPressed(runOSOnBackPressed = { super.onBackPressed() })
}