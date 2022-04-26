# StreamEasy
A simplified streaming application written in Java for native Android. It allows easy streaming of the device's camera to any given RTMP server with just a press of a button. You can check our latest release notes [here](https://github.com/emosy/streameasy-1340#release-notes).

![Version: v1.0.0](https://img.shields.io/badge/Version-v0.5.0-yellow.svg) [![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
# Installation Guide
How to setup and install Streameasy. This section is divided into two parts, the first part goes over downloading, setting up, and building the application and its development environment using Android Studio. The second section covers how to install the application APK, which covers how to install either the APK you built yourself or how to install the included APK. If you want to install the included APK without having to go through the source code, then you can skip to the [APK installation](https://github.com/emosy/streameasy-1340#apk-installation) section.
## Setting up Environment in Android Studio
This section will cover how to set up the development environment and build the application using Android Studio. This process should be similar for both Windows and Mac users. If you want to install the APK directly without having the build the source code, you should skip this section and reference the APK Installation section.
### Prerequisites
Please go through this list of prerequisites and make sure that you have everything required to ensure a smooth setup process.
* Make sure that you have [Android Studio](https://developer.android.com/studio) set up and installed, you can find a full guide [here](https://developer.android.com/studio/install).
* Make sure that you have JDK (Java Development Kit) <b>1.8</b> installed and setup. Here is a guide for [Windows](https://codenotfound.com/java-download-install-jdk-8-windows.html#:~:text=Sign%20in%20using%20your%20Oracle,button.) and for [Mac](https://docs.oracle.com/javase/8/docs/technotes/guides/install/mac_jdk.html).
### Dependencies
* The only external library in-use in this project is [rtmp-rtsp-stream-client-java](https://github.com/pedroSG94/rtmp-rtsp-stream-client-java). The dependency should be already added to project's Gradle scripts once you download the source files, but if you need to install the dependency manually, you can find a guide [here](https://github.com/pedroSG94/rtmp-rtsp-stream-client-java).
### Download
We recommend downloading the source code through one of the following methods:
* You can download the source code as a zip file by clicking [here](https://github.com/emosy/streameasy-1340/archive/refs/heads/main.zip). Make sure to unzip the files before use.
* You can clone the GitHub repository:
  * Make sure that you have [git](https://git-scm.com/) installed. You can find an installation guide [here](https://github.com/git-guides/install-git).
  * Open your terminal.
  * Go to the location where you want your project folder to be downloaded.
  * Type the following command in your terminal: `git clone https://github.com/emosy/streameasy-1340.git`
  * Then to navigate to the project folder within your terminal, use: `cd streameasy-1340`
### Build
Once you have downloaded the source files, it's time to set up the project in Android Studio:
* Open Android Studio
* Navigate to `File > New > Import Project` and choose the project folder you just downloaded.
* The included scripts should set everything up for you, but if you run into any issues, make sure to reference the [Troubleshooting](https://github.com/emosy/streameasy-1340#troubleshooting) section.
* Once Android studio has finished setting up the project, you can build it by going to `Build >` and choosing the build option that you prefer.
* You can also run the application within Android Studio by connecting a supported Android device to your computer, or by [setting up an emulator](https://developer.android.com/studio/run/emulator).
  * If you choose to use an emulator, make sure that you choose `Android 8.0` as a minimum, and choose `SDK 26` for your minimum SDK. `Android 12.0` is preferable, with `SDK 31`.
* Once you have built the project and generated an APK file, you can reference the next section, [APK installation](https://github.com/emosy/streameasy-1340#apk-installation), for a guide on how to install the APK on a mobile device.
* If you ran into any issues, please reference the Troubleshooting section.
### Troubleshooting
* If you run into any issues while building the project, make sure that you have all the latest Android Studio tools. You can find an update guide [here](https://developer.android.com/studio/intro/update#channels).
* Make sure that you are using a `minSdk` of 26 and `targetSdk` and `compileSdk` of 31 in the `build.gradle` file.
* Make sure that you are using gradle version <b>7.2</b>. More information [here](https://stackoverflow.com/questions/25205113/how-to-change-the-version-of-the-default-gradle-wrapper-in-intellij-idea).
* If you run into any problems with building the gradle files, you should reference the gradle scripts on the repository and make sure that all the version numbers match.
## APK Installation
### Prerequisites
This application requires a smartphone running `Android 8.0` or above. You also need a smartphone with a modern camera that supports either `H.264` or `H.265` hardware encoding.
### Installation
To install the application on mobile, head over to the builds folder and download the `app-debug.apk` file on your Android device. Then, you should open that file and choose to open the file with `Package Installer` once prompted. The `Package Installer` will then commence to install the application on your Android device.
### Running
To run the application, simply click on its icon. The application has the default Android icon, which looks like this:
* <img src="https://i.stack.imgur.com/3hRmg.png" alt="Default Android Icon" width="50"/>
You can search for the application name, `Streameasy`, if you don't know where the newly installed applications on your phone go.
If you have any issues installing and running the APK, please reference the [Troubleshooting](https://github.com/emosy/streameasy-1340#troubleshooting-1) section.
### Troubleshooting
If the APK fails to install on your device, the two likely causes are:
* You are using a phone which runs an Android version below `Android 8.0`, in that case you should try installing the app on a device with a supported Android version.
  * You may also be using a phone with unsupported hardware.
* If you are using a supported phone and the `Package Manager` still gives you an error while installing, you may want to make sure that you're allowing your device to install packages from unkown sources. You can find a detailed guide [here](https://www.appaloosa.io/guides/how-to-install-apps-from-unknown-sources-in-android/).
# Release Notes
## v1.0.0 <i>(Latest)</i>
### Features
* Adds Audio Settings to the Settings Menu
  * Adds Sample Rate and Audio Bit Rate
* Changes FPS from a number input to a dropdown selection input. 
* Links up the Settings Menu with the Streaming library so the settings can take effect
* Adds the ability for the settings to be saved automatically upon change
* Adds the ability for the stream destination to be saved automatically upon change
* Adds an indicator to show the user the current Stream Destination on the main screen
* Adds the ability for the Stream Destination indicator to change color based on whether it's setup or not
* Adds required UI for the user to provide stream location
* Adds required UI for the user to edit stream settings
* 'Stream' button begins and ends streaming
* Error dialogue appears when streaming fails
* 'Stream' button turns red while streaming to better convey status (as opposed to blinking white)
* Full screen live camera preview
* 'Stream' button to toggle video streaming
### Bug Fixes
* Fixes a bug that required the app to be restarted after the stream has ended to be able to initiate another stream.
* Fixed a bug where the Stream Destination text on the main menu wouldn't change after updating the settings.
### Known Issues
* The dropdown list for the resulotions might show resolutions not supported by the device's camera, and will show a black screen if selected.
* There is no support for text/image overlays, which is required for adding a scoreboard feature.
* Lacks the ability to switch to the front camera.