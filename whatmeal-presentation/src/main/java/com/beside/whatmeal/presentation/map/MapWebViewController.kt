package com.beside.whatmeal.presentation.map

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.*

@SuppressLint("SetJavaScriptEnabled")
class MapWebViewController(private val webView: WebView, activity: MapActivity) {
    private val webViewClient: MapWebViewClient = MapWebViewClient(activity)

    init {
        webView.alpha = 0f

        webView.settings.javaScriptEnabled = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true

        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccess = true

        webView.settings.databaseEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT

        webView.webViewClient = webViewClient

        webView.settings.setGeolocationEnabled(true)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onGeolocationPermissionsShowPrompt(
                origin: String?,
                callback: GeolocationPermissions.Callback?
            ) {
                callback?.invoke(origin, true, false)
            }
        }

        webView.addJavascriptInterface(MapJsInterface, "WhatMeal")
    }

    private fun fadeIn() {
        webView.animate().startDelay = 1000
        webView.animate().alpha(1f)
        webView.animate().duration = 200
    }

    fun loadUrl(url: String) {
        if (!isLocationProvided) {
            MapJsInterface.setOnLocationProvidedCallback {
                webView.post {
                    webView.loadUrl(url)
                    fadeIn()
                }
            }
            webView.loadUrl(MapWebViewClient.LOCATION_PROVIDE_URL)
        } else {
            webView.loadUrl(url)
            fadeIn()
        }
    }

    fun backToPreviousPage(onPreviousPageIsNotExit: () -> Unit) {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            onPreviousPageIsNotExit()
        }
    }

    companion object {
        private var isLocationProvided = false
        private const val TAG = "MapWebViewController"
    }

    object MapJsInterface {
        private var callback: () -> Unit = {}

        fun setOnLocationProvidedCallback(callback: () -> Unit) {
            this.callback = callback
        }

        @JavascriptInterface
        fun onLocationProvided() {
            if (isLocationProvided) {
                return
            }
            isLocationProvided = true
            callback()
        }
    }
}

