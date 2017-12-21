package com.vomont.yundudao.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;
import com.base64.CCode;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.ImageUtils;
import com.vomont.yundudao.utils.ShareUtil;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Base64;

public class VideoUpService extends Service
{
    
    private List<byte[]> mlist;
    
    private final OkHttpClient client = new OkHttpClient();
    
    private VideoFragmentHelper sqliteHelper;
    
    private OnUpdataProgressListener onUpdataProgressListener;
    
    private Context context;
    
    private ShareUtil shareUtil;
    
    private VideoHelpter videoHelpter;
    
    // public static boolean isStop_Load;
    
    private ACache aCache;
    
    private int forSize;
    
    private boolean isPack;
    
    private OnLoadListener onLoadListener;
    
    @Override
    public IBinder onBind(Intent intent)
    {
        return new VideoBinder();
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        context = this;
        shareUtil = new ShareUtil(context);
        videoHelpter = new VideoHelpter(this);
        sqliteHelper = new VideoFragmentHelper(this);
        aCache = ACache.get(context);
        client.setConnectTimeout(10, TimeUnit.SECONDS);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }
    
    public void main1(String path, int size)
    {
        File f = new File(path);
        final int BUFFER_SIZE = 1024 * 200;// 缓冲区大小为3M
        RandomAccessFile randomAccessFile;
        try
        {
            randomAccessFile = new RandomAccessFile(f, "r");
            for (int i = 0; i < size; i++)
            {
                if (i == 0)
                {
                    randomAccessFile.seek(0);
                }
                else
                {
                    randomAccessFile.seek(i * BUFFER_SIZE - 1);
                }
                
                byte[] bs;
                if (i == (size - 1))
                {
                    bs = new byte[(int)(f.length() - (size - 1) * BUFFER_SIZE)];
                }
                else
                {
                    bs = new byte[BUFFER_SIZE];
                }
                randomAccessFile.read(bs);
                randomAccessFile.readChar();
                randomAccessFile.readInt();// 读取一个整数
                // mlist.add(bs);
            }
            randomAccessFile.close();
            
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
    
    public void cutFileUpload1(String filePath)
    {
        
    }
    
    /**
     * 文件分割
     * 
     * @param filePath
     */
    public void cutFileUpload(String path)
    {
        File file = new File(path);
        int mBufferSize = 1024 * 300;
        forSize = (int)((file.length() % mBufferSize == 0) ? file.length() / mBufferSize : (file.length() / mBufferSize + 1));
    }
    
    /**
     * 视频片段上传的逻辑
     * 
     * @param path
     *            视频的本地全路径名称
     * @param accountid
     *            用户的id
     */
    public void upDataVideo(final VideoBean videoBean)
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                cutFileUpload(videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4");
                // 查看这个视频在数据库里是否存在 如果不存在 就将这些片段添加到数据库中
                if (sqliteHelper.selectByPath(VideoUpService.this, videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4") == 0)
                {
                    aCache.put(videoBean.getDesp(), "0");
                    if (forSize != 0)
                        for (int i = 0; i < forSize; i++)
                        {
                            VideoFragmentInfo file = new VideoFragmentInfo();
                            file.setCreattime(videoBean.getCreattime());
                            file.setFragmentid(i);
                            file.setIsLoad(0);
                            file.setIsPack(0);
                            file.setPath(videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4");
                            sqliteHelper.insertInfo(VideoUpService.this, file);
                        }
                }
                if (videoHelpter.getPathLoading(videoBean.getName()) == 1)
                {
                    return;
                }
                
                VideoBean bean = videoHelpter.selectByName(videoBean.getName());
                if (bean.getLoadstate() == 2)
                {
                    return;
                }
                videoHelpter.updateLoadState(videoBean.getName(), 1, 0);
                if (onLoadListener != null)
                {
                    onLoadListener.needRerpull();
                }
                if (forSize != 0)
                {
                    RandomAccessFile randomAccessFile = null;
                    try
                    {
                        randomAccessFile = new RandomAccessFile(videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4", "r");
                        for (int i = 0; i < forSize; i++)
                        {
                            if (aCache.getAsString(videoBean.getDesp()).equals("1"))
                            {
                                videoHelpter.updateLoadState(videoBean.getName(), 0, 0);
                                break;
                            }
                            // 查询该片段是否已经上传
                            VideoFragmentInfo file = sqliteHelper.selectById(context, videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4", i);
                            // 如果没有上传 则上传文件片段
                            if (file != null)
                            {
                                byte[] upcontent = null;
                                // 这一段用的c++封装的base64编码 而不是系统的编码
                                CCode cCode = new CCode();
                                int size = 1024 * 1024;
                                byte[] outcontent = new byte[size];
                                byte[] buff = new byte[1024 * 300];
                                randomAccessFile.seek(i * 1024 * 300);
                                int length = randomAccessFile.read(buff);
                                byte[] buffer = new byte[length];
                                if (i == (forSize - 1))
                                {
                                    // 最后一个只取有用的部分
                                    System.arraycopy(buff, 0, buffer, 0, length);
                                }
                                else
                                {
                                    buffer = buff;
                                }
                                
                                int nCode = cCode.encode(buffer, buffer.length, outcontent, size);
                                upcontent = new byte[nCode];
                                System.arraycopy(outcontent, 0, upcontent, 0, nCode);
                                final Map<String, String> params = new HashMap<String, String>();
                                params.put("msgid", "386");
                                params.put("accountid", shareUtil.getShare().getAccountid() + "");
                                params.put("fragmentid", i + "");
                                params.put("fragementcontent", new String(upcontent));
                                StringBuffer sb = new StringBuffer();
                                // 设置表单参数
                                for (String key : params.keySet())
                                {
                                    sb.append(key + "=" + params.get(key) + "&");
                                }
                                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sb.toString());
                                Request request =
                                    new Request.Builder().url("http://" + shareUtil.getShare().getVfilesvr().getVfilesvrip() + ":" + shareUtil.getShare().getVfilesvr().getVfilesvrport())
                                        .post(body)
                                        .build();
                                Response response;
                                try
                                {
                                    response = client.newCall(request).execute();
                                    if (response.isSuccessful())
                                    {
                                        Gson gson = new Gson();
                                        String callback = response.body().string();
                                        VideoFragmentInfo info = gson.fromJson(callback, VideoFragmentInfo.class);
                                        if (info != null)
                                        {
                                            int result = info.getResult();
                                            if (result == 0)
                                            {
                                                // 上传成功
                                                String fragmentcontext = info.getFragmentcontext();
                                                // 更新数据库的context；
                                                sqliteHelper.upDataContext(context, videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4", i, fragmentcontext);
                                                if (onLoadListener != null)
                                                {
                                                    onLoadListener.needRerpull();
                                                }
                                            }
                                            else
                                            {
                                                // 上传失败
                                                if (onLoadListener != null)
                                                {
                                                    onLoadListener.needRerpull();
                                                }
                                            }
                                        }
                                        else
                                        {
                                            // 上传失败
                                            if (onLoadListener != null)
                                            {
                                                onLoadListener.needRerpull();
                                            }
                                        }
                                        
                                    }
                                    else
                                    {
                                        videoHelpter.updateLoadState(videoBean.getName(), 0, 0);
                                        // 上传失败
                                        if (onLoadListener != null)
                                        {
                                            onLoadListener.needRerpull();
                                        }
                                        break;
                                    }
                                }
                                catch (IOException e)
                                {
                                    videoHelpter.updateLoadState(videoBean.getName(), 0, 0);
                                    // 上传失败
                                    if (onLoadListener != null)
                                    {
                                        onLoadListener.needRerpull();
                                    }
                                    e.printStackTrace();
                                    break;
                                }
                            }
                            // 当最后一个也上传结束后 查询是否还有片段没有上传 如果有 状态设置成 stop
                            if (i == (forSize - 1))
                            {
                                VideoFragmentInfo lastfrag = sqliteHelper.selectHasNoUpload(context, videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4");
                                if (lastfrag != null)
                                {
                                    videoHelpter.updateLoadState(videoBean.getName(), 0, 0);
                                    break;
                                }
                            }
                            // 查询未上传片段
                            int m = sqliteHelper.getNoUpdataContext(context, videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4");
                            // 当所有的视频都已经上传成功后 发送打包请求
                            if (m == 0)
                            {
                                // 所有片段都上传成功后 不需要继续循环查看是否有位上传片段了 而是 直接打包
                                packFile(videoBean);
                                break;
                            }
                        }
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    try
                    {
                        randomAccessFile.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    
    // 将byte数组写入文件
    public void createFile(String path, byte[] content)
        throws IOException
    {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(content);
        fos.close();
    }
    
    // 写文件
    public void writeSDFile(String fileName, String write_str)
        throws IOException
    {
        
        File file = new File(fileName);
        if (!file.exists())
        {
            file.mkdirs();
        }
        
        FileOutputStream fos = new FileOutputStream(file);
        
        byte[] bytes = write_str.getBytes();
        
        fos.write(bytes);
        
        fos.close();
    }
    
    /**
     * 视频打包
     * 
     * @throws IOException
     */
    public void packFile(final VideoBean videoBean)
    {
        if (!sqliteHelper.getNoPackContext(context, videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4"))
        {
            return;
        }
        
        if (isPack)
        {
            return;
        }
        
        // 暂时这里用的okhttp 前面的 asynchttp的都需要改了 有问题的
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        List<VideoFragmentInfo> mlist = sqliteHelper.getByPath(context, videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4");
        if (mlist != null && mlist.size() != 0)
        {
            String contextlist = "";
            for (int i = 0; i < mlist.size(); i++)
            {
                contextlist = contextlist + mlist.get(i).getFragmentcontext() + ",";
            }
            try
            {
                writeSDFile(VideoManager.detail_img_cash + "/text.txt", contextlist);
            }
            catch (IOException e2)
            {
                e2.printStackTrace();
            }
            RequestBody requestBody;
            try
            {
                Bitmap bitmap = null;
                File file = new File(VideoManager.video_face_img + "/" + videoBean.getName() + ".jpg");
                if (file.exists())
                {
                    bitmap = BitmapUtil.getLoacalBitmap(VideoManager.video_face_img + "/" + videoBean.getName() + ".jpg");
                }
                else
                {
                    bitmap = BitmapUtil.getVideoThumbnail(videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4");
                }
                Bitmap upBitmap = BitmapUtil.imageZoom(bitmap);
                bitmap.recycle();
                MediaPlayer player = new MediaPlayer();
                player.setDataSource(videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4");
                player.prepare();
                int length = player.getDuration() / 1000;
                int looker = 0;
                if (videoBean.getLooker() == null)
                {
                    looker = 1;
                }
                else
                {
                    looker = 2;
                }
                requestBody =
                    new FormEncodingBuilder().add("msgid", 387 + "")
                        .add("accountid", shareUtil.getShare().getAccountid() + "")
                        .add("contextlist", contextlist)
                        .add("ownersubfactid", videoBean.getSubfatoryid() + "")
                        .add("creattime", videoBean.getCreattime() + "")
                        .add("videolength", length + "")
                        .add("videotype", 2 + "")
                        .add("imagetype", 2 + "")
                        .add("imagecontent", Base64.encodeToString(ImageUtils.bitmap2Bytes(upBitmap, CompressFormat.JPEG), Base64.DEFAULT))
                        .add("videodesp", videoBean.getDesp())
                        .add("viewertype", looker + "")
                        .add("vieweraccountids", videoBean.getLookername())
                        .build();
                isPack = true;
                Request request =
                    new Request.Builder().url("http://" + shareUtil.getShare().getVfilesvr().getVfilesvrip() + ":" + shareUtil.getShare().getVfilesvr().getVfilesvrport()).post(requestBody).build();
                client.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onResponse(Response arg0)
                        throws IOException
                    {
                        isPack = false;
                        try
                        {
                            String name = arg0.body().string();
                            JSONObject json = new JSONObject(name);
                            if (json.getInt("result") == 0)
                            {
                                // 打包成功 修改该数据库里的上传状态以及videoid的值
                                int videoid = json.getInt("videoid");
                                sqliteHelper.upDataPack(context, videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4");
//                                videoHelpter.updatePack(videoBean.getName());
                                videoHelpter.updateLoadState(videoBean.getName(), 2, videoid);
                                if (onUpdataProgressListener != null)
                                {
                                    onUpdataProgressListener.upDataSucess();
                                }
                                if (onLoadListener != null)
                                {
                                    onLoadListener.Sucess();
                                    onLoadListener.needRerpull();
                                }
                                ACache aCache = ACache.get(context);
                                aCache.put("videosend", "1");
                            }
                            else
                            {
                                videoHelpter.updateLoadState(videoBean.getName(), 0, 0);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Request arg0, IOException arg1)
                    {
                        isPack = false;
                        videoHelpter.updateLoadState(videoBean.getName(), 0, 0);
                        if (onLoadListener != null)
                        {
                            onLoadListener.needRerpull();
                        }
                    }
                });
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e1)
            {
                e1.printStackTrace();
            }
            catch (SecurityException e1)
            {
                e1.printStackTrace();
            }
            catch (IllegalStateException e1)
            {
                e1.printStackTrace();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }
    
    public void setOnProgressListener(OnUpdataProgressListener onUpdataProgressListener)
    {
        this.onUpdataProgressListener = onUpdataProgressListener;
    }
    
    //
    public interface OnUpdataProgressListener
    {
        void upDataSucess();
    }
    
    public class VideoBinder extends Binder
    {
        /**
         * 获取当前Service的实例
         * 
         * @return
         */
        public VideoUpService getService()
        {
            return VideoUpService.this;
        }
    }
    
    public void setOnLoadListener(OnLoadListener onLoadListener)
    {
        this.onLoadListener = onLoadListener;
    }
    
    public interface OnLoadListener
    {
        void Sucess();
        
        void Load();
        
        void needRerpull();
    }
    
}
