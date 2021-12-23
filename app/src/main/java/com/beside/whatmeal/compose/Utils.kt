package com.beside.whatmeal.compose

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
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