package com.base64;

public class CCode {
	
	 public native int encode(byte[] inBuf, int nSize, byte[] outBuf , int nOutBufSize);

		static 
		{  
	        System.loadLibrary("base64sdk");
	    }    
}
