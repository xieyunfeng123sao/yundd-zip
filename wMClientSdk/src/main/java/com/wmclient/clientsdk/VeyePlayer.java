package com.wmclient.clientsdk;

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

public class VeyePlayer implements IPlayer
{
	private static final int Errcode_Buf_Over = 11;
	private static final int StreamType_Video_H264_DATA = 0;
	private static final int StreamType_Video_H264_HEAD = 1;
	private static final int StreamType_Audio_AAC = 9;
	private static final int StreamType_Hikvision = 16;
	private static final int StreamType_Audio_Speex = 18;
	
	/********************************************/
	private final static String TAG = "VeyePlayer";

	//status
	public static final int Status_NotPlay=0x0000;    
	public static final int Status_ConnectingUrl=0x0003;     ////
	public static final int Status_ConnectedUrl=0x0004;      //

	public final static int PlayMessage_Prepared = 0x1000;
	public final static int PlayMessage_Error = 0x1001;
	public final static int PlayMessage_Completion= 0x1002;
	public final static int PlayMessage_SeekComplete = 0x1003;
	public final static int PlayMessage_SurfaceDestroyed = 0x1004;
	
	private WMPlayer mWMPlayer = null;
	private HKPlayer mHkPlayer = null;
	private SurfaceHolder mSurfaceHolder=null;
	private int mPlayerStreamType = Constants.WMStreamType_RealTime;
	
	private int mStatus = Status_NotPlay;
	
	private int mWidth = 0;
	private int mHight = 0;
	private int mSampleRate = 0;
	private int mChannels = 0;
	
