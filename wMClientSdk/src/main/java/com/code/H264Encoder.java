package com.code;

public class H264Encoder extends Thread{
	public static final int EncoderId_Err = 0;

	public native int CompressBuffer(long encoder, int type, byte[] in, int insize, byte[] out, long nInPts);

	public native long CompressBegin(int width, int height, int framerate, int maxQP, int minQP, int bitRate);

	public native int CompressEnd(long encoder);

	static 
	{
		System.loadLibrary("H264Android");
	}

	private static H264Encoder m_sdkinstance = new H264Encoder();

	public static H264Encoder getInstance() 
	{
        return m_sdkinstance;
    }
}