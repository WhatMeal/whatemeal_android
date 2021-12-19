package com.beside.whatmeal.tutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beside.whatmeal.R
import com.beside.whatmeal.compose.PrimaryButton
import com.beside.whatmeal.compose.WhatMealColor
import com.beside.whatmeal.compose.WhatMealTextStyle
import com.beside.whatmeal.data.SettingLocalDataSource
import com.beside.whatmeal.survey.SurveyActivity

class TutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TutorialScreen(
                onStartButtonClick = {
                    SettingLocalDataSource.setTutorialShown(this, true)
                    startActivity(Intent(this, SurveyActivity::class.java))
                    finish()
                }
            )
        }
    }

    @Composable
    fun TutorialScreen(onStartButtonClick: () -> Unit) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .background(color = WhatMealColor.Bg0)
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.tutorial_logo),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 73.57.dp)
            )
            Text(
                text = stringResource(id = R.string.tutorial_top_bold_description),
                style = WhatMealTextStyle.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 19.47.dp)
            )
            Text(
                text = stringResource(id = R.string.tutorial_top_regular_description),
                style = WhatMealTextStyle.Regular,
                color = WhatMealColor.Bg60,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.tutorial_top_image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = stringResource(id = R.string.tutorial_bottom_bold_description),
                style = WhatMealTextStyle.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 72.dp)
            )
            Text(
                text = stringResource(id = R.string.tutorial_bottom_regular_description),
                style = WhatMealTextStyle.Regular,
                color = WhatMealColor.Bg60,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.tutorial_bottom_image),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 32.dp, start = 45.dp, end = 45.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            PrimaryButton(
                onClick = onStartButtonClick,
                modifier = Modifier.padding(top = 56.dp),
                text = stringResource(id = R.string.tutorial_start_button_text)
            )
        }
    }

    @Preview
    @Composable
    private fun TutorialScreenPreview() = TutorialScreen { /* Do nothing */ }
}