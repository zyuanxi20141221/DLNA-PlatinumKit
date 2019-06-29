package com.pngcui.skyworth.dlna.image;

import com.pngcui.skyworth.dlna.util.CommonLog;
import com.pngcui.skyworth.dlna.util.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DownLoadHelper {

	private static final CommonLog log = LogFactory.createLog();
	
	private final static int THREAD_COUNT = 1;
	private ExecutorService mExecutorService;

	public DownLoadHelper(){
		
	}
	
	public void init(){
		if (mExecutorService == null){
			mExecutorService = Executors.newFixedThreadPool(THREAD_COUNT);
		}
	}
	
	
	public void unInit(){
		if (mExecutorService != null)
		{
			mExecutorService.shutdown();
			mExecutorService.shutdownNow();
			mExecutorService = null;
		}
	}
	
	public static interface IDownLoadCallback{
		public void downLoadResult(final boolean isSuccess, final String savePath);
	}
	

	public  boolean syncDownLoadFile(String requestUrl, String saveUrl, IDownLoadCallback callback){
		if (mExecutorService == null){
			return false;
		}
		log.d("syncDownLoadFile  requestUrl = "  + requestUrl);
		FileDownTask fTask = new FileDownTask(requestUrl, saveUrl, callback);
		mExecutorService.execute(fTask);
	
		return true;
		
	}
	
	
	
	
	
}
