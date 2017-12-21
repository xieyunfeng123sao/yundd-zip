package com.wmclient.clientsdk;

import java.io.IOException;
import java.io.OutputStreamWriter;
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

public class RtspPlayer implements IPlayer
{
	private static final int Errcode_Buf_Over = 11;
	private static final int StreamType_Video_H264_DATA = 0;
	private static final int StreamType_Video_H264_HEAD = 1;
	private static final int StreamType_Audio_AAC = 9;
	private static final int StreamType_Audio_PCM_MULAW = 13;
	private static final int StreamType_Audio_PCM_ALAW = 14;
	private static final int StreamType_Audio_Speex = 18;
	private static final long WAIT_VIDEO_AUDIO_TIME = 1500; //ms
	
	/********************************************/
	private final static String TAG = "TalkPlayer";

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
	private SurfaceHolder mSurfaceHolder=null;
	
	private int mStatus = Status_NotPlay;
	
	private int mWidth = 0;
	private int mHight = 0;
	private int mSampleRate = 0;
	private int mChannels = 0;
	
	private String mVideoCodeType = "";
	private String mAudioCodeType = "";
	
	private long mVideoComeTime = 0;
	private long mAudioComeTime = 0;
	private long mFristComeTime = 0;

	
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
		
		mVideoComeTime = 0;
		mAudioComeTime = 0;
		mFristComeTime = 0;
		
