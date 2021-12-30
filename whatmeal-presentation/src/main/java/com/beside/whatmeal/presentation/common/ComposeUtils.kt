package com.beside.whatmeal.presentation.common

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

@Composable
fun <T : Any> rememberSaveableMutableStateListOf(
    value: List<T>
): SnapshotStateList<T> = rememberSaveable(
    saver = listSaver(
        save = { it.toList() },
        restore = { it.toMutableStateList() }
    )
) {
    value.toMutableStateList()
}

@Composable
fun <T : Any> rememberSaveableMutableStateOf(value: T): MutableState<T> = rememberSaveable {
    mutableStateOf(value)
}

operator fun <T> SnapshotStateList<T>.getValue(thisObj: Any?, property: KProperty<*>) =
    this.toList()

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun <T> ((T) -> Unit).alsoAnimatedScrollToTop(
    scrollState: ScrollState
): (T) -> Unit {
    rememberCoroutineScope().launch {
        scrollState.animateScrollTo(0, tween(300, 0, LinearOutSlowInEasing))
    }
    return this
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun (() -> Unit).alsoAnimatedScrollToTop(
    scrollState: ScrollState
): () -> Unit {
    rememberCoroutineScope().launch {
        scrollState.animateScrollTo(0, tween(300, 0, LinearOutSlowInEasing))
    }
    return this
}