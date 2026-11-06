#include <jni.h>

// A minimal native function demonstrating the JNI bridge.
// Clamps `radius` into the inclusive range [min, max].
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_example_dracodemo_NativeDemo_clampDisplayRadius(
        JNIEnv* /*env*/,
        jobject /*thiz*/,
        jfloat radius,
        jfloat min,
        jfloat max) {
    if (max < min) {
        // Swap so the range is always well-formed.
        jfloat tmp = min;
        min = max;
        max = tmp;
    }
    if (radius < min) return min;
    if (radius > max) return max;
    return radius;
}
