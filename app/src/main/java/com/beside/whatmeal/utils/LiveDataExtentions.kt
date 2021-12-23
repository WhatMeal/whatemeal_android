package com.beside.whatmeal.utils

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.beside.whatmeal.compose.rememberSaveableMutableStateListOf

fun <T : Any> LiveData<T>.observeDistinctUntilChanged(
    lifecycleOwner: LifecycleOwner,
    onChanged: (T?) -> Unit
): Observer<T> {
    var isInitialized = false
    var previousValue: T? = null
    val observer = Observer<T> { t: T? ->
        if (!isInitialized || t != previousValue) {
            isInitialized = true
            previousValue = t
            onChanged(t)
        }
    }
    observe(lifecycleOwner, observer)
    return observer
}

fun <T : Any> LiveData<T>.observeNotNull(
    lifecycleOwner: LifecycleOwner,
    onChanged: (T) -> Unit
): Observer<T> {
    val observer = Observer<T> { t: T? ->
        if (t != null) {
            onChanged(t)
        }
    }
    observe(lifecycleOwner, observer)
    return observer
}

@Composable
fun <T : Any> LiveData<T>.observeAsNotNullState(): State<T> =
    observeAsState(value ?: throw NullPointerException("$value is null!"))

@Composable
fun <T : Any> LiveData<List<T>>.observeAsNotNullState(): SnapshotStateList<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val stateList = rememberSaveableMutableStateListOf(this.value)
    DisposableEffect(this, lifecycleOwner) {
        val observer = Observer<List<T>> {
            stateList.clear()
            stateList.addAll(it)
        }
        observe(lifecycleOwner, observer)
        onDispose { removeObserver(observer) }
    }
    return stateList
}
