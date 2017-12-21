package com.vomont.yundudao.view.video;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.vomont.yundudao.R;

/**
 * qiangyu on 1/26/16 15:33 csdn鍗氬:http://blog.csdn.net/yissan
 */
@SuppressLint({"InflateParams", "ClickableViewAccessibility", "HandlerLeak"}) public class CommonVideoView extends FrameLayout implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, View.OnTouchListener, View.OnClickListener,
    Animator.AnimatorListener, SeekBar.OnSeekBarChangeListener
{
    
    private final int UPDATE_VIDEO_SEEKBAR = 1000;
    
    private Context context;
    
    private FrameLayout viewBox;
    
    private VideoView videoView;
    
    private LinearLayout videoPauseBtn;
    
    private LinearLayout screenSwitchBtn;
    
//    private LinearLayout touchStatusView;
    
    private LinearLayout videoControllerLayout;
    
    private ImageView touchStatusImg;
    
    private LinearLayout videoPlayImg;
    
    private ImageView videoPauseImg;
    
//    private TextView touchStatusTime;
    
    private TextView videoCurTimeText;
    
    private TextView videoTotalTimeText;
    
    private SeekBar videoSeekBar;
    
    private ProgressBar progressBar;
    
    private int duration;
    
//    private String formatTotalTime;
    
    private Timer timer = new Timer();
    
//    private float touchLastX;
    
//    private int position;
    
//    private int touchStep = 1000;
    
//    private int touchPosition = -1;
    
    private boolean videoControllerShow = true;
    
    private boolean animation = false;
    
    private String url;
    
//    private String path;
    
    private TimerTask timerTask;
    
    private int videoWidth, videoHeight;
    
    private OnScreenListener onScreenListener;
    
    private boolean isScreen;
    
    @SuppressLint("HandlerLeak") private Handler videoHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case UPDATE_VIDEO_SEEKBAR:
                    if (videoView.isPlaying())
                    {
                        videoSeekBar.setProgress(videoView.getCurrentPosition());
                    }
                    break;
            }
        }
    };
    
    public CommonVideoView(Context context)
    {
        this(context, null);
    }
    
    public CommonVideoView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }
    
    public CommonVideoView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    
    public void startPath(String path)
    {
        Log.e("insert",path);
        videoPauseBtn.setEnabled(false);
        videoSeekBar.setEnabled(false);
        videoView.setVideoPath(path);
        videoPlayImg.setVisibility(View.GONE);
        videoView.start();
    }
    
    public void start(String url)
    {
        this.url = url;
        videoPauseBtn.setEnabled(false);
        videoSeekBar.setEnabled(false);
        videoView.setVideoURI(Uri.parse(url));
        videoPlayImg.setVisibility(View.GONE);
        videoView.start();
    }
    
    public void setFullScreen()
    {
        touchStatusImg.setImageResource(R.drawable.iconfont_exit);
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        videoView.requestLayout();
    }
    
    public void setNormalScreen()
    {
        touchStatusImg.setImageResource(R.drawable.iconfont_enter_32);
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        videoView.requestLayout();
    }
    
    public void goneSeekBar()
    {
        
    }
    
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        initView();
    }
    
    private void initView()
    {
        View view = LayoutInflater.from(context).inflate(R.layout.common_video_view, null);
        viewBox = (FrameLayout)view.findViewById(R.id.viewBox);
        videoView = (VideoView)view.findViewById(R.id.videoView);
        videoPauseBtn = (LinearLayout)view.findViewById(R.id.videoPauseBtn);
        screenSwitchBtn = (LinearLayout)view.findViewById(R.id.screen_status_btn);
        videoControllerLayout = (LinearLayout)view.findViewById(R.id.videoControllerLayout);
//        touchStatusView = (LinearLayout)view.findViewById(R.id.touch_view);
        touchStatusImg = (ImageView)view.findViewById(R.id.touchStatusImg);
//        touchStatusTime = (TextView)view.findViewById(R.id.touch_time);
        videoCurTimeText = (TextView)view.findViewById(R.id.videoCurTime);
        videoTotalTimeText = (TextView)view.findViewById(R.id.videoTotalTime);
        videoSeekBar = (SeekBar)view.findViewById(R.id.videoSeekBar);
        videoPlayImg = (LinearLayout)view.findViewById(R.id.videoPlayImg);
        videoPlayImg.setVisibility(GONE);
        videoPauseImg = (ImageView)view.findViewById(R.id.videoPauseImg);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        
        videoPauseBtn.setOnClickListener(this);
        videoSeekBar.setOnSeekBarChangeListener(this);
        videoPauseBtn.setOnClickListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        screenSwitchBtn.setOnClickListener(this);
        videoPlayImg.setOnClickListener(this);
        videoView.setOnErrorListener(this);
        viewBox.setOnTouchListener(this);
        viewBox.setOnClickListener(this);
        addView(view);
    }
    
    @Override
    public void onPrepared(MediaPlayer mp)
    {
        videoPlayImg.setVisibility(View.GONE);
        duration = videoView.getDuration();
        int[] time = getMinuteAndSecond(duration);
        videoTotalTimeText.setText(String.format("%02d:%02d", time[0], time[1]));
//        formatTotalTime = String.format("%02d:%02d", time[0], time[1]);
        videoSeekBar.setMax(duration);
        progressBar.setVisibility(View.GONE);
        mp.start();
        videoPauseBtn.setEnabled(true);
        videoSeekBar.setEnabled(true);
        videoPauseImg.setImageResource(R.drawable.icon_video_pause);
        if (timerTask != null)
        {
            timerTask.cancel(); // 将原任务从队列中移除
        }
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                videoHandler.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }
    
    @Override
    public void onCompletion(MediaPlayer mp)
    {
        videoView.seekTo(0);
        videoSeekBar.setProgress(0);
        videoPauseImg.setImageResource(R.drawable.icon_video_play);
        videoPlayImg.setVisibility(View.VISIBLE);
    }
    
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        videoPauseImg.setImageResource(R.drawable.icon_video_play);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(context, "该视频无法播放!", Toast.LENGTH_SHORT).show();
        return true;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                // if (!videoView.isPlaying())
                // {
                // return false;
                // }
                // float downX = event.getRawX();
                // touchLastX = downX;
                // Log.d("FilmDetailActivity", "downX" + downX);
                // this.position = videoView.getCurrentPosition();
                break;
            case MotionEvent.ACTION_MOVE:
                // if (!videoView.isPlaying())
                // {
                // return false;
                // }
                // float currentX = event.getRawX();
                // float deltaX = currentX - touchLastX;
                // float deltaXAbs = Math.abs(deltaX);
                // if (deltaXAbs > 1)
                // {
                // if (touchStatusView.getVisibility() != View.VISIBLE)
                // {
                // touchStatusView.setVisibility(View.VISIBLE);
                // }
                // touchLastX = currentX;
                // Log.d("FilmDetailActivity", "deltaX" + deltaX);
                // if (deltaX > 1)
                // {
                // position += touchStep;
                // if (position > duration)
                // {
                // position = duration;
                // }
                // touchPosition = position;
                // touchStatusImg.setImageResource(R.drawable.ic_fast_forward_white_24dp);
                // int[] time = getMinuteAndSecond(position);
                // touchStatusTime.setText(String.format("%02d:%02d/%s", time[0], time[1], formatTotalTime));
                // }
                // else if (deltaX < -1)
                // {
                // position -= touchStep;
                // if (position < 0)
                // {
                // position = 0;
                // }
                // touchPosition = position;
                // touchStatusImg.setImageResource(R.drawable.ic_fast_rewind_white_24dp);
                // int[] time = getMinuteAndSecond(position);
                // touchStatusTime.setText(String.format("%02d:%02d/%s", time[0], time[1], formatTotalTime));
                // // mVideoView.seekTo(position);
                // }
                // }
                break;
            case MotionEvent.ACTION_UP:
                // if (touchPosition != -1)
                // {
                // videoView.seekTo(touchPosition);
                // touchStatusView.setVisibility(View.GONE);
                // touchPosition = -1;
                // if (videoControllerShow)
                // {
                // return true;
                // }
                // }
                break;
        }
        return false;
    }
    
    private int[] getMinuteAndSecond(int mils)
    {
        mils /= 1000;
        int[] time = new int[2];
        time[0] = mils / 60;
        time[1] = mils % 60;
        return time;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.videoPlayImg:
                videoView.start();
                videoPlayImg.setVisibility(View.INVISIBLE);
                videoPauseImg.setImageResource(R.drawable.icon_video_pause);
                break;
            case R.id.videoPauseBtn:
                if (videoView.isPlaying())
                {
                    videoView.pause();
                    videoPauseImg.setImageResource(R.drawable.icon_video_play);
                    // videoPlayImg.setVisibility(View.VISIBLE);
                }
                else
                {
                    videoView.start();
                    videoPauseImg.setImageResource(R.drawable.icon_video_pause);
                    videoPlayImg.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.viewBox:
                float curY = videoControllerLayout.getY();
                if (!animation && videoControllerShow)
                {
                    animation = true;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(videoControllerLayout, "y", curY, curY + videoControllerLayout.getHeight());
                    animator.setDuration(200);
                    animator.start();
                    animator.addListener(this);
                }
                else if (!animation)
                {
                    animation = true;
                    ObjectAnimator animator = ObjectAnimator.ofFloat(videoControllerLayout, "y", curY, curY - videoControllerLayout.getHeight());
                    animator.setDuration(200);
                    animator.start();
                    animator.addListener(this);
                }
                break;
            case R.id.screen_status_btn:
                if (videoView != null && videoView.isPlaying())
                {
                    videoView.setDrawingCacheEnabled(true);
                    Bitmap bitmap = videoView.getDrawingCache();
                    videoWidth = bitmap.getWidth();
                    videoHeight = bitmap.getHeight();
                    bitmap.recycle();
                }
                if (videoWidth > videoHeight)
                {
                    int i = getResources().getConfiguration().orientation;
                    if (i == Configuration.ORIENTATION_PORTRAIT)
                    {
                        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    else if (i == Configuration.ORIENTATION_LANDSCAPE)
                    {
                        ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                }
                else
                {
                    if (onScreenListener != null)
                    {
                        if (isScreen)
                        {
                            // 退出全屏
                            onScreenListener.NoScreen();
                        }
                        else
                        {
                            // 开启全屏
                            onScreenListener.startScreen();
                        }
                        isScreen = !isScreen;
                    }
                }
                break;
        }
    }
    
    public void setOnScreenListener(OnScreenListener onScreenListener)
    {
        this.onScreenListener = onScreenListener;
    }
    
    public interface OnScreenListener
    {
        void NoScreen();
        
        void startScreen();
    }
    
    @Override
    public void onAnimationStart(Animator animation)
    {
    }
    
    @Override
    public void onAnimationEnd(Animator animation)
    {
        this.animation = false;
        this.videoControllerShow = !this.videoControllerShow;
    }
    
    @Override
    public void onAnimationCancel(Animator animation)
    {
        
    }
    
    @Override
    public void onAnimationRepeat(Animator animation)
    {
        
    }
    
    /**
     * 抓图
     * 
     * @param path
     * @param name
     */
    public Bitmap getPicFromVideo()
    {
        // MediaMetadataRetriever rev = new MediaMetadataRetriever();
        //
        // rev.setDataSource(context, Uri.parse(url)); //
        // // 这里第一个参数需要Context，传this指针
        // if (videoView != null)
        // {
        // Bitmap bitmap = rev.getFrameAtTime(
        // videoView.getCurrentPosition() * 1000,
        // MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        // // videoView.setDrawingCacheEnabled(true);
        // // Bitmap bitmap = videoView.getDrawingCache();
        // saveBitmap(path, name, bitmap);
        // }
        // else
        // {
        // return;
        // }
        
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        
        try
        {
            mediaMetadataRetriever.setDataSource(url, new HashMap<String, String>());
            // 取得指定时间的Bitmap，即可以实现抓图（缩略图）功能
            bitmap = mediaMetadataRetriever.getFrameAtTime(videoView.getCurrentPosition() * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        }
        catch (IllegalArgumentException ex)
        {
            return null;
        }
        catch (RuntimeException ex)
        {
            return null;
        }
        finally
        {
            try
            {
                mediaMetadataRetriever.release();
            }
            catch (RuntimeException ex)
            {
                return null;
            }
        }
        
        if (bitmap == null)
        {
            return null;
        }
        // bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        bitmap = Bitmap.createBitmap(bitmap);
        // saveBitmap(path, name, bitmap);
        return bitmap;
    }
    
    // public void getBitmapsFromVideo() {
    // MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    // retriever.setDataSource(path+"/"+name+".mp4");
    // // 取得视频的长度(单位为毫秒)
    // String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
    // // 取得视频的长度(单位为秒)
    // int seconds = Integer.valueOf(time) / 1000;
    //
    //
    // // 得到每一秒时刻的bitmap比如第一秒,第二秒
    // for (int i = 1; i <= seconds; i++) {
    // Bitmap bitmap =
    // retriever.getFrameAtTime(videoView.getCurrentPosition()*1000,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
    // String path = Environment.getExternalStorageDirectory()+ File.separator + i + ".jpg";
    // FileOutputStream fos = null;
    // try {
    // fos = new FileOutputStream(path);
    // bitmap.compress(CompressFormat.JPEG, 80, fos);
    // fos.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // }
    
    /** 保存方法 */
    public void saveBitmap(String path, String name, Bitmap bm)
    {
        File f = new File(path, name + ".jpg");
        if (f.exists())
        {
            f.delete();
            
        }
        try
        {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
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
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        int[] time = getMinuteAndSecond(progress);
        videoCurTimeText.setText(String.format("%02d:%02d", time[0], time[1]));
    }
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        videoView.pause();
    }
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        videoView.seekTo(videoSeekBar.getProgress());
        videoView.start();
        videoPlayImg.setVisibility(View.INVISIBLE);
        videoPauseImg.setImageResource(R.drawable.icon_video_pause);
    }
}
