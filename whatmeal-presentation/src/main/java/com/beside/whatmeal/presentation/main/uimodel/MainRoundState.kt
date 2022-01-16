package com.beside.whatmeal.presentation.main.uimodel

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

// @TODO: Fix it when figma is updated.
enum class MainRoundState(
    @IntRange(from = 1) val pageOrder: Int,
    val hasHeader: Boolean,
    @FloatRange(from = 0.0, to = 1.0) val percentage: Float,
    @IntRange(from = 0) val selectableCount: Int,
    val boldDescriptionText: String,
    val descriptionText: String,
    val selectedOptionColor: Color
) {
    BASIC(
        pageOrder = 1,
        hasHeader = false,
        percentage = 0.2f,
        selectableCount = 1,
        boldDescriptionText = "먼저, 기본을 구성해 보세요!",
        descriptionText = "메뉴의 향방을 결정 짓는\n기본을 선택 해주세요!:)",
        selectedOptionColor = Color(0xFFF1EA47)
    ),
    SOUP(
        pageOrder = 2,
        hasHeader = true,
        percentage = 0.4f,
        selectableCount = 1,
        boldDescriptionText = "다음, 국물 여부를 선택하세요!",
        descriptionText = "국물이 있는 음식과 없는 음식은\n하늘과 땅 차이죠? ^_^",
        selectedOptionColor = Color(0xFFD898FF)
    ),
    COOK(
        pageOrder = 3,
        hasHeader = true,
        percentage = 0.6f,
        selectableCount = 1,
        boldDescriptionText = "조리 방법을 선택하세요!",
        descriptionText = "어떤 조리 방법이 끌리시나요?:)",
        selectedOptionColor = Color(0xFFB1F147)
    ),
    INGREDIENT(
        pageOrder = 4,
        hasHeader = true,
        percentage = 0.8f,
        selectableCount = 1,
        boldDescriptionText = "메인 재료를 선택하세요!",
        descriptionText = "어떤 재료를 골라 볼까요?\n결과까지 얼마 남지 않았어요!:)",
        selectedOptionColor = Color(0xFF9AAAFF)
    ),
    STATE(
        pageOrder = 5,
        hasHeader = true,
        percentage = 1f,
        selectableCount = 1,
        boldDescriptionText = "마지막으로,\n현재 기분을 선택해주세요!",
        descriptionText = "기분에 따라 추천드리는 메뉴가 달라집니다:)",
        selectedOptionColor = Color(0xFFFB9CF7)
    );

    companion object {
        private val PAGE_ORDER_VALUE_MAP = values().associateBy { it.pageOrder }

        fun of(@IntRange(from = 1, to = 5) pageOrder: Int): MainRoundState =
            PAGE_ORDER_VALUE_MAP[pageOrder]
                ?: throw IllegalArgumentException("Not supported pageOrder: $pageOrder")
    }
}