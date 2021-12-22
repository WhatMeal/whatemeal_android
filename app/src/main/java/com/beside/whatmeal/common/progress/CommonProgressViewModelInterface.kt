package com.beside.whatmeal.common.progress

import androidx.lifecycle.LiveData

interface CommonProgressViewModelInterface {
    val autoIncrementProgress: LiveData<Float>
    val loadingFinished: LiveData<Boolean>
}