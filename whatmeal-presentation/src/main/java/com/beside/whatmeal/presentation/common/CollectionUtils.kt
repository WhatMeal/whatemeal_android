package com.beside.whatmeal.presentation.common

@Suppress("UNCHECKED_CAST")
fun <T, C: Any> MutableList<T>?.firstOrThrow(): C =
    this?.firstOrNull() as? C ?: throw IllegalStateException()

@Suppress("UNCHECKED_CAST")
fun <K, V, C> MutableMap<K, V>.getOrThrow(key: K): C =
    this[key] as? C ?: throw IllegalStateException()