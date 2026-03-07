This is simple single activity (most likely to be single fragment) native android application that will enable to automate logging in our university's system named Neptun.

Initially, there is 2 text fields if we try to open neptun via web.
First one is for neptun username and second one is for password.

https://neptun.elte.hu/Account/Login

This is the url of login page.
There is a login button next to these text fields. 
After tapping on this button, it asks for OTP.
But there is also a button that asks for new OTP pairing. If we tap on that button we can generate new otp pairing. This is a code in large String format.
I consider that user themselves will enter this pairing with login credentials. After knowing them, we can autogenerate that required OTP codes every 30 seconds. 

After successfully entering generated OTP code, Neptun main page appear. There is a tab "Student Web" where we aim to go. 
The url of Student Web is: https://neptun.elte.hu/ToNeptunWeb/ToNeptunHWeb

After clicking on this tab neptun dashboard has been opened: https://hallgato5.neptun.elte.hu/dashboard

So given flow and Nagi's desktop app, this mobile app will be simple. Just three fields and OK button.
User will enter username, password and pairing code.
Then we cache the pairing code and be able to generate OTP whenever we want.

After user accept, we have done with app's user interface. 
The next page is AppBar and WebView. App bar is used for remove session/ pairing key. 
I'll use Appium (more precisely, I intended to use it, I don't know technical capabilities of it at this moment) for automation.
That's it.

