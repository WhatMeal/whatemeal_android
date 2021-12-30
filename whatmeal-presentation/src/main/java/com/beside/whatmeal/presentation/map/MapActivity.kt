package com.beside.whatmeal.presentation.map

import android.os.Bundle
import android.webkit.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.beside.whatmeal.presentation.common.WhatMealBoDelegator
import com.beside.whatmeal.presentation.common.view.Header
import com.beside.whatmeal.presentation.common.resource.WhatMealColor
import com.linecorp.lich.component.getComponent

class MapActivity : ComponentActivity() {
    private val webView: WebView by lazy { WebView(this) }
    private val viewModel: MapViewModel by viewModels {
        val whatMealBo = getComponent(WhatMealBoDelegator)
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
                        modifier = Modifier.align(Alignment.Center)
                    )
                    AndroidView(
                        factory = { webView },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(WhatMealColor.Bg0)
                    )
                }
            }
        }

        viewModel.viewAction.subscribe(this, this::handleViewAction)
    }

    private fun handleViewAction(viewAction: MapViewAction) = when (viewAction) {
        is MapViewAction.FinishScreen -> finish()
    }
}