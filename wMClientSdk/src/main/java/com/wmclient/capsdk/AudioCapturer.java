package com.wmclient.capsdk;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioCapturer implements com.wmclient.capsdk.WMCapSdk.OnEncodeDataCallBack
{
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public static final int mSampleRate = 22050;
    
    public static final int mChannels = 2;
    
    public static final int mAudiobitRate = mSampleRate * mChannels * 400 * 8 / 1000;
    
    // 音频获取源
    private int mAudioSource = MediaRecorder.AudioSource.MIC;
    
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    @SuppressWarnings("unused")
    public static int mChannelConfig = (mChannels == 1) ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO;
    
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    public static int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
    
    // 缓冲区字节大小
    @SuppressLint("NewApi")
	public static int mBufferSizeInBytes = AudioRecord.getMinBufferSize(mSampleRate, mChannelConfig, mAudioFormat);
    
    private AudioRecord mAudioRecord = null;
    
    private boolean mbRecord = false;
    
    private volatile int mStreamType = 0;
    
    private String TAG = "AudioCapturer";
    
    private int mFrameSize = mBufferSizeInBytes;
    
    private boolean mbEncode = true;
    
    private AudioEncoder mAuidoEncoder = null;
    
    public boolean mbAuidoCapturer = false;
   
    private boolean mbHasEndThread = false;
    private Lock mThreadLock = new ReentrantLock();
	private Condition mThreadCondition = mThreadLock.newCondition();
	
    public AudioCapturer()
    {
    	
    }
    
    public boolean startAudio(int samplingRate, int nChannels, int nbitRate)
    {
    	if (mbAuidoCapturer) {
			return false;
		}
    	
    	try {
			mAuidoEncoder = new AudioEncoder(mSampleRate, mChannels, mAudiobitRate);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return false;
		}
    	
    	mAuidoEncoder.setEncodeDataCallBack(this);
    	
    	 if (!startRecord())
         {
    		 mAuidoEncoder.close();
             mFrameSize = 0;
             
             return false;
         }
    	 
    	 mbAuidoCapturer = true;
    	
        Log.i(TAG, "startAudio");
        return true;
    }
    
    public void stopAudio()
    {
    	mbAuidoCapturer = false;
    	
    	stopRecord();
    	
    	if (null != mAuidoEncoder) {
    		mAuidoEncoder.close();
		}
    }
    
    //
    @SuppressLint("NewApi")
	private boolean startRecord()
    {
        
        // 创建AudioRecord对象
        mAudioRecord = new AudioRecord(mAudioSource, mSampleRate, mChannelConfig, mAudioFormat, mBufferSizeInBytes);
        if (null == mAudioRecord)
        {
            Log.e(TAG, "AudioRecord init fail, mSampleRate:" + mSampleRate + " mChannelConfig:" + mChannelConfig + " mAudioFormat:" + mAudioFormat + " mBufferSizeInBytes:%d" + mBufferSizeInBytes);
            return false;
        }
        
        mbRecord = true;
        mAudioRecord.startRecording();
        
        new Thread(new AudioRecordThread()).start();
        
        return true;
    }
    
    @SuppressLint("NewApi")
	private void stopRecord()
    {
        
        if (mAudioRecord != null)
        {
            mbRecord = false;
            
            //wait stop
            mThreadLock.lock();	
			try {
				while (!mbHasEndThread) {
					mThreadCondition.await(25, TimeUnit.MILLISECONDS);
				}
			} catch (InterruptedException e) {
				Log.e(TAG, "InterruptedException");
			} 
			mThreadLock.unlock();
			
			//close and release
            mAudioRecord.stop();
            mAudioRecord.release();// 释放资源
            mAudioRecord = null;
        }
    }
    
    /**
     * 这里将数据写入文件，但是并不能播放，因为AudioRecord获得的音频是原始的裸音频，
     */
    class AudioRecordThread implements Runnable
    {
        
        @SuppressLint("NewApi")
		@Override
        public void run()
        {
        	mbHasEndThread = false;
            byte[] audiodata = new byte[mFrameSize*2];
            while (mbRecord)
            {
                
                int readsize = mAudioRecord.read(audiodata, 0, audiodata.length);
                
                if (readsize > 0 && null != mAuidoEncoder && mbEncode)
                {
                    long curTime = System.currentTimeMillis();
                    mAuidoEncoder.encode(audiodata, audiodata.length, curTime * 1000);
                }
            }
            
            mThreadLock.lock();
			mbHasEndThread = true;
			mThreadCondition.signalAll();
			mThreadLock.unlock();
        }
    }
    
    
    public int startRealPlay(int nStreamType, int nMark)
    {
        mStreamType = nStreamType;
        return Api.Api_Code_Result_OK;
    }
    
    public int stopRealPlay(int nStreamType, int nMark)
    {
        return Api.Api_Code_Result_OK;
    }
    
    public void stopAllRealPlay()
    {
    	
    }
    
    @Override
    public boolean OnEncodeDataCallBack(byte[] data, int nSize, long nPts)
    {
        // TODO Auto-generated method stub
        if (nSize > 0)
        {
            WMCapSdk.getInstance().InputData(mStreamType, Api.Api_Code_DataType_AAC, 0, data, nSize, nPts);
        }
        
        return true;
    }
}
