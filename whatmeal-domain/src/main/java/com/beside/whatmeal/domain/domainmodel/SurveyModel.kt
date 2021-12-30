package com.beside.whatmeal.domain.domainmodel

interface SurveyModel

enum class Age : SurveyModel {
    TEENAGE, TWENTIES, THIRTIES, FORTIES, OVER_FIFTY, PRIVATE
}

enum class MealTime : SurveyModel {
    BREAKFAST, LUNCH, DINNER
}

enum class Standard : SurveyModel {
    TASTE, PRICE, TIME, REVIEW, PERSON
}