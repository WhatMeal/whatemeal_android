package com.beside.whatmeal.presentation.survey.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.beside.whatmeal.presentation.common.getValue
import com.beside.whatmeal.presentation.main.viewmodel.MainViewModel
import com.beside.whatmeal.presentation.survey.uimodel.SurveyItem
import com.beside.whatmeal.presentation.survey.uimodel.SurveyRoundState
import com.beside.whatmeal.presentation.survey.uimodel.SurveyViewAction
import com.beside.whatmeal.presentation.survey.viewmodel.SurveyViewModel
import com.beside.whatmeal.presentation.common.observeAsNotNullState

class SurveyActivity : ComponentActivity() {
    private val surveyViewModel: SurveyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowSurveyScreen()
        }

        surveyViewModel.surveyViewAction.subscribe(this, this::handleViewAction)
    }

    @Composable
    private fun ShowSurveyScreen() {
        val roundState: SurveyRoundState by surveyViewModel.surveyRoundState.observeAsNotNullState()
        val allItems: List<SurveyItem> by surveyViewModel.allItems.observeAsNotNullState()
        val selectedItems: List<SurveyItem> by surveyViewModel.selectedItems.observeAsNotNullState()

        SurveyScreen(
            roundState,
            allItems,
            selectedItems,
            surveyViewModel::onUpButtonClick,
            surveyViewModel::onOptionSelect,
            surveyViewModel::onNextClick
        )
    }

    private fun handleViewAction(viewAction: SurveyViewAction) = when (viewAction) {
        is SurveyViewAction.StartMainScreen -> {
            val intent = MainViewModel.createIntent(
                context = this,
                viewAction.age,
                viewAction.mealTime,
                viewAction.standard1,
                viewAction.standard2
            )
            startActivity(intent)
        }
    }
}