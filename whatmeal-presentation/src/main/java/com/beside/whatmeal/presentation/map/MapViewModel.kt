package com.beside.whatmeal.presentation.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.beside.whatmeal.presentation.common.MutableOneShotEvent
import com.beside.whatmeal.presentation.common.OneShotEvent
import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import com.beside.whatmeal.presentation.common.getOrThrow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MapViewModel(
    private val whatMealBo: WhatMealBoDelegator,
    private val webViewController: MapWebViewController,
    private val savedState: SavedStateHandle
) : ViewModel() {
    private val coroutineScope: CoroutineScope = viewModelScope

    private val mutableViewAction: MutableOneShotEvent<MapViewAction> = MutableOneShotEvent()
    val viewAction: OneShotEvent<MapViewAction> = mutableViewAction

    fun onBackPressed() {
        webViewController.backToPreviousPage {
            mutableViewAction.post(MapViewAction.FinishScreen)
        }
    }

    fun onLocationLoaded(latitude: String, longitude: String) =
        loadMapUrl(savedState, latitude, longitude)

    private fun loadMapUrl(savedState: SavedStateHandle, latitude: String, longitude: String) =
        coroutineScope.launch {
            whatMealBo.loadMapUri(
                longitude, latitude, savedState.getOrThrow(INTENT_PARAM_FOOD_NAME)
            ).onSuccess {
                webViewController.loadUrl(it)
            }.onFailure {
                // @TODO: We should properly inform the user of the error situation.
                mutableViewAction.post(MapViewAction.FinishScreen)
            }
        }

    class Factory(
        private val whatMealBo: WhatMealBoDelegator,
        private val webViewController: MapWebViewController,
        owner: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
    ) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T = MapViewModel(whatMealBo, webViewController, handle) as T
    }

    companion object {
        private const val TAG = "MapViewModel"

        private const val INTENT_PARAM_FOOD_NAME = "food_name"

        fun createIntent(
            context: Context,
            foodName: String
        ): Intent = Intent(context, MapActivity::class.java)
            .putExtras(bundleOf(INTENT_PARAM_FOOD_NAME to foodName))
    }
}