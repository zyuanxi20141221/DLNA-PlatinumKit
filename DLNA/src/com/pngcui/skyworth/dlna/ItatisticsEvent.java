package com.pngcui.skyworth.dlna;

import java.util.HashMap;

public interface ItatisticsEvent {

	public void onEvent(String eventID);
	public void onEvent(String eventID, HashMap<String, String> map);
}
