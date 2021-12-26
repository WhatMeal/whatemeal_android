package com.beside.whatmeal.foodlist.view

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.beside.whatmeal.R
import com.beside.whatmeal.compose.PrimaryButton
import com.beside.whatmeal.compose.WhatMealColor
import com.beside.whatmeal.compose.WhatMealTextStyle
import com.beside.whatmeal.compose.getValue
import com.beside.whatmeal.data.remote.model.Food
import com.beside.whatmeal.foodlist.uimodel.FoodListPagingState
import com.beside.whatmeal.foodlist.viewmodel.FoodListViewModelInterface as FoodListViewModel
import com.beside.whatmeal.foodlist.viewmodel.FoodListViewModelPreviewParameterProvider
import com.beside.whatmeal.utils.observeAsNotNullState

@Composable
fun FoodListScreen(viewModel: FoodListViewModel) {
    val foodList: List<Food> by viewModel.foodList.observeAsNotNullState()
    val selectedFood: Food? by viewModel.selectedFood.observeAsState()
    val pagingState: FoodListPagingState by viewModel.pagingState.observeAsNotNullState()

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

            Box(modifier = Modifier.weight(0.6f))

            AnimatedContent(
                targetState = pagingState,
                transitionSpec = {
                    if(pagingState == FoodListPagingState.LOADING) {
                        fadeIn(0f, tween(500)) with fadeOut(1f, tween(0))
                    } else {
                        fadeIn(1f, tween(0)) with fadeOut(0f, tween(500))
                    }
                }
            ) {
                if (pagingState == FoodListPagingState.LOADING) {
                    ListPlaceHolder()
                } else {
                    FoodListView(foodList = foodList, onFoodSelect = viewModel::onFoodSelect)
                }
            }

            Box(modifier = Modifier.weight(0.3f))

            RefreshButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onRefreshClick = viewModel::onRefreshClick,
                pagingState = pagingState
            )

            NextButton(
                onNextClick = viewModel::onNextClick,
                enabled = selectedFood != null
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
        .padding(top = 44.dp, start = 20.dp, end = 20.dp)
        .fillMaxWidth()
)

@Composable
private fun Description() {
    Text(
        text = "왓밀이 추천하는 메뉴입니다. 원하는 메뉴를 누르시면 주소검색 후 맛집을 추천해드릴게요.",
        modifier = Modifier
            .padding(top = 3.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        color = WhatMealColor.Gray1,
        fontSize = 14.sp,
        style = WhatMealTextStyle.Medium,
        softWrap = true
    )
}

@Composable
private fun ListPlaceHolder() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 72.dp, end = 37.dp)
    ) {
        val (square, topRectangle, bottomRectangle) = createRefs()
        Canvas(
            modifier = Modifier
                .constrainAs(square) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 53.dp)
                    end.linkTo(parent.end, 59.dp)
                }
                .size(248.dp),
            onDraw = { drawRect(color = Color(0xFFC4C4C4)) }
        )
        Canvas(
            modifier = Modifier
                .constrainAs(topRectangle) {
                    top.linkTo(square.bottom, 18.dp)
                    start.linkTo(square.start)
                }
                .width(159.dp)
                .height(26.dp),
            onDraw = { drawRect(color = Color(0xFFC4C4C4)) }
        )
        Canvas(
            modifier = Modifier
                .constrainAs(bottomRectangle) {
                    top.linkTo(topRectangle.bottom, 19.dp)
                    start.linkTo(square.start)
                }
                .width(113.dp)
                .height(26.dp),
            onDraw = { drawRect(color = Color(0xFFC4C4C4)) }
        )
    }
}

@Composable
private fun FoodListView(foodList: List<Food>, onFoodSelect: (Food) -> Unit) {
    LazyRow(
        modifier = Modifier
            .padding(top = 44.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        items(
            items = foodList,
            itemContent = { item ->
                Card(
                    modifier = Modifier
                        .width(240.dp)
                        .height(360.dp)
                        .padding(
                            start = if (foodList.indexOf(item) == 0) 20.dp else 0.dp,
                            end = 16.dp
                        ),
                    elevation = 1.dp,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(0.3.dp, Color(0xFFBDBDBD)),
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
            .padding(top = 37.dp)
            .size(40.dp)
            .clip(CircleShape),
        enabled = pagingState == FoodListPagingState.HAS_NEXT
    ) {
        when (pagingState) {
            FoodListPagingState.HAS_NEXT -> Icon(
                painter = painterResource(R.drawable.refresh),
                contentDescription = null
            )
            else -> {
                // @TODO: Not implemented yet.
            }
        }
    }
}

@Composable
private fun NextButton(
    onNextClick: () -> Unit,
    enabled: Boolean
) = PrimaryButton(
    onClick = onNextClick,
    enabled = enabled,
    text = "다음",
    modifier = Modifier.padding(top = 6.dp)
)

@Preview(heightDp = 750)
@Composable
private fun FoodListPreviewPortrait(
    @PreviewParameter(FoodListViewModelPreviewParameterProvider::class) viewModel: FoodListViewModel
) {
    FoodListScreen(viewModel)
}

@Preview(widthDp = 1080, heightDp = 360)
@Composable
private fun FoodListPreviewLandScape(
    @PreviewParameter(FoodListViewModelPreviewParameterProvider::class) viewModel: FoodListViewModel
) {
    FoodListScreen(viewModel)
}
