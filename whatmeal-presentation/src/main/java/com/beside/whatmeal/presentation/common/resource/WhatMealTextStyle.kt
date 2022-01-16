package com.beside.whatmeal.presentation.common.resource

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import com.beside.whatmeal.presentation.R

object WhatMealTextStyle {
    val Bold: TextStyle = TextStyle(
        fontFamily = Font(R.font.spoqahansansneo_bold).toFontFamily(),
        letterSpacing = (-0.25).sp
    )

    val Medium: TextStyle = TextStyle(
        fontFamily = Font(R.font.spoqahansansneo_medium).toFontFamily(),
        letterSpacing = (-0.25).sp
    )

    val Regular: TextStyle = TextStyle(
        fontFamily = Font(R.font.spoqahansansneo_regular).toFontFamily(),
        letterSpacing = (-0.25).sp
    )
}
