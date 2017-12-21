package com.wmclient.clientsdk;

public interface DevLocationCallBack 
{	/* 
	* nDevId 设备id, nChannelId 通道id, nTime 时间， nLongitude 经度， nLatitude 纬度 
	*/
	
	public abstract void fDevLocationCallBack(int nDevId, int nChannelId, long nTime, long nLongitude, long nLatitude);
};
