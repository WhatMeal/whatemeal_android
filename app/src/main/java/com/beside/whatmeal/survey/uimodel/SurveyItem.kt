package com.beside.whatmeal.survey.uimodel

import androidx.annotation.DrawableRes
import com.beside.whatmeal.R

interface SurveyItem {

    val text: String
    val id: Int
}

interface SurveyWithIconItem : SurveyItem {
    @get:DrawableRes
    val iconDrawableRes: Int
}

enum class Age(
    override val text: String,
    override val id: Int
) : SurveyItem {
    TEENAGE("10대", 0),
    TWENTIES("20대", 1),
    THIRTIES("30대", 2),
    FORTIES("40대", 3),
    OVER_FIFTY("50대 이상", 4),
    PRIVATE("선택하지 않음", 5)
}

enum class MealTime(
    @DrawableRes override val iconDrawableRes: Int,
    override val text: String,
    override val id: Int
) : SurveyWithIconItem {
    BREAKFAST(R.drawable.morning, "아침 식사", 0),
    LUNCH(R.drawable.lunch, "점심 식사", 1),
    DINNER(R.drawable.evening, "저녁 식사", 2)
}

enum class Standard(
    @DrawableRes override val iconDrawableRes: Int,
    override val text: String,
    override val id: Int
) : SurveyWithIconItem {
    TASTE(R.drawable.taste, "맛", 0),
    PRICE(R.drawable.money, "가격", 1),
    TIME(R.drawable.time, "시간", 2),
    REVIEW(R.drawable.review, "리뷰", 3),
    PERSON(R.drawable.person, "함께 먹는 사람", 4)
}