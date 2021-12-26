package com.beside.whatmeal.map

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView

@SuppressLint("SetJavaScriptEnabled")
class MapWebViewController(private val webView: WebView, activity: MapActivity) {

    init {
        webView.settings.javaScriptEnabled = true
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webView.settings.setGeolocationEnabled(true)
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.webViewClient = MapWebViewClient(activity)
    }

    private fun fadeIn() {
        webView.animate().startDelay = 1000
        webView.animate().alpha(1f)
        webView.animate().duration = 200
    }

    fun loadUrl(url: String) {
        webView.alpha = 0f
        webView.loadUrl(url)
        fadeIn()
    }

    fun backToPreviousPage(onPreviousPageIsNotExit: () -> Unit) {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            onPreviousPageIsNotExit()
        }
    }

    companion object {
        private const val TAG = "MapWebViewController"
    }
}

