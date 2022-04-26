# StreamEasy
A simplified streaming application for iOS and Android that gives users the power to stream live video from their phone to a custom destination.
# Installation Guide
How to setup and install Streameasy. This section will cover how to install the application on mobile directly using an APK or through building the source code via Android Studio.
## Setting up Android Studio
This section will cover how to set up the development environment and build the application using Android Studio. This process should be similar for both Windows and Mac users.
### Prerequisites
Please go through this list of prerequisites and make sure that you have everything required to ensure a smooth setup process.
* Make sure that you have [Android Studio](https://developer.android.com/studio) set up and installed, you can find a full guide [here](https://developer.android.com/studio/install).
* Make sure that you have JDK (Java Development Kit) <b>1.8</b> installed and setup. Here is a guide for [Windows](https://codenotfound.com/java-download-install-jdk-8-windows.html#:~:text=Sign%20in%20using%20your%20Oracle,button.) and for [Mac](https://docs.oracle.com/javase/8/docs/technotes/guides/install/mac_jdk.html).
### Dependencies
* The only external library in-use in this project is [rtmp-rtsp-stream-client-java](https://github.com/pedroSG94/rtmp-rtsp-stream-client-java). The dependency should be already added to project's Gradle scripts once you download the source files, but if you need to install the dependency manually, you can find a guide [here](https://github.com/pedroSG94/rtmp-rtsp-stream-client-java).
### Download

## APK Installation
### Prerequisites
This application requires a smartphone running `Android 8.0` or above.
### Installation
To install the application on mobile, head over to the builds folder and download the `app-debug.apk` file on your Android device. Then, you should open that file and choose to open the file with `Package Installer` once prompted. The `Package Installer` will then commence to install the application on your Android device.
### Running
To run the application, simply click on its icon. The application has the default Android icon, which looks like this:
* <img src="https://i.stack.imgur.com/3hRmg.png" alt="Default Android Icon" width="50"/>
You can search for the application name, `Streameasy`, if you don't know where the newly installed applications on your phone go.
If you have any issues installing and running the APK, please reference the Troubleshooting section.
### Troubleshooting
If the APK fails to install on your device, the two likely causes are:
* You are using a phone which runs an Android version below `Android 8.0`, in that case you should try installing the app on a device with a supported Android version.
* If you are using a supported phone and the `Package Manager` still gives you an error while installing, you may want to make sure that you're allowing your device to install packages from unkown sources. You can find a detailed guide [here](https://www.appaloosa.io/guides/how-to-install-apps-from-unknown-sources-in-android/).
# Release Notes
## v0.1.0
### Features
* Full screen live camera preview
* 'Stream' button to toggle video streaming
### Bug Fixes
* N/A
### Known Issues
* 'Stream' button does not blink while streaming
* 'Stream' button can be toggled on but not toggled off
---
## v0.2.0
### Features
* 'Stream' button begins and ends streaming
* Error dialogue appears when streaming fails
* 'Stream' button turns red while streaming to better convey status (as opposed to blinking white)
### Bug Fixes
* fixed 'Stream' button not indicating active streaming
* fixed 'Stream' button not toggling properly
### Known Issues
* N/A
---
## v0.3.0
### Features
* N/A
### Bug Fixes
* N/A
### Known Issues
* N/A
---
## v0.4.0
### Features
* Adds required UI for the user to provide stream location
* Adds required UI for the user to edit stream settings
### Bug Fixes
* N/A
### Known Issues
* N/A
---

