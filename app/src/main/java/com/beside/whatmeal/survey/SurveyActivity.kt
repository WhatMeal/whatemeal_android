package com.beside.whatmeal.survey

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import com.beside.whatmeal.main.MainActivity

class SurveyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("Hello world!!")
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}