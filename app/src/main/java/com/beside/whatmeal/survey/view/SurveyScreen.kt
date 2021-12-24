package com.beside.whatmeal.survey.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beside.whatmeal.compose.*
import com.beside.whatmeal.survey.uimodel.*
import com.beside.whatmeal.survey.viewmodel.SurveyViewModel
import com.beside.whatmeal.utils.observeAsNotNullState
import com.beside.whatmeal.utils.observeDistinctUntilChanged
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SurveyScreen(
    viewModel: SurveyViewModel
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    var previousRoundState: SurveyRoundState by rememberSaveableMutableStateOf(SurveyRoundState.AGE)
    val roundState: SurveyRoundState by viewModel.surveyRoundState.observeAsNotNullState()
    val scrollState = rememberLazyListState()
    if(previousRoundState != roundState) {
        previousRoundState = roundState
        coroutineScope.launch {
            scrollState.scrollToItem(0)
        }
    }

    val allItems: List<SurveyItem> by viewModel.allItems.observeAsNotNullState()
    val selectedItems: List<SurveyItem> by viewModel.selectedItems.observeAsNotNullState()
    val nextButtonEnabled: Boolean by viewModel.nextButtonEnabled.observeAsNotNullState()

    Column(
        modifier = Modifier
            .background(color = WhatMealColor.Bg0)
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = scrollState
        ) {
            stickyHeader {
                Header(
                    onUpButtonClick = { viewModel.onUpButtonClick() },
                    isUpButtonVisible = roundState.isUpButtonVisible
                )
            }
            item {
                Description(
                    boldDescriptionText = roundState.boldDescriptionText,
                    descriptionText = roundState.descriptionText
                )
                Selector(
                    onOptionSelect = { viewModel.onOptionSelect(it) },
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    allItems = allItems,
                    selectedItems = selectedItems,
                    listType = roundState.listType
                )
            }
        }
        NextButton(
            onNextClick = { viewModel.onNextClick() },
            enabled = nextButtonEnabled
        )
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
        lineHeight = 25.sp,
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
    text = "다음"
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
        allItems.chunked(2).forEach { (surveyItem1, surveyItem2) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally)
            ) {
                RectangleOption(
                    onOptionSelect = onOptionSelect,
                    optionSelected = selectedItems.contains(surveyItem1),
                    item = surveyItem1,
                    modifier = Modifier.weight(1f)
                )
                RectangleOption(
                    onOptionSelect = onOptionSelect,
                    optionSelected = selectedItems.contains(surveyItem2),
                    item = surveyItem2,
                    modifier = Modifier.weight(1f)
                )
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
    Box(
        modifier = Modifier
            .then(modifier)
            .width(156.dp)
            .height(110.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onOptionSelect(item) }
            .background(if (optionSelected) WhatMealColor.Brand100 else WhatMealColor.Bg20),
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
    Row(
        modifier = Modifier
            .height(62.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onOptionSelect(item) }
            .background(if (optionSelected) WhatMealColor.Brand100 else WhatMealColor.Bg20),
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

@ExperimentalFoundationApi
@Preview
@Composable
private fun SurveyPreview() = SurveyScreen(SurveyViewModel())
