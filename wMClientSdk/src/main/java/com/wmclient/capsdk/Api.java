package com.wmclient.capsdk;

public class Api {
	public static final int Api_Code_Result_OK = 0x00;
	public static final int Api_Code_Result_Error = 0x01;
	
	public static final int Api_Code_DataType_H264 = 0x00;
	public static final int Api_Code_DataType_AAC = 0x01;
	public static final int Api_Code_DataType_Speex = 0x02;
	
	public static final int Api_Code_Status_ConnSuccess = 0x00;
	public static final int Api_Code_Status_ConnError = 0x01;
	public static final int Api_Code_Status_RigisterSuccess = 0x02;
	public static final int Api_Code_Status_RigisterError = 0x03;
	
	public static final long Api_Code_Gps_Prec = 10000000000000000L;
	
	public static final int Api_Code_FrameType_I = 1;
	public static final int Api_Code_FrameType_Other = 2;

	public native int Init(int nLogLevel);

	public native int Uninit();

	public native int SetConfigInfo(String jsonConfigInfo);

	public native int Start();

	public native int Stop();
	
	public native int ActiveRigister();

	public native int Exit();

	public native int ResponseRealPlay(int nResponseResult, int nStreamType, int nMark);
	public native int ResponseStopRealPlay(int nResponseResult, int nStreamType, int nMark);

	public native int InputData(int streamType, int nDataType, int nFrameType, byte[] dataBuf, int nBufSize, long nPts);
	public native int InputGps(long nTime, long nLongitude, long nLatitude, long nAltitude);

	/*****************************call back*************************************/
	public native int SetStatusCB(Object callBack);

	public native int SetRealPlayCB(Object callBack);
	public native int SetStopRealPlayCB(Object callBack);

	public native int SetGetDevConfigCB(Object callBack);
	public native int SetSetDevConfigCB(Object callBack);
	
	public native int SetStartVoiceTalkCB(Object callBack);
	public native int SetStopVoiceTalkCB(Object callBack);
	public native int SetVoiceTransDataCB(Object callBack);
	
	public native int SetRemoteDefinitionCB(Object callBack);
	public native int SetVisitorNumChangeCB(Object callBack);
	
	public native static int ProcessYUVData(byte[] indata, int ninSize, int srcWidth, int srcHight, byte[] out, 
			boolean bRo, boolean bFront);
    
	static 
	{  
        System.loadLibrary("capsdk");
    }   
}
