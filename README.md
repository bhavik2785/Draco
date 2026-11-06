# Draco Demo — Android Studio project

A standalone Android gaming-utility UI and training simulator using **synthetic/demo data**.
It does not connect to BGMI/PUBG, inspect another app's memory, hook a game, automate aiming,
or bypass anti-cheat.

## Included
- Local demo login (no server; demo credentials shown on login screen)
- Compose dashboard and settings
- Standalone ESP-style training scene with generated dummy targets
- Simulated vehicle markers and head-position indicators
- FOV/radius visualization only
- Optional movable Android overlay panel
- Small, harmless C++ JNI helper for clamping a UI radius value

## Requirements
- Android Studio (current stable)
- Android SDK Platform 35
- Android SDK Build-Tools 35.x
- Android NDK (Side by side) and CMake 3.22.1 (for the native demo library)
- JDK 17

## Open and build
1. Extract the ZIP.
2. In Android Studio choose **Open** and select the `DracoDemo` folder.
3. Allow Gradle sync and install missing SDK/NDK/CMake components if prompted.
4. Select an emulator or connect an Android phone with USB debugging enabled.
5. Press **Run** to install the app.

To build an APK:
- Android Studio: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
- Or terminal in this folder: `gradle :app:assembleDebug`
- APK output: `app/build/outputs/apk/debug/app-debug.apk`

For a 64-bit ARM device, this project configures `arm64-v8a`.
To also build for emulator, add `x86_64` to `abiFilters` in `app/build.gradle.kts`.

## Demo login
- Username: `demo`
- Password: `demo123`

This is intentionally a local demonstration login, not secure production authentication.
Do not reuse a real password.

## Overlay permission
The floating panel needs Android's **Display over other apps** permission.
Tap **Floating panel** in the app and grant the permission when Android asks.
The overlay can be moved by dragging its header and closed with ×.

## Project structure
- `app/src/main/java/com/example/dracodemo/MainActivity.kt` — app UI and navigation
- `.../SimulatorScreen.kt` — synthetic target simulator
- `.../FloatingPanelService.kt` — optional movable overlay
- `.../NativeDemo.kt` — JNI bridge for a harmless UI helper
- `app/src/main/cpp/` — C++ source and CMake configuration

## Safety and scope
All names, health values, distances, vehicles, and positions in the simulator are generated locally.
The FOV ring is a visual training aid only. Native code does not access other processes or games.
