# Draco Demo

A small Android app, rebuilt as a clean, original project. It shows:

- **Jetpack Compose UI** — a main screen with a list of sample targets.
- **A draggable floating panel** drawn over other apps using the
  `SYSTEM_ALERT_WINDOW` overlay permission (`FloatingPanelService`).
- **A tiny NDK / JNI sample** — one native function, `clampDisplayRadius`,
  in `app/src/main/cpp/dracodemo.cpp`, called from Kotlin via `NativeDemo`.

Package: `com.example.dracodemo` · min SDK 24 · target SDK 34.

---

## Build the APK automatically on GitHub (no computer setup needed)

This repo ships a GitHub Actions workflow (`.github/workflows/android.yml`)
that builds a debug APK on every push. A debug build needs **no signing keys
and no secrets**, so it works for free on a public or private repo.

1. Create a new repository on GitHub (empty, no README).
2. Upload this project. Either:
   - **Web upload:** on the new repo page, click *uploading an existing file*,
     drag in everything from this folder, and commit; **or**
   - **Git:**
     ```bash
     git init
     git add .
     git commit -m "Draco Demo"
     git branch -M main
     git remote add origin https://github.com/<you>/<repo>.git
     git push -u origin main
     ```
3. Go to the repo's **Actions** tab. The **Build APK** run starts on its own
   (or run it manually with *Run workflow*).
4. When it finishes (green check), open the run and download the
   **`app-debug-apk`** artifact from the *Artifacts* section. Unzip it to get
   `app-debug.apk`.
5. Copy the APK to an Android phone and install it (enable *Install unknown
   apps* for your file manager / browser first).

> The first build takes a few minutes while it downloads the Android SDK, NDK
> and Gradle. Later builds are faster thanks to caching.

---

## Build locally in Android Studio

1. Open the project folder in Android Studio (Koala or newer).
2. Let it sync. When prompted, accept installing the Android SDK, the NDK
   (`27.0.12077973`) and CMake `3.22.1`.
3. Press **Run**, or build an APK with **Build → Build APK(s)**.

## Build locally from the command line

```bash
./gradlew assembleDebug
# APK lands at: app/build/outputs/apk/debug/app-debug.apk
```

---

## Using the floating panel

Tap **Start floating panel**. The first time, Android opens the *Display over
other apps* settings screen — turn the toggle on for Draco Demo, go back, and
tap the button again. Drag the panel anywhere; **Bump** increments the counter
(the value is run through the native clamp), and **Close** stops it.
