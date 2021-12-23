package com.beside.whatmeal.survey

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import com.beside.whatmeal.common.progress.CommonProgressScreen
import com.beside.whatmeal.main.MainActivity
import com.beside.whatmeal.survey.uimodel.SurveyViewState
import com.beside.whatmeal.survey.view.SurveyScreen
import com.beside.whatmeal.survey.viewmodel.SurveyViewModel
import com.beside.whatmeal.utils.observeAsNotNullState
import com.beside.whatmeal.utils.observeNotNull

class SurveyActivity : ComponentActivity() {
    private val surveyViewModel: SurveyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewState by surveyViewModel.surveyViewState.observeAsNotNullState()
            when (viewState) {
                SurveyViewState.ROUND -> SurveyScreen(surveyViewModel)
                SurveyViewState.PROGRESS -> {
                    CommonProgressScreen(surveyViewModel)
                    surveyViewModel.startAutoIncrement(1000L)
                }
            }
        }

        surveyViewModel.progressFinishEvent.observeNotNull(this) {
            onProgressFinished()
        }
    }

    private fun onProgressFinished() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() =
        surveyViewModel.onBackPressed(runOSOnBackPressed = { super.onBackPressed() })
}