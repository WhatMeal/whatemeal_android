package com.beside.whatmeal.presentation.common.view.progress

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
import com.beside.whatmeal.presentation.R
import com.beside.whatmeal.presentation.common.view.RoundedCornerLinearProgressIndicator
import com.beside.whatmeal.presentation.common.resource.WhatMealColor
import com.beside.whatmeal.presentation.common.resource.WhatMealTextStyle
import com.beside.whatmeal.presentation.common.observeAsNotNullState

@Composable
fun CommonProgressScreen(commonProgressViewModel: CommonProgressViewModel) {
    val autoIncrementProgress
            by commonProgressViewModel.autoIncrementProgress.observeAsNotNullState()
    val loadingFinished by commonProgressViewModel.isTaskFinished.observeAsNotNullState()
    val progress = when {
        loadingFinished && autoIncrementProgress == CommonProgressViewModel.MAX_PROGRESS -> 1f
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
private fun CommonProgressScreenPreview() {
    CommonProgressScreen(object : CommonProgressViewModel() {})
}