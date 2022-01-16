package com.beside.whatmeal.data.remote.remotemodel

interface SurveyData {
    val id: String
}

enum class Age(override val id: String) : SurveyData {
    TEENAGE("10"),
    TWENTIES("20대"),
    THIRTIES("30대"),
    FORTIES("40대"),
    OVER_FIFTY("50대 이상"),
    PRIVATE("선택하지 않음")
}

enum class MealTime(override val id: String) : SurveyData {
    BREAKFAST("아침 식사"),
    LUNCH("점심 식사"),
    DINNER("저녁 식사")
}

enum class Standard(override val id: String) : SurveyData {
    TASTE("맛"),
    PRICE("가격"),
    TIME("시간"),
    REVIEW("리뷰"),
    PERSON("함께 먹는 사람")
}