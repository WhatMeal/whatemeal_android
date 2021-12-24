package com.beside.whatmeal.tutorial

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import com.beside.whatmeal.data.SettingLocalDataSource
import com.beside.whatmeal.survey.SurveyActivity
import com.beside.whatmeal.tutorial.presenter.TutorialPresenter
import com.beside.whatmeal.tutorial.presenter.TutorialPresenterImpl

@ExperimentalFoundationApi
class TutorialActivity : AppCompatActivity(), TutorialActivityInterface {
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