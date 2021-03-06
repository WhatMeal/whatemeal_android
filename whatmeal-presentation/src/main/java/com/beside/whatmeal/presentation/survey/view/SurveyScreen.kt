package com.beside.whatmeal.presentation.survey.view

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beside.whatmeal.presentation.survey.uimodel.*
import com.beside.whatmeal.presentation.common.resource.WhatMealColor
import com.beside.whatmeal.presentation.common.resource.WhatMealTextStyle
import com.beside.whatmeal.presentation.common.view.Header
import com.beside.whatmeal.presentation.common.view.PrimaryButton
import kotlinx.coroutines.launch

@Composable
fun SurveyScreen(
    roundState: SurveyRoundState,
    allItems: List<SurveyItem>,
    selectedItems: List<SurveyItem>,
    onUpButtonClick: () -> Unit,
    onOptionSelect: (SurveyItem) -> Unit,
    onNextClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    BackHandler(
        enabled = roundState.pageOrder != 1,
        onBack = {
            coroutineScope.launch {
                scrollState.animateScrollTo(0, tween(300, 0, LinearOutSlowInEasing))
            }
            onUpButtonClick()
        }
    )

    val nextButtonEnabled: Boolean = selectedItems.size == roundState.necessarySelectionCount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhatMealColor.Bg0)
    ) {
        if (roundState.hasHeader) {
            Header(
                onUpButtonClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(0, tween(300, 0, LinearOutSlowInEasing))
                    }
                    onUpButtonClick()
                }
            )
        }
        AnimatedContent(
            targetState = roundState,
            transitionSpec = {
                if (targetState.pageOrder > initialState.pageOrder) {
                    slideInHorizontally(
                        initialOffsetX = { it * 3 },
                        animationSpec = tween(300, 0, FastOutSlowInEasing)
                    ) with slideOutHorizontally(
                        targetOffsetX = { -it * 3 },
                        animationSpec = tween(0, 0, FastOutSlowInEasing)
                    )
                } else {
                    slideInHorizontally(
                        initialOffsetX = { -it * 3 },
                        animationSpec = tween(300, 0, FastOutSlowInEasing)
                    ) with slideOutHorizontally(
                        targetOffsetX = { it * 3 },
                        animationSpec = tween(0, 0, FastOutSlowInEasing)
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(top = if (roundState.hasHeader) 0.dp else 44.dp)
            ) {

                Description(
                    boldDescriptionText = roundState.boldDescriptionText,
                    descriptionText = roundState.descriptionText
                )
                Selector(
                    onOptionSelect = onOptionSelect,
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, top = 30.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    allItems = allItems,
                    selectedItems = selectedItems,
                    listType = roundState.listType
                )
                Box(modifier = Modifier.weight(1f))

                NextButton(
                    onNextClick = {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0, tween(300, 0, LinearOutSlowInEasing))
                        }
                        onNextClick()
                    },
                    enabled = nextButtonEnabled
                )
            }
        }
    }
}

@Composable
private fun Description(
    boldDescriptionText: String,
    descriptionText: String
) {
    Text(
        text = boldDescriptionText,
        color = WhatMealColor.Gray1,
        style = WhatMealTextStyle.Bold,
        fontSize = 21.sp,
        lineHeight = 31.sp,
        modifier = Modifier
            .padding(top = 24.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
    )
    Text(
        text = descriptionText,
        color = WhatMealColor.Gray1,
        style = WhatMealTextStyle.Regular,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        modifier = Modifier
            .padding(top = 14.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun NextButton(
    onNextClick: () -> Unit,
    enabled: Boolean
) = PrimaryButton(
    onClick = onNextClick,
    enabled = enabled,
    text = "??????"
)

@Composable
private fun Selector(
    onOptionSelect: (SurveyItem) -> Unit,
    modifier: Modifier,
    allItems: List<SurveyItem>,
    selectedItems: List<SurveyItem>,
    listType: SurveyListType
) {
    when (listType) {
        SurveyListType.GRID -> GridSelector(
            onOptionSelect = onOptionSelect,
            modifier = modifier,
            allItems = allItems,
            selectedItems = selectedItems
        )
        SurveyListType.VERTICAL -> VerticalSelector(
            onOptionSelect = onOptionSelect,
            modifier = modifier,
            allItems = allItems,
            selectedItems = selectedItems
        )
    }
}

@Composable
private fun GridSelector(
    onOptionSelect: (SurveyItem) -> Unit,
    modifier: Modifier,
    allItems: List<SurveyItem>,
    selectedItems: List<SurveyItem>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.Top)
    ) {
        allItems.chunked(2).forEach { chunkedList ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally)
            ) {
                chunkedList.forEach {
                    RectangleOption(
                        onOptionSelect = onOptionSelect,
                        optionSelected = selectedItems.contains(it),
                        item = it,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun VerticalSelector(
    onOptionSelect: (SurveyItem) -> Unit,
    modifier: Modifier,
    allItems: List<SurveyItem>,
    selectedItems: List<SurveyItem>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.Top)
    ) {
        allItems.forEach { surveyItem ->
            WideRectangleOption(
                onOptionSelect = onOptionSelect,
                optionSelected = selectedItems.contains(surveyItem),
                item = surveyItem
            )
        }
    }
}

@Composable
private fun RectangleOption(
    onOptionSelect: (SurveyItem) -> Unit,
    modifier: Modifier,
    optionSelected: Boolean,
    item: SurveyItem
) {
    val animatedColor = animateColorAsState(
        targetValue = if (optionSelected) WhatMealColor.Brand100 else WhatMealColor.Bg20,
        animationSpec = tween(20, 0, LinearOutSlowInEasing)
    )
    Box(
        modifier = Modifier
            .then(modifier)
            .width(156.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(
                indication = rememberRipple(
                    color = if (optionSelected) WhatMealColor.Bg20 else WhatMealColor.Brand100
                ),
                interactionSource = remember {
                    MutableInteractionSource()
                },
                onClick = { onOptionSelect(item) }
            )
            .background(animatedColor.value),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = item.text,
            style = WhatMealTextStyle.Medium,
            fontSize = 16.sp,
            color = if (optionSelected) Color.White else Color.Black
        )
    }
}

@Preview
@Composable
private fun RectangleOptionPreviewSelected() = RectangleOption(
    onOptionSelect = {},
    optionSelected = true,
    item = Age.TEENAGE,
    modifier = Modifier
)

@Preview
@Composable
private fun RectangleOptionPreviewNotSelected() = RectangleOption(
    onOptionSelect = {},
    optionSelected = false,
    item = Age.PRIVATE,
    modifier = Modifier
)

@Composable
private fun WideRectangleOption(
    onOptionSelect: (SurveyItem) -> Unit,
    optionSelected: Boolean,
    item: SurveyItem
) {
    val animatedColor = animateColorAsState(
        targetValue = if (optionSelected) WhatMealColor.Brand100 else WhatMealColor.Bg20,
        animationSpec = tween(20, 0, LinearOutSlowInEasing)
    )
    Row(
        modifier = Modifier
            .height(62.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(
                indication = rememberRipple(
                    color = if (optionSelected) WhatMealColor.Bg20 else WhatMealColor.Brand100
                ),
                interactionSource = remember {
                    MutableInteractionSource()
                },
                onClick = { onOptionSelect(item) }
            )
            .background(animatedColor.value),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item is SurveyWithIconItem) {
            Image(
                painter = painterResource(id = item.iconDrawableRes),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(24.dp)
            )
        }
        Text(
            text = item.text,
            style = WhatMealTextStyle.Medium,
            color = if (optionSelected) Color.White else Color.Black,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp, end = 20.dp)
        )
    }
}

@Preview
@Composable
private fun WideRectangleOptionPreviewSelected() = WideRectangleOption(
    onOptionSelect = {},
    optionSelected = true,
    item = Standard.TASTE
)

@Preview
@Composable
private fun WideRectangleOptionPreviewNotSelected() = WideRectangleOption(
    onOptionSelect = {},
    optionSelected = false,
    item = Standard.PRICE
)

@Preview
@Composable
fun SurveyPreview(
    @PreviewParameter(SurveyScreenPreviewParameterProvider::class)
    params: SurveyScreenPreviewParameterProvider.SurveyScreenParams
) {
    SurveyScreen(
        params.roundState,
        params.allItems,
        params.selectedItems,
        params.onUpButtonClick,
        params.onOptionSelect,
        params.onNextClick
    )
}
