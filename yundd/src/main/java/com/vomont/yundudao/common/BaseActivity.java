package com.vomont.yundudao.common;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class BaseActivity extends Activity
{
    public  Handler handlerMsg = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            getHandlderMsg(msg);
            
        };
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    
    public void getHandlderMsg(android.os.Message msg)
    {
        
    }
}
