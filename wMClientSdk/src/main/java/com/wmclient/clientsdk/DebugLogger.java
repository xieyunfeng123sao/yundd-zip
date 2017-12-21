package com.wmclient.clientsdk;

import android.util.Log;

public class DebugLogger {
	
	public static void i(String msg) {
		//System.out.println("---------------------------:" + msg);
		Log.d("vomont-com", msg);
	}
	
}
