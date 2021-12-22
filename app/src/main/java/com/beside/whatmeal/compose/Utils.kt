package com.beside.whatmeal.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.*
import kotlin.reflect.KProperty

@Composable
fun <T : Any> rememberSaveableMutableStateListOf(
    value: List<T>? = null
): SnapshotStateList<T> = rememberSaveable(
    saver = listSaver(
        save = { it.toList() },
        restore = { it.toMutableStateList() }
    )
) {
    value?.toMutableStateList() ?: mutableStateListOf()
}

@Composable
fun <T : Any> rememberSaveableMutableStateOf(value: T): MutableState<T> = rememberSaveable {
    mutableStateOf(value)
}

operator fun <T> SnapshotStateList<T>.getValue(thisObj: Any?, property: KProperty<*>) =
    this.toList()

@Composable
fun rememberSaveableAutoIncrementProgressMutableState(timeMillis: Long): MutableState<Float> {
    val coroutineScope = rememberCoroutineScope()
    return rememberSaveable {
        AutoIncrementProgressMutableState(0f, timeMillis, coroutineScope)
    }
}

private class AutoIncrementProgressMutableState(
    override var value: Float,
    private val timeMillis: Long,
    coroutineScope: CoroutineScope
): MutableState<Float> {
    init {
        val startTime = System.currentTimeMillis()
        coroutineScope.launch {
            var currentTime = System.currentTimeMillis()
            while (currentTime - startTime < timeMillis) {
                value = (currentTime - startTime) / timeMillis.toFloat()
                Log.e("progressValue.value", value.toString())
                delay(50)
                currentTime = System.currentTimeMillis()
            }
            value = 0.99f
        }
    }

    override operator fun component1(): Float = value
    override operator fun component2(): (Float) -> Unit = { value = it }
}

