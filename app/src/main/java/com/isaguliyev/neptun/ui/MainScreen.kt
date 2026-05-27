package com.isaguliyev.neptun.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isaguliyev.neptun.MainViewModel
import com.isaguliyev.neptun.web.NeptunWebViewClient
import android.os.Build
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebSettings
import androidx.activity.compose.BackHandler
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val (username, password) = viewModel.getSessionCredentials()
    val pairingKey = viewModel.getPairingKey()
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    val shouldHandleBack = username != null && password != null && pairingKey != null && canGoBack

    BackHandler(enabled = shouldHandleBack) {
        webViewRef?.goBack()
        canGoBack = webViewRef?.canGoBack() == true
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (username != null && password != null && pairingKey != null) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { ctx ->
                    WebView(ctx).apply {
                        webViewRef = this
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true

                        // Desktop mode
                        settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                        settings.useWideViewPort = true
                        settings.loadWithOverviewMode = true

                        // Allow user to zoom in
                        settings.setSupportZoom(true)
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false

                        // Force override the page's own viewport meta tag
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
                        }

                        // WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
                        webViewClient = NeptunWebViewClient(username, password, pairingKey)
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                canGoBack = view?.canGoBack() == true
                            }
                        }
                        loadUrl("https://neptun.elte.hu/Account/Login")
                    }
                },
                update = { webView ->
                    webViewRef = webView
                    canGoBack = webView.canGoBack()
                }
            )
        } else {
            Text("Missing credentials or pairing key.")
        }

        FloatingActionButton(
            onClick = { viewModel.clearSession() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.DeleteSweep,
                contentDescription = "Clear session"
            )
        }
    }
}
