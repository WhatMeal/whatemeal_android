package com.beside.whatmeal.data.remote.remotemodel

interface SurveyData {
    val id: Int
}

enum class Age(override val id: Int) : SurveyData {
    TEENAGE(0),
    TWENTIES(1),
    THIRTIES(2),
    FORTIES(3),
    OVER_FIFTY(4),
    PRIVATE(5)
}

enum class MealTime(override val id: Int) : SurveyData {
    BREAKFAST(0),
    LUNCH(1),
    DINNER(2)
}

enum class Standard(override val id: Int) : SurveyData {
    TASTE(0),
    PRICE(1),
    TIME(2),
    REVIEW(3),
    PERSON(4)
}