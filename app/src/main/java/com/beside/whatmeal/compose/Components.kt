package com.beside.whatmeal.compose

import androidx.annotation.FloatRange
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beside.whatmeal.R

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String
) = Button(
    onClick = onClick,
    modifier = Modifier
        .padding(top = 8.dp, start = 24.dp, end = 24.dp, bottom = 34.dp)
        .fillMaxWidth()
        .then(modifier),
    enabled = enabled,
    shape = RoundedCornerShape(18.dp),
    colors = ButtonDefaults.buttonColors(
        backgroundColor = WhatMealColor.Bg100,
        contentColor = WhatMealColor.Bg0,
        disabledBackgroundColor = WhatMealColor.Bg40,
        disabledContentColor = WhatMealColor.Bg0
    ),
    contentPadding = PaddingValues(horizontal = 25.dp, vertical = 17.dp),
    content = {
        Text(
            text = text,
            style = WhatMealTextStyle.Medium,
            fontSize = 16.sp
        )
    }
)

@Preview(name = "EnabledPrimaryButton")
@Composable
private fun EnabledPrimaryButtonPreView() =
    PrimaryButton(onClick = { /* Do nothing */ }, text = "미리보기")

@Preview(name = "DisabledPrimaryButton")
@Composable
private fun DisabledPrimaryButtonPreView() =
    PrimaryButton(onClick = { /* Do nothing */ }, text = "미리보기", enabled = false)

@Composable
fun RoundedCornerLinearProgressIndicator(
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    modifier: Modifier = Modifier,
    color: Color = WhatMealColor.Bg100,
    backgroundColor: Color = Color(0xFFC4C4C4)
) = Canvas(
    modifier
        .progressSemantics(progress)
        .focusable()
) {
    val strokeWidth = size.height
    drawRoundedCornerLinearIndicator(1f, backgroundColor, strokeWidth)
    if (progress > 0.0f) {
        drawRoundedCornerLinearIndicator(progress, color, strokeWidth)
    }
}

private fun DrawScope.drawRoundedCornerLinearIndicator(
    progress: Float,
    color: Color,
    strokeWidth: Float
) {
    val width = size.width
    val radius = size.height / 2
    val barEnd = ((progress * width) - radius).coerceIn(0f + radius, width - radius)

    // Progress line
    drawLine(
        color = color,
        start = Offset(radius, radius),
        end = Offset(barEnd, radius),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}

@Preview
@Composable
private fun RoundedCornerLinearProgressIndicatorPreview() =
    Column(
        modifier = Modifier
            .width(360.dp)
            .height(100.dp)
            .padding(start = 20.dp, end = 20.dp)
            .background(color = WhatMealColor.Bg0)
    ) {
        Text(text = "0%")
        RoundedCornerLinearProgressIndicator(
            progress = 0f,
            modifier = Modifier
                .width(320.dp)
                .height(10.dp)
        )
        Text(text = "10%")
        RoundedCornerLinearProgressIndicator(
            progress = 0.1f,
            modifier = Modifier
                .width(320.dp)
                .height(10.dp)
        )
        Text(text = "100%")
        RoundedCornerLinearProgressIndicator(
            progress = 1f,
            modifier = Modifier
                .width(320.dp)
                .height(10.dp)
        )
    }

@Composable
fun Header(onUpButtonClick: () -> Unit, isUpButtonVisible: Boolean) =
    Column(
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
            .background(WhatMealColor.Bg0),
        verticalArrangement = Arrangement.Center
    ) {
        if (isUpButtonVisible) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 7.dp, start = 15.dp, bottom = 7.dp)
                    .clickable { onUpButtonClick() }
            )
        }
    }