package com.isaguliyev.neptun

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.isaguliyev.neptun.data.SecureSessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val store = SecureSessionStore(application)

    private val _isLoggedIn = MutableStateFlow(store.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun onLoginSuccess(pairingKey: String) {
        store.setPairingKey(pairingKey)
        store.setLoggedIn(true)
        _isLoggedIn.value = true
    }

    fun clearSession() {
        store.clearSession()
        _isLoggedIn.value = false
    }
}