		return Constants.success;
	}
	
	@Override
	public int StopPlay()
	{
		if(null == mWMPlayer)
		{
			return Constants.fail;
		}
		
		stopPlay();
		
		//reset
		mWMPlayer = null;
		mSurfaceHolder = null;
		
		return Constants.success;
	}

	@Override
	public int InputData(byte[] pBuf, int nSize)
	{
		int nHeadSize = 4 + 4 + 2 + 2;
		if (nSize < nHeadSize) {
			return Constants.fail;
		}
		
		//head
//		int offset = 0;
//		int nWidth = CommonUtil.getInt(pBuf, offset);
//		offset += 4;
//		
//		int nHight = CommonUtil.getInt(pBuf, offset);
//		offset += 4;
//		
//		int nSampleRate = CommonUtil.getInt(pBuf, offset);
//		offset += 4;
//		
//		int nChannels = CommonUtil.getInt(pBuf, offset);
//		offset += 4;
//		
//		//reserved
//		offset += 64;
		long curTime = System.currentTimeMillis();
		if (mFristComeTime <= 0 ) {
			mFristComeTime = curTime;
		}
		
		int offset = 0;
		mWidth = 1920;
		mHight = 1920;
		
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
		if (nInputDataType == StreamType_Video_H264_DATA || nInputDataType == StreamType_Video_H264_HEAD) {
			
			nDataSize = nSize - nHeadSize;
			realData = new byte[nDataSize];
			System.arraycopy(pBuf, offset, realData, 0, nDataSize);
			
			nDataType = MediaFormat.CODEID_H264;
			mVideoCodeType = MediaFormat.CODE_TYPE_H264;
			
			mVideoComeTime = curTime;
		}
		else if (nInputDataType == StreamType_Audio_AAC)
		{
			mChannels = CommonUtil.getInt(pBuf, offset);
			offset += 4;
			
			mSampleRate = CommonUtil.getInt(pBuf, offset);
			offset += 4;
			
			//
			nDataSize = nSize - offset;
			realData = new byte[nDataSize];
			System.arraycopy(pBuf, offset, realData, 0, nDataSize);
			
			nDataType = MediaFormat.CODEID_AAC;
			mAudioCodeType = MediaFormat.CODE_TYPE_AAC;
			
			mAudioComeTime = curTime;
		}
		else if (nInputDataType == StreamType_Audio_PCM_MULAW)
		{
			mChannels = CommonUtil.getInt(pBuf, offset);
			offset += 4;
			
			mSampleRate = CommonUtil.getInt(pBuf, offset);
			offset += 4;
			
			//
			nDataSize = nSize - offset;
			realData = new byte[nDataSize];
			System.arraycopy(pBuf, offset, realData, 0, nDataSize);
			
			nDataType = MediaFormat.CODEID_PCM_MULAW;
			mAudioCodeType = MediaFormat.CODE_TYPE_PCM_MULAW;
			
			mAudioComeTime = curTime;
		}
		else if (nInputDataType == StreamType_Audio_PCM_ALAW)
		{
			mChannels = CommonUtil.getInt(pBuf, offset);
			offset += 4;
			
			mSampleRate = CommonUtil.getInt(pBuf, offset);
			offset += 4;
			
			//
			nDataSize = nSize - offset;
			realData = new byte[nDataSize];
			System.arraycopy(pBuf, offset, realData, 0, nDataSize);
			
			nDataType = MediaFormat.CODEID_PCM_ALAW;
			mAudioCodeType = MediaFormat.CODE_TYPE_PCM_ALAW;
			
			mAudioComeTime = curTime;
		}
		else 
		{
			return Constants.fail;
		}
		
		//check video and audio
		if ((0 != mVideoComeTime && 0 != mAudioComeTime)
				|| (0 != mVideoComeTime && (mVideoComeTime - mFristComeTime > WAIT_VIDEO_AUDIO_TIME))) {
			long pts = (long)nSpts*1000*1000 + nUSpts;
			
			if (null == mWMPlayer) {
				if (!startPlay(mWidth, mHight, mSampleRate, mChannels, null)) {
					return Constants.fail;
				}
			}
			
			if (!mWMPlayer.isPlaying()) {
				return Constants.fail;
			}
			
			inputData(nDataType, realData, nDataSize, pts);
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
		return Constants.success;
	}
	
	@Override
	public int CloseSound()
	{
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
		
	    if(mWMPlayer == null){
	    	return Constants.fail;
	    }
		if(!mWMPlayer.isPlaying()){
			return Constants.fail;
		}
		
		boolean snapshot = mWMPlayer.snapshot(fileName);
		if(!snapshot){
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
		if(null == mWMPlayer){
			return false;
		}
		
		
		mWMPlayer.snapshot(fileName);
		
		return true;
	}
	
	public boolean startMuxer(String fileName)
	{
		if(null == mWMPlayer){
			return false;
		}
		
		
		mWMPlayer.startMuxer(fileName);
		
		return true;
	}
	
	public boolean stopMuxer()
	{
		if(null == mWMPlayer){
			return false;
		}
		
		
		mWMPlayer.stopMuxer();
		
		return true;
	}
	
	public void PostMsg(int nMessageId)
	{
		if (null == mHandler) {
			return;
		}
		
		Message msg=new Message();
		msg.what=nMessageId;
		mHandler.sendMessage(msg);
	}
	
	WMPlayer.OnPreparedListener mPreparedListener = new WMPlayer.OnPreparedListener() {

		@Override
		public void onPrepared(WMPlayer mp) {
			Log.v(TAG, "onPrepared");
			
			setStatus(Status_ConnectedUrl);
			mp.start();
		}
	};

	WMPlayer.OnErrorListener mErrorListener = new WMPlayer.OnErrorListener() {

		@Override
		public boolean onError(WMPlayer mp, int what, int extra) {
			Log.v(TAG, "[what:]" + String.valueOf(what) + "[extra:]" + String.valueOf(extra));

			PostMsg(PlayMessage_Error);
			
			stopPlay();
			
			return true;
		}
	};

	WMPlayer.OnInfoListener mInfoListener = new WMPlayer.OnInfoListener() {

		@Override
		public boolean onInfo(WMPlayer mp, int what, int extra) {
			if(what==WMPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
			{
				Log.v(TAG, "WMPlayer.MEDIA_INFO_VIDEO_RENDERING_START");

				return true;
			}

			return false;
		}
	};

	WMPlayer.OnCompletionListener mCompletionListener = new WMPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(WMPlayer mp) {
			PostMsg(PlayMessage_Completion);
			
			stopPlay();
		}
	};

	WMPlayer.OnSeekCompleteListener mOnSeekCompleteListener = new WMPlayer.OnSeekCompleteListener() {

		@Override
		public void onSeekComplete(WMPlayer mp) {
			PostMsg(PlayMessage_SeekComplete);
			
			Log.v(TAG, "WMPlayer seek complete");
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
	
	private void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	public void setPam(int nWidth, int nHight, int nSampleRate, int nChannels){
		mWidth = nWidth;
		mHight = nHight;
		mSampleRate = nSampleRate;
		mChannels = nChannels;
	}
	
	public boolean startPlay(int nWidth, int nHight, int nSampleRate, int nChannels, 
			WMPlayer.OnAudioSpeexTrackDataListener listener){

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
				videoFormat = MediaFormat.createVideoFormat(MediaFormat.Video + mVideoCodeType, nWidth, nHight);
			}
			
			if (nSampleRate > 0 && nChannels > 0) {
				audioFormat = MediaFormat.createAudioFormat(MediaFormat.Audio + mAudioCodeType, nSampleRate, nChannels);
			}
			
			mWMPlayer.setMediaFormat(videoFormat, audioFormat);

			mWMPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mWMPlayer.setScreenOnWhilePlaying(true);

			mWMPlayer.setOnPreparedListener(mPreparedListener);
			mWMPlayer.setOnErrorListener(mErrorListener);
			mWMPlayer.setOnCompletionListener(mCompletionListener);
			mWMPlayer.setOnInfoListener(mInfoListener);
			mWMPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
			mWMPlayer.setOnAudioSpeexTrackDataListener(listener);

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
		else if (MediaFormat.CODEID_AAC == nDataType
				|| MediaFormat.CODEID_PCM_MULAW == nDataType
				|| MediaFormat.CODEID_PCM_ALAW == nDataType) {
			
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
