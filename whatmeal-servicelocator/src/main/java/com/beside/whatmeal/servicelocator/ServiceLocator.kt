package com.beside.whatmeal.servicelocator

import android.content.Context
import android.util.Log

fun <T : Any> Context.getInstance(factory: InstantiationFactory<T>): T =
    ServiceLocator.createInstance(this.applicationContext, factory)

fun <T : Any> Context.getInstance(factory: ClassLoaderFactory<T>): T =
    ServiceLocator.createInstanceByClassLoader(this.applicationContext, factory)

@Suppress("UNCHECKED_CAST")
internal object ServiceLocator {
    private val factoryInstanceMap: MutableMap<WhatMealSingletonFactory, Any> = mutableMapOf()

    fun <T : Any> createInstance(context: Context, factory: InstantiationFactory<T>): T {
        val cachedInstance = factoryInstanceMap[factory] as? T
        if (cachedInstance != null) {
            return cachedInstance
        }

        val instance = factory.createInstance(context)
        factoryInstanceMap[factory] = instance
        return instance
    }

    fun <T : Any> createInstanceByClassLoader(
        context: Context,
        factory: ClassLoaderFactory<T>
    ): T {
        val instantiationFactory =
            Class.forName(
                factory.getInstantiationFactoryClassName(),
                true,
                javaClass.classLoader
            ).newInstance() as InstantiationFactory<T>
        return createInstance(context, instantiationFactory)
    }
}

private interface WhatMealSingletonFactory

abstract class InstantiationFactory<T : Any> : WhatMealSingletonFactory {
    abstract fun createInstance(context: Context): T
}

abstract class ClassLoaderFactory<T : Any> : WhatMealSingletonFactory {
    abstract fun getInstantiationFactoryClassName(): String
}