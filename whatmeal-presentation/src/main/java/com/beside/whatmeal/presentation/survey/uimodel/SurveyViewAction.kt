package com.beside.whatmeal.presentation.survey.uimodel

sealed class SurveyViewAction {
    data class StartMainScreen(
        val age: Age,
        val mealTime: MealTime,
        val standard1: Standard,
        val standard2: Standard
    ) : SurveyViewAction()
}