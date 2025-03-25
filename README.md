This is a Kotlin Multiplatform project targeting Android, iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


### INSTALLATION
- Secrets Files
  - Must create `AppSecrets.plist` from within Xcode - in password vault, for google maps account ID's.
  - Can get `google-services.json` from Google Cloud Console for google maps account ID's.
    - Get a [Google Maps API key](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
- Add to `local.properties` file located in the root directory (create if it doesn't exist):
  - `MAPS_API_KEY=YOUR_KEY` where `YOUR_KEY` is your key from previous step;
  - `sdk.dir=YOUR_SDK_PATH` where `YOUR_SDK_PATH` is a path to Android SDK in your system.
- From root dir, must run `./gradlew :shared:generateDummyFramework`.
- Must run `pod update` then `pod install` in the `iosApp` folder.
- Must build the Android app before running the iOS app.
- 