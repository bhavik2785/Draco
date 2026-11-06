package com.example.dracodemo

/**
 * Kotlin bridge to the tiny NDK sample library (libdracodemo.so).
 *
 * The native side exposes a single function that clamps a display radius into
 * a sensible range — a minimal, self-contained demonstration of JNI wiring.
 */
object NativeDemo {

    init {
        System.loadLibrary("dracodemo")
    }

    /**
     * Clamp [radius] into [min]..[max] in native code.
     * @return the clamped value.
     */
    external fun clampDisplayRadius(radius: Float, min: Float, max: Float): Float

    /**
     * Preview-/test-safe wrapper: uses the native implementation at runtime,
     * and falls back to a pure-Kotlin clamp when the native library is not
     * loaded (e.g. in an Android Studio Compose preview or a unit test).
     */
    fun clampSafe(radius: Float, min: Float, max: Float): Float = try {
        clampDisplayRadius(radius, min, max)
    } catch (_: UnsatisfiedLinkError) {
        radius.coerceIn(minOf(min, max), maxOf(min, max))
    }
}
