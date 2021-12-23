package com.beside.whatmeal.main.uimodel

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

// @TODO: Fix it when figma is updated.
enum class MainRoundState(
    @IntRange(from = 1) val pageOrder: Int,
    val isUpButtonVisible: Boolean,
    val titleText: String,
    @FloatRange(from = 0.0, to = 1.0) val percentage: Float,
    @IntRange(from = 0) val selectableCount: Int,
    val boldDescriptionText: String,
    val descriptionText: String,
    val optionTextSize: TextUnit
) {
    BASIC(
        pageOrder = 1,
        isUpButtonVisible = false,
        titleText = "기본",
        percentage = 0f,
        selectableCount = 2,
        boldDescriptionText = "먼저, 기본을 구성 해보세요!",
        descriptionText = "선택항목이 없다면\n그 외를 선택해주세요:)",
        optionTextSize = 21.sp
    ),
    SOUP(
        pageOrder = 2,
        isUpButtonVisible = true,
        titleText = "국물 여부",
        percentage = 0.2f,
        selectableCount = 1,
        boldDescriptionText = "먼저, 기본을 구성 해보세요!",
        descriptionText = "선택항목이 없다면\n그 외를 선택해주세요:)",
        optionTextSize = 21.sp
    ),
    COOK(
        pageOrder = 3,
        isUpButtonVisible = true,
        titleText = "메인 조리 방법",
        percentage = 0.4f,
        selectableCount = 2,
        boldDescriptionText = "조리 방법을 선택하세요!",
        descriptionText = "선택항목이 없다면\n그 외를 선택해주세요:)",
        optionTextSize = 21.sp
    ),
    INGREDIENT(
        pageOrder = 4,
        isUpButtonVisible = true,
        titleText = "재료",
        percentage = 0.6f,
        selectableCount = 2,
        boldDescriptionText = "먼저, 기본을 구성해보세요!",
        descriptionText = "선택항목이 없다면\n그 외를 선택해주세요:)",
        optionTextSize = 16.sp
    ),
    STATE(
        pageOrder = 5,
        isUpButtonVisible = true,
        titleText = "상태",
        percentage = 0.8f,
        selectableCount = 2,
        boldDescriptionText = "먼저, 기본을 구성해보세요!",
        descriptionText = "선택항목이 없다면\n그 외를 선택해주세요:)",
        optionTextSize = 16.sp
    );

    companion object {
        private val PAGE_ORDER_VALUE_MAP = values().associateBy { it.pageOrder }

        fun of(@IntRange(from = 1, to = 5) pageOrder: Int): MainRoundState =
            PAGE_ORDER_VALUE_MAP[pageOrder]
                ?: throw IllegalArgumentException("Not supported pageOrder: $pageOrder")
    }
}