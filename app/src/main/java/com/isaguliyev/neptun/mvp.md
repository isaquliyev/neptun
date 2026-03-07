This is a simple single activity native Android application that will automate logging in to our university's system named Neptun.

The login page is at `https://neptun.elte.hu/Account/Login`. It has two text fields for username and password, and a login button. After tapping the login button, it asks for an OTP code. There is also a button to generate a new OTP pairing key that is a long string. The user enters this pairing key once along with their credentials into our app.

So the app has three input fields and an OK button. User enters username, password and pairing key. We cache the pairing key locally so we can generate the correct OTP code whenever needed, every 30 seconds, using the TOTP algorithm (same as Google Authenticator).

After the user taps OK, we are done with the input UI. The next screen is simply an AppBar and a WebView. The AppBar will have a button to clear the session and remove the cached pairing key.

Now the automation part happens entirely inside the WebView using `WebViewClient` and `evaluateJavascript`. There is no need for any external tool. The flow is:

1. WebView opens the login page
2. We detect the page via `onPageFinished` and auto-fill username and password, then click the login button
3. When the OTP page appears, we generate the OTP from the cached pairing key and auto-fill it, then submit
4. After successful login, we navigate to Student Web at `https://neptun.elte.hu/ToNeptunWeb/ToNeptunHWeb` which redirects to the dashboard at `https://hallgato5.neptun.elte.hu/dashboard`

For OTP generation inside the Android app, we use the `dev.samstevens.totp` Kotlin library. This replaces my earlier idea of using Appium — Appium is actually an external testing tool, not something that runs inside your app. Everything we need is already built into Android's WebView APIs.