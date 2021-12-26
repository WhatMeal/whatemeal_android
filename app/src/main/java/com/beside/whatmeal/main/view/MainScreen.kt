package com.beside.whatmeal.main.view

import android.annotation.SuppressLint
import androidx.annotation.FloatRange
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.beside.whatmeal.compose.*
import com.beside.whatmeal.main.uimodel.*
import com.beside.whatmeal.main.uimodel.State
import com.beside.whatmeal.main.viewmodel.MainViewModelInterface as MainViewModel
import com.beside.whatmeal.main.viewmodel.MainViewModelPreviewParameterProvider
import com.beside.whatmeal.utils.observeAsNotNullState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    var previousRoundState: MainRoundState by rememberSaveableMutableStateOf(MainRoundState.BASIC)
    val roundState: MainRoundState by viewModel.mainRoundState.observeAsNotNullState()
    val scrollState = rememberScrollState()
    if (previousRoundState != roundState) {
        previousRoundState = roundState
        coroutineScope.launch {
            scrollState.animateScrollTo(0, tween(300, 0, LinearOutSlowInEasing))
        }
    }

    val allItems: List<MainItem> by viewModel.allItems.observeAsNotNullState()
    val selectedItems: List<MainItem> by viewModel.selectedItems.observeAsNotNullState()
    val nextButtonEnabled: Boolean = selectedItems.size in 1..roundState.selectableCount

    Column(
        modifier = Modifier
            .background(color = WhatMealColor.Bg0)
            .fillMaxSize()
    ) {
        if (roundState.hasHeader) {
            Header(
                onUpButtonClick = { viewModel.onUpButtonClick() }
            )
        }
        AnimatedContent(
            targetState = roundState,
            transitionSpec = {
                if (targetState.pageOrder > initialState.pageOrder) {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(300, 0, LinearOutSlowInEasing)
                    ) with slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(300, 0, LinearOutSlowInEasing)
                    )
                } else {
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(300, 0, LinearOutSlowInEasing)
                    ) with slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(300, 0, LinearOutSlowInEasing)
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
                Title(text = "Round ${roundState.pageOrder}. ${roundState.titleText}")
                Progress(roundState.percentage)
                Description(
                    multiSelectableNoteVisible = roundState.selectableCount > 1,
                    boldDescriptionText = roundState.boldDescriptionText,
                    descriptionText = roundState.descriptionText
                )


                Box(modifier = Modifier.weight(0.3f))
                Selector(
                    onOptionSelect = { viewModel.onOptionSelect(it) },
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 17.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    optionTextSize = roundState.optionTextSize,
                    allItems = allItems,
                    selectedItems = selectedItems
                )
                Box(modifier = Modifier.weight(0.8f))

                NextButton(
                    onNextClick = { viewModel.onNextClick() },
                    enabled = nextButtonEnabled
                )
            }
        }
    }
}

