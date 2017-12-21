
package com.code;

public class Speex  {
	public static final int CoderId_Err = 0;

	public static final int DEFAULT_COMPRESSION = 8;
	
	static {
		System.loadLibrary("speex1");
	}

	public Speex() {
	}
	
	public native int encoderOpen(int compression, int sampleRate, int filterLength);
	public native void encoderClose(int handle);
	public native int getEncoderFrameSize(int handle);
	public native int encodeData(int handle, short data[], int recOffset, int Size, 
			short play[], int playOffset, int platSize, byte encoded[], int encodeOffset, 
			Boolean bAecLost, Boolean bPreprocessLost);
	
	public native int decoderOpen(int compression, int sampleRate);
	public native void decoderClose(int handle);
	public native int getDecoderFrameSize(int handle);
	public native int decodeData(int handle, byte encoded[], int size, short decode[]);
	
	private static Speex m_sdkinstance = new Speex();

	public static Speex getInstance() 
	{
        return m_sdkinstance;
    }
}
