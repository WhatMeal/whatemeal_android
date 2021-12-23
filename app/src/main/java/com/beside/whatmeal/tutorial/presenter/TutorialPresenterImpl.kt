package com.beside.whatmeal.tutorial.presenter

import com.beside.whatmeal.data.SettingLocalDataSource
import com.beside.whatmeal.tutorial.TutorialActivityInterface

class TutorialPresenterImpl(
    private val view: TutorialActivityInterface,
    private val settingLocalDataSource: SettingLocalDataSource
) : TutorialPresenter {

    override fun onClickStartButton() {
        settingLocalDataSource.setTutorialShown(true)
        view.startSurveyActivity()
    }
}