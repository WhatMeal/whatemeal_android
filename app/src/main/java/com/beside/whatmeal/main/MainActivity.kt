package com.beside.whatmeal.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.beside.whatmeal.data.SettingLocalDataSource
import com.beside.whatmeal.survey.SurveyActivity
import com.beside.whatmeal.tutorial.TutorialActivity
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    private val coroutineScope: CoroutineScope = lifecycleScope

    private val isPastMinimumTime: MutableLiveData<Boolean> = MutableLiveData(false)
    private val nextScreenIntent: MutableLiveData<Intent> = MutableLiveData()
    private val nextScreenIntentOrNull: MediatorLiveData<Intent> =
        MediatorLiveData<Intent>().apply {
            addSource(isPastMinimumTime) { value = getNextScreenIntentOrNull() }
            addSource(nextScreenIntent) { value = getNextScreenIntentOrNull() }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashScreen(installSplashScreen())
        waitMinimumTime()
        checkNextScreen()

        nextScreenIntentOrNull.observe(this) {
            it?.let {
                startActivity(it)
                finish()
            }
        }
    }

    private fun initSplashScreen(splashScreen: SplashScreen) {
        splashScreen.setKeepVisibleCondition { isPastMinimumTime.value != true }
    }

    private fun waitMinimumTime() = coroutineScope.launch {
        withContext(Dispatchers.IO) {
            delay(MINIMUM_TIME)
            isPastMinimumTime.postValue(true)
        }
    }

    private fun checkNextScreen() {
        val isTutorialShown = SettingLocalDataSource.isTutorialShown(this)
        val nextScreenIntent = if (isTutorialShown) {
            Intent(this, SurveyActivity::class.java)
        } else {
            Intent(this, TutorialActivity::class.java)
        }
        this.nextScreenIntent.value = nextScreenIntent
    }

    private fun getNextScreenIntentOrNull(): Intent? =
        if (isPastMinimumTime.value == true) {
            nextScreenIntent.value
        } else {
            null
        }

    companion object {
        private const val MINIMUM_TIME: Long = 2000L
    }
}