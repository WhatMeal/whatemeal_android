package com.beside.whatmeal.survey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.beside.whatmeal.survey.view.SurveyScreen
import com.beside.whatmeal.survey.viewmodel.SurveyViewModel

class SurveyActivity : ComponentActivity() {
    private val surveyViewModel: SurveyViewModel by viewModels {
        SurveyViewModel.Factory(SurveyViewActionHandler(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SurveyScreen(surveyViewModel)
        }
    }

    override fun onBackPressed() =
        surveyViewModel.onBackPressed(runOSOnBackPressed = { super.onBackPressed() })
}