package com.wmclient.capsdk;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.media.MediaCodecInfo;
import android.util.Log;

public class VideoEncoder{
	private Lock mEncoderLock = new ReentrantLock();
	private Condition mEncoderCondition = mEncoderLock.newCondition();
	
	private Lock mYUVLock = new ReentrantLock();
	private byte[] yuv = null;
	
	public boolean mbStop = true;
	
	private String TAG = "VideoEncoder";
	
	public static int mEncodeWidth = 240;
	public static int mEncodeHight = 320;
	public static int mEncodeFrameRate = 10;
	public static int mEncodeBitRate = 2500000;
	
	public static final int mMaxQP = 23;
	public static final int mMinQP = 23;
	
	private AvcEncoder mAvcEncoder = null;
	public static boolean mbDevCode = true;
	
	private byte[] mEncodeBuf = null;
	
	private int mTotFrameNumber = 0;
	

	private boolean mbHasEndThread = false;
	private int mStreamType = 0;
	private boolean mbFront = true;
	
	private Lock mSleepLock = new ReentrantLock();
	private Condition mCondition = mSleepLock.newCondition();


	public VideoEncoder() 
	{
		mbStop = true;
		mbHasEndThread = false;
	}
	
	public boolean isStart()
	{
		return !mbStop;
	}

	public boolean startEncoder(int nEncodeWidth, int nEncodeHight, int nFrameRate, int nBitRate){
		
		try {
			mEncoderLock.lock();
			mAvcEncoder = new AvcEncoder(nEncodeWidth, nEncodeHight, nFrameRate, nBitRate);
			yuv = new byte[nEncodeWidth*nEncodeHight*3/2];  
			mEncoderLock.unlock();
			
			mEncodeWidth = nEncodeWidth;
			mEncodeHight = nEncodeHight;
			mEncodeFrameRate = nFrameRate;
			
			mbStop = false;
			mbHasEndThread = false;
			new Thread(new VideoEncodeThread()).start(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}

		return true;
	}

	public void stopEncoder()
	{
		if (null != mAvcEncoder) {
			mbStop = true;
			
			mEncoderLock.lock();
			
			try {
				while (!mbHasEndThread) {
					mEncoderCondition.await(25, TimeUnit.MILLISECONDS);
				}

			} catch (InterruptedException e) {
				Log.e(TAG, "InterruptedException");
			} 
			
			mEncoderLock.unlock();
			
			mAvcEncoder.close();
			mAvcEncoder = null;			
		}
	}
	
	public void decodeData(byte[] data, int nSize, int nWidth, int nHight, int nStreamType, boolean bFront)
	{
			mYUVLock.lock();
			System.arraycopy(data, 0, yuv, 0, data.length);
			mStreamType = nStreamType;
			mbFront = bFront;
			mYUVLock.unlock();
	}
	
	public void yuvchangecolor(byte[] yv12buf, byte[] yuv420sp, int width, int height)
	{
		if (yv12buf == null)
		throw new NullPointerException("buffer 'outputbuf' is null");
		final int frameSize = width * height;
		 
		for (int i = 0; i < frameSize; i++)
		{
			yv12buf[i] = yuv420sp[i];
		}
		
		for (int i = 0; i < frameSize/4; i++) 
		{
			yv12buf[frameSize + i] = yuv420sp[frameSize + i * 2];
			yv12buf[frameSize*5/4 + i]= yuv420sp[frameSize + i * 2 + 1];
		}
	}
	
	class VideoEncodeThread implements Runnable { 

		@Override 
		public void run() 
		{ 
			mbHasEndThread = false;
			long nBeginFrameTime = 0;
			mTotFrameNumber = 0;
			byte[] yuv420 = new byte[mEncodeWidth*mEncodeHight*3/2];
			byte[] yuvClo = new byte[mEncodeWidth*mEncodeHight*3/2];
			mEncodeBuf = new byte[mEncodeWidth*mEncodeHight];
			while (!mbStop) 
			{
				if (!WMCapSdk.getInstance().IsNeedCodeVideoData()) {
					avSyncWait(10*1000*1000);
					continue;
				}
				
				long nCurrentTime = System.currentTimeMillis();
				nBeginFrameTime = (nBeginFrameTime > 0)?nBeginFrameTime:nCurrentTime;
				
				long nWaitTime = 1000/mEncodeFrameRate;
				long nSleepTime = nWaitTime*mTotFrameNumber - (nCurrentTime - nBeginFrameTime);
				
				//Log.i(TAG, "nSleepTime: " + nSleepTime);
				avSyncWait(nSleepTime*1000*1000);
				
				//get yuv
				mYUVLock.lock();
				nCurrentTime = System.currentTimeMillis();
				int nOutSize = 0;
				
				Api.ProcessYUVData(yuv, yuv.length, mEncodeHight, mEncodeWidth, yuv420, (!VideoCapturer.mbLandscape), mbFront);
				long nInputTime = System.currentTimeMillis() - nCurrentTime;
				//Log.e(TAG, "yuv data process time: " + nInputTime);
				
				mYUVLock.unlock();
				
				mTotFrameNumber++;
				
				//input queue
				if (null != mAvcEncoder)
				{
					int nClor = mAvcEncoder.getClor();
					boolean bNotSemiPlanar = false;
					if (MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar != nClor) {
						bNotSemiPlanar = true;
					}
					
					if (bNotSemiPlanar) {
						yuvchangecolor(yuvClo, yuv420, mEncodeWidth, mEncodeHight);
						mAvcEncoder.input(yuvClo, computePresentationTime(mTotFrameNumber));
					} else {
						mAvcEncoder.input(yuv420, computePresentationTime(mTotFrameNumber));
					}
					
					mAvcEncoder.offerEncoder(mEncodeBuf, mStreamType);
				}
			}
			
			mEncoderLock.lock();
			mbHasEndThread = true;
			mEncoderCondition.signalAll();
			mEncoderLock.unlock();
		} 
	}
	
	private void avSyncWait(long nanosTimeout)
	{
		if (nanosTimeout <= 0) {
			Log.w(TAG, "nanosTimeout: " + nanosTimeout);
			
			return;
		}
		
		mSleepLock.lock();
		try {
			mCondition.awaitNanos(nanosTimeout);
			
		} catch (InterruptedException e)
		{
			mSleepLock.unlock();
		} 
		finally 
		{
			mSleepLock.unlock();
		}
	}
	
	 private long computePresentationTime(long frameIndex) {
	        return frameIndex * 1000000 / mEncodeFrameRate;
	    }
	 
	   private void YV12RotateNegative90(byte[] dst, byte[] src, int srcWidth, int srcHeight)
	    {
	        int wh = srcWidth * srcHeight;
	        int uvHeight = srcHeight >> 1; // uvHeight = height / 2
	        
	        // 鏃嬭浆Y
	        int k = 0;
	        for (int i = 0; i < srcWidth; i++)
	        {
	            int nPos = srcWidth - 1;
	            for (int j = 0; j < srcHeight; j++)
	            {
	                dst[k] = src[nPos - i];
	                k++;
	                nPos += srcWidth;
	            }
	        }
	        
	        for (int i = 0; i < srcWidth; i += 2)
	        {
	            int nPos = wh + srcWidth - 1;
	            for (int j = 0; j < uvHeight; j++)
	            {
	                dst[k] = src[nPos - i - 1];
	                dst[k + 1] = src[nPos - i];
	                k += 2;
	                nPos += srcWidth;
	            }
	        }
	    }
	    
	    private void YV12Rotate90(byte[] dst, byte[] src, int srcWidth, int srcHeight)
	    {
	        int wh = srcWidth * srcHeight;
	        // 鏃嬭浆Y
	        int k = 0;
	        for (int i = 0; i < srcWidth; i++)
	        {
	            for (int j = srcHeight - 1; j >= 0; j--)
	            {
	                dst[k] = src[srcWidth * j + i];
	                k++;
	            }
	        }
	        
	        for (int i = 0; i < srcWidth; i += 2)
	        {
	            for (int j = srcHeight / 2 - 1; j >= 0; j--)
	            {
	                dst[k] = src[wh + srcWidth * j + i];
	                dst[k + 1] = src[wh + srcWidth * j + i + 1];
	                k += 2;
	            }
	        }
	    }
}