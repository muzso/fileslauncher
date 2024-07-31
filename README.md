# fileslauncher

This is a minimalistic AAOS (Android Automotive OS) app to launch the builtin "Files" application.
It's based on the Hello World app in Google's [car-samples](https://github.com/android/car-samples/).

Sometimes car manufacturers keep the builtin "Files" application on the head unit, but try to prevent users from accessing it.
The "Files" app usually has permission to initiate APK installations (sideloading) via the "Package installer" app.

To work around this issue:

- you can compile the app in the "automotive" folder with your own `applicationId` (modify this property in `automotive/build.gradle`)
- build an AAB (doesn't necessarily have to be signed)
- create a new app in your Google Play Console
- upload the AAB into this app
- make it into an Internal Testing app
- add your vehicle's Google account to the app's list of testers
- install the app on the head unit (remotely) by logging into play.google.com with the vehicle's Google account and using the app's Internal Testing URL (you can do this on your desktop PC in a browser)

Of course for this to work, the head unit must have internet access, either built-in or via WiFi tethering.

Note: the app tries to look for the "Files" application first with the "com.android.documentsui" package name and if it is not found, it tries again with the "com.google.android.documentsui" package name. The latter is used in Android Studio's emulators.
