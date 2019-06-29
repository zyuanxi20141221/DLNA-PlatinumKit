package com.pngcui.skyworth.dlna.player;

import android.content.Context;

public class CheckDelayTimer extends AbstractTimer{

	private int lastPos = 0;
	
	public CheckDelayTimer(Context context) {
		super(context);


		
	}
	

	public void setPos(int pos){
		lastPos = pos;
	}
	
	public boolean isDelay(int pos){
		if (pos == 0 || pos != lastPos){
			return false;
		}
		
		return true;
	}

}
