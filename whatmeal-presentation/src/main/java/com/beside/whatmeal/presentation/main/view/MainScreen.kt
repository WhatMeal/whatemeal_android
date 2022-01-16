package com.beside.whatmeal.presentation.main.view

import androidx.activity.compose.BackHandler
import androidx.annotation.FloatRange
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.beside.whatmeal.presentation.common.resource.WhatMealColor
import com.beside.whatmeal.presentation.common.resource.WhatMealTextStyle
import com.beside.whatmeal.presentation.common.view.Header
import com.beside.whatmeal.presentation.common.view.PrimaryButton
import com.beside.whatmeal.presentation.main.uimodel.*
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    roundState: MainRoundState,
    allItems: List<MainItem>,
    selectedItems: List<MainItem>,
    onUpButtonClick: () -> Unit,
    onOptionSelect: (MainItem) -> Unit,
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

    val nextButtonEnabled: Boolean = selectedItems.size in 1..roundState.selectableCount

    Column(
        modifier = Modifier
            .background(color = WhatMealColor.Bg0)
            .fillMaxSize()
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
                Progress(roundState.percentage)
                Description(
                    multiSelectableNoteVisible = roundState.selectableCount > 1,
                    boldDescriptionText = roundState.boldDescriptionText,
                    descriptionText = roundState.descriptionText
                )


                Box(modifier = Modifier.weight(0.5f))
                Selector(
                    onOptionSelect = onOptionSelect,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 17.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    selectedOptionColor = roundState.selectedOptionColor,
                    allItems = allItems,
                    selectedItems = selectedItems
                )
                Box(modifier = Modifier.weight(0.5f))

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
private fun Progress(@FloatRange(from = 0.0, to = 1.0) percentage: Float) {
    LinearProgressIndicator(
        progress = percentage,
        modifier = Modifier
            .height(8.dp)
            .fillMaxWidth(),
        color = WhatMealColor.Bg100,
        backgroundColor = Color(0xFFC4C4C4)
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
            .padding(top = 32.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
    )
    Text(
        text = descriptionText,
        color = WhatMealColor.Gray1,
        style = WhatMealTextStyle.Regular,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        modifier = Modifier
            .padding(top = 13.dp, start = 20.dp, end = 20.dp)
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
    selectedOptionColor: Color,
    allItems: List<MainItem>,
    selectedItems: List<MainItem>
) {
    when (allItems.size) {
        2 -> TwoOptionsSelector(
            onOptionSelect = onOptionSelect,
            modifier = modifier,
            selectedOptionColor = selectedOptionColor,
            allItems = allItems,
            selectedItems = selectedItems
        )
        4 -> FourOptionsSelector(
            onOptionSelect = onOptionSelect,
            modifier = modifier,
            selectedOptionColor = selectedOptionColor,
            allItems = allItems,
            selectedItems = selectedItems
        )
        7 -> SevenOptionsSelector(
            onOptionSelect = onOptionSelect,
            modifier = modifier,
            selectedOptionColor = selectedOptionColor,
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
    selectedOptionColor: Color,
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
                size = 120.dp,
                optionSelected = selectedItems.contains(mainItem),
                selectedOptionColor = selectedOptionColor,
                item = mainItem
            )
        }
    }
}

@Composable
private fun FourOptionsSelector(
    onOptionSelect: (MainItem) -> Unit,
    modifier: Modifier,
    selectedOptionColor: Color,
    allItems: List<MainItem>,
    selectedItems: List<MainItem>
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        CircleOption(
            onOptionSelect = onOptionSelect,
            size = 120.dp,
            optionSelected = selectedItems.contains(allItems[0]),
            selectedOptionColor = selectedOptionColor,
            item = allItems[0]
        )
        CircleOption(
            onOptionSelect = onOptionSelect,
            size = 120.dp,
            optionSelected = selectedItems.contains(allItems[1]),
            selectedOptionColor = selectedOptionColor,
            item = allItems[1]
        )
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        CircleOption(
            onOptionSelect = onOptionSelect,
            size = 120.dp,
            optionSelected = selectedItems.contains(allItems[2]),
            selectedOptionColor = selectedOptionColor,
            item = allItems[2]
        )
        CircleOption(
            onOptionSelect = onOptionSelect,
            size = 120.dp,
            optionSelected = selectedItems.contains(allItems[3]),
            selectedOptionColor = selectedOptionColor,
            item = allItems[3]
        )
    }
}

@Composable
private fun SevenOptionsSelector(
    onOptionSelect: (MainItem) -> Unit,
    modifier: Modifier,
    selectedOptionColor: Color,
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
                selectedOptionColor = selectedOptionColor,
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
    selectedOptionColor: Color,
    item: MainItem
) {
    val animatedColor = animateColorAsState(
        targetValue = if (optionSelected) selectedOptionColor else WhatMealColor.Bg20,
        animationSpec = tween(20, 0, LinearOutSlowInEasing)
    )
    Box(
        modifier = Modifier
            .then(modifier)
            .size(size)
            .clip(CircleShape)
            .clickable(
                indication = rememberRipple(
                    color = if (optionSelected) WhatMealColor.Bg20 else selectedOptionColor
                ),
                interactionSource = remember {
                    MutableInteractionSource()
                },
                onClick = { onOptionSelect(item) }
            )
            .background(animatedColor.value),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (item is MainWithIconItem) {
                Image(
                    painter = painterResource(id = item.iconDrawableRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(bottom = 4.dp)
                )
            }
            Text(
                text = item.text,
                style = WhatMealTextStyle.Medium,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

@Preview(heightDp = 750)
@Composable
private fun BasicPreviewPortrait(
    @PreviewParameter(MainScreenPreviewParameterProvider::class)
    params: MainScreenPreviewParameterProvider.MainScreenParams
) {
    MainScreen(
        params.roundState,
        params.allItems,
        params.selectedItems,
        params.onUpButtonClick,
        params.onOptionSelect,
        params.onNextClick
    )
}

@Preview(widthDp = 1080, heightDp = 800)
@Composable
private fun BasicPreviewLandScape(
    @PreviewParameter(MainScreenPreviewParameterProvider::class)
    params: MainScreenPreviewParameterProvider.MainScreenParams
) {
    MainScreen(
        params.roundState,
        params.allItems,
        params.selectedItems,
        params.onUpButtonClick,
        params.onOptionSelect,
        params.onNextClick
    )
}