package com.vomont.yundudao.ui.videoimport;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.patrol.VedioPlayActivity;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.video.VideoUtil;

@SuppressLint({"SimpleDateFormat", "ClickableViewAccessibility", "HandlerLeak"})
public class VideoFaceActivity extends Activity implements OnClickListener
{
    
    private ImageView video_faceimg_goback;
    
    private Button video_faceimg_next;
    
    private VideoView videoView_faceimg;
    
    private LinearLayout edit_faceimg_video;
    
    private TextView video_face_length;
    
    private LinearLayout select_faceimg;
    
    private String videoName;
    
    private String videoPath;
    
    private int videoLength;
    
    private int oldVideoLength;
    
    private Dialog dialog;
    
    private boolean isStop;


    private Context context;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_face_img);
        context=this;
        initView();
        initListener();
        initData();
    }
    
    private void initData()
    {
        videoName = getIntent().getStringExtra("name");
        oldVideoLength = getIntent().getIntExtra("length", -1);
        videoPath = getIntent().getStringExtra("path");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(oldVideoLength);
        video_face_length.setText("总时长:" + hms);
        if (videoPath != null)
        {
            videoView_faceimg.setVideoPath(videoPath + "/" + videoName + ".mp4");
        }
        else
        {
            videoPath = VideoManager.path;
            videoView_faceimg.setVideoPath(VideoManager.path + "/" + videoName + ".mp4");
        }
        videoView_faceimg.start();
        videoView_faceimg.seekTo(1);
        videoView_faceimg.pause();
        
        // videoView_faceimg.postDelayed(new Runnable()
        // {
        // public void run()
        // {
        // // 視頻長度的顯示
        // asyncloadImage();
        // videoLength = videoView_faceimg.getDuration();
        // }
        // }, 200);
        
        videoView_faceimg.setOnPreparedListener(new OnPreparedListener()
        {
            
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                videoLength = videoView_faceimg.getDuration();
                asyncloadImage();
            }
        });
    }
    
    private void initListener()
    {
        video_faceimg_goback.setOnClickListener(this);
        video_faceimg_next.setOnClickListener(this);
        select_faceimg.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        moveViewWithFinger(select_faceimg, event.getRawX());
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
    
    private void initView()
    {
        video_faceimg_goback = (ImageView)findViewById(R.id.video_faceimg_goback);
        video_faceimg_next = (Button)findViewById(R.id.video_faceimg_next);
        videoView_faceimg = (VideoView)findViewById(R.id.videoView_faceimg);
        edit_faceimg_video = (LinearLayout)findViewById(R.id.edit_faceimg_video);
        select_faceimg = (LinearLayout)findViewById(R.id.select_faceimg);
        video_face_length = (TextView)findViewById(R.id.video_face_length);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.video_faceimg_goback:
                finish();
                break;
            case R.id.video_faceimg_next:
                dialog = ProgressDialog.createLoadingDialog(VideoFaceActivity.this, "");
                dialog.show();
                asynLoadFaceImg();
                break;
            default:
                break;
        }
    }
    
    private void asynLoadFaceImg()
    {
        Runnable runnable = new Runnable()
        {
            @SuppressWarnings("deprecation")
            @Override
            public void run()
            {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                MediaPlayer mediaPlayer = new MediaPlayer();
                try
                {
                    mediaPlayer.setDataSource(videoPath + "/" + videoName + ".mp4");
                    mediaPlayer.prepare();
                    mmr.setDataSource(videoPath + "/" + videoName + ".mp4");
                    int now_left = select_faceimg.getLeft();
                    int longWith = getWindowManager().getDefaultDisplay().getWidth() - select_faceimg.getWidth();
                    long time = videoLength * now_left / longWith;
                    Bitmap bitmap = mmr.getFrameAtTime(time == 0 ? 1 * 1000 : time * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    BitmapUtil.savePhotoByte(bitmap, VideoManager.video_face_img, videoName + ".jpg");
                    mHandler.sendEmptyMessage(30);
                    mediaPlayer.release();
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (SecurityException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalStateException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        
        new Thread(runnable).start();
    }
    
    /**
     * 获取8长图片用于显示
     */
    private void asyncloadImage()
    {
        // 子线程，开启子线程去下载或者去缓存目录找图片，并且返回图片在缓存目录的地址
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                // 创建MediaMetadataRetriever对象
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                // 设置资源位置
                // 绑定资源
                MediaPlayer mediaPlayer = new MediaPlayer();
                try
                {
                    mediaPlayer.setDataSource(videoPath + "/" + videoName + ".mp4");
                    mediaPlayer.prepare();
                    mmr.setDataSource(videoPath + "/" + videoName + ".mp4");
                    for (int i = 0; i < 8; i++)
                    {
                        int time = i * videoLength / 8;
                        Bitmap bitmap = mmr.getFrameAtTime(time * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                        BitmapUtil.savePhotoByte(bitmap, VideoManager.detail_img_cash, videoName + "_" + i + ".jpg");
                        if(bitmap!=null)
                        {
                            bitmap.recycle();
                        }
                       
                        Message message = new Message();
                        message.what = 10;
                        message.obj = VideoManager.detail_img_cash + "/" + videoName + "_" + i + ".jpg";
                        if (!isStop)
                            mHandler.sendMessage(message);
                    }
                    mediaPlayer.release();
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (SecurityException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalStateException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                
            }
        };
        new Thread(runnable).start();
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        File file = new File(VideoManager.detail_img_cash);
        File[] item = file.listFiles();
        if (item != null)
        {
            for (File f : item)
            {
                f.delete();
            }
        }
        isStop = true;
        context=null;
    }
    
    private Handler mHandler = new Handler()
    {
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 10:
                    // 显示8张图图片
                    // Bitmap bitmap = (Bitmap)msg.obj;
                    ImageView img = new ImageView(VideoFaceActivity.this);
                    img.setScaleType(ScaleType.FIT_XY);
                    LayoutParams params = new LayoutParams(getWindowManager().getDefaultDisplay().getWidth() / 8, LayoutParams.MATCH_PARENT);
                    img.setLayoutParams(params);
                    if(context!=null) {
                        Glide.with(context).load(new File(msg.obj.toString())).into(img);
                        edit_faceimg_video.addView(img);
                    }
                        break;
                case 30:
                    Intent intentFinish = getIntent();
                    intentFinish.putExtra("finish", 0);
                    setResult(RESULT_OK, intentFinish);
                    Intent intent = new Intent(VideoFaceActivity.this, VedioPlayActivity.class);
                    intent.putExtra("name", videoName);
                    intent.putExtra("path", videoPath);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
    
    @SuppressWarnings("deprecation")
    private void moveViewWithFinger(View view, float rawX)
    {
        if ((int)rawX >= view.getWidth())
        {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
            params.leftMargin = (int)rawX - view.getWidth();
            view.setLayoutParams(params);
            videoView_faceimg.start();
            videoView_faceimg.seekTo(videoLength * ((int)rawX - view.getWidth()) / (getWindowManager().getDefaultDisplay().getWidth() - view.getWidth()));
            videoView_faceimg.pause();
        }
    }
}
