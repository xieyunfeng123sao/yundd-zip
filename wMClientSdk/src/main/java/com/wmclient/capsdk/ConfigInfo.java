package com.wmclient.capsdk;


public class ConfigInfo {
	public int mHasVideo = 1;
	public int mWidth = VideoCapturer.mDisWidth;
	public int mHight = VideoCapturer.mDisHight;
	public int mFrameRate = VideoCapturer.mFrameRate;
	public int mBitRate = VideoCapturer.mBitRate;
	
	public int mHasAudio = 1;
	public int mSampleRate = AudioCapturer.mSampleRate;
	public int mChannels = AudioCapturer.mChannels;
	
	//
	private String mVeyeDevNum = null;
	private String mUserName = null;  
	private String mPassWord = null; 

	private String mSvrName = null;  
	private int mSvrType = 0;  
	private String mSvrIP = null;  
	private int mSvrPort = 0;
	
	public ConfigInfo(String mVeyeDevNum, String mUserName, String mPassWord,
			String mSvrName, int mSvrType, String mSvrIP, int mSvrPort)
	{
		this.mVeyeDevNum = mVeyeDevNum;
		this.mUserName = mUserName;
		this.mPassWord = mPassWord;
		this.mSvrName = mSvrName;
		this.mSvrType = mSvrType;
		this.mSvrIP = mSvrIP;
		this.mSvrPort = mSvrPort;
	}
}
