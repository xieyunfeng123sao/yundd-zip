package com.wmclient.clientsdk;

public interface IPlayer 
{
	public abstract boolean IsPlaying();
	
	public abstract int StartPlay(byte[] pStreamHead, int nSize, int streamType, Object showObj);
	public abstract int StopPlay();
	
	public abstract int InputData(byte[] pBuf, int nSize);

	public abstract int PausePlay(int bPause);

	public abstract int ControlFilePlay(int nControlCode, int nParam);
	public abstract int GetPlaySpeed();

	public abstract int GetPlayTime();
	public abstract int SetPlayTime();

	public abstract int OpenSound();
	public abstract int CloseSound();

	public abstract int SetVolume(int nVolume);
	public abstract int GetVolume();

	public abstract int SaveSnapshot(String fileName, int nFormat);

	public abstract int ResetSourceBuffer();
	public abstract int GetSourceBufferSize();
	
	public abstract int StartVoiceTalk(AudioCaptureCallBack callBack);
	public abstract int StopVoiceTalk();
	
	public abstract int GetLastError();	
}
