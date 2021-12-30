package com.beside.whatmeal.presentation.common

import androidx.lifecycle.SavedStateHandle

fun <T> SavedStateHandle.getOrThrow(key: String): T = this.get(key) ?: throw IllegalArgumentException()