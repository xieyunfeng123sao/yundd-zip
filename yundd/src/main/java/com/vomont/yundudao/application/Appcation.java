package com.vomont.yundudao.application;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.vomont.yundudao.bean.IPInfo;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

@SuppressLint("HandlerLeak")
public class Appcation extends Application
{
    public List<Activity> activityList = new LinkedList<Activity>();
    
    public static Appcation instance;
    
    private Handler handler;
    
    public static Context context;
    
    // private IPInfo ipInfo;
    // 118.244.236.67:8051 192.168.0.88:8050 101.201.79.176   8050
    
    public static String URL="101.201.79.176:8050";
    public static String BASE_URL = "http://"+URL;
    
    private ACache aCache;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        aCache = ACache.get(this);
        context = this;
//        getURL();
        initImageloader();
        createFile();
        initSmallVideo();
    }

    public static void initSmallVideo() {
        // 设置拍摄视频缓存路径
//        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File dcim=new File(VideoManager.base);
        if(!dcim.exists())
        {
            dcim.mkdirs();
        }
//        if (DeviceUtils.isZte()) {
//            if (dcim.exists()) {
//                JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
//            } else {
//                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
//                        "/sdcard-ext/")
//                        + "/mabeijianxi/");
//            }
//        } else {
            JianXiCamera.setVideoCachePath(dcim + "/compress/");
//        }
        // 初始化拍摄，遇到问题可选择开启此标记，以方便生成日志
        JianXiCamera.initialize(false,null);
    }
    
    private void createFile()
    {
        File file = new File(VideoManager.path);
        if (!file.exists())
        {
            file.mkdirs();
        }
        File file2 = new File(VideoManager.toppath);
        if (!file2.exists())
        {
            file2.mkdirs();
        }
        File file3 = new File(VideoManager.change_img);
        if (!file3.exists())
        {
            file3.mkdirs();
        }
    }
    
    public void initImageloader()
    {
        DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(false) // default
            .delayBeforeLoading(0)
            .cacheInMemory(true)
            // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            // default
            .bitmapConfig(Bitmap.Config.RGB_565)
            // default
            .displayer(new SimpleBitmapDisplayer())
            // default
            .handler(new Handler())
            // default
            .build();
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).memoryCacheExtraOptions(480, 800)
        // default = device screen dimensions
            .threadPoolSize(3)
            // default
            .threadPriority(Thread.NORM_PRIORITY - 1)
            // default
            .tasksProcessingOrder(QueueProcessingType.FIFO)
            // // default
            // .denyCacheImageMultipleSizesInMemory()
            // .memoryCache(new WeakMemoryCache())
            // // .memoryCacheSize(1*1024 * 1024)
            // .memoryCacheSizePercentage(3)
            // default
            .imageDownloader(new BaseImageDownloader(getApplicationContext()))
            // default
            .imageDecoder(new BaseImageDecoder(true))
            // default
            .defaultDisplayImageOptions(options)
            // default
            .writeDebugLogs()
            .build();
        ImageLoader.getInstance().init(config);
    }
    
    private void getURL()
    {
        // 判断缓存里是否已经有ip地址 如果有就使用缓存的ip地址 如果没有就获取rip地址
        if (aCache.getAsString("ip") != null)
        {
            BASE_URL = "http://" + aCache.getAsString("ip");
            String[] str = BASE_URL.split(":");
            if (str.length != 3)
            {
                BASE_URL = BASE_URL + ":8080";
            }
        }
        
        //第一次保存 默认的ip地址
       if(aCache.getAsObject("ips")==null)
       {
           List<String> ips=new ArrayList<String>();
           ips.add(URL);
           aCache.put("ips",(Serializable)ips);
       }
        
        
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                if (msg.what == 100)
                {
                    IPInfo ipInfo = (IPInfo)msg.obj;
                    String new_url = "http://" + ipInfo.getVfilesvrip() + ":" + ipInfo.getVfilesvrport();
                    if (!new_url.equals(BASE_URL))
                    {
                        aCache.put("ip", ipInfo.getVfilesvrip() + ":" + ipInfo.getVfilesvrport());
                        BASE_URL = new_url;
                    }
                }
                // onUrlListener.onSucess(BASE_URL);
            }
        };
    }
    
    // public interface OnUrlListener {
    // void onSucess(String url);
    // }
    //
    // public void setListener(OnUrlListener onUrlListener) {
    // this.onUrlListener = onUrlListener;
    // }
    
    public static String getNewUrl()
    {
        return BASE_URL;
    }
    
    public Appcation()
    {
    }
    
    // 单例模式中获取唯一的ExitApplication实例
    public static Appcation getInstance()
    {
        if (null == instance)
        {
            instance = new Appcation();
        }
        return instance;
    }
    
    // 添加Activity到容器中
    public void addActivity(Activity activity)
    {
        activityList.add(activity);
    }
    
    // 遍历所有Activity并finish
    
    public void exit()
    {
        for (Activity activity : activityList)
        {
            activity.finish();
        }
        System.exit(0);
    }
    
    public static final String HOST = "http://www.vomont.com/yundd/addr.php";
    
    private void getUrl()
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpgets = new HttpGet(HOST);
        HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 2000);
        HttpConnectionParams.setSoTimeout(httpclient.getParams(), 2000);
        HttpResponse response;
        try
        {
            IPInfo ipInfo = new IPInfo();
            response = httpclient.execute(httpgets);
            String jsonStr = EntityUtils.toString(response.getEntity());
            JSONObject json = new JSONObject(jsonStr);
            String ip = json.getString("ip");
            String port = json.getString("port");
            ipInfo.setVfilesvrip(ip);
            ipInfo.setVfilesvrport(Integer.parseInt(port));
            Message message = new Message();
            message.what = 100;
            message.obj = ipInfo;
            handler.sendMessage(message);
        }
        catch (ClientProtocolException e)
        {
            Message message = new Message();
            message.what = 10;
            message.obj = BASE_URL;
            handler.sendMessage(message);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Message message = new Message();
            message.what = 10;
            message.obj = BASE_URL;
            handler.sendMessage(message);
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            Message message = new Message();
            message.what = 10;
            message.obj = BASE_URL;
            handler.sendMessage(message);
            e.printStackTrace();
        }
    }
    
}
