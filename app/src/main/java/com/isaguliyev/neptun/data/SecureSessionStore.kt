package com.isaguliyev.neptun.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

private const val PREFS_NAME = "neptun_secure_prefs"
private const val KEY_PAIRING_KEY = "pairing_key"
private const val KEY_IS_LOGGED_IN = "is_logged_in"
private const val KEY_USERNAME = "username"
private const val KEY_PASSWORD = "password"

class SecureSessionStore(context: Context) {

    private val appContext = context.applicationContext

    private val masterKey = MasterKey.Builder(appContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        appContext,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getPairingKey(): String? {
        val value = prefs.getString(KEY_PAIRING_KEY, null)
        return if (value.isNullOrEmpty()) null else value
    }

    fun setPairingKey(pairingKey: String) {
        prefs.edit().putString(KEY_PAIRING_KEY, pairingKey).apply()
    }

    fun getCredentials(): Pair<String?, String?> {
        val username = prefs.getString(KEY_USERNAME, null)
        val password = prefs.getString(KEY_PASSWORD, null)
        return Pair(
            if (username.isNullOrEmpty()) null else username,
            if (password.isNullOrEmpty()) null else password
        )
    }

    fun setCredentials(username: String, password: String) {
        prefs.edit()
            .putString(KEY_USERNAME, username)
            .putString(KEY_PASSWORD, password)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply()
    }

    fun clearSession() {
        prefs.edit()
            .remove(KEY_PAIRING_KEY)
            .remove(KEY_USERNAME)
            .remove(KEY_PASSWORD)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }
}
