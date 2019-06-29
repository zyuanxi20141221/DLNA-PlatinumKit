#include <jni.h>
#include "PltMediaRendererDelegate.h"
#include "Platinum.h"
#include <assert.h>
#include <sys/types.h>
#include "com_didi365_dlna_jni_PlatinumJniProxy.h"

////////////////pngcui///////////////////////
#include<android/log.h>
#define TAG "MediaRenderDelegate" 
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__)

extern JavaVM *g_vm ;
extern jclass g_inflectClass;  
extern jmethodID g_methodID; 

//***********************************************************************
//			add by pngcui for PltMediaRendererDelegate
//***********************************************************************
PltMediaRendererDelegate::PltMediaRendererDelegate() :	
    PLT_MediaRendererDelegate()
{
		LOGI("PltMediaRendererDelegate()");
}

PltMediaRendererDelegate::~PltMediaRendererDelegate()
{
		LOGI("~PltMediaRendererDelegate()");
}


/*----------------------------------------------------------------------
|   ActionInflect to java
+---------------------------------------------------------------------*/
void PltMediaRendererDelegate::ActionInflect(int cmd, const char* value, const char* data)  
{  
    int status;  
    JNIEnv *env = NULL;  
    bool isAttach = false;
	if(g_vm == NULL)
		LOGI("g_vm == NULL");
    status = g_vm->GetEnv((void **) &env, JNI_VERSION_1_4); //JNI_VERSION_1_6 
    if(status != 0)
    {
        status = g_vm->AttachCurrentThread(&env, NULL);  
		LOGI("status = %d",status);
        if(status < 0) {
            LOGI("callback_handler: failed to attach , current thread, status = %d", status);  
            return;  
        }  
        isAttach = true;  
    }
  
    jstring valueString = NULL;  
    jstring dataString = NULL; 
	
    jclass inflectClass = g_inflectClass;
    if (inflectClass == NULL)  
    {  
    	LOGI("inflectClass == NULL return");
    }
    jmethodID inflectMethod = g_methodID;
    if (inflectMethod == NULL)
    {  
    	LOGI("inflectMethod == NULL return");
    }
  
    if (inflectClass == NULL || inflectMethod == NULL)  
    {  	
		LOGI("goto end");	
        goto end;  
    }  
  
  
  	LOGI("CMD = %d VALUE = %s DATA = %s",cmd, value, data);  
      
    valueString = env->NewStringUTF(value);  
    dataString = env->NewStringUTF(data);  
	
    env->CallStaticVoidMethod(inflectClass, inflectMethod, cmd, valueString, dataString);  
  
    env->DeleteLocalRef(valueString);  
    env->DeleteLocalRef(dataString);  
  
end:  
    if (env->ExceptionOccurred())  
    {  
    	LOGI("clear...");
        env->ExceptionDescribe();  
        env->ExceptionClear();  
    }  
    if (isAttach)  
    {  
        g_vm->DetachCurrentThread();  
    }    
}  


/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnGetCurrentConnectionInfo
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnGetCurrentConnectionInfo(PLT_ActionReference& action)
{
	LOGI("connectInfo");
	
//	NPT_String curURI;	
//	action->GetArgumentValue("CurrentTransportState", curURI);	
//	NPT_String metaData;
//	action->GetArgumentValue("CurrentURIMetaData", metaData); 
//	LOGI("CurrentTransportState = %s",curURI.GetChars());
//	ActionInflect(MEDIA_RENDER_CTL_MSG_PLAY, curURI.GetChars(),metaData.GetChars());
	
	return NPT_SUCCESS;
}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnNext
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnNext(PLT_ActionReference& action)
{
	LOGI("OnNext");

	NPT_String curURI;
	action->GetArgumentValue("NextURI", curURI);
	NPT_String metaData;
	action->GetArgumentValue("NextURIMetaData", metaData);
	ActionInflect(MEDIA_RENDER_CTL_MSG_NEXT, curURI.GetChars(), metaData.GetChars());

	return NPT_SUCCESS;
}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnPause
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnPause(PLT_ActionReference& action)
{
	LOGI("OnPause");

	NPT_String curURI;	
	action->GetArgumentValue("CurrentURI", curURI);	
	NPT_String metaData ;	
	action->GetArgumentValue("CurrentURIMetaData", metaData); 
	ActionInflect(MEDIA_RENDER_CTL_MSG_PAUSE, curURI.GetChars(),metaData.GetChars());

	return NPT_SUCCESS;
}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnPlay
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnPlay(PLT_ActionReference& action)
{
	LOGI("OnPlay");

	NPT_String curURI;	
	action->GetArgumentValue("CurrentURI", curURI);	
	NPT_String metaData ;	
	action->GetArgumentValue("CurrentURIMetaData", metaData); 
	ActionInflect(MEDIA_RENDER_CTL_MSG_PLAY, curURI.GetChars(),metaData.GetChars());

	return NPT_SUCCESS;
}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnPrevious
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnPrevious(PLT_ActionReference& action)
{
	LOGI("OnPrevious");

	NPT_String curURI;	
	action->GetArgumentValue("CurrentURI", curURI);	
	NPT_String metaData ;	
	action->GetArgumentValue("CurrentURIMetaData", metaData); 
	ActionInflect(MEDIA_RENDER_CTL_MSG_PRE, curURI.GetChars(),metaData.GetChars());

	return NPT_SUCCESS;

}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnSeek
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnSeek(PLT_ActionReference& action)
{
	LOGI("OnSeek");

	NPT_String curURI;	
	action->GetArgumentValue("Target", curURI);
	NPT_String metaData;
	action->GetArgumentValue("Unit", metaData);

	LOGI("Target = %s,Unit = %s",curURI.GetChars(),metaData.GetChars());

	ActionInflect(MEDIA_RENDER_CTL_MSG_SEEK, curURI.GetChars(),metaData.GetChars());

	return NPT_SUCCESS;
}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnStop
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnStop(PLT_ActionReference& action)
{
	LOGI("OnStop");

	NPT_String curURI;	
	action->GetArgumentValue("CurrentURI", curURI);	
	NPT_String metaData ;	
	action->GetArgumentValue("CurrentURIMetaData", metaData); 
	ActionInflect(MEDIA_RENDER_CTL_MSG_STOP, curURI.GetChars(),metaData.GetChars());

	return NPT_SUCCESS;

}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnSetAVTransportURI
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnSetAVTransportURI(PLT_ActionReference& action)
{
	LOGI("OnSetAVTransportURI");

	NPT_String curURI;	
	action->GetArgumentValue("CurrentURI", curURI);	
	NPT_String metaData ;	
	action->GetArgumentValue("CurrentURIMetaData", metaData); 
	ActionInflect(MEDIA_RENDER_CTL_MSG_SET_AV_URL, curURI.GetChars(),metaData.GetChars());	

	return NPT_SUCCESS;

}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnSetPlayMode
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnSetPlayMode(PLT_ActionReference& action)
{
	LOGI("OnSetPlayMode");

	NPT_String playMode;	
	action->GetArgumentValue("NewPlayMode", playMode);	
	NPT_String metaData ;	
	action->GetArgumentValue("CurrentURIMetaData", metaData); 
	ActionInflect(MEDIA_RENDER_CTL_MSG_SETPLAYMODE, playMode.GetChars(),metaData.GetChars());

	return NPT_SUCCESS;

}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnSetVolume
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnSetVolume(PLT_ActionReference& action)
{
	LOGI("OnSetVolume");

	NPT_String curURI;	
	action->GetArgumentValue("DesiredVolume", curURI);
	NPT_String metaData ;	
	action->GetArgumentValue("Channel", metaData); 
	LOGI("DesiredVolume = %s ,Channel = %s",curURI.GetChars(),metaData.GetChars());
	ActionInflect(MEDIA_RENDER_CTL_MSG_SETVOLUME, curURI.GetChars(),metaData.GetChars());

	return NPT_SUCCESS;
}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnSetVolumeDB
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnSetVolumeDB(PLT_ActionReference& action)
{
	LOGI("OnSetVolumeDB");

//	NPT_String curURI;	
//	action->GetArgumentValue("CurrentURI", curURI);	
//	NPT_String metaData ;	
//	action->GetArgumentValue("CurrentURIMetaData", metaData); 
//	ActionInflect(MEDIA_RENDER_CTL_MSG_SETVOLUME, curURI.GetChars(),metaData.GetChars());

	return NPT_SUCCESS;

}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnGetVolumeDBRange
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnGetVolumeDBRange(PLT_ActionReference& action)
{
	LOGI("OnGetVolumeDBRange");
//	NPT_String curURI;	
//	action->GetArgumentValue("CurrentURI", curURI);	
//	NPT_String metaData;
//	action->GetArgumentValue("CurrentURIMetaData", metaData); 
//	ActionInflect(MEDIA_RENDER_CTL_MSG_PLAY, curURI.GetChars(),metaData.GetChars());

	return NPT_SUCCESS;

}
/*----------------------------------------------------------------------
|   PltMediaRendererDelegate::OnSetMute
+---------------------------------------------------------------------*/
NPT_Result
PltMediaRendererDelegate::OnSetMute(PLT_ActionReference& action)
{
	LOGI("OnSetMute");

	NPT_String curURI;	
	action->GetArgumentValue("DesiredMute", curURI);	
	NPT_String metaData ;	
	action->GetArgumentValue("CurrentURIMetaData", metaData); 
	LOGI("value = %s,data = %s",curURI.GetChars(),metaData.GetChars());
	ActionInflect(MEDIA_RENDER_CTL_MSG_SETMUTE, curURI.GetChars(),metaData.GetChars());

	return NPT_SUCCESS;
}


