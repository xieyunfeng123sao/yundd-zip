package com.wmclient.capsdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class CallBack
{
    @SuppressWarnings("unused")
    private Context mContext = null;
    
    private Handler mHandler = null;
    
    public static final int CallBack_Message_Status = 0x00;
    
    public static final int CallBack_Message_RealPlay = 0x01;
    
    public static final int CallBack_Message_StopRealPlay = 0x02;
    
    public CallBack(Context context, Handler handler)
    {
        this.mContext = context;
        this.mHandler = handler;
    }
    
    public void OnStatusCB(int nSatus)
    {
        Message message = new Message();
        message.what = CallBack_Message_Status;
        message.arg1 = nSatus;
        PostMessage(message);
    }
    
    public int OnRealPlayCB(int nStreamType, int nMark)
    {
        Message message = new Message();
        message.what = CallBack_Message_RealPlay;
        message.arg1 = nStreamType;
        message.arg2 = nMark;
        PostMessage(message);
        
        return Api.Api_Code_Result_OK;
    }
    
    public int OnStopRealPlayCB(int nStreamType, int nMark)
    {
        Message message = new Message();
        message.what = CallBack_Message_StopRealPlay;
        message.arg1 = nStreamType;
        message.arg2 = nMark;
        PostMessage(message);
        
        return Api.Api_Code_Result_OK;
    }
    
    private void PostMessage(Message message)
    {
        if (null == mHandler)
        {
            return;
        }
        
        mHandler.sendMessage(message);
    }
}
