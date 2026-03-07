package com.isaguliyev.neptun.totp

import android.util.Log
import dev.samstevens.totp.code.DefaultCodeGenerator
import dev.samstevens.totp.code.HashingAlgorithm
import dev.samstevens.totp.time.SystemTimeProvider

private const val TAG = "TotpGenerator"

/**
 * Generates TOTP codes (6 digits, 30-second period) from the raw Neptun pairing key.
 * Uses the pairing key string directly as the TOTP secret (no Base32 encoding).
 * Equivalent to: TimeBasedOneTimePasswordGenerator(codeGenerator, timeProvider).generate(secret)
 */
object TotpGenerator {

    private val timeProvider = SystemTimeProvider()
    private val codeGenerator = DefaultCodeGenerator(HashingAlgorithm.SHA1)

    /**
     * Returns the current 6-digit TOTP code for the given raw Neptun pairing key, or null if invalid.
     */
    fun getCurrentCode(secret: String): String? {
        if (secret.isBlank()) {
            Log.e(TAG, "OTP generation failed: secret is blank")
            return null
        }
        return try {
            val trimmed = secret.trim()
            val counter = timeProvider.getTime() / 30
            val otp = codeGenerator.generate(trimmed, counter)
            Log.d(TAG, "Generated OTP: $otp")
            otp
        } catch (e: Exception) {
            Log.e(TAG, "OTP generation failed: ${e.message}", e)
            null
        }
    }
}
