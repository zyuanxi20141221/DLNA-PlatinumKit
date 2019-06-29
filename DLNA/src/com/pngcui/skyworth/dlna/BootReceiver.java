package com.pngcui.skyworth.dlna;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.pngcui.skyworth.dlna.center.MediaRenderProxy;

/**
 * Created by pngcui on 2017/5/26. 
 */

public class BootReceiver extends BroadcastReceiver{

    private MediaRenderProxy mRenderProxy;
    private static final String TAG = "BootReceiver";
    
    private ConnectivityManager connectivity;
    private NetworkInfo netWorkinfo;
    
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	mContext = context;
    	
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            Log.i(TAG,"get BOOT_COMPLETED=============");
 			Log.i(TAG,"get BOOT_COMPLETED=============");
            startDLNA();
        }else if(intent.getAction().equals("com.mstar.android.ethernet.ETHERNET_STATE_CHANGED")){
        	Log.i(TAG,"get ETHERNET_STATE_CHANGED=============");
        	Log.i(TAG,"get ETHERNET_STATE_CHANGED=============");
        	if(isNetWorkConnected()){
        		Log.i(TAG,"start DLNA.....");
        		startDLNA();
        	}else{
        		Log.i(TAG,"stop DLNA.....");
        		stopDLNA();
        	}
        
        }
    }

    private void stopDLNA() {
    	mRenderProxy = MediaRenderProxy.getInstance();
        mRenderProxy.stopEngine();
	}

	private void startDLNA(){
		mRenderProxy = MediaRenderProxy.getInstance();
        mRenderProxy.startEngine();
    }
    
    private boolean isNetWorkConnected() {
        try{
            connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivity != null){
                netWorkinfo = connectivity.getActiveNetworkInfo();
                if(netWorkinfo != null && netWorkinfo.isAvailable()){
                    if(netWorkinfo.getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }catch(Exception e){
            return false;
        }
        return false;
    }
}

