package com.beside.whatmeal.tutorial

import com.beside.whatmeal.data.SettingLocalDataSource

class TutorialPresenterImpl(
    private val view: TutorialView,
    private val settingLocalDataSource: SettingLocalDataSource
) : TutorialPresenter {

    override fun onClickStartButton() {
        settingLocalDataSource.setTutorialShown(true)
        view.startSurveyActivity()
    }
}