/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_didi365_dlna_jni_PlatinumJniProxy */

#ifndef _Included_com_didi365_dlna_jni_PlatinumJniProxy
#define _Included_com_didi365_dlna_jni_PlatinumJniProxy
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_didi365_dlna_jni_PlatinumJniProxy
 * Method:    startDlnaMediaRender
 * Signature: ([B[B)I
 */
JNIEXPORT jint JNICALL Java_com_didi365_dlna_jni_PlatinumJniProxy_startDlnaMediaRender
  (JNIEnv *, jclass, jbyteArray, jbyteArray);

/*
 * Class:     com_didi365_dlna_jni_PlatinumJniProxy
 * Method:    stopDlnaMediaRender
 * Signature: ()I
 */
JNIEXPORT void JNICALL Java_com_didi365_dlna_jni_PlatinumJniProxy_stopDlnaMediaRender
  (JNIEnv *, jclass);

/*
 * Class:     com_didi365_dlna_jni_PlatinumJniProxy
 * Method:    responseGenaEvent
 * Signature: (I[B[B)Z
 */
JNIEXPORT jboolean JNICALL Java_com_didi365_dlna_jni_PlatinumJniProxy_responseGenaEvent
  (JNIEnv *, jclass, jint, jbyteArray, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
