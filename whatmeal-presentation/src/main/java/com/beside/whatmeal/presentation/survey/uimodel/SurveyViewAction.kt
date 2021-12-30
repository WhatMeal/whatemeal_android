package com.beside.whatmeal.presentation.survey.uimodel

sealed class SurveyViewAction {
    data class StartMainScreen(
        val age: Age,
        val mealTime: MealTime,
        val standards: List<Standard>
    ) : SurveyViewAction()
}