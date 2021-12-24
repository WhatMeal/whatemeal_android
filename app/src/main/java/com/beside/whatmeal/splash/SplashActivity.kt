package com.beside.whatmeal.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.beside.whatmeal.data.SettingLocalDataSource
import com.beside.whatmeal.splash.viewmodel.SplashViewModel
import com.beside.whatmeal.survey.SurveyActivity
import com.beside.whatmeal.tutorial.TutorialActivity
import com.beside.whatmeal.tutorial.TutorialActivityInterface

@ExperimentalFoundationApi
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels {
        SplashViewModel.Factory(SettingLocalDataSource(this))
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