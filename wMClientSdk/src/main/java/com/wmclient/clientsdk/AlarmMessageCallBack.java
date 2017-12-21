package com.wmclient.clientsdk;

public interface AlarmMessageCallBack {
	
	/*报警类型*/
	public static final int EventAlarmType_Invalid = 0;
	public static final int DevAlarmType_DiskFull = 3;				//硬盘满
	public static final int DevAlarmType_FrameLost = 4;			    //丢帧
	public static final int DevAlarmType_MoveDetect = 5;			//移动侦测
	public static final int DevAlarmType_VideoShelter = 6;			//视频遮挡
	
	/* 
	* alarmType 报警类型, nDevId 设备id, nChannelId 通道id
	*/
	
	public abstract void fAlarmMessageCallBack(int alarmType, int nDevId, int nChannelId);
}
