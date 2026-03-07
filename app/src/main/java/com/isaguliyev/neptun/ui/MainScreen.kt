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
import android.webkit.WebView
import androidx.compose.ui.viewinterop.AndroidView

private const val LOGIN_URL = "https://neptun.elte.hu/Account/Login"

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
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        webViewClient = NeptunWebViewClient(username, password, pairingKey)
                        loadUrl(LOGIN_URL)
                    }
                }
            )
        } else {
            Text("Missing credentials or pairing key.")
        }
    }
}
