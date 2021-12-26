package com.beside.whatmeal.survey.uimodel

import androidx.annotation.IntRange

// @TODO: Fix it when figma is updated.
enum class SurveyRoundState(
    @IntRange(from = 1) val pageOrder: Int,
    val hasHeader: Boolean,
    @IntRange(from = 0) val necessarySelectionCount: Int,
    val boldDescriptionText: String,
    val descriptionText: String,
    val listType: SurveyListType
) {
    AGE(
        pageOrder = 1,
        hasHeader = false,
        necessarySelectionCount = 1,
        boldDescriptionText = "안녕하세요:)\n나이가 어떻게 되세요?",
        descriptionText = "연령에 따라 선호 음식이 달라요.\n더욱 정교한 메뉴 추천을 해드릴게요.",
        listType = SurveyListType.GRID
    ),
    MEAL_TIME(
        pageOrder = 2,
        hasHeader = true,
        necessarySelectionCount = 1,
        boldDescriptionText = "현재 고민중인\n식사시간은 언제인가요?",
        descriptionText = "식사 시간별로 추천드리는 메뉴가 달라져요",
        listType = SurveyListType.VERTICAL
    ),
    STANDARD(
        pageOrder = 3,
        hasHeader = true,
        necessarySelectionCount = 2,
        boldDescriptionText = "메뉴 선택시 가장 중요하게\n생각하는 2가지는 무엇인가요?",
        descriptionText = "중요도에 따라 더 정교한 메뉴를 추천해 드릴게요",
        listType = SurveyListType.VERTICAL
    );

    companion object {
        private val PAGE_ORDER_VALUE_MAP = values().associateBy { it.pageOrder }

        fun of(@IntRange(from = 1, to = 3) pageOrder: Int): SurveyRoundState =
            PAGE_ORDER_VALUE_MAP[pageOrder]
                ?: throw IllegalArgumentException("Not supported pageOrder: $pageOrder")
    }
}

enum class SurveyListType {
    GRID, VERTICAL
}