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

class TutorialActivity : AppCompatActivity(), TutorialView {
    private val tutorialPresenter: TutorialPresenter by lazy {
        TutorialPresenterImpl(this, SettingLocalDataSource(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TutorialScreen(tutorialPresenter)
        }
    }

    override fun startSurveyActivity() {
        startActivity(Intent(this, SurveyActivity::class.java))
        finish()
    }
}