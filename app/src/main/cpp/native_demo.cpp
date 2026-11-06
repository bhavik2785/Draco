#include <jni.h>
#include <algorithm>

// Safe native demonstration: clamps a display-only UI radius value.
extern "C"
JNIEXPORT jint JNICALL
Java_com_example_dracodemo_NativeDemo_clampDisplayRadius(
        JNIEnv *, jobject, jint value) {
    return std::max(40, std::min(240, static_cast<int>(value)));
}