@Composable
private fun Title(text: String) =
    Text(
        text = text,
        color = Color(0xFF4D4F52),
        style = WhatMealTextStyle.Medium,
        fontSize = 21.sp,
        modifier = Modifier
            .padding(top = 24.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
    )

@Composable
private fun Progress(@FloatRange(from = 0.0, to = 1.0) percentage: Float) {
    RoundedCornerLinearProgressIndicator(
        progress = percentage,
        modifier = Modifier
            .padding(top = 14.dp, start = 20.dp, end = 20.dp)
            .height(8.dp)
            .fillMaxWidth(),
        color = WhatMealColor.Bg100,
        backgroundColor = Color(0xFFC4C4C4)
    )
    Text(
        text = "${(percentage * 100).toInt()}%",
        color = Color(0xFF000000),
        style = WhatMealTextStyle.Bold,
        fontSize = 12.sp,
        textAlign = TextAlign.End,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun Description(
    multiSelectableNoteVisible: Boolean,
    boldDescriptionText: String,
    descriptionText: String
) {
    if (multiSelectableNoteVisible) {
        Text(
            text = "*중복선택 가능",
            color = WhatMealColor.Brand100,
            style = WhatMealTextStyle.Bold,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth()
        )
    }
    Text(
        text = boldDescriptionText,
        color = WhatMealColor.Gray1,
        style = WhatMealTextStyle.Bold,
        fontSize = 21.sp,
        lineHeight = 31.sp,
        modifier = Modifier
            .padding(top = 8.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
    )
    Text(
        text = descriptionText,
        color = WhatMealColor.Gray1,
        style = WhatMealTextStyle.Regular,
        fontSize = 14.sp,
        lineHeight = 25.sp,
        modifier = Modifier
            .padding(top = 5.dp, start = 20.dp, end = 20.dp)
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
    onOptionSelect: (MainItem) -> Unit,
    modifier: Modifier,
    optionTextSize: TextUnit,
    allItems: List<MainItem>,
    selectedItems: List<MainItem>
) {
    when (allItems.size) {
        2 -> TwoOptionsSelector(
            onOptionSelect = onOptionSelect,
            modifier = modifier,
            optionTextSize = optionTextSize,
            allItems = allItems,
            selectedItems = selectedItems
        )
        4 -> FourOptionsSelector(
            onOptionSelect = onOptionSelect,
            modifier = modifier,
            optionTextSize = optionTextSize,
            allItems = allItems,
            selectedItems = selectedItems
        )
        7 -> SevenOptionsSelector(
            onOptionSelect = onOptionSelect,
            modifier = modifier,
            optionTextSize = optionTextSize,
            allItems = allItems,
            selectedItems = selectedItems
        )
        else -> Box(modifier = modifier)
    }
}

@Composable
private fun TwoOptionsSelector(
    onOptionSelect: (MainItem) -> Unit,
    modifier: Modifier,
    optionTextSize: TextUnit,
    allItems: List<MainItem>,
    selectedItems: List<MainItem>
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        allItems.forEach { mainItem ->
            CircleOption(
                onOptionSelect = onOptionSelect,
                size = 124.dp,
                optionSelected = selectedItems.contains(mainItem),
                optionTextSize = optionTextSize,
                item = mainItem
            )
        }
    }
}

@Composable
private fun FourOptionsSelector(
    onOptionSelect: (MainItem) -> Unit,
    modifier: Modifier,
    optionTextSize: TextUnit,
    allItems: List<MainItem>,
    selectedItems: List<MainItem>
) {
    ConstraintLayout(modifier = modifier) {
        val (topOption, startOption, bottomOption, endOption, centerDummy) = createRefs()
        Canvas(
            modifier = Modifier.constrainAs(centerDummy) {
                top.linkTo(parent.top, margin = 158.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onDraw = { /* Do nothing */ }
        )
        val fourOptionsModifierArray = arrayOf(
            Modifier.constrainAs(topOption) { circular(centerDummy, 0f, 100.dp) },
            Modifier.constrainAs(startOption) { circular(centerDummy, 270f, 102.dp) },
            Modifier.constrainAs(bottomOption) { circular(centerDummy, 180f, 100.dp) },
            Modifier.constrainAs(endOption) { circular(centerDummy, 90f, 102.dp) },
        )
        allItems.forEachIndexed { index, mainItem ->
            CircleOption(
                onOptionSelect = onOptionSelect,
                modifier = fourOptionsModifierArray[index],
                size = 116.dp,
                optionSelected = selectedItems.contains(mainItem),
                optionTextSize = optionTextSize,
                item = mainItem
            )
        }
    }
}

@Composable
private fun SevenOptionsSelector(
    onOptionSelect: (MainItem) -> Unit,
    modifier: Modifier,
    optionTextSize: TextUnit,
    allItems: List<MainItem>,
    selectedItems: List<MainItem>
) {
    ConstraintLayout(modifier = modifier) {
        val (firstOption, secondOption, thirdOption, fourthOption, fifthOption) = createRefs()
        val (sixthOption, seventhOption, eightOption, centerDummy) = createRefs()
        Canvas(
            modifier = Modifier.constrainAs(centerDummy) {
                top.linkTo(parent.top, margin = 160.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onDraw = { /* Do nothing */ }
        )
        val sevenOptionsModifierArray = arrayOf(
            Modifier.constrainAs(firstOption) { circular(centerDummy, 330f, 110.dp) },
            Modifier.constrainAs(secondOption) { circular(centerDummy, 30f, 110.dp) },
            Modifier.constrainAs(thirdOption) { circular(centerDummy, 270f, 110.dp) },
            Modifier.constrainAs(fourthOption) { circular(centerDummy, 0f, 0.dp) },
            Modifier.constrainAs(fifthOption) { circular(centerDummy, 90f, 110.dp) },
            Modifier.constrainAs(sixthOption) { circular(centerDummy, 150f, 110.dp) },
            Modifier.constrainAs(seventhOption) { circular(centerDummy, 210f, 110.dp) },
            Modifier.constrainAs(eightOption) { circular(centerDummy, 0f, 110.dp) },
        )
        allItems.forEachIndexed { index, mainItem ->
            CircleOption(
                onOptionSelect = onOptionSelect,
                modifier = sevenOptionsModifierArray[index],
                size = 100.dp,
                optionSelected = selectedItems.contains(mainItem),
                optionTextSize = optionTextSize,
                item = mainItem
            )
        }
    }
}

@Composable
private fun CircleOption(
    onOptionSelect: (MainItem) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp,
    optionSelected: Boolean,
    optionTextSize: TextUnit,
    item: MainItem
) {
    val animatedColor = animateColorAsState(
        targetValue = if (optionSelected) WhatMealColor.Brand100 else WhatMealColor.Bg20,
        animationSpec = tween(20, 0, LinearOutSlowInEasing)
    )
    Box(
        modifier = Modifier
            .then(modifier)
            .size(size)
            .clip(CircleShape)
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
            fontSize = optionTextSize,
            color = if (optionSelected) Color.White else Color.Black
        )
    }
}

@Preview
@Composable
private fun CircleOptionPreviewSelected() =
    CircleOption({}, Modifier, 100.dp, true, 21.sp, Basic.BREAD)

@Preview
@Composable
private fun CircleOptionPreview() =
    CircleOption({}, Modifier, 100.dp, false, 16.sp, State.STRESS)


@Preview(heightDp = 750)
@Composable
private fun BasicPreviewPortrait(
    @PreviewParameter(MainViewModelPreviewParameterProvider::class) viewModel: MainViewModel
) {
    MainScreen(viewModel)
}

@Preview(widthDp = 1080, heightDp = 800)
@Composable
private fun BasicPreviewLandScape(
    @PreviewParameter(MainViewModelPreviewParameterProvider::class) viewModel: MainViewModel
) {
    MainScreen(viewModel)
}