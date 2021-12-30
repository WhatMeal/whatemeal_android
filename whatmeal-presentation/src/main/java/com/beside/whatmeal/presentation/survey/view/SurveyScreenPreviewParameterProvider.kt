package com.beside.whatmeal.presentation.survey.view

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.beside.whatmeal.presentation.survey.uimodel.*

class SurveyScreenPreviewParameterProvider :
    PreviewParameterProvider<SurveyScreenPreviewParameterProvider.SurveyScreenParams> {
    override val values: Sequence<SurveyScreenParams> = sequenceOf(
        AGE, MEAL_TIME, STANDARD
    )

    companion object {
        private val AGE = SurveyScreenParams(
            roundState = SurveyRoundState.AGE,
            allItems = Age.values().toList(),
            selectedItems = listOf()
        )

        private val MEAL_TIME = SurveyScreenParams(
            roundState = SurveyRoundState.MEAL_TIME,
            allItems = MealTime.values().toList(),
            selectedItems = listOf(MealTime.BREAKFAST)
        )

        private val STANDARD = SurveyScreenParams(
            roundState = SurveyRoundState.STANDARD,
            allItems = Standard.values().toList(),
            selectedItems = listOf(Standard.PRICE, Standard.TASTE)
        )
    }

    data class SurveyScreenParams(
        val roundState: SurveyRoundState,
        val allItems: List<SurveyItem>,
        val selectedItems: List<SurveyItem>,
        val onUpButtonClick: () -> Unit = {},
        val onOptionSelect: (SurveyItem) -> Unit = {},
        val onNextClick: () -> Unit = {}
    )
}
