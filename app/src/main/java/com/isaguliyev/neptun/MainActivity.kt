package com.isaguliyev.neptun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.isaguliyev.neptun.ui.LoginScreen
import com.isaguliyev.neptun.ui.MainScreen
import com.isaguliyev.neptun.ui.SplashScreen
import com.isaguliyev.neptun.ui.theme.NeptunTheme
import app.rive.RiveLog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RiveLog.logger = RiveLog.LogcatLogger()
        enableEdgeToEdge()
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            if (showSplash) {
                SplashScreen(onSplashEnd = { showSplash = false })
            } else {
                val viewModel: MainViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        @Suppress("UNCHECKED_CAST")
                        return MainViewModel(application) as T
                    }
                }
                )
                val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
                NeptunTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        if (isLoggedIn) {
                            MainScreen(
                                viewModel = viewModel,
                                modifier = Modifier.padding(innerPadding)
                            )
                        } else {
                            LoginScreen(
                                onLoginSuccess = viewModel::onLoginSuccess,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NeptunTheme {
        Greeting("Android")
    }
}