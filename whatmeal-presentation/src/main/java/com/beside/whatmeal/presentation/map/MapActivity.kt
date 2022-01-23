package com.beside.whatmeal.presentation.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.webkit.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import com.beside.whatmeal.presentation.common.view.Header
import com.beside.whatmeal.presentation.common.resource.WhatMealColor
import com.beside.whatmeal.servicelocator.getInstance
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnTokenCanceledListener

import com.google.android.gms.tasks.CancellationToken

import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY


class MapActivity : ComponentActivity() {
    private val webView: WebView by lazy { WebView(this) }
    private val viewModel: MapViewModel by viewModels {
        val whatMealBo = getInstance(WhatMealBoDelegator)
        val webViewController = MapWebViewController(webView, this)
        MapViewModel.Factory(whatMealBo, webViewController, this, intent.extras)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Header(onUpButtonClick = viewModel::onBackPressed)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WhatMealColor.Bg0)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Black
                    )
                    AndroidView(
                        factory = { webView },
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
        loadCurrentLocation()
        viewModel.viewAction.subscribe(this, this::handleViewAction)
    }

    // @TODO: Please consider which layer this is.
    @SuppressLint("MissingPermission")
    private fun loadCurrentLocation() {
        if (!hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            !hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (it.all { permission -> !permission.value }) {
                    // @TODO: We need to inform the user that the service is not available.
                    finish()
                    return@registerForActivityResult
                }
                loadCurrentLocation()
            }.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        val locationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        locationClient.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken =
                    this

                override fun isCancellationRequested(): Boolean = this@MapActivity.isDestroyed
            }
        ).addOnSuccessListener { location ->
            if(location == null) {
                finish()
            }
            viewModel.onLocationLoaded(location.latitude.toString(), location.longitude.toString())
        }
    }

    private fun hasPermission(permission: String) =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    private fun handleViewAction(viewAction: MapViewAction) = when (viewAction) {
        is MapViewAction.FinishScreen -> finish()
    }
}