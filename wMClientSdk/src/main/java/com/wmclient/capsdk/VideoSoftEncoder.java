package com.wmclient.capsdk;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.code.H264Encoder;

public class VideoSoftEncoder extends Thread{
	private long mEncoderId = H264Encoder.EncoderId_Err;

	private Lock mLock = new ReentrantLock();

	public VideoSoftEncoder() 
	{

	}
	
	public boolean isStart()
	{
		if (mEncoderId == H264Encoder.EncoderId_Err) {
			return false;
		}
		
		return true;
	}

	public boolean startEncoder(int nWidth, int nHight, int nFrameRate, int nBitRate){
		mLock.lock();
		mEncoderId= H264Encoder.getInstance().CompressBegin(nWidth, nHight, nFrameRate, 28, 13, nBitRate);
		if(H264Encoder.EncoderId_Err == mEncoderId)
		{
			mLock.unlock();
			return false;
		}

		mLock.unlock();

		return true;
	}

	public void stopEncoder()
	{
		mLock.lock();
		if (H264Encoder.EncoderId_Err != mEncoderId) {
			H264Encoder.getInstance().CompressEnd(mEncoderId); 
			mEncoderId = H264Encoder.EncoderId_Err;
		}

		mLock.unlock();
	}


	public int decodeData(byte[] data, int nSize, long pts, byte[] encodeBuf){
		mLock.lock();
		if(H264Encoder.EncoderId_Err == mEncoderId)
		{
			mLock.unlock();
			return 0;
		}

		int nRet = H264Encoder.getInstance().CompressBuffer(mEncoderId, -1, data, nSize, encodeBuf, pts);
		if(nRet <= 0)
		{
			mLock.unlock();
			return 0;
		}

		mLock.unlock();

		return nRet;
	}
}