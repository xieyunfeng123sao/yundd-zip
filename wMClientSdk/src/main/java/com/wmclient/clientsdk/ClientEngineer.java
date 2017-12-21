package com.wmclient.clientsdk;

public class ClientEngineer 
{
	public native int init(int logLevel);
	public native int uninit();
	
	public native int login(String userName, String password, String svrIp, int svrPort);
	public native int logout();
	
	public native int UpdatePassword(String oldPassword, String newPassword);
	
	public native WMDeviceGroup[] GetDeviceGroupList();
	public native WMDeviceInfo[] GetDeviceList();	
	public native WMMapNodeInfo[] GetMapNodeList();
	
	public native int[] GetOnlineDeviceIds();
	
    public native int StartRealPlay(int deviceId, int devChannelId, int stteamType, Object callback);
    public native int StopRealPlay(int playerId);
    
    //public native int SetRealPlayCallBackFunc(int playerId, Object callback);
    //public native int ResetRealPlayCallBackFunc(int playerId);
    
    public native int PTZControl(int deviceId, int devChannelId, int ptzCommand, int nStop, int nSpeed);
    
    public native int StartRecord(int playerId, String filePath);
    public native int StopRecord(int playerId);
    
    public native int StartVoiceTalk(int deviceId, int devChannelId, int playerId);
    public native int StopVoiceTalk(int deviceId, int devChannelId, int playerId);
    
    public native int SendVoiceTalkData(int deviceId, int devChannelId, byte[] dataBuf, int dataLen);
    
    public native WMFileInfo[] FileSearch(int deviceId, int devChannelId, long startTime, long endTime);
    
    public native int StartFilePlay(String url, long startTime, long endTime, Object callback);
    
    public native int StopFilePlay(int playerId);
    
    public native int GetFilePlayPos(int playerId);
    public native int SetFilePlayPos(int playerId, int nPos);
    
    public native int SetDevLocationCB(Object callback);
    
    public native int SetAlarmMessageCB(Object callback);
    
    /*********/
    public native WMFileInfo[] FrontEndFileSearch(int deviceId, int devChannelId, long startTime, long endTime);
    
    public native int StartFrontEndFilePlay(String url, int nPos, long nFileSize, 
    		long startTime, long endTime, Object callback);
    
    public native int StopFrontEndFilePlay(int playerId);
    
    public native int GetFrontEndFilePlayPos(int playerId);
    
    public native int GetLastError();
    
    public native int SetDevStatusCB(Object callback);
    
    public native int AuthorizeDevice(int nGroupId, int nDevType, String szName, int nChannelCnt, String szSerialNumber);
    
    public native String SearchAuthorizeDevice(String szSerialNumber);
    
	static 
	{  
		System.loadLibrary("live555");
        System.loadLibrary("wmclientsdk");
    }    
}