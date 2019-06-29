package com.pngcui.skyworth.dlna.center;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.pngcui.skyworth.dlna.image.ImageActivity;
import com.pngcui.skyworth.dlna.jni.PlatinumReflection;
import com.pngcui.skyworth.dlna.jni.PlatinumReflection.ActionReflectionListener;
import com.pngcui.skyworth.dlna.music.MusicActivity;
import com.pngcui.skyworth.dlna.util.CommonLog;
import com.pngcui.skyworth.dlna.util.CommonUtil;
import com.pngcui.skyworth.dlna.util.DlnaUtils;
import com.pngcui.skyworth.dlna.util.LogFactory;
import com.pngcui.skyworth.dlna.video.VideoActivity;

import android.service.dreams.DreamService;
import android.service.dreams.IDreamManager;
import android.content.ComponentName;
import android.os.ServiceManager;


public class DMRCenter implements ActionReflectionListener, IDMRAction{

	private static final CommonLog log = LogFactory.createLog();

	private static final String TAG = "DMRCenter";
	
	private Context mContext;
	
	private DlnaMediaModel mMusicMediaInfo;
	private DlnaMediaModel mVideoMediaInfo;
	private DlnaMediaModel mImageMediaInfo;
	
	private int mCurMediaInfoType = -1;
	public static final int CUR_MEDIA_TYPE_MUSCI = 0x0001;
	public static final int CUR_MEDIA_TYPE_VIDEO = 0x0002;
	public static final int CUR_MEDIA_TYPE_PICTURE = 0x0003;

	private IDreamManager mDreamManager;
	
	public DMRCenter(Context context){
		mContext = context;

		mDreamManager = IDreamManager.Stub.asInterface(
                			ServiceManager.getService(DreamService.DREAM_SERVICE));
	}
	
	@Override
	public synchronized void onActionInvoke(int cmd, String value, String data) {

		disableSrceenSaver();
	
		switch(cmd){		
			case PlatinumReflection.MEDIA_RENDER_CTL_MSG_SET_AV_URL:		
				onRenderAvTransport(value, data);
				break;
			case PlatinumReflection.MEDIA_RENDER_CTL_MSG_PLAY:				
				onRenderPlay(value, data);
				break;
			case PlatinumReflection.MEDIA_RENDER_CTL_MSG_PAUSE:
				onRenderPause(value, data);
				break;
			case PlatinumReflection.MEDIA_RENDER_CTL_MSG_STOP:
				onRenderStop(value, data);
				break;
			case PlatinumReflection.MEDIA_RENDER_CTL_MSG_SEEK:
				onRenderSeek(value, data);
				break;
			case PlatinumReflection.MEDIA_RENDER_CTL_MSG_SETMUTE:
				onRenderSetMute(value, data);
				break;
			case PlatinumReflection.MEDIA_RENDER_CTL_MSG_SETVOLUME:
				onRenderSetVolume(value, data);
				break;
			default:
				log.e("unrognized cmd!!!");
				break;
		}
	}

	/*
	* add by pngcui
	*/
	public void disableSrceenSaver(){
		try{
			if(mDreamManager.isDreaming()){
				mDreamManager.awaken();
			}
		}catch (Exception e) {
        	e.printStackTrace();
    	}
		return ;
	}
	
	/*
	 * add by pngcui
	 */
	public boolean isVideoFormat(String value){
		
		if(value.endsWith(".mp4") || value.endsWith(".mov") 
				|| value.endsWith(".3gp") || value.endsWith(".3gpp") 
						|| value.endsWith(".mkv")){
			return true;
		}
		return false;
	}
	
	/*
	 * add by pngcui
	 */
	public boolean isAudioFormat(String value){
		
		if(value.endsWith(".mp3") || value.endsWith(".flv") || value.endsWith(".wav")){
			
			return true;
		}
		return false;
	}
	
	/*
	 * add by pngcui
	 */
	public boolean isImageFormat(String value){
		
		if(value.endsWith(".jpg") || value.endsWith(".jpeg") 
				|| value.endsWith(".bmp") || value.endsWith(".png")){
			
			return true;
		}
		return false;
	}
	
	@Override
	public void onRenderAvTransport(String value, String data) {

		if (data == null){
			log.e("meteData = null!!!");
			return ;
		}
		
		if (value == null || value.length() < 2){
			log.e("url = " + value + ", it's invalid...");
			return ;
		}
		Log.i(TAG,"value = "+value+",data = "+data);
		DlnaMediaModel mediaInfo = DlnaMediaModelFactory.createFromMetaData(data);	
		mediaInfo.setUrl(value);
		if (DlnaUtils.isAudioItem(mediaInfo)){
			Log.i(TAG,"music");
			mMusicMediaInfo = mediaInfo;
			mCurMediaInfoType = CUR_MEDIA_TYPE_MUSCI;
		}else if (DlnaUtils.isVideoItem(mediaInfo)){
			Log.i(TAG,"video");
			mVideoMediaInfo = mediaInfo;
			mCurMediaInfoType = CUR_MEDIA_TYPE_VIDEO;
		}else if (DlnaUtils.isImageItem(mediaInfo)){
			Log.i(TAG,"picture");
			mImageMediaInfo = mediaInfo;
			mCurMediaInfoType = CUR_MEDIA_TYPE_PICTURE;
		}else{
			//add by pngcui for miui/baofeng
			if(isVideoFormat(value)){
				Log.i(TAG,"--video");
				mVideoMediaInfo = mediaInfo;
				mCurMediaInfoType = CUR_MEDIA_TYPE_VIDEO;
			}else if(isAudioFormat(value)){
				Log.i(TAG,"--music");
				mMusicMediaInfo = mediaInfo;
				mCurMediaInfoType = CUR_MEDIA_TYPE_MUSCI;
			}else if (isImageFormat(value)){
				Log.i(TAG,"--picture");
				mImageMediaInfo = mediaInfo;
				mCurMediaInfoType = CUR_MEDIA_TYPE_PICTURE;
			}else {
				Toast.makeText(mContext, "unSupport media type!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onRenderPlay(String value, String data) {
		Log.i(TAG,"onRenderPlay");
		switch(mCurMediaInfoType){
			case CUR_MEDIA_TYPE_MUSCI:
				if (mMusicMediaInfo != null){
					delayToPlayMusic(mMusicMediaInfo);
				}else{
					MediaControlBrocastFactory.sendPlayBrocast(mContext);
				}
				clearState();
				break;
			case CUR_MEDIA_TYPE_VIDEO:
				if (mVideoMediaInfo != null){
					delayToPlayVideo(mVideoMediaInfo);
				}else{
					MediaControlBrocastFactory.sendPlayBrocast(mContext);
				}
				clearState();
				break;
			case CUR_MEDIA_TYPE_PICTURE:
				if (mImageMediaInfo != null){
					delayToPlayImage(mImageMediaInfo);
				}else{
					MediaControlBrocastFactory.sendPlayBrocast(mContext);
				}
				clearState();
				break;
		}
	}

	@Override
	public void onRenderPause(String value, String data) {
		MediaControlBrocastFactory.sendPauseBrocast(mContext);
	}

	@Override
	public void onRenderStop(String value, String data) {
		delayToStop();
		MediaControlBrocastFactory.sendStopBorocast(mContext);
	}

	@Override
	public void onRenderSeek(String value, String data) {
		int seekPos = 0;
		value = data+"="+value;
		Log.i(TAG,value+" data ="+data);
		try {
			seekPos = DlnaUtils.parseSeekTime(value);
			Log.i(TAG,"===>pos = "+seekPos);
			MediaControlBrocastFactory.sendSeekBrocast(mContext, seekPos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRenderSetMute(String value, String data) {
		
		 if ("1".equals(value)){
			CommonUtil.setVolumeMute(mContext);
		}else if("0".equals(value)){
			CommonUtil.setVolumeUnmute(mContext);
		}
	}

	@Override
	public void onRenderSetVolume(String value, String data) {
		try {
			int volume = Integer.valueOf(value);
			if(volume < 101){				
				CommonUtil.setCurrentVolume(volume, mContext);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	
	private void clearState(){
		mMusicMediaInfo = null;
		mVideoMediaInfo = null;
		mImageMediaInfo = null;
	}
	

	
	private static final int DELAYTIME = 200;
	private void delayToPlayMusic(DlnaMediaModel mediaInfo){
		if (mediaInfo != null)
		{
			clearDelayMsg();
			Message msg = mHandler.obtainMessage(MSG_START_MUSICPLAY, mediaInfo);
			mHandler.sendMessageDelayed(msg, DELAYTIME);
		}
	}
	
	private void delayToPlayVideo(DlnaMediaModel mediaInfo){
		if (mediaInfo != null)
		{
			clearDelayMsg();
			Message msg = mHandler.obtainMessage(MSG_START_VIDOPLAY, mediaInfo);
			mHandler.sendMessageDelayed(msg, DELAYTIME);
		}
	}
	
	private void delayToPlayImage(DlnaMediaModel mediaInfo){
		if (mediaInfo != null){
			clearDelayMsg();
			Message msg = mHandler.obtainMessage(MSG_START_PICPLAY, mediaInfo);
			mHandler.sendMessageDelayed(msg, DELAYTIME);
		}
	}
	
	private void delayToStop(){
		clearDelayMsg();
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SEND_STOPCMD), DELAYTIME);
	}
	
	private void clearDelayMsg(){
		clearDelayMsg(MSG_START_MUSICPLAY);
		clearDelayMsg(MSG_START_PICPLAY);
		clearDelayMsg(MSG_START_VIDOPLAY);
		clearDelayMsg(MSG_SEND_STOPCMD);
	}
	
	private void clearDelayMsg(int num){
		mHandler.removeMessages(num);
	}
	

	
	private static final int MSG_START_MUSICPLAY = 0x0001;
	private static final int MSG_START_PICPLAY = 0x0002;
	private static final int MSG_START_VIDOPLAY = 0x0003;
	private static final int MSG_SEND_STOPCMD = 0x0004;
	
	private Handler mHandler = new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			
			try {
				switch(msg.what){
				case MSG_START_MUSICPLAY:
					DlnaMediaModel mediaInfo1 = (DlnaMediaModel) msg.obj;
					startPlayMusic(mediaInfo1);
					break;
				case MSG_START_PICPLAY:
					DlnaMediaModel mediaInfo2 = (DlnaMediaModel) msg.obj;
					startPlayPicture(mediaInfo2);
					break;
				case MSG_START_VIDOPLAY:
					DlnaMediaModel mediaInfo3 = (DlnaMediaModel) msg.obj;
					startPlayVideo(mediaInfo3);
					break;
				case MSG_SEND_STOPCMD:
					MediaControlBrocastFactory.sendStopBorocast(mContext);
					break;
			}
			} catch (Exception e) {
				e.printStackTrace();
				log.e("DMRCenter transdel msg catch Exception!!! msgID = " + msg.what);
			}
			
		}
		
	};

	
	private void startPlayMusic(DlnaMediaModel mediaInfo){
			log.d("startPlayMusic");
			Intent intent = new Intent();
			intent.setClass(mContext, MusicActivity.class);
	        DlnaMediaModelFactory.pushMediaModelToIntent(intent, mediaInfo);		
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			mContext.startActivity(intent);
	}
	


	private void startPlayVideo(DlnaMediaModel mediaInfo){
			log.d("startPlayVideo");
			Intent intent = new Intent();
			intent.setClass(mContext, VideoActivity.class);
	        DlnaMediaModelFactory.pushMediaModelToIntent(intent, mediaInfo);		
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);
			mContext.startActivity(intent);

	}
	
	private void startPlayPicture(DlnaMediaModel mediaInfo){
			log.d("startPlayPicture");
			Intent intent = new Intent();
			intent.setClass(mContext, ImageActivity.class);
		    DlnaMediaModelFactory.pushMediaModelToIntent(intent, mediaInfo);		
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_SINGLE_TOP);
	        mContext.startActivity(intent);	
	}

	

}
