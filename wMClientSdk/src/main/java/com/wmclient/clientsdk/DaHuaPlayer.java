package com.wmclient.clientsdk;

import android.content.Context;
import android.os.Handler;

import com.company.PlaySDK.IPlaySDKCallBack.*;
import com.company.PlaySDK.Constants;
import com.company.PlaySDK.IPlaySDK;

public class DaHuaPlayer implements IPlayer
{
	private int m_hPlayer = 0;
	private static final int Max_volume = 100;
	
	@Override
	public boolean IsPlaying()
	{
		return (m_hPlayer != 0) ? true : false;
	}
	
	@Override
	public int StartPlay(byte[] pStreamHead, int nSize, int streamType, Object showObj)
	{	
		return com.wmclient.clientsdk.Constants.success;
	}
	
	@Override
	public int StopPlay()
	{
		return com.wmclient.clientsdk.Constants.success;
	}

	@Override
	public int InputData(byte[] pBuf, int nSize)
	{
		return com.wmclient.clientsdk.Constants.success;
	}

	@Override
	public int PausePlay(int bPause)
	{
		return com.wmclient.clientsdk.Constants.success;
	}

	@Override
	public int ControlFilePlay(int nControlCode, int nParam)
	{
		return com.wmclient.clientsdk.Constants.success;
	}
	
	@Override
	public int GetPlaySpeed()
	{
		return com.wmclient.clientsdk.Constants.success;
	}

	@Override
	public int GetPlayTime()
	{
		return com.wmclient.clientsdk.Constants.success;
	}
	
	@Override
	public int SetPlayTime()
	{
		return com.wmclient.clientsdk.Constants.success;
	}

	@Override
	public int OpenSound()
	{
		return com.wmclient.clientsdk.Constants.success;
	}
	
	@Override
	public int CloseSound()
	{
		return com.wmclient.clientsdk.Constants.success;
	}

	@Override
	public int SetVolume(int nVolume)
	{
		return com.wmclient.clientsdk.Constants.success;
	}
	
	@Override
	public int GetVolume()
	{
		return com.wmclient.clientsdk.Constants.success;
	}

	@Override
	public int SaveSnapshot(String fileName, int nFormat)
	{
		return com.wmclient.clientsdk.Constants.success;
	}

	@Override
	public int ResetSourceBuffer()
	{
		return com.wmclient.clientsdk.Constants.success;
	}
	
	@Override
	public int GetSourceBufferSize()
	{
		return com.wmclient.clientsdk.Constants.success;
	}	
	
	@Override
	public int StartVoiceTalk(AudioCaptureCallBack callBack)
	{
		return com.wmclient.clientsdk.Constants.success;
	}	
	
	@Override
	public int StopVoiceTalk()
	{
		return com.wmclient.clientsdk.Constants.success;
	}		
	
	@Override
	public int GetLastError()
	{
		return 0;
	}
}
