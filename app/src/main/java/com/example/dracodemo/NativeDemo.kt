package com.example.dracodemo

/**
 * Safe JNI demonstration. This helper only clamps a UI setting; it does not
 * inspect or interact with other applications or games.
 */
object NativeDemo {
    init {
        System.loadLibrary("dracodemo")
    }

    external fun clampDisplayRadius(value: Int): Int
}
