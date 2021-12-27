package com.beside.whatmeal.tutorial.presenter

import com.beside.whatmeal.data.local.SettingLocalDataSource
import com.beside.whatmeal.tutorial.TutorialActivityInterface

class TutorialPresenterImpl(
    // @TODO: Please rename it.
    private val view: TutorialActivityInterface,
    private val settingLocalDataSource: SettingLocalDataSource
) : TutorialPresenter {

    override fun onClickStartButton() {
        settingLocalDataSource.setTutorialShown(true)
        view.startSurveyActivity()
    }
}