package com.wmclient.clientsdk;

public interface RealPlayCallBack 
{	
	public abstract void fRealDataCallBack(int playerId, int dataType, byte[] pDataBuffer, int iDataSize);
};
