package com.beside.whatmeal.survey.uimodel

sealed class SurveyViewAction {
    data class StartMainScreenAction(
        val age: String,
        val mealTime: String,
        val standards: String
    ) : SurveyViewAction()
}