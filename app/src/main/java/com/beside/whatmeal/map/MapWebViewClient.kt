package com.beside.whatmeal.map

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets

class MapWebViewClient(private val activity: MapActivity) : WebViewClient() {
    private val coroutineScope: CoroutineScope = activity.lifecycleScope
    private var job: Job? = null

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        if (view == null || request == null) {
            return false
        }
        return shouldOverrideUrlLoading(view, request.url.toString())
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        Log.d(TAG, "shouldOverrideUrlLoading url: $url")

        return when {
            // @TODO: We meed to handle naver map intent.
            //      intent://route?dlat=37.5713506&dlng=126.9749617&did=11678778&dname=%EA%B4%91%ED%99%94%EB%AC%B8%EC%A7%91&appname=https%3A%2F%2Fm.place.naver.com%2Frestaurant%2F11678778%2Fhome#Intent;scheme=nmap;action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package=com.nhn.android.nmap;S.browser_fallback_url=https%3A%2F%2Fm.search.naver.com%2Fsearch.naver%3Fwhere%3Dm%26query%3D%25EB%25B9%25A0%25EB%25A5%25B8%25EA%25B8%25B8%25EC%25B0%25BE%25EA%25B8%25B0%26nso_path%3DplaceType%255Eplace%253Bname%255E%253Baddress%255E%253Blatitude%255E%253Blongitude%255E%253Bcode%255E%257Ctype%255Eplace%253Bname%255E%25EA%25B4%2591%25ED%2599%2594%25EB%25AC%25B8%25EC%25A7%2591%253Baddress%255E%253Bcode%255E11678778%253Blongitude%255E126.9749617%253Blatitude%255E37.5713506%257Cobjtype%255Epath%253Bby%255Epubtrans;end
            url.startsWith("http:") || url.startsWith("https:") -> false
            url.startsWith("intent:") || url.startsWith("tel:") -> {
                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
                activity.startActivity(intent)
                true
            }
            else -> {
                Log.e(TAG, "onPageCommitVisible - unsupported scheme: $url")
                true
            }
        }
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        Log.i(TAG, "onPageCommitVisible url: ${url.toString()}")

    }

    override fun onPageFinished(view: WebView?, url: String?) {
        if (url.isNullOrEmpty() || view == null) {
            return
        }
        job?.cancel()

        val uri = Uri.parse(url)
        when (uri.host) {
            "m.map.naver.com" -> runJsScriptForMap(view)
            "m.place.naver.com" -> runJsScriptForPlace(view)
            else -> return
        }
    }

    private fun runJsScriptForMap(webView: WebView) {
        val jsScript = """
                    javascript:(function() {
                        var header = document.getElementById('header');
                        header.style.display = 'none';

                        var ct = document.getElementById('ct');
                        ct.style.height = '100%';

                        var naver_map = document.getElementById('naver_map');
                        naver_map.style.height = '100%';

                        var map = document.getElementById('map');
                        map.style.height = '100%';
                    })();
                """.trimIndent()

        job = coroutineScope.launch {
            withContext(Dispatchers.IO) {
                repeat(Int.MAX_VALUE) {
                    withContext(Dispatchers.Main) {
                        webView.evaluateJavascript(jsScript, null)
                    }
                    delay(100)
                }
            }
        }
    }

    private fun runJsScriptForPlace(webView: WebView) {
        val jsScript = """
                    .g3Iyq { display: none; }
                    .Xw8gb { display: none; }
                    ._1nU3J { display: none; }
                    ._3nUBc { display: none; }
                    ._3T5PD { height: 0px; }
                    .place_fixed_maintab { display: none; }
                    body > #app-root > div > header { display: none; }
                """.trimIndent().toCssApplyScript()
        webView.evaluateJavascript(jsScript, null)
    }

    private fun String.toCssApplyScript(): String =
        """
                javascript:(function() {
                        var parent = document.getElementsByTagName('head').item(0);
                        var style = document.createElement('style');
                        style.type = 'text/css';
                        style.innerHTML = window.atob('${stringToBase64(this)}');
                        parent.appendChild(style)
                })();
        """

    private fun stringToBase64(input: String): String {
        val inputStream: InputStream =
            ByteArrayInputStream(input.toByteArray(StandardCharsets.UTF_8))
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()

        return android.util.Base64.encodeToString(buffer, android.util.Base64.NO_WRAP)
    }


    companion object {
        private const val TAG = "MapWebViewClient"
    }
}