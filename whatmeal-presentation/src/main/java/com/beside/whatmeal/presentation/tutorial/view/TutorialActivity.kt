package com.beside.whatmeal.presentation.tutorial.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import com.beside.whatmeal.presentation.survey.view.SurveyActivity
import com.beside.whatmeal.presentation.tutorial.presenter.TutorialPresenter
import com.beside.whatmeal.presentation.tutorial.presenter.TutorialPresenterImpl
import com.linecorp.lich.component.getComponent

class TutorialActivity : AppCompatActivity(), TutorialView {
    private val tutorialPresenter: TutorialPresenter by lazy {
        val whatMealBo = getComponent(WhatMealBoDelegator)
        TutorialPresenterImpl(this, whatMealBo)
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