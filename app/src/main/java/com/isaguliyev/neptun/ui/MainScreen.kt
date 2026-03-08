package com.isaguliyev.neptun.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.isaguliyev.neptun.MainViewModel
import com.isaguliyev.neptun.web.NeptunWebViewClient
import android.os.Build
import android.webkit.WebView
import android.webkit.WebSettings
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val (username, password) = viewModel.getSessionCredentials()
    val pairingKey = viewModel.getPairingKey()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Neptun") },
                actions = {
                    TextButton(onClick = { viewModel.clearSession() }) {
                        Text("Clear session")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (username != null && password != null && pairingKey != null) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                factory = { ctx ->
                    WebView(ctx).apply {
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
                        loadUrl("https://neptun.elte.hu/Account/Login")
                    }
                }
            )
        } else {
            Text("Missing credentials or pairing key.")
        }
    }
}
