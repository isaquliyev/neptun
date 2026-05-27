package com.isaguliyev.neptun.web

import android.webkit.WebView

private const val LOGIN_PATH = "/Account/Login"
private const val LOGIN_2FA_PATH = "/Login2FA"
private const val ROOT_URL = "https://neptun.elte.hu/"
private const val STUDENT_WEB_URL = "https://neptun.elte.hu/ToNeptunWeb/ToNeptunHWeb"
private const val DASHBOARD_URL_H4 = "https://hallgato4.neptun.elte.hu/dashboard"
private const val DASHBOARD_URL_H5 = "https://hallgato5.neptun.elte.hu/dashboard"

private fun isDashboardUrl(url: String): Boolean {
    return url.startsWith(DASHBOARD_URL_H4) || url.startsWith(DASHBOARD_URL_H5)
}

fun shouldSkipOnBack(url: String): Boolean {
    return when {
        url.contains(LOGIN_2FA_PATH) -> true
        url.contains(LOGIN_PATH) -> true
        url == ROOT_URL -> true
        url == STUDENT_WEB_URL -> true
        else -> false
    }
}

fun WebView.hasUserBackTarget(): Boolean {
    val history = copyBackForwardList()
    val currentUrl = history.currentItem?.url ?: url.orEmpty()
    if (isDashboardUrl(currentUrl)) return false

    for (index in history.currentIndex - 1 downTo 0) {
        val url = history.getItemAtIndex(index).url ?: continue
        if (!shouldSkipOnBack(url)) return true
    }
    return false
}

fun WebView.goBackSkippingAuthRedirects(): Boolean {
    val history = copyBackForwardList()
    val currentUrl = history.currentItem?.url ?: url.orEmpty()
    if (isDashboardUrl(currentUrl)) return false

    var targetIndex = history.currentIndex - 1
    while (targetIndex >= 0) {
        val url = history.getItemAtIndex(targetIndex).url ?: ""
        if (!shouldSkipOnBack(url)) break
        targetIndex--
    }
    if (targetIndex < 0) return false

    val delta = targetIndex - history.currentIndex
    if (delta != 0) {
        goBackOrForward(delta)
        return true
    }
    return false
}
