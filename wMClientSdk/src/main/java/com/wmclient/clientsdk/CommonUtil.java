package com.wmclient.clientsdk;

public class CommonUtil {
	static public int writeToStream(byte[] bytes, int offset, int n)
	{
    	bytes[offset] = (byte)((n & 0xff000000) >> 24 );
    	bytes[offset + 1] = (byte) ((n & 0xff0000) >> 16);
    	bytes[offset + 2] = (byte)((n & 0xff00) >> 8);
    	bytes[offset + 3] = (byte) (n & 0xff);
    	return offset + 4;
    }
    
    static public int writeToStream(byte[] bytes, int offset, byte b) {
    	bytes[offset] = (byte) (b & 0xff);    	
    	return offset + 1;
    }
    
    static int writeToStream(byte[] bytes, int offset, short s) {
    	bytes[offset] = (byte) ( (s & 0xff00) >> 8 );
    	bytes[offset + 1] = (byte) (s & 0xff); 	
    	return offset + 2;
    }
    
    static public int writeToStream(byte[] bytes, int offset, byte[] bs, short bsLen) {
    	if(bsLen != 0){
        	System.arraycopy(bs, 0, bytes, offset, bsLen);	  		
    	}    	
    	return offset + bsLen;
    }

    //
    static public int CodeInt(byte[] bytes, int offset)
	{
    	int nLength = bytes[offset + 3] & 0xFF | (bytes[offset + 2] & 0xFF) << 8 | (bytes[offset + 1] & 0xFF) << 16
				| (bytes[offset] & 0xFF) << 24;
    	
    	return nLength;
    }
    
    static public int getInt(byte[] bytes, int offset)
   	{
       	int nLength = bytes[offset] & 0xFF | (bytes[offset + 1] & 0xFF) << 8 | (bytes[offset + 2] & 0xFF) << 16
   				| (bytes[offset + 3] & 0xFF) << 24;
       	
       	return nLength;
    }
    
    static public int getShort(byte[] bytes, int offset)
   	{
       	int nLength = bytes[offset] & 0xFF | (bytes[offset + 1] & 0xFF) << 8;
       	
       	return nLength;
    }
}
