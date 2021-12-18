package com.beside.whatmeal

import android.app.Application
import androidx.annotation.CallSuper

class WhatMealApplication : Application() {
    @CallSuper
    override fun onCreate() {
        super.onCreate()
    }
}