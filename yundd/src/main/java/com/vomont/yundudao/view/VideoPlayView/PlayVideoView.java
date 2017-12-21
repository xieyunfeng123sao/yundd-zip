package com.vomont.yundudao.view.VideoPlayView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.view.VideoPlayView.TextureVideoPlayer.OnVideoPlayingListener;

public class PlayVideoView extends RelativeLayout
{
    
//    private RelativeLayout play_preview;
    
    private ImageView control_play;
    
    private TextView nowtime;
    
    private SeekBar play_seekbar;
    
    private TextView playtime;
    
//    private ImageView expand_bt;
//    
//    private LinearLayout replay_ll;
//    
//    private RelativeLayout replay_rl;
//    
//    private LinearLayout control_ll;
//    
//    private ProgressBar progressBar;
    
    private TextureVideoPlayer videoplay;
    
    // 播放的地址
    private String url;
    
//    private String path;
    
    private OnPlayListener onPlayListener;
    
    public PlayVideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }
    
    public PlayVideoView(Context context)
    {
        super(context);
        initView(context);
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
    
    private void initView(Context context)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.vedio_playview, this);
//        play_preview = (RelativeLayout)findViewById(R.id.play_preview);
        control_play = (ImageView)findViewById(R.id.play_button);
        nowtime = (TextView)findViewById(R.id.nowtime);
        play_seekbar = (SeekBar)findViewById(R.id.play_seekbar);
        playtime = (TextView)findViewById(R.id.playtime);
//        expand_bt = (ImageView)findViewById(R.id.expand_bt);
//        replay_rl = (RelativeLayout)findViewById(R.id.replay_rl);
//        control_ll = (LinearLayout)findViewById(R.id.control_ll);
//        replay_ll = (LinearLayout)findViewById(R.id.replay_ll);
//        progressBar = (ProgressBar)findViewById(R.id.progressBar);
//        videoplay = (TextureVideoPlayer)findViewById(R.id.videoplay);
        
        play_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                videoplay.setSeek(progress);
            }
        });
        
        videoplay.setOnVideoPlayingListener(new OnVideoPlayingListener()
        {
            @Override
            public void onVideoSizeChanged(int vWidth, int vHeight)
            {
                
            }
            
            @Override
            public void onTextureDestory()
            {
                
            }
            
            @Override
            public void onStart()
            {
                control_play.setImageResource(R.drawable.vedio_stop);
            }
            
            @Override
            public void onRestart()
            {
                control_play.setImageResource(R.drawable.vedio_stop);
            }
            
            @Override
            public void onPlayingFinish()
            {
                control_play.setImageResource(R.drawable.vedio_start);
            }
            
            @Override
            public void onPlaying(int duration, int percent)
            {
                play_seekbar.setMax(duration);
                playtime.setText(timeToString(duration));
                nowtime.setText(timeToString(percent));
                play_seekbar.setProgress(percent);
            }
            
            @Override
            public void onPause()
            {
                
            }
            
            @Override
            public void onCanPlay()
            {
                if (onPlayListener != null)
                {
                    onPlayListener.onCanPlay();
                }
            }
        });
    }
    
    public String timeToString(int time)
    {
        int max = time / 1000;
        int ss;
        int mm;
        String m = "";
        String s = "";
        ss = max % 60;
        mm = max / 60;
        if (ss > 9)
        {
            s = ss + "";
        }
        else
        {
            s = "0" + ss;
        }
        if (mm > 9)
        {
            m = mm + "";
        }
        else
        {
            m = "0" + mm;
        }
        return m + ":" + s;
        
    }
    
    /**
     * 网络视频的url
     * 
     * @param url
     */
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    /**
     * 本地视频的地址
     * 
     * @param path
     */
    public void setPath(String path)
    {
//        this.path = path;
    }
    
    /**
     * 判断是否可以开始播放的回调
     * 
     * @param onPlayListener
     */
    public void setOnCanPlayListener(OnPlayListener onPlayListener)
    {
        this.onPlayListener = onPlayListener;
    }
    
    public interface OnPlayListener
    {
        void onCanPlay();
    }
    
    /**
     * 开始播放
     */
    public void onStart()
    {
        videoplay.setUrl(url);
        videoplay.play();
        videoplay.setVideoMode(2);
    }
    
}
