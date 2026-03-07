package com.isaguliyev.neptun.web

import android.annotation.SuppressLint
import android.net.http.SslError
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.isaguliyev.neptun.totp.TotpGenerator

private const val TAG = "NeptunWebViewClient"

private const val LOGIN_URL = "https://neptun.elte.hu/Account/Login"
private const val LOGIN_2FA = "/Login2FA"
private const val ROOT_URL = "https://neptun.elte.hu/"
private const val STUDENT_WEB_URL = "https://neptun.elte.hu/ToNeptunWeb/ToNeptunHWeb"

/**
 * Escapes a string for safe use inside a JavaScript single-quoted string.
 */
private fun escapeForJs(value: String): String {
    return value
        .replace("\\", "\\\\")
        .replace("'", "\\'")
        .replace("\r", "\\r")
        .replace("\n", "\\n")
}

class NeptunWebViewClient(
    private val username: String,
    private val password: String,
    private val pairingKey: String
) : WebViewClient() {

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(
        view: WebView,
        handler: SslErrorHandler,
        error: SslError
    ) {
        // The cr_X509Util / Chromium "handshake failed" log lines always appear before this
        // callback is invoked — they are diagnostic, not a sign of failure. Calling proceed()
        // here tells the WebView to load the page anyway (university/proxy CA not in Android's
        // default trust store). WARNING: Insecure — use only on trusted networks.
        Log.w(TAG, "SSL error on ${error.url} — code ${error.primaryError}, proceeding anyway")
        handler.proceed()
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        if (request?.isForMainFrame == true && view != null) {
            view.loadUrl(
                "data:text/html," + java.net.URLEncoder.encode(
                    """
                    <html><body style='font-family:sans-serif;padding:2em;'>
                    <h2>Page failed to load</h2>
                    <p>Error: ${error?.description ?: "Unknown"} (code ${error?.errorCode ?: -1}).</p>
                    <p>Check your internet connection and try again.</p>
                    </body></html>
                    """.trimIndent(),
                    "UTF-8"
                )
            )
        }
    }

    override fun onPageFinished(view: WebView, url: String?) {
        val safeUrl = url ?: return
        super.onPageFinished(view, safeUrl)
        Log.d(TAG, "onPageFinished: $safeUrl")

        when {
            safeUrl.contains(LOGIN_2FA) -> {
                fillOtp(view)
            }
            safeUrl.contains("/Account/Login") && !safeUrl.contains(LOGIN_2FA) -> {
                view.postDelayed({ fillLogin(view) }, 500)
            }
            safeUrl == ROOT_URL -> {
                view.postDelayed({ view.loadUrl(STUDENT_WEB_URL) }, 500)
            }
        }
    }

    private fun fillLogin(view: WebView) {
        val u = escapeForJs(username)
        val p = escapeForJs(password)
        view.evaluateJavascript(
            """
            (function() {
                var loginName = document.querySelector('#LoginName');
                var passwordEl = document.querySelector('#Password');
                var btn = document.querySelector('input.btn.btn-primary');
                if (loginName) loginName.value = '$u';
                if (passwordEl) passwordEl.value = '$p';
                if (btn) btn.click();
            })();
            """.trimIndent(),
            null
        )
    }

    private fun fillOtp(view: WebView) {
        val otp = TotpGenerator.getCurrentCode(pairingKey) ?: run {
            Log.e(TAG, "TOTP generation failed — pairingKey blank or not valid base32. Key length: ${pairingKey.length}")
            return
        }
        Log.d(TAG, "Filling TOTP code")
        val o = escapeForJs(otp)
        view.evaluateJavascript(
            """
            (function tryFill(retries) {
                var totpInput = document.querySelector('#TOTPCode');
                var btn = document.querySelector('input.btn.btn-primary') 
                       || document.querySelector('button.btn-primary')
                       || document.querySelector('button[type="submit"]');
                
                console.log('TOTPCode: ' + (totpInput ? 'FOUND' : 'NOT FOUND'));
                console.log('Button: ' + (btn ? 'FOUND — ' + btn.outerHTML : 'NOT FOUND'));
                
                if (totpInput && btn) {
                    totpInput.value = '$o';
                    totpInput.dispatchEvent(new Event('input',  { bubbles: true }));
                    totpInput.dispatchEvent(new Event('change', { bubbles: true }));
                    totpInput.dispatchEvent(new Event('blur',   { bubbles: true }));
                    btn.click();
                } else if (retries > 0) {
                    setTimeout(function() { tryFill(retries - 1); }, 300);
                }
            })(15);
            """.trimIndent(),
            null
        )
    }
}
