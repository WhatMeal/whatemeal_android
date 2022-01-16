package com.beside.whatmeal.presentation.foodlist.view

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.beside.whatmeal.presentation.R
import com.beside.whatmeal.presentation.common.view.PrimaryButton
import com.beside.whatmeal.presentation.common.resource.WhatMealColor
import com.beside.whatmeal.presentation.common.resource.WhatMealTextStyle
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodItem
import com.beside.whatmeal.presentation.foodlist.uimodel.FoodListPagingState

@Composable
fun FoodListScreen(
    foodItemList: List<FoodItem>,
    selectedFoodItem: FoodItem?,
    pagingState: FoodListPagingState,
    onFoodSelect: (FoodItem) -> Unit,
    onRefreshClick: () -> Unit,
    onNextClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .background(color = WhatMealColor.Bg0)
            .fillMaxSize()
    ) {
        val verticalScrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(state = verticalScrollState)
        ) {
            Title()
            Description()

            Box(modifier = Modifier.weight(0.3f))

            AnimatedContent(
                targetState = pagingState,
                transitionSpec = {
                    if (pagingState == FoodListPagingState.LOADING) {
                        fadeIn(0f, tween(500)) with fadeOut(1f, tween(0))
                    } else {
                        fadeIn(1f, tween(0)) with fadeOut(0f, tween(500))
                    }
                }
            ) {
                if (pagingState == FoodListPagingState.LOADING) {
                    ListPlaceHolder()
                } else {
                    FoodListView(
                        foodDataList = foodItemList,
                        selectedFoodItem = selectedFoodItem,
                        onFoodSelect = onFoodSelect
                    )
                }
            }
            Box(modifier = Modifier.weight(0.3f))

            RefreshButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onRefreshClick = onRefreshClick,
                pagingState = pagingState
            )

            Box(modifier = Modifier.weight(0.3f))

            NextButton(
                onNextClick = onNextClick,
                enabled = selectedFoodItem != null
            )
        }
    }
}

@Composable
private fun Title() = Text(
    text = "추천 메뉴 리스트",
    color = WhatMealColor.Gray1,
    style = WhatMealTextStyle.Bold,
    fontSize = 21.sp,
    modifier = Modifier
        .padding(top = 44.dp, start = 28.dp, end = 28.dp)
        .fillMaxWidth()
)

@Composable
private fun Description() {
    Text(
        text = "왓밀이 추천하는 메뉴들입니다!\n원하는 메뉴를 선택하면 지도에서 찾아 드릴께요!:)",
        modifier = Modifier
            .padding(top = 13.dp, start = 28.dp, end = 28.dp)
            .fillMaxWidth(),
        color = Color.Black,
        fontSize = 14.sp,
        lineHeight = 25.sp,
        style = WhatMealTextStyle.Medium
    )
}

@Composable
private fun ListPlaceHolder() {
    ConstraintLayout(
        modifier = Modifier
            .padding(top = 43.dp, bottom = 18.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (square, topRectangle) = createRefs()
        Canvas(
            modifier = Modifier
                .constrainAs(square) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .width(240.dp)
                .height(268.dp),
            onDraw = { drawRect(color = Color(0xFFC4C4C4)) }
        )
        Canvas(
            modifier = Modifier
                .constrainAs(topRectangle) {
                    top.linkTo(square.bottom, 30.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .width(113.dp)
                .height(26.dp),
            onDraw = { drawRect(color = Color(0xFFC4C4C4)) }
        )
    }
}

@Composable
private fun FoodListView(
    foodDataList: List<FoodItem>,
    selectedFoodItem: FoodItem?,
    onFoodSelect: (FoodItem) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .padding(top = 25.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(modifier = Modifier
                .width(((LocalConfiguration.current.screenWidthDp - 272) / 2).dp)
            )
        }
        items(
            items = foodDataList,
            itemContent = { item ->
                Card(
                    modifier = Modifier
                        .width(240.dp)
                        .height(360.dp),
                    elevation = 1.dp,
                    shape = RoundedCornerShape(16.dp),
                    border = if (item == selectedFoodItem) {
                        BorderStroke(6.dp, WhatMealColor.Brand100)
                    } else {
                        BorderStroke(0.3.dp, Color(0xFFBDBDBD))
                    },
                    indication = rememberRipple(color = WhatMealColor.Bg100),
                    onClick = { onFoodSelect(item) }
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                        val (image, divider, text) = createRefs()
                        Image(
                            painter = rememberImagePainter(data = item.imageUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .constrainAs(image) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(divider.bottom)
                                    centerHorizontallyTo(parent)
                                }
                                .height(270.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier
                            .constrainAs(divider) {
                                top.linkTo(image.bottom)
                                bottom.linkTo(text.top)
                                centerHorizontallyTo(parent)
                            }
                            .height(0.3.dp)
                            .fillMaxWidth()
                            .background(color = Color(0xFFBDBDBD))
                        )
                        Box(
                            modifier = Modifier
                                .constrainAs(text) {
                                    top.linkTo(divider.bottom)
                                    bottom.linkTo(parent.bottom)
                                    centerHorizontallyTo(parent)
                                }
                                .height(89.7.dp)
                        ) {
                            Text(
                                text = item.name,
                                modifier = Modifier
                                    .align(Alignment.Center),
                                style = WhatMealTextStyle.Bold,
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
        )
        item {
            Box(modifier = Modifier
                .width(((LocalConfiguration.current.screenWidthDp - 272) / 2).dp)
            )
        }
    }
}

@Composable
private fun RefreshButton(
    modifier: Modifier,
    onRefreshClick: () -> Unit,
    pagingState: FoodListPagingState
) {
    IconButton(
        onClick = onRefreshClick,
        modifier = Modifier
            .then(modifier)
            .padding(top = 25.dp, bottom = 25.dp)
            .size(40.dp)
            .clip(CircleShape),
        enabled = pagingState != FoodListPagingState.LOADING
    ) {
        Icon(
            painter = painterResource(R.drawable.refresh),
            contentDescription = null
        )
    }
}

@Composable
private fun NextButton(
    onNextClick: () -> Unit,
    enabled: Boolean
) = PrimaryButton(
    onClick = onNextClick,
    enabled = enabled,
    text = "찾기"
)

@Preview(heightDp = 750)
@Composable
private fun FoodListPreviewPortrait(
    @PreviewParameter(FoodListScreenPreviewParameterProvider::class)
    params: FoodListScreenPreviewParameterProvider.FoodListScreenParams
) {
    FoodListScreen(
        params.foodItemList,
        params.selectedFoodItem,
        params.pagingState,
        params.onFoodSelect,
        params.onRefreshClick,
        params.onNextClick
    )
}

@Preview(widthDp = 1080, heightDp = 360)
@Composable
private fun FoodListPreviewLandScape(
    @PreviewParameter(FoodListScreenPreviewParameterProvider::class)
    params: FoodListScreenPreviewParameterProvider.FoodListScreenParams
) {
    FoodListScreen(
        params.foodItemList,
        params.selectedFoodItem,
        params.pagingState,
        params.onFoodSelect,
        params.onRefreshClick,
        params.onNextClick
    )
}
