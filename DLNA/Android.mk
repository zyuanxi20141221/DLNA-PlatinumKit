
LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_STATIC_LIBRARIES := libplatinum-jni

LOCAL_MODULE_TAGS := optional
LOCAL_PROGUARD_ENABLED:= disabled
LOCAL_PACKAGE_NAME := DLNA
LOCAL_CERTIFICATE := platform

LOCAL_SRC_FILES += $(call all-java-files-under, src)

include $(BUILD_PACKAGE)


include $(CLEAR_VARS)
LOCAL_PREBUILT_LIBS := libplatinum-jni:libs/armeabi/libplatinum-jni.so
include $(BUILD_MULTI_PREBUILT)
