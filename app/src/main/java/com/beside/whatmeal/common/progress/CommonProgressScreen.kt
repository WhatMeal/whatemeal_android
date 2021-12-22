package com.beside.whatmeal.common.progress

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beside.whatmeal.R
import com.beside.whatmeal.compose.RoundedCornerLinearProgressIndicator
import com.beside.whatmeal.compose.WhatMealColor
import com.beside.whatmeal.compose.WhatMealTextStyle
import com.beside.whatmeal.main.MainViewModel
import com.beside.whatmeal.utils.observeAsNotNullState

@Composable
fun CommonProgressScreen(commonProgressViewModel: CommonProgressViewModelInterface) {
    val autoIncrementProgress
            by commonProgressViewModel.autoIncrementProgress.observeAsNotNullState()
    val loadingFinished by commonProgressViewModel.loadingFinished.observeAsNotNullState()
    val progress = when {
        loadingFinished && autoIncrementProgress == 0.99f -> 1f
        else -> autoIncrementProgress
    }

    Column(
        modifier = Modifier
            .background(color = WhatMealColor.Bg0)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.progress_logo),
            contentDescription = null
        )
        Text(
            text = "${(progress * 100).toInt()}%",
            style = WhatMealTextStyle.Bold,
            fontSize = 31.sp,
            modifier = Modifier.padding(top = 10.44.dp)
        )
        RoundedCornerLinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .padding(top = 48.dp)
                .width(248.dp)
                .height(8.dp)
        )
        Text(
            text = "입력하신 정보를 종합하여\n선택지를 구성하고 있습니다",
            style = WhatMealTextStyle.Medium,
            lineHeight = 25.sp,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 32.dp)
        )
    }
}

@Preview
@Composable
private fun CommonProgressScreenPreviewLoadingTrue() {
    CommonProgressScreen(MainViewModel())
}

@Preview
@Composable
private fun CommonProgressScreenPreviewLoadingFalse() {
    CommonProgressScreen(MainViewModel())
}