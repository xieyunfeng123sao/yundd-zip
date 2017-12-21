package com.vomont.yundudao.utils;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.video.opengl.GLSurfaceView20;
import com.vomont.yundudao.bean.FileInfo;
import com.vomont.yundudao.upload.VideoManager;
import com.wmclient.clientsdk.Constants;
import com.wmclient.clientsdk.StreamPlayer;
import com.wmclient.clientsdk.WMClientSdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class Playutil implements SurfaceHolder.Callback
{
    private Activity context;
    
    private int playid = -1;
    
    private StreamPlayer streamPlayer = null;
    
    private String path;
    
    private RelativeLayout relativeLayout;
    
    private SurfaceView surfaceView;
    
    private ShareUtil util;
    
    private PlayListener playListener;
    
    private boolean isStoped = true;
    
    private PlayRunnable pendingPlayRunnable;
    
    private int DeviceId;
    
    private int DevChannelId;
    
    public interface PlayListener
    {
        void onPlaySuccess();
        
        void onPlayFailed();
        
        void onPlayConnectTimeOut();
        
    }
    
    public void addPlayListener(PlayListener l)
    {
        this.playListener = l;
    }
    
    public Playutil(Activity context)
    {
        this.context = context;
        util = new ShareUtil(context);
        path = VideoManager.yundd_phone + util.getShare().getNum();
    }
    
    /**
     * 获取播放权限
     * 
     * @param userId
     *            用户的id
     * @param key
     *            用户的key
     */
    public int authenticate(String userId, String key,String serverAddress,int serverPort)
    {
        int i = -1;
        WMClientSdk.getInstance().init(31);
        Log.e("insert", userId+ key+serverAddress+serverPort);
        i = WMClientSdk.getInstance().authenticate(userId, key,serverAddress,serverPort);
        return i;
    }
    
    public void stopPlayVideo()
    {
        if (playid == Constants.WMPLAYERID_INVALID)
        {
            return;
        }
        synchronized (this)
        {
            notify();
        }
        stopPlay();
        destoryStreamPlayer();
    }
    
    /**
     * 建立媒体播放器
     * 
     * @param type
     *            播放器类型
     * @param relativeLayout
     *            播放器的容器
     */
    public void getStreamPlayer(int type, RelativeLayout relativeLayout, int deviceId, int devChannelId, int vedioType)
    {
        
        this.relativeLayout = relativeLayout;
        if (surfaceView != null)
        {
            relativeLayout.removeView(surfaceView);
            surfaceView = null;
        }
        if (type == Constants.DEVICE_TYPE_XM_DEV)
        {
            
            surfaceView = new GLSurfaceView20(context);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            surfaceView.setLayoutParams(layoutParams);
            streamPlayer = WMClientSdk.getInstance().CreatePlayer(type, surfaceView.getTag(), Constants.WMStreamType_RealTime);
        }
        else
        {
            surfaceView = new SurfaceView(context);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            surfaceView.setLayoutParams(layoutParams);
            streamPlayer = WMClientSdk.getInstance().CreatePlayer(type, surfaceView.getHolder(), Constants.WMStreamType_RealTime);
        }
        relativeLayout.addView(surfaceView, 0);
        if (!isStoped)
        {
            pendingPlayRunnable = new PlayRunnable(deviceId, devChannelId, vedioType);
            return;
        }
        DeviceId=deviceId;
        DevChannelId=devChannelId;
        // surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);// 设置背景透明
        new Thread(new PlayRunnable(deviceId, devChannelId, vedioType)).start();
    }

    class PlayRunnable implements Runnable
    {
        int deviceId;
        
        int devChannelId;
        
        int vedioType;
        
        public PlayRunnable(int deviceId, int devChannelId, int vedioType)
        {
            this.deviceId = deviceId;
            this.devChannelId = devChannelId;
            this.vedioType = vedioType;
        }
        
        @Override
        public void run()
        {
            isStoped = false;
            playid = WMClientSdk.getInstance().startRealPlay(deviceId, devChannelId, streamPlayer, vedioType);
            if (playid == Constants.WMPLAYERID_INVALID)
            {
                int lastError=  WMClientSdk.getInstance().getLastError();
                if (playListener != null&&lastError==23)
                {
                    playListener.onPlayConnectTimeOut();
                }
                else if (playListener != null)
                {
                    playListener.onPlayFailed();
                }
                isStoped = true;
                return;
            }
            if (playListener != null)
            {
                playListener.onPlaySuccess();
            }
            synchronized (Playutil.this)
            {
                try
                {
                    Playutil.this.wait();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            stopPlay();
            destoryStreamPlayer();
            if (pendingPlayRunnable != null)
            {
                new Thread(pendingPlayRunnable).start();
                pendingPlayRunnable = null;
            }
            isStoped = true;
        }
    }
    
    public void stopIng()
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
    
    /**
     * 
     * @return 图片存储路径
     */
    public String getPath()
    {
        return path;
    }
    
    public List<String> getImagPath()
    {
        File file = new File(path);
        if (!file.exists() && !file.mkdir())
        {
            return null;
        }
        List<String> mlist = new ArrayList<String>();
        File[] fs = file.listFiles();
        for (int i = 0; i < fs.length; i++)
        {
            mlist.add(fs[i].getAbsolutePath());
        }
        return mlist;
    }
    
    /**
     * 
     * @return 所有截图的文件名称
     */
    public List<String> getImageName()
    {
        File file = new File(path);
        if (!file.exists() && !file.mkdir())
        {
            return null;
        }
        List<String> mlist = new ArrayList<String>();
        File[] str = file.listFiles();
        
        ArrayList<FileInfo> fileList = new ArrayList<FileInfo>();// 将需要的子文件信息存入到FileInfo里面
        for (int i = 0; i < str.length; i++)
        {
            File f = str[i];
            FileInfo fileInfo = new FileInfo();
            
            fileInfo.name = f.getName();
            fileInfo.path = f.getPath();
            fileInfo.lastModified = f.lastModified();
            fileList.add(fileInfo);
        }
        Collections.sort(fileList, new FileComparator());// 通过重写Comparator的实现类
        Collections.reverse(fileList);
        for (int i = 0; i < fileList.size(); i++)
        {
            mlist.add(fileList.get(i).getName());
        }
        return mlist;
    }
    
    public class FileComparator implements Comparator<FileInfo>
    {
        public int compare(FileInfo file1, FileInfo file2)
        {
            if (file1.lastModified < file2.lastModified)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
    }
    
   
    
    @SuppressLint("DefaultLocale") public FileFilter fileFilter = new FileFilter()
    {
        public boolean accept(File file)
        {
            String tmp = file.getName().toLowerCase();
            if (tmp.endsWith(".mov") || tmp.endsWith(".jpg"))
            {
                return true;
            }
            return false;
        }
    };
    
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
        if (Constants.success == WMClientSdk.getInstance().saveSnapshot(playid, fileName))
        {
            return name;
        }
        Toast.makeText(context, "抓拍失败！", Toast.LENGTH_LONG).show();
        return null;
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
    public void ptzControlend(int size)
    {
//       WMClientSdk.getInstance().ptzControl(DeviceId, DevChannelId, size, 1, 4);
    }
    
    
    public void ptzControlStart(final int size)
    {
     int i=  WMClientSdk.getInstance().ptzControl(DeviceId, DevChannelId, size, 0, 4);
     if(i==0)
     {
         new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                context.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        WMClientSdk.getInstance().ptzControl(DeviceId, DevChannelId, size, 1, 4);
                    }
                });
            }
        }).start();
     }
    }
    
    /**
     * 关闭播放器
     */
    private void stopPlay()
    {
        if (playid != 0)
        {
            WMClientSdk.getInstance().stopRealPlay(playid);
        }
        
    }
    
    /**
     * 退出播放器权限
     */
    public void unAuthenticate()
    {
        WMClientSdk.getInstance().unAuthenticate();
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        
    }
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        
    }
    
}
