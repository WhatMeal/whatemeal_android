package com.beside.whatmeal.presentation.map

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
            url.startsWith("http:") || url.startsWith("https:") -> false
            url.startsWith("tel:") -> {
                val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
                activity.startActivity(intent)
                true
            }
            url.startsWith("intent:") -> {
                Log.e(TAG, "test - intent $url")
                val parsedIntent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                val packageName = parsedIntent.`package` ?: return true
                val intent = activity.packageManager.getLaunchIntentForPackage(packageName)
                    ?: Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse("market://details?id=$packageName"))
                Log.e(TAG, "test $parsedIntent $packageName $intent")
                activity.startActivity(intent)
                return true
            }
            else -> {
                Log.e(TAG, "shouldOverrideUrlLoading - unsupported scheme: $url")
                true
            }
        }
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