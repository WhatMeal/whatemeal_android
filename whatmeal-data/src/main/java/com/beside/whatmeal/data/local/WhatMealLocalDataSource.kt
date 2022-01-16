package com.beside.whatmeal.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.annotation.VisibleForTesting
import com.beside.whatmeal.servicelocator.InstantiationFactory

class WhatMealLocalDataSource @VisibleForTesting constructor(private val context: Context) {
    fun isTutorialShown(): Boolean =
        context.getSharedPreferences(SETTING_SHARED_PREFERENCE, MODE_PRIVATE)
            .getBoolean(TUTORIAL_SHOWN, false)

    fun setTutorialShown(tutorialShown: Boolean) =
        context.getSharedPreferences(SETTING_SHARED_PREFERENCE, MODE_PRIVATE)
            .edit().putBoolean(TUTORIAL_SHOWN, tutorialShown).apply()

    companion object: InstantiationFactory<WhatMealLocalDataSource>() {
        private const val SETTING_SHARED_PREFERENCE = "setting_shared_preference"
        private const val TUTORIAL_SHOWN = "is_tutorial_shown"

        override fun createInstance(context: Context): WhatMealLocalDataSource =
            WhatMealLocalDataSource(context)
    }
}