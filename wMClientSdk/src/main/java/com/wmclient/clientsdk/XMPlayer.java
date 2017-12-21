package com.wmclient.clientsdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import com.lib.decoder.video.H264Decoder;
import com.xm.intercom.AudioDataCallBack;
import com.xm.intercom.IntercomSdk;

public class XMPlayer implements IPlayer, AudioDataCallBack
{
	private int m_hPlayer = 0;
	private static final int Max_volume = 100;
	
	private IntercomSdk mIntercomSdk = null;
	private AudioCaptureCallBack mCallBack = null;
	
	@Override
	public boolean IsPlaying()
	{
		return (m_hPlayer != 0) ? true : false;
	}
	
	@Override
	public int StartPlay(byte[] pStreamHead, int nSize, int streamType, Object showObj)
	{
		m_hPlayer = H264Decoder.OpenStream(showObj, 200, 0);	//200ms cache
		if(m_hPlayer == 0) 
		{
			return Constants.fail;
		}
		
		return Constants.success;
	}
	
	@Override
	public int StopPlay()
	{
		if(m_hPlayer != 0)
		{
			H264Decoder.Close(m_hPlayer);
			m_hPlayer = 0;
		}
		
		return Constants.success;
	}

	@Override
	public int InputData(byte[] pBuf, int nSize)
	{
		if(m_hPlayer != 0)
		{
			H264Decoder.InputData(m_hPlayer, pBuf, 0, nSize);
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
		if(m_hPlayer != 0)
		{		
			H264Decoder.SetSound(m_hPlayer, Max_volume/2);
		}
		
		return Constants.success;
	}
	
	@Override
	public int CloseSound()
	{
		if(m_hPlayer != 0)
		{		
			H264Decoder.SetSound(m_hPlayer, 0);
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
		
		//snap
		H264Decoder.SnapImage(m_hPlayer, fileName);
		
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
		mIntercomSdk = new IntercomSdk(null, null);
		mIntercomSdk.setAudioDataListener(this);
		
		mCallBack = callBack;
		mIntercomSdk.onStart();
		
		return Constants.success;
	}	
	
	@Override
	public int StopVoiceTalk()
	{
		mIntercomSdk.onStop();
		mCallBack = null;
		
		return Constants.success;
	}		
	
	@Override
	public int GetLastError()
	{
		return 0;
	}
	
	@Override
	public void onData(byte[] data, int len) 
	{
		if(mCallBack != null) 
		{
			mCallBack.fAudioCaptureCallBack(data, len);			
		}
	}
}
