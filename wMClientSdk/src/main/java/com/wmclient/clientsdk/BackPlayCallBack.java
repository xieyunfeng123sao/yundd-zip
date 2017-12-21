package com.wmclient.clientsdk;

public interface BackPlayCallBack {
	public abstract void fBackPlayDataCallBack(int playerId, int dataType, byte[] pDataBuffer, int iDataSize);
}
