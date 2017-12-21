package com.vomont.yundudao.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import com.video.opengl.GLSurfaceView20;
import com.vomont.yundudao.upload.VideoManager;
import com.wmclient.clientsdk.Constants;
import com.wmclient.clientsdk.StreamPlayer;
import com.wmclient.clientsdk.WMClientSdk;
import com.wmclient.clientsdk.WMFileInfo;

/**
 * 
 * 回放的接口封装
 * 
 * @author 谢云峰
 * @email 7736907@qq.com
 * @version [V1.00, 2016-9-14]
 * @see [相关类/方法]
 * @since V1.00
 */
public class PlayBackUtil
{
    
    private int playid;
    
    private RelativeLayout relativeLayout;
    
    private SurfaceView surfaceView;
    
    private StreamPlayer streamPlayer;
    
    private Activity context;
    
    private String path;
    
    private ShareUtil util;
    
    private boolean isStoped = true;
    
    private PlayBackListener playBackListener;
    
    private PlayBackRunnable playBackRunnable;
    
    public PlayBackUtil(Activity context)
    {
        this.context = context;
        util = new ShareUtil(context);
        path = VideoManager.yundd_phone + util.getShare().getNum();
    }
    
    /**
     * 建立媒体播放器
     * 
     * @param type
     *            播放器类型
     * @param relativeLayout
     *            播放器的容器
     */
    public StreamPlayer getStreamPlayer(int type, RelativeLayout relativeLayout, int deviceId, int devChannelId)
    {
        this.relativeLayout = relativeLayout;
        if (surfaceView != null)
        {
            stopPlay();
        }
        if (type == Constants.DEVICE_TYPE_XM_DEV)
        {
            surfaceView = new GLSurfaceView20(context);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            surfaceView.setLayoutParams(layoutParams);
            streamPlayer = WMClientSdk.getInstance().CreatePlayer(type, surfaceView.getTag(), Constants.WMStreamType_RealTime);
            relativeLayout.addView(surfaceView);
            return streamPlayer;
        }
        else
        {
            surfaceView = new SurfaceView(context);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            surfaceView.setLayoutParams(layoutParams);
            streamPlayer = WMClientSdk.getInstance().CreatePlayer(type, surfaceView.getHolder(), Constants.WMStreamType_RealTime);
            relativeLayout.addView(surfaceView);
            return streamPlayer;
        }
    }
    
    /**
     * 检索前端录像
     * 
     * @param fileList
     *            搜索的文件列表结果
     * @param deviceId
     *            设备id
     * @param devChannelId
     *            通道id
     * @param startTime
     *            开始时间戳
     * @param endTime
     *            结束时间戳
     * @return 0表示成功，其它表示失败原因
     */
    public int searchFrontEndFileList(List<WMFileInfo> fileList, int deviceId, int devChannelId, long startTime, long endTime)
    {
        
        int m = WMClientSdk.getInstance().searchFrontEndFileList(fileList, deviceId, devChannelId, startTime, endTime);// 663
                                                                                                                       // 12
                                                                                                                       // 1486915200000
                                                                                                                       // 1487001600000
                                                                                                                       // 1455292800000
        return m;
    }
    
    /**
     * 开始前端文件播放
     * 
     * @param nPos
     *            播放进度 1-100
     * @param info
     *            文件信息
     * @param player
     *            流媒体播放器对象
     */
    public void startFrontEndFilePlay(int nPos, WMFileInfo info, StreamPlayer player)
    {
        // playid = WMClientSdk.getInstance().startFrontEndFilePlay(nPos, info, player);
        // return playid;
        if (!isStoped)
        {
            playBackRunnable = new PlayBackRunnable(nPos, info, player);
            return;
        }
        new Thread(new PlayBackRunnable(nPos, info, player)).start();
    }
    
    class PlayBackRunnable implements Runnable
    {
        int nPos;
        
        WMFileInfo info;
        
        StreamPlayer player;
        
        public PlayBackRunnable(int nPos, WMFileInfo info, StreamPlayer player)
        {
            this.nPos = nPos;
            this.info = info;
            this.player = player;
        }
        
        @Override
        public void run()
        {
            isStoped = false;
            playid = WMClientSdk.getInstance().startFrontEndFilePlay(nPos, info, player);
            
            if (playid == Constants.WMPLAYERID_INVALID)
            {
                int lastError = WMClientSdk.getInstance().getLastError();
                
                if (playBackListener != null && lastError == 23)
                {
                    playBackListener.onConnectTimeOut();
                }
                else if (playBackListener != null)
                {
                    playBackListener.onFail();
                }
                isStoped = true;
                return;
            }
            if (playBackListener != null)
            {
                playBackListener.onSucess();
            }
            synchronized (PlayBackUtil.this)
            {
                try
                {
                    PlayBackUtil.this.wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            stop();
            // destoryStreamPlayer();
            if (playBackRunnable != null)
            {
                new Thread(playBackRunnable).start();
                playBackRunnable = null;
            }
            isStoped = true;
        }
    }
    
    public void setPlayBackListener(PlayBackListener playBackListener)
    {
        this.playBackListener = playBackListener;
    }
    
    public interface PlayBackListener
    {
        void onSucess();
        
        void onFail();
        
        void onConnectTimeOut();
        
    }
    
    /**
     * 停止前端文件播放
     * 
     * @param playerId
     *            播放流Id
     * @return 0表示成功，其它表示失败原因
     */
    public int stopFrontEndFilePlay()
    {
        int m;
        if (playid != -1)
        {
            m = WMClientSdk.getInstance().stopFrontEndFilePlay(playid);
            playid = -1;
            return m;
        }
        
        return 1;
    }
    
    /**
     * 
     * @return 图片存储路径
     */
    public String getPath()
    {
        return path;
    }
    
    public void stopPlay()
    {
        if (playid == Constants.WMPLAYERID_INVALID)
        {
            return;
        }
        synchronized (this)
        {
            notify();
        }
    }
    
    private void stop()
    {
        destoryStreamPlayer();
        stopFrontEndFilePlay();
        
    }
    
    /**
     * 销毁流媒体播放器
     */
    private void destoryStreamPlayer()
    {
        context.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (streamPlayer != null)
                {
                    WMClientSdk.getInstance().DestroyPlayer(streamPlayer);
                }
                if (surfaceView != null)
                {
                    relativeLayout.removeView(surfaceView);
                    surfaceView = null;
                }
            }
        });
    }
    
    /**
     * 获取播放经度
     * 
     * @param playerId
     *            播放流Id
     * @return 进度值 0-100
     */
    public int getFrontEndFilePlayPos()
    {
        if (playid != -1 && streamPlayer != null && surfaceView != null)
        {
            int pos = WMClientSdk.getInstance().getFrontEndFilePlayPos(playid);
            return pos;
        }
        return 0;
    }
    
    /**
     * 截图
     * 
     * @param path
     *            保存的路径
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public String saveSnapshot(int factoryid, int subfactoryid, int deviceid)
    {
        File directory = new File(path);
        if (!directory.exists() && !directory.mkdir())
        {
            return null;
        }
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = sDateFormat.format(new java.util.Date()) + "-" + factoryid + "-" + subfactoryid + "-" + deviceid;
        String fileName = path + File.separator + name;
        // savePlayBackSnapshot(playid, fileName)
        if (Constants.success == WMClientSdk.getInstance().savePlayBackSnapshot(playid, fileName))
        {
            Toast.makeText(context, "抓拍成功！", Toast.LENGTH_LONG).show();
            return name;
        }
        Toast.makeText(context, "抓拍失败！", Toast.LENGTH_LONG).show();
        return null;
    }
}
