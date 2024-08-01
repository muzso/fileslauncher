# fileslauncher

This is a minimalistic AAOS (Android Automotive OS) app to launch the builtin "Files" (com.android.documentsui or com.google.android.documentsui) application.
It's based on the Hello World app in Google's [car-samples](https://github.com/android/car-samples/).

Sometimes car manufacturers keep the builtin "Files" application on the head unit, but try to prevent user access by hiding it from the app list and prohibiting Google Assistant from launching it. The reason is that the "Files" app usually has the permission to initiate app installations from local APK files (i.e. sideloading) via the "Package installer" (com.google.android.packageinstaller) app.

To work around this issue:

- you can compile this app with your own `applicationId` (modify this property in `app/build.gradle`)
- build an Android App Bundle, aka. AAB (must be signed)
- create a new app in your Google Play Console
- create a new release and upload the AAB
- make it into an Internal Testing app
- add your vehicle's Google account to the app's list of testers
- install the app on the head unit (remotely) by logging into play.google.com with the vehicle's Google account and using the app's Internal Testing URL (you can do this on your desktop PC in a browser)

Of course for all of this to work the head unit must have internet access: either with a built-in SIM/eSIM or via WiFi tethering.

Note: the app tries to look for the "Files" application first with the "com.android.documentsui" package name (this is the package name used in [AOSP sources](https://android.googlesource.com/platform/packages/apps/DocumentsUI/+/refs/heads/android12L-platform-release/AndroidManifest.xml)) and if it is not found, it tries again with the "com.google.android.documentsui" package name (this is used in Android Studio's automotive emulator images).
