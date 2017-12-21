package com.wmclient.clientsdk;

public class Constants 
{
	public static final int success = 0;
	public static final int fail = 1;
	
	public static final int ErrorCode_ConnectFail = 2;
	public static final int ErrorCode_PlatformHasInit = 3;
	public static final int ErrorCode_PlatformNoInit = 4;
	public static final int ErrorCode_AccountError = 5;
	public static final int ErrorCode_LoginError = 6;
	public static final int ErrorCode_AsyncLogin = 7;
	public static final int ErrorCode_HasLogin = 8;
	public static final int ErrorCode_NoLogin = 9;
	public static final int ErrorCode_SDKCallInitFail = 10;
	public static final int ErrorCode_DeleteValidPlan = 11;
	public static final int ErrorCode_RepeatRealPlay = 12;
	public static final int ErrorCode_HasStartedFileStorage = 13;
	public static final int ErrorCode_HasStopedFileStorage = 14;
	public static final int ErrorCode_DeviceNotOnline = 15;
	public static final int ErrorCode_DeleteDevDroup_HasMember = 16; 
	public static final int ErrorCode_InvalidStorageSvr = 17;
	public static final int ErrorCode_InvalidStreamSvr = 18;
	public static final int ErrorCode_PlayerBufIsFull = 19;
	public static final int ErrorCode_LoadConfigError = 20;
	public static final int ErrorCode_InvalidMatrix = 21;
	public static final int ErrorCode_ResponseTimeout = 22;
	public static final int ErrorCode_WaitAsyncResult = 23;
	public static final int ErrorCode_InvalidParameter = 24;
	public static final int ErrorCode_NoValidStreamServer = 25;
	public static final int ErrorCode_CreatePlayerFail = 26;
	public static final int ErrorCode_NoOpenChannel = 27;
	public static final int ErrorCode_FileNoExist = 28;
	public static final int ErrorCode_DirectoryDeleteError = 29;
	public static final int ErrorCode_CacheDataNotEnough = 30;
	public static final int ErrorCode_VersionTooLow = 31;
	public static final int ErrorCode_UserNameExists = 32;
	public static final int ErrorCode_PasswordError = 33;
	public static final int ErrorCode_ExceedUserAccessLimit = 39;
	
	public static final int ErrorCode_GetHttpError = 10001;
	
	public static final int DEVICE_TYPE_INVALID = -1;
	public static final int DEVICE_TYPE_RTSP_DEV = 0;
	public static final int DEVICE_TYPE_HK_DEV = 1;
	public static final int DEVICE_TYPE_DH_DEV = 2;
	public static final int DEVICE_TYPE_HK_PUSHDEV = 3;
	public static final int DEVICE_TYPE_XNS_DEV = 4;
	public static final int DEVICE_TYPE_UNV_DEV = 5;
	public static final int DEVICE_TYPE_AXIS_241S_DEV = 6;
	public static final int DEVICE_TYPE_HIKCARD_DEV = 7;
	public static final int DEVICE_TYPE_HWNVR_DEV = 8;
	public static final int DEVICE_TYPE_SONY_DEV = 9;
	public static final int DEVICE_TYPE_LAUNCH_DEV = 10;
	public static final int DEVICE_TYPE_THARGES_DEV = 11;
	public static final int DEVICE_TYPE_XM_DEV = 12;
	public static final int DEVICE_TYPE_ONVIF_DEV = 16;
	public static final int DEVICE_TYPE_VEYE_DEV = 17;
	
	public static final int StreamDataType_Head = 0;
	public static final int StreamDataType_MediaData = 1;
	
	public static final int WMSnapshotType_JPEG = 0;
	public static final int WMSnapshotType_BMP = 1;
	
	public static final int WMPLAYERID_INVALID = 0;
	
	public static final int STREAMPLAY_HANDLE_INVALID = -1;
	
	public static final int WMStreamType_RealTime = 0;
	public static final int WMStreamType_File = 1;
	
	public static final int WMPlayControlCode_SpeedUp = 0;
	public static final int WMPlayControlCode_SpeedDown = 1;
	
	public static final int ErrorCode_PlayerBufOver = 101;	
	
	public static final int DeviceStatus_Offline = 0;
	public static final int DeviceStatus_Online = 1;
	
	public static final int WMPtzCommand_Up = 0;
	public static final int WMPtzCommand_Down = 1;
	public static final int WMPtzCommand_Left = 2;
	public static final int WMPtzCommand_Right = 3;
	public static final int	WMPtzCommand_ZoomAdd = 9;
	public static final int	WMPtzCommand_ZoomDec = 10;
	public static final int	WMPtzCommand_FocusAdd = 11;
	public static final int	WMPtzCommand_FocusDec = 12;
	
	public static final int STREAM_TYPE_MAIN = 0;
	public static final int STREAM_TYPE_SUB = 1;
}
