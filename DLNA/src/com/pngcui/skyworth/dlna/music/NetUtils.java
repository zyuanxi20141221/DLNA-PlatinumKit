package com.pngcui.skyworth.dlna.music;

import android.graphics.drawable.Drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.pngcui.skyworth.dlna.util.CommonLog;
import com.pngcui.skyworth.dlna.util.LogFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtils {

private static final CommonLog log = LogFactory.createLog();
	
	public static Drawable requestDrawableByUri(String uri){
		if (uri == null || uri.length() == 0){
			return null;
		}
		
		Drawable drawable = null;
		int index = 0;
		while(true){
			if (index >= 3){
				break;
			}
			drawable = getDrawableFromUri(uri);
			if (drawable != null){
				break;
			}
			index++;
		}
			
		return drawable;
	}
	
	public static Drawable getDrawableFromUri(String uri){
		if (uri == null || uri.length() < 1){
			return null;
		}
		Drawable drawable = null;
		try {
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			InputStream input = conn.getInputStream();
			if (conn.getResponseCode() != 200){
			    log.e("getDrawableFromUri.getResponseCode() = " + conn.getResponseCode() + "\n" +
			    		"uri :" + uri + "is invalid!!!");
			    input.close();
				return null;
			}
			drawable = Drawable.createFromStream(input, "src");
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		//	log.e("getDrawableFromUri catch exception!!!e = " + e.getMessage());
		}
		
		return drawable;
	}

	//add by pngcui
	public static Drawable decodeDrawable(InputStream input){
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			o.inSampleSize = 4;
			Bitmap bitmap = BitmapFactory.decodeStream(input, null, o);
			Drawable dw = new BitmapDrawable(bitmap);
			return dw;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
