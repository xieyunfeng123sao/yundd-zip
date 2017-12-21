package com.vomont.yundudao.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;

public class NetWorkSpeedUtils
{
    private Context context;  
    private Handler mHandler;  
  
    private long lastTotalRxBytes = 0;  
    private long lastTimeStamp = 0;  
  
    public NetWorkSpeedUtils(Context context, Handler mHandler){  
        this.context = context;  
        this.mHandler = mHandler;  
    }  
  
    TimerTask task = new TimerTask() {  
        @Override  
        public void run() {  
            showNetSpeed();  
        }  
    };  
  
    public void startShowNetSpeed(){  
        lastTotalRxBytes = getTotalRxBytes();  
        lastTimeStamp = System.currentTimeMillis();  
        new Timer().schedule(task, 1000, 2000); // 1s后启动任务，每2s执行一次  
    }  
  
    private long getTotalRxBytes() {  
        return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 :(TrafficStats.getTotalRxBytes()/1024);//转为KB  
    }  
  
    private void showNetSpeed() {  
        long nowTotalRxBytes = getTotalRxBytes();  
        long nowTimeStamp = System.currentTimeMillis();
        long m=nowTimeStamp - lastTimeStamp;
        if(m==0)
        {
        	m=1000;
        }
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 /m);//毫秒转换  
        long speed2 = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 % m);//毫秒转换  
        
        lastTimeStamp = nowTimeStamp;  
        lastTotalRxBytes = nowTotalRxBytes;  
  
        Message msg = mHandler.obtainMessage();  
        msg.what = 100;  
        if(speed>=10)
        {
            speed =10;
        }
        msg.obj = String.valueOf(speed*25) + "." + String.valueOf(speed2).substring(0, 1) + " kb/s";  
        mHandler.sendMessage(msg);//更新界面  
    }  
}  
