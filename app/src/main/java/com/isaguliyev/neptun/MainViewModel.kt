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

    private var sessionUsername: String?
    private var sessionPassword: String?

    init {
        val (user, pass) = store.getCredentials()
        sessionUsername = user
        sessionPassword = pass
    }

    fun onLoginSuccess(username: String, password: String, pairingKey: String) {
        sessionUsername = username
        sessionPassword = password
        store.setCredentials(username, password)
        store.setPairingKey(pairingKey)
        store.setLoggedIn(true)
        _isLoggedIn.value = true
    }

    fun getSessionCredentials(): Pair<String?, String?> = Pair(sessionUsername, sessionPassword)

    fun getPairingKey(): String? = store.getPairingKey()

    fun clearSession() {
        sessionUsername = null
        sessionPassword = null
        store.clearSession()
        _isLoggedIn.value = false
    }
}
