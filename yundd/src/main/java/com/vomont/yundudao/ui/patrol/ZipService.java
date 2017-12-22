package com.vomont.yundudao.ui.patrol;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.mabeijianxi.smallvideorecord2.LocalMediaCompress;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.LocalMediaConfig;
import com.mabeijianxi.smallvideorecord2.model.OnlyCompressOverBean;
import com.vomont.fileloadsdk.WMFileLoadSdk;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoHelpter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class ZipService extends Service {

    private VideoHelpter helpter;

    private Map<String,String> map=new HashMap<String, String>();

    OnZipListener onZipListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ZipBinder();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        helpter=new VideoHelpter(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    public void zipVideo(String path,final String name,final VideoBean upVideoBean) {
        String filePath = path + "/" + name + ".mp4";
        File file = new File(filePath);
        if (file.exists()) {
            //压缩设置
            LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
            final LocalMediaConfig config = buidler
                    .setVideoPath(filePath)
                    .captureThumbnailsTime(1)
                    .doH264Compress(new AutoVBRMode())
                    .setFramerate(15)
                    .setScale(1.0f)
                    .build();
            // 上传状态改为1 表示正在上传
            upVideoBean.setLoadstate(3);
            // 表示状态是上传 而不是保存的数据
            upVideoBean.setIsSave(1);
            helpter.updateBean(name, upVideoBean);
            if(onZipListener!=null)
            {
                onZipListener.onCallBack(name,upVideoBean);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //压缩
                    LocalMediaCompress localMediaCompress = new LocalMediaCompress(config);
                    OnlyCompressOverBean onlyCompressOverBean = localMediaCompress.startCompress();
                    if (onlyCompressOverBean.isSucceed()) {
                        String path = onlyCompressOverBean.getVideoPath();
                        Message message = new Message();
                        message.what = 10;
                        String[] strings=path.split("/");
                        String  names=strings[strings.length-1].replace(".mp4","");
                        String filePath=path.replace("/"+strings[strings.length-1],"");
                        map.put(names,name);
                        upVideoBean.setName(names);
                        upVideoBean.setVideoPath(filePath);
                        if(onZipListener!=null)
                        {
                            onZipListener.onSucess(name,upVideoBean);
                        }
                        message.obj = upVideoBean;
                        mHandler.sendMessage(message);
                    } else {
                        Message message = new Message();
                        message.what = 10;
                        message.obj = upVideoBean;
                        mHandler.sendMessage(message);
                    }
                }
            }).start();
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    //压缩成功
                    upLoad((VideoBean) msg.obj);
                    break;
                case 20:
                    //压缩失败
                    upLoad((VideoBean) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };


    private void  upLoad(VideoBean upVideoBean)
    {
        // 第一次上传
        int fileId = WMFileLoadSdk.getInstance().WM_VFile_UploadBigFile(upVideoBean.getVideoPath() + "/" + upVideoBean.getName() + ".mp4");
        if (fileId == 0) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
        } else {
            upVideoBean.setFileId(fileId);
            // 上传状态改为1 表示正在上传
            upVideoBean.setLoadstate(1);
            // 表示状态是上传 而不是保存的数据
            upVideoBean.setIsSave(1);
            helpter.updateBean(map.get(upVideoBean.getName()), upVideoBean);
        }
    }


    public class ZipBinder extends Binder {
        public ZipService getService() {
            return ZipService.this;
        }
    }


    public void  setOnZipListener(OnZipListener onZipListener)
    {
            this.onZipListener=onZipListener;
    }

    public  interface  OnZipListener
    {
        void  onCallBack(String  oldName,VideoBean videoBean);

        void  onSucess(String  oldName,VideoBean videoBean);
    }
}
