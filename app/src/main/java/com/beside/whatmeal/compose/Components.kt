package com.beside.whatmeal.compose

import androidx.annotation.FloatRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
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
) {
    val animatedColor = animateColorAsState(
        targetValue = if (enabled) WhatMealColor.Bg100 else WhatMealColor.Bg40,
        animationSpec = tween(100, 0, LinearOutSlowInEasing)
    )
    Button(
        onClick = onClick,
        indication = rememberRipple(color = WhatMealColor.Bg0),
        modifier = Modifier
            .then(modifier)
            .padding(top = 8.dp, start = 24.dp, end = 24.dp, bottom = 34.dp)
            .fillMaxWidth(),
        enabled = enabled,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = animatedColor.value,
            contentColor = WhatMealColor.Bg0,
            disabledBackgroundColor = animatedColor.value,
            disabledContentColor = WhatMealColor.Bg0
        ),
        contentPadding = PaddingValues(horizontal = 25.dp, vertical = 17.dp),
        elevation = null,
        content = {
            Text(
                text = text,
                style = WhatMealTextStyle.Medium,
                fontSize = 16.sp
            )
        }
    )
}

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication = rememberRipple(),
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        modifier = modifier,
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = elevation?.elevation(enabled, interactionSource)?.value ?: 0.dp,
        onClick = onClick,
        enabled = enabled,
        role = Role.Button,
        interactionSource = interactionSource,
        indication = indication
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = MaterialTheme.typography.button
            ) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

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
fun Header(onUpButtonClick: () -> Unit) =
    Column(
        modifier = Modifier
            .height(44.dp)
            .fillMaxWidth()
            .background(WhatMealColor.Bg0),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 7.dp, start = 15.dp, bottom = 7.dp)
                .clickable(
                    indication = rememberRipple(
                        bounded = false,
                        color = WhatMealColor.Bg100
                    ),
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    onClick = { onUpButtonClick() }
                )
        )
    }