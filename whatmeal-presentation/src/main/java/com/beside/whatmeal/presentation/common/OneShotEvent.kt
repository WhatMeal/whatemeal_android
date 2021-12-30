package com.beside.whatmeal.presentation.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashSet

@Suppress("UNCHECKED_CAST")
abstract class OneShotEvent<T> {
    private val eventObservable: AutoSetChangedObservable = AutoSetChangedObservable()
    private val handledObserverSet: HashSet<Int> = HashSet()

    fun subscribe(owner: LifecycleOwner, onEventReceived: (T) -> Unit) {
        val observer = Observer { _, arg ->
            val hashCode = hashCode()
            if (arg == null || handledObserverSet.contains(hashCode)) {
                return@Observer
            }

            handledObserverSet.add(hashCode)
            owner.lifecycleScope.launch {
                onEventReceived(arg as T)
            }
        }
        eventObservable.addObserver(observer)

        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    eventObservable.deleteObserver(observer)
                    source.lifecycle.removeObserver(this)
                }
            }
        })
    }

    protected open fun post(event: T) {
        handledObserverSet.clear()
        eventObservable.notifyObservers(event)
    }

    private class AutoSetChangedObservable: Observable() {
        override fun notifyObservers(arg: Any?) {
            setChanged()
            super.notifyObservers(arg)
        }
    }
}

class MutableOneShotEvent<T>: OneShotEvent<T>() {
    public override fun post(event: T) = super.post(event)
}