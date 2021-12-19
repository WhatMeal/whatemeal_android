package com.beside.whatmeal.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.beside.whatmeal.data.SettingLocalDataSource
import com.beside.whatmeal.survey.SurveyActivity
import com.beside.whatmeal.tutorial.TutorialActivity

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.Factory(SettingLocalDataSource(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepVisibleCondition {
            mainViewModel.tutorialShownOrNull.value == null
        }

        mainViewModel.tutorialShownOrNull.observe(this) { tutorialShown ->
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