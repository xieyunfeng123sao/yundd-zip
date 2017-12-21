package com.wmclient.capsdk;

import java.io.IOException;

import vomont.ppmedia.DataPkg;
import vomont.ppmedia.MediaFormat;
import vomont.ppmedia.WMPlayer;
import vomont.ppmedia.WMPlayerInterface;
import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

public class VoiceTalkPlayer {

	public static final int VeyeDevVoiceEncodeType_G711A = 0;
	public static final int VeyeDevVoiceEncodeType_AAC = 1;
	public static final int VeyeDevVoiceEncodeType_G711U = 2;
	
	private final static String TAG = "TalkPlayer";


	private WMPlayer mWMPlayer = null;
	private SurfaceHolder mSurfaceHolder=null;

	private int mWidth = 0;
	private int mHight = 0;
	private int mSampleRate = 0;
	private int mChannels = 0;
	
	public static final int Status_NotPlay=0x0000;    
	public static final int Status_ConnectingUrl=0x0003;     ////
	public static final int Status_ConnectedUrl=0x0004;      //

	public final static int PlayMessage_Prepared = 0x1000;
	public final static int PlayMessage_Error = 0x1001;
	public final static int PlayMessage_Completion= 0x1002;
	public final static int PlayMessage_SeekComplete = 0x1003;
	public final static int PlayMessage_SurfaceDestroyed = 0x1004;
	
	private int mStatus = Status_NotPlay;

	public int getStatus(){
		return mStatus;
	}
	
	public void setStatus(int status){
		mStatus = status;
	}

	public int StartPlay(Object showObj)
	{
		setSurfaceView((SurfaceHolder) showObj);

		return Api.Api_Code_Result_OK;
	}

	public int StopPlay()
	{
		if(null == mWMPlayer)
		{
			return Api.Api_Code_Result_Error;
		}

		stopPlay();

		//reset
		mWMPlayer = null;
		mSurfaceHolder = null;

		return Api.Api_Code_Result_OK;
	}

	public int InputData(int nEncodeType, int nSampleRate, int nChannel, byte[] pBuf, int nSize)
	{
		mSampleRate = nSampleRate;
		mChannels = nChannel;
		
		int nDataType = MediaFormat.CODEID_AAC;
		byte[] realData = pBuf;
		int nDataSize = nSize;
		
		if (null == mWMPlayer) {
			startPlay(0, 0, nSampleRate, nChannel);
		}

		inputData(nDataType, realData, nDataSize, System.currentTimeMillis()*1000);

		return Api.Api_Code_Result_OK;
	}

	public WMPlayer getWMPlayer(){
		return mWMPlayer;
	}

	WMPlayer.OnPreparedListener mPreparedListener = new WMPlayer.OnPreparedListener() {

		@Override
		public void onPrepared(WMPlayer mp) {
			Log.i(TAG, "onPrepared");

			setStatus(Status_ConnectedUrl);
			mp.start();
		}
	};

	WMPlayer.OnErrorListener mErrorListener = new WMPlayer.OnErrorListener() {

		@Override
		public boolean onError(WMPlayer mp, int what, int extra) {
			Log.i(TAG, "[what:]" + String.valueOf(what) + "[extra:]" + String.valueOf(extra));


			stopPlay();

			return true;
		}
	};

	WMPlayer.OnInfoListener mInfoListener = new WMPlayer.OnInfoListener() {

		@Override
		public boolean onInfo(WMPlayer mp, int what, int extra) {
			if(what==WMPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
			{
				Log.i(TAG, "WMPlayer.MEDIA_INFO_VIDEO_RENDERING_START");

				return true;
			}

			return false;
		}
	};

	WMPlayer.OnCompletionListener mCompletionListener = new WMPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(WMPlayer mp) {

			stopPlay();
		}
	};

	WMPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new WMPlayer.OnSeekCompleteListener() {

		@Override
		public void onSeekComplete(WMPlayer mp) {

			Log.i(TAG, "WMPlayer seek complete");
		}
	};

	public void setSurfaceView(SurfaceHolder holder) {
		if (null == holder) {
			return;
		}

		//surfaceView.setZOrderOnTop(true);
		//surfaceView.getHolder().addCallback(this);
		mSurfaceHolder = holder;
	}

	public void setPam(int nWidth, int nHight, int nSampleRate, int nChannels){
		mWidth = nWidth;
		mHight = nHight;
		mSampleRate = nSampleRate;
		mChannels = nChannels;
	}

	public boolean startPlay(int nWidth, int nHight, int nSampleRate, int nChannels){

		//mLock.lock();
		if (null != mWMPlayer || null == mSurfaceHolder) 
		{
			//mLock.unlock();
			return false;
		}

		mWMPlayer = new WMPlayer();
		mWMPlayer.setDisplay(mSurfaceHolder);
		try {
			mWMPlayer.setMode(WMPlayerInterface.Mode_InputData);
			mWMPlayer.setIsSpeex(false);
			mWMPlayer.setDataSource("");

			MediaFormat videoFormat = null, audioFormat = null;
			if (nWidth > 0 && nHight > 0) {
				videoFormat = MediaFormat.createVideoFormat(MediaFormat.Video + MediaFormat.CODE_TYPE_H264, nWidth, nHight);
			}

			if (nSampleRate > 0 && nChannels > 0) {
				audioFormat = MediaFormat.createAudioFormat(MediaFormat.Audio + MediaFormat.CODE_TYPE_AAC, nSampleRate, nChannels);
			}

			mWMPlayer.setMediaFormat(videoFormat, audioFormat);

			mWMPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mWMPlayer.setScreenOnWhilePlaying(true);

			mWMPlayer.setOnPreparedListener(mPreparedListener);
			mWMPlayer.setOnErrorListener(mErrorListener);
			mWMPlayer.setOnCompletionListener(mCompletionListener);
			mWMPlayer.setOnInfoListener(mInfoListener);
			mWMPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);

			mWMPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			//mLock.unlock();
			return false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			//mLock.unlock();
			return false;
		} catch (SecurityException e) {
			e.printStackTrace();
			//mLock.unlock();
			return false;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//mLock.unlock();
			return false;
		}

		//mLock.unlock();

		setStatus(Status_ConnectingUrl);

		return true;
	}

	public boolean stopPlay(){

		//mLock.lock();
		if (null != mWMPlayer) {
			if(mWMPlayer.isPlaying())
			{
				mWMPlayer.stop();
			}

			mWMPlayer.release();
			mWMPlayer = null;
			setStatus(Status_NotPlay);
		} else {
			//mLock.unlock();
			return false;
		}

		//mLock.unlock();

		return true;
	}

	public void inputData(int nDataType, byte[] data, int nSize, long pts){
		//mLock.lock();
		if (null == mWMPlayer) {
			//mLock.unlock();
			return;
		}

		if (MediaFormat.CODEID_H264 == nDataType) {
			DataPkg inPutPkg = mWMPlayer.getVideoDataPkg();
			if (null == inPutPkg) {
				//mLock.unlock();
				return;
			}

			inPutPkg.setPts(pts);  //us
			inPutPkg.setSize(nSize);
			inPutPkg.getData().put(data);
			mWMPlayer.inputVideoDataPkg(inPutPkg);
		}
		else if (MediaFormat.CODEID_SPEEX == nDataType) {

		}
		else if (MediaFormat.CODEID_AAC == nDataType) {
			DataPkg inPutPkg = mWMPlayer.getAudioDataPkg();
			if (null == inPutPkg) {
				//mLock.unlock();
				return;
			}

			inPutPkg.setPts(pts);  //us
			inPutPkg.setSize(nSize);
			inPutPkg.getData().put(data);
			mWMPlayer.inputAudioDataPkg(inPutPkg);
		}
	}
}
