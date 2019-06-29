#include<android/log.h>

#define TAG_P "Platinum-jni"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG_P ,__VA_ARGS__)  
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG_P ,__VA_ARGS__)  
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG_P ,__VA_ARGS__)   
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG_P ,__VA_ARGS__)  
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG_P ,__VA_ARGS__)
