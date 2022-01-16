package com.beside.whatmeal.presentation.survey.uimodel

import androidx.annotation.DrawableRes
import com.beside.whatmeal.presentation.R

interface SurveyItem {
    val text: String
}

interface SurveyWithIconItem : SurveyItem {
    @get:DrawableRes
    val iconDrawableRes: Int
}

enum class Age(
    override val text: String
) : SurveyItem {
    TEENAGE("10대"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    OVER_FIFTY("50대 이상"),
    PRIVATE("선택하지 않음")
}

enum class MealTime(
    @DrawableRes override val iconDrawableRes: Int,
    override val text: String
) : SurveyWithIconItem {
    BREAKFAST(R.drawable.morning, "아침 식사"),
    LUNCH(R.drawable.lunch, "점심 식사"),
    DINNER(R.drawable.evening, "저녁 식사")
}

enum class Standard(
    @DrawableRes override val iconDrawableRes: Int,
    override val text: String
) : SurveyWithIconItem {
    TASTE(R.drawable.taste, "맛"),
    PRICE(R.drawable.money, "가격"),
    TIME(R.drawable.time, "시간"),
    REVIEW(R.drawable.review, "리뷰"),
    PERSON(R.drawable.person, "함께 먹는 사람")
}