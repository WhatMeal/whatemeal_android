package com.beside.whatmeal.data

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SettingLocalDataSource(private val context: Context) {
    fun isTutorialShown(): Boolean =
        context.getSharedPreferences(SETTING_SHARED_PREFERENCE, MODE_PRIVATE)
            .getBoolean(TUTORIAL_SHOWN, false)

    fun setTutorialShown(tutorialShown: Boolean) =
        context.getSharedPreferences(SETTING_SHARED_PREFERENCE, MODE_PRIVATE)
            .edit().putBoolean(TUTORIAL_SHOWN, tutorialShown).apply()

    companion object {
        private const val SETTING_SHARED_PREFERENCE = "setting_shared_preference"
        private const val TUTORIAL_SHOWN = "is_tutorial_shown"
    }
}