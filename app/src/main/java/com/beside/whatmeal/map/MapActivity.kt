package com.beside.whatmeal.map

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.beside.whatmeal.compose.Header
import com.beside.whatmeal.compose.WhatMealColor

class MapActivity : ComponentActivity() {
    private lateinit var webViewController: MapWebViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val webView = WebView(this)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Header(onUpButtonClick = { onBackPressed() })
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

        val foodName: String = intent.extras?.getString(INTENT_PARAM_FOOD_NAME)
            ?: throw IllegalArgumentException("Food name can not be null.")
        val naverMapUrl: String = createNaverMapUrl(foodName)
        webViewController = MapWebViewController(webView, this)
        webViewController.loadUrl(naverMapUrl)
    }

    override fun onBackPressed() {
        webViewController.backToPreviousPage(onPreviousPageIsNotExit = { super.onBackPressed() })
    }

    private fun createNaverMapUrl(foodName: String): String =
        "https://m.map.naver.com/search2/search.naver?query=$foodName&style=v5#/map/1"

    companion object {
        private const val INTENT_PARAM_FOOD_NAME = "food_name"

        fun createIntent(context: Context, foodName: String) =
            Intent(context, MapActivity::class.java).putExtra(INTENT_PARAM_FOOD_NAME, foodName)
    }
}