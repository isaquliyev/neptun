package com.isaguliyev.neptun.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.rive.Rive
import app.rive.RiveFileSource
import app.rive.Result
import app.rive.rememberRiveFile
import app.rive.rememberRiveWorkerOrNull
import com.isaguliyev.neptun.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashEnd: () -> Unit
) {
    var splashEnded by remember { mutableStateOf(false) }
    val errorState = remember { mutableStateOf<Throwable?>(null) }
    val riveWorker = rememberRiveWorkerOrNull(errorState)

    LaunchedEffect(splashEnded) {
        if (!splashEnded) return@LaunchedEffect
        onSplashEnd()
    }

    if (riveWorker == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Failed to initialize Rive",
                modifier = Modifier.align(Alignment.Center)
            )
            LaunchedEffect(Unit) { splashEnded = true }
        }
        return
    }

    val riveFile = rememberRiveFile(
        RiveFileSource.RawRes.from(R.raw.neptun),
        riveWorker
    )

    Box(modifier = Modifier.fillMaxSize()) {
        when (riveFile) {
            is Result.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is Result.Error -> {
                Text(
                    text = "Failed to load animation",
                    modifier = Modifier.align(Alignment.Center)
                )
                LaunchedEffect(Unit) { splashEnded = true }
            }
            is Result.Success -> {
                Rive(
                    file = riveFile.value,
                    modifier = Modifier.fillMaxSize()
                )
                LaunchedEffect(Unit) {
                    delay(2500)
                    splashEnded = true
                }
            }
        }
    }
}