	//private Lock mLock = new ReentrantLock();
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}		
	};			

	@Override
	public boolean IsPlaying()
	{
		return (mSurfaceHolder != null) ? true : false;
	}
	
	@Override
	public int StartPlay(byte[] pStreamHead, int nSize, int streamType, Object showObj)
	{
		setSurfaceView((SurfaceHolder) showObj);
		mPlayerStreamType = streamType;
		
		return Constants.success;
	}
	
	@Override
	public int StopPlay()
	{
		if(null != mWMPlayer)
		{
			stopPlay();
			//reset
			mWMPlayer = null;
		}
		
		if (null != mHkPlayer) {
			mHkPlayer.StopPlay();
			mHkPlayer = null;
		}
		
		mSurfaceHolder = null;
		
		return Constants.success;
	}

	@Override
	public int InputData(byte[] pBuf, int nSize)
	{
		int nHeadSize = 4 + 4 + 4 + 4 + 64 + 4 + 4 + 2 + 2;
		if (nSize < nHeadSize) {
			return Constants.fail;
		}
		
		//head
		int offset = 0;
		int nWidth = CommonUtil.getInt(pBuf, offset);
		offset += 4;
		
		int nHight = CommonUtil.getInt(pBuf, offset);
		offset += 4;
		
		int nSampleRate = CommonUtil.getInt(pBuf, offset);
		offset += 4;
		
		int nChannels = CommonUtil.getInt(pBuf, offset);
		offset += 4;
		
		//reserved
		offset += 64;
		
		//body	
		int nSpts = CommonUtil.getInt(pBuf, offset);
		offset += 4;
		
		int nUSpts = CommonUtil.getInt(pBuf, offset);
		offset += 4;
		
		int nInputDataType = CommonUtil.getShort(pBuf, offset);
		offset += 2;
		
		int nFreameType = CommonUtil.getShort(pBuf, offset);
		offset += 2;
		
		int nDataType = -1;
		byte[] realData = null;
		int nDataSize = 0;
		if (StreamType_Hikvision != nInputDataType) {
			if (nInputDataType == StreamType_Video_H264_DATA || nInputDataType == StreamType_Video_H264_HEAD) {
				
				nDataSize = nSize - nHeadSize;
				realData = new byte[nDataSize];
				System.arraycopy(pBuf, offset, realData, 0, nDataSize);
				
				nDataType = MediaFormat.CODEID_H264;
			}
			else if (nInputDataType == StreamType_Audio_Speex) {
				//audio data
				if (nSize < 8) {
					return Constants.fail;
				}
				
				offset += 4 + 4;
				
				//
				nDataSize = nSize - offset;
				realData = new byte[nDataSize];
				System.arraycopy(pBuf, offset, realData, 0, nDataSize);
				
				nDataType = MediaFormat.CODEID_SPEEX;
			}
			else if (nInputDataType == StreamType_Audio_AAC) {
				if (nSize < 8) {
					return Constants.fail;
				}
				
				offset += 4 + 4;
				
				//
				nDataSize = nSize - offset;
				realData = new byte[nDataSize];
				System.arraycopy(pBuf, offset, realData, 0, nDataSize);
				
				nDataType = MediaFormat.CODEID_AAC;
			}
			else {
				return Constants.fail;
			}
			
			long pts = (long)nSpts*1000*1000 + nUSpts;
			
			if (null == mWMPlayer) {
				if (!startPlay(nWidth, nHight, nSampleRate, nChannels)) {
					return Constants.fail;
				}
			}
			
			if (!mWMPlayer.isPlaying()) {
				return Constants.fail;
			}
			
			inputData(nDataType, realData, nDataSize, pts);
		} else {
			if (null == mHkPlayer) {
				mHkPlayer = new HKPlayer();
				mHkPlayer.StartPlay(null, 0, mPlayerStreamType, mSurfaceHolder);
			}
			
			nDataSize = nSize - nHeadSize;
			realData = new byte[nDataSize];
			System.arraycopy(pBuf, offset, realData, 0, nDataSize);
			mHkPlayer.InputData(realData, nDataSize);
		}
	
		return Constants.success;
	}

	@Override
	public int PausePlay(int bPause)
	{
		return Constants.success;
	}

	@Override
	public int ControlFilePlay(int nControlCode, int nParam)
	{
		return Constants.success;
	}
	
	@Override
	public int GetPlaySpeed()
	{
		return Constants.success;
	}

	@Override
	public int GetPlayTime()
	{
		return Constants.success;
	}
	
	@Override
	public int SetPlayTime()
	{
		return Constants.success;
	}

	@Override
	public int OpenSound()
	{
		if (null != mHkPlayer) {
			mHkPlayer.OpenSound();
		}
		
		return Constants.success;
	}
	
	@Override
	public int CloseSound()
	{
		if (null != mHkPlayer) {
			mHkPlayer.CloseSound();
		}
		
		return Constants.success;
	}

	@Override
	public int SetVolume(int nVolume)
	{
		return Constants.success;
	}
	
	@Override
	public int GetVolume()
	{
		return Constants.success;
	}

	@Override
	public int SaveSnapshot(String fileName, int nFormat)
	{
		if(Constants.WMSnapshotType_JPEG != nFormat)
		{
			return Constants.fail;
		}
		
		if (!snapShot(fileName)) {
			return Constants.fail;
		}
	    
		return Constants.success;
	}

	@Override
	public int ResetSourceBuffer()
	{
		return Constants.success;
	}
	
	@Override
	public int GetSourceBufferSize()
	{
		return Constants.success;
	}	

	@Override
	public int StartVoiceTalk(AudioCaptureCallBack callBack)
	{
		return Constants.success;
	}	
	
	@Override
	public int StopVoiceTalk()
	{
		return Constants.success;
	}		
	
	@Override
	public int GetLastError()
	{
		return 0;
	}

	public int getStatus(){
		return mStatus;
	}
	
	public WMPlayer getWMPlayer(){
		return mWMPlayer;
	}
	
	public void setStatus(int status){
		mStatus = status;
	}
	
	public boolean snapShot(String fileName)
	{
		if(null == mWMPlayer && null == mHkPlayer){
			return false;
		}
		
		if (null != mWMPlayer) {
			mWMPlayer.snapshot(fileName);
		}
		else if (null != mHkPlayer) {
			mHkPlayer.SaveSnapshot(fileName, Constants.WMSnapshotType_JPEG);
		}
		
		return true;
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
			
			int offset = 0;
			int nPkgNum = CommonUtil.getInt(data, offset);
			offset += 4;
			
			for (int i = 0; i < nPkgNum; i++) {
				
				int nFrameSize = CommonUtil.getInt(data, offset);
				offset += 4;
			
				mWMPlayer.inputAudioSpeexData(data, offset, nFrameSize, pts);
				offset += nFrameSize;
			}
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
		
		//mLock.unlock();
	}
}
