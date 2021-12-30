package com.beside.whatmeal.presentation.tutorial.presenter

import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import com.beside.whatmeal.presentation.tutorial.view.TutorialView

class TutorialPresenterImpl(
    private val view: TutorialView,
    private val whatMealBo: WhatMealBoDelegator
) : TutorialPresenter {

    override fun onClickStartButton() {
        whatMealBo.setTutorialShown(true)
        view.startSurveyActivity()
    }
}