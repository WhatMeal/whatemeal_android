package com.beside.whatmeal.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) = Button(
    onClick = onClick,
    modifier = Modifier
        .padding(start = 20.dp, end = 20.dp, bottom = 34.dp)
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
    content = content
)