package com.beside.whatmeal.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import com.beside.whatmeal.presentation.splash.viewmodel.SplashViewModel
import com.beside.whatmeal.presentation.survey.view.SurveyActivity
import com.beside.whatmeal.presentation.tutorial.view.TutorialActivity
import com.beside.whatmeal.servicelocator.getInstance

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels {
        val whatMealBO = getInstance(WhatMealBoDelegator)
        SplashViewModel.Factory(whatMealBO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepVisibleCondition {
            splashViewModel.tutorialShownOrNull.value == null
        }

        splashViewModel.tutorialShownOrNull.observe(this) { tutorialShown ->
            if (tutorialShown == null) {
                return@observe
            }

            val activity =
                if (tutorialShown) SurveyActivity::class.java else TutorialActivity::class.java
            startActivity(Intent(this, activity))
            finish()
        }
    }
}