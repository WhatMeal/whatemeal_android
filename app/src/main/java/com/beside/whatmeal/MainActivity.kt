package com.beside.whatmeal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    private val coroutineScope: CoroutineScope = lifecycleScope
    private val shouldKeepShowSplashScreenLiveData: MutableLiveData<Boolean> =
        MutableLiveData(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashScreen(installSplashScreen())

        shouldKeepShowSplashScreenLiveData.observe(this) { shouldKeepShowSplashScreen ->
            if(!shouldKeepShowSplashScreen) {
                startActivity(Intent(this, TutorialActivity::class.java))
                finish()
            }
        }
    }

    private fun initSplashScreen(splashScreen: SplashScreen) {
        splashScreen.setKeepVisibleCondition { shouldKeepShowSplashScreenLiveData.value ?: false }

        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                delay(2000L)
                shouldKeepShowSplashScreenLiveData.postValue(false)
            }
        }
    }
}