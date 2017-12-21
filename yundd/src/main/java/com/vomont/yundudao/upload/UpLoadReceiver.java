package com.vomont.yundudao.upload;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vomont.fileloadsdk.UpLoadUtil;
import com.vomont.fileloadsdk.WMFileLoadSdk;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.ShareUtil;

public class UpLoadReceiver extends BroadcastReceiver
{
    private int type;
    
    private int fileId;
    
    private int progress;
    
    private String url;
    
    private int errorId;
    
    OnLoadListener onLoadListener;
    
    ShareUtil shareUtil;
    
    private final OkHttpClient client = new OkHttpClient();
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        type = intent.getExtras().getInt("type");
        fileId = intent.getExtras().getInt("fileId");
        url = intent.getExtras().getString("videourl");
        progress = intent.getExtras().getInt("progress");
        errorId = intent.getExtras().getInt("errorid");
        shareUtil = new ShareUtil(Appcation.context);
        if (type == 0)
        {
            VideoHelpter helpter = new VideoHelpter(Appcation.context);
            VideoBean bean = helpter.selectByFileId(fileId);
            if (bean != null)
            {
                // 是视频上传
                File file = new File(VideoManager.video_face_img + "/" + bean.getName() + ".jpg");
                if (!file.exists())
                {
                    // 视频的封面不存在就创建一个封面
                    Bitmap bitmap = BitmapUtil.getVideoThumbnail(bean.getVideoPath() + "/" + bean.getName() + ".mp4");
                    if(bitmap==null)
                    {
                        
                        helpter.updateLoadState(bean.getName(), 0, bean.getVideoid());
                        // 上传失败
                        if (onLoadListener != null)
                        {
                            onLoadListener.onFail(3, bean.getFileId(), 4);
                        }
                        return;
                    }
                    BitmapUtil.savePhotoByte(bitmap, VideoManager.video_face_img, bean.getName() + ".jpg");
                    if (bitmap != null)
                        bitmap.recycle();
                }
                File sendFile = new File(VideoManager.video_face_img + "/" + bean.getName() + ".jpg");
                int faceImag_fileId = 0;
                // 判断封面图片的大小是否超过了分片最大值
                if (sendFile.length() > UpLoadUtil.fragmentSize)
                {
                    // 超过了就大文件上传
                    faceImag_fileId = WMFileLoadSdk.getInstance().WM_VFile_UploadBigFile(VideoManager.video_face_img + "/" + bean.getName() + ".jpg");
                }
                else
                {
                    // 没超过就小文件上传
                    faceImag_fileId = WMFileLoadSdk.getInstance().WM_VFile_UploadFile(VideoManager.video_face_img + "/" + bean.getName() + ".jpg");
                }
                bean.setVideoUrl(url);
                bean.setImageFileId(faceImag_fileId);
                // helpter.updateBean(bean.getName(), bean);
                helpter.updateVideoUrl(bean.getName(), url, faceImag_fileId);
            }
            else
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        VideoHelpter videoHelpter = new VideoHelpter(Appcation.context);
                        VideoBean videoBean = videoHelpter.selectImageFileId(fileId);
                        if (videoBean != null && videoBean.getLoadstate() != 2)
                        {
                            // 图片上传成功
                            videoBean.setFaceImageUrl(url);
                            if(videoBean.getLoadstate()==4)
                            {
                                return;
                            }
                            videoHelpter.updateImageUrl(videoBean.getName(), url);
                            try
                            {
                                MediaPlayer player = new MediaPlayer();
                                player.setDataSource(videoBean.getVideoPath() + "/" + videoBean.getName() + ".mp4");
                                player.prepare();
                                int length = player.getDuration() / 1000;
                                String desp = videoBean.getDesp();
                                final Map<String, String> params = new HashMap<String, String>();
                                params.put("msgid", "386");
                                params.put("userid", shareUtil.getShare().getUserid() + "");
                                params.put("filepath", videoBean.getVideoUrl());
                                params.put("vfilesvrid", shareUtil.getShare().getVfilesvr().getVfilesvrid() + "");
                                params.put("ownersubfactid", videoBean.getSubfatoryid() + "");
                                params.put("createtime", videoBean.getCreattime() + "");
                                params.put("videolength", length + "");
                                params.put("videodesp", desp);
                                params.put("viewertype", "1");
                                params.put("imagepath", url);
                                
                                StringBuffer sb = new StringBuffer();
                                // 设置表单参数
                                for (String key : params.keySet())
                                {
                                    sb.append(key + "=" + params.get(key) + "&");
                                }
                                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sb.toString());
                                Request request = new Request.Builder().url(Appcation.BASE_URL).post(body).build();
                                Response response;
                                response = client.newCall(request).execute();
                                if (response.isSuccessful())
                                {
                                    String name = response.body().string();
                                    Log.e("upvideo", name);
                                    JSONObject json;
                                    try
                                    {
                                        json = new JSONObject(name);
                                        if (json.getInt("result") == 0)
                                        {
                                            // 上传成功
                                            videoHelpter.updateLoadState(videoBean.getName(), 2, json.getInt("videoid"));
                                            if (onLoadListener != null)
                                            {
                                                onLoadListener.onSuccess(videoBean.getFileId(), "");
                                            }
                                        }
                                        else
                                        {
                                            // 上传失败
                                            if (onLoadListener != null)
                                            {
                                                onLoadListener.onFail(3, videoBean.getFileId(), 4);
                                            }
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else
                                {
                                    if (onLoadListener != null)
                                    {
                                        onLoadListener.onFail(3, videoBean.getFileId(), -2);
                                    }
                                    // 网络问题就暂停
                                    videoHelpter.updateLoadState(videoBean.getName(), 0, 0);
                                }
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
                            catch (UnsupportedEncodingException e)
                            {
                                e.printStackTrace();
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                
            }
        }
        else if (type == 1)
        {
            VideoHelpter vHelpter = new VideoHelpter(context);
            VideoBean vBean = vHelpter.selectByFileId(fileId);
            if (vBean != null)
            {
                if (onLoadListener != null)
                    onLoadListener.onProgress(fileId, progress);
                vBean.setPos(progress);
                vBean.setLoadstate(1);
                vHelpter.updateProgress(vBean.getName(), progress, fileId);
            }
        }
        else
        {
            VideoHelpter vhHelpter = new VideoHelpter(context);
            VideoBean vbBean = vhHelpter.selectByFileId(fileId);
            if (vbBean != null)
            {
                // 視頻上传出错
                if (onLoadListener != null)
                    onLoadListener.onFail(0, fileId, errorId);
                vhHelpter.updateLoadState(vbBean.getName(), 0, 0);
            }
            else
            {
                VideoHelpter videoH = new VideoHelpter(context);
                VideoBean videoB= vhHelpter.selectByFileId(fileId);
                if (videoB != null)
                {
                    // 封面上传出错
                    if (onLoadListener != null)
                        onLoadListener.onFail(1, fileId, errorId);
                    videoH.updateLoadState(videoB.getName(), 0, 0);
                }
            }
        }
        
    }
    
    public void setOnLoadListener(OnLoadListener onLoadListener)
    {
        this.onLoadListener = onLoadListener;
    }
    
    public interface OnLoadListener
    {
        void onProgress(int fileId, int progress);
        
        void onSuccess(int fileId, String url);
        
        void onFail(int type, int fileId, int errorId);
    }
}
