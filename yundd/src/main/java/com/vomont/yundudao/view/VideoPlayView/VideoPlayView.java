package com.vomont.yundudao.view.VideoPlayView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.vomont.yundudao.R;
import vomont.ppmedia.WMPlayer;
import vomont.ppmedia.WMPlayer.OnErrorListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class VideoPlayView extends RelativeLayout implements SurfaceHolder.Callback
{
    
    private RelativeLayout play_preview;
    
    private ImageView control_play;
    
    private TextView nowtime;
    
    private SeekBar play_seekbar;
    
    private TextView playtime;
    
    private ImageView expand_bt;
    
    private WMPlayer wmPlayer = null;
    
    private LinearLayout replay_ll;
    
    private RelativeLayout replay_rl;
    
    private LinearLayout control_ll;
    
    private SurfaceView surfaceView;
    
    private String path;
    
    private Timer timer;
    
    private MyTimeTask task;
    
    private OnChangeClickListener onChangeClickListener;
    
    private ProgressBar progressBar;
    
    private boolean isPlaying;
    
    private boolean controlIsShow;
    
    private CanStopListener canStopListener;
    
    private boolean canLandWin;
    
    public VideoPlayView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(context);
    }
    
    public VideoPlayView(Context context)
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
        play_preview = (RelativeLayout)findViewById(R.id.play_preview);
        control_play = (ImageView)findViewById(R.id.play_button);
        nowtime = (TextView)findViewById(R.id.nowtime);
        play_seekbar = (SeekBar)findViewById(R.id.play_seekbar);
        playtime = (TextView)findViewById(R.id.playtime);
        expand_bt = (ImageView)findViewById(R.id.expand_bt);
        replay_rl = (RelativeLayout)findViewById(R.id.replay_rl);
        control_ll = (LinearLayout)findViewById(R.id.control_ll);
        replay_ll = (LinearLayout)findViewById(R.id.replay_ll);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        play_preview.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (controlIsShow)
                {
                    control_ll.setVisibility(View.GONE);
                }
                else
                {
                    control_ll.setVisibility(View.VISIBLE);
                }
                controlIsShow = !controlIsShow;
            }
        });
        
        play_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                if (wmPlayer != null && wmPlayer.isPlaying())
                {
                    if (timer != null)
                    {
                        timer.cancel();
                        timer = null;
                        task.cancel();
                        task = null;
                    }
                    wmPlayer.seekTo(seekBar.getProgress() * 1000);
                    timer = new Timer();
                    task = new MyTimeTask();
                    timer.schedule(task, 1000, 1000); // timeTask
                }
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                
            }
        });
        
        replay_ll.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                replay_rl.setVisibility(View.GONE);
                control_ll.setVisibility(View.VISIBLE);
                startPlay(path);
            }
        });
        expand_bt.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                onChangeClickListener.OnClick(v);
            }
        });
        
        control_play.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (isPlaying)
                {
                    control_play.setImageResource(R.drawable.vedio_start);
                    if (wmPlayer != null)
                    {
                        wmPlayer.pause();
                    }
                }
                else
                {
                    control_play.setImageResource(R.drawable.vedio_stop);
                    if (wmPlayer != null)
                    {
                        wmPlayer.start();
                    }
                }
                isPlaying = !isPlaying;
            }
        });
        
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        if (surfaceView != null && wmPlayer != null)
        {
            LayoutParams params = new LayoutParams(wmPlayer.getVideoWidth() * h / wmPlayer.getVideoHeight(), h);
            params.leftMargin = (w - wmPlayer.getVideoWidth() * h / wmPlayer.getVideoHeight()) / 2;
            LayoutParams paramsSur = new LayoutParams(wmPlayer.getVideoWidth() * h / wmPlayer.getVideoHeight(), h);
            play_preview.setLayoutParams(params);
            // wmPlayer.changeSurface(surfaceView.getHolder());
            surfaceView.setLayoutParams(paramsSur);
        }
    }
    
    public boolean canLandWindow()
    {
        return canLandWin;
    }
    
    public void startPlay(String path)
    {
        this.path = path;
        canLandWin = false;
        if (surfaceView != null)
        {
            play_preview.removeView(surfaceView);
        }
        surfaceView = new SurfaceView(getContext());
        play_preview.addView(surfaceView);
        surfaceView.getHolder().addCallback(this);
        control_play.setImageResource(R.drawable.vedio_start);
        progressBar.setVisibility(View.VISIBLE);
        isPlaying = false;
        replay_rl.setVisibility(View.GONE);
        control_ll.setVisibility(View.VISIBLE);
    }
    
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (wmPlayer == null)
        {
            //
            wmPlayer = new WMPlayer();
            wmPlayer.setDisplay(holder);
            try
            {
                wmPlayer.setDataSource(path);
                wmPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                wmPlayer.setScreenOnWhilePlaying(true);
                wmPlayer.setOnPreparedListener(mPreparedListener);
                wmPlayer.setOnErrorListener(merrErrorListener);
                wmPlayer.setOnCompletionListener(mCompletionListener);
                wmPlayer.setOnInfoListener(mInfoListener);
                wmPlayer.prepareAsync();
            }
            catch (IllegalStateException e)
            {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
                e.printStackTrace();
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    // wmPlayer.snapshot(fileName);
    public boolean snapshot(String fileName)
    {
        if (wmPlayer != null && wmPlayer.isPlaying())
        {
            boolean isSucess = wmPlayer.snapshot(fileName);
            return isSucess;
        }
        return false;
    }
    
    WMPlayer.OnErrorListener merrErrorListener = new OnErrorListener()
    {
        @Override
        public boolean onError(WMPlayer mp, int what, int extra)
        {
            Toast.makeText(getContext(), "视频打开失败！", Toast.LENGTH_LONG).show();
            canLandWin = true;
            control_play.setImageResource(R.drawable.vedio_start);
            progressBar.setVisibility(View.GONE);
            if (canStopListener != null)
            {
                canStopListener.canStop();
            }
            if (wmPlayer != null)
            {
                wmPlayer.release();
                wmPlayer = null;
            }
            return true;
        }
    };
    
    WMPlayer.OnInfoListener mInfoListener = new WMPlayer.OnInfoListener()
    {
        
        @Override
        public boolean onInfo(WMPlayer mp, int what, int extra)
        {
            progressBar.setVisibility(View.GONE);
            if (canStopListener != null)
            {
                canStopListener.canStop();
            }
            canLandWin = true;
            isPlaying = true;
            if (what == WMPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
            {
                Log.v("insert", "WMPlayer.MEDIA_INFO_VIDEO_RENDERING_START");
                return true;
            }
            return false;
        }
    };
    
    public void canStopView(CanStopListener canStopListener)
    {
        this.canStopListener = canStopListener;
    }
    
    public interface CanStopListener
    {
        void canStop();
    }
    
    WMPlayer.OnCompletionListener mCompletionListener = new WMPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(WMPlayer mp)
        {
            if (wmPlayer != null)
            {
                wmPlayer.stop();
                wmPlayer.release();
                wmPlayer = null;
                replay_rl.setVisibility(View.VISIBLE);
                control_ll.setVisibility(View.GONE);
                replay_ll.setVisibility(View.VISIBLE);
            }
        }
    };
    
    public void stop()
    {
        control_play.setImageResource(R.drawable.vedio_start);
        progressBar.setVisibility(View.VISIBLE);
        isPlaying = false;
        replay_rl.setVisibility(View.GONE);
        control_ll.setVisibility(View.VISIBLE);
    }
    
    public int getPos()
    {
        if (wmPlayer != null && wmPlayer.isPlaying())
        {
            return wmPlayer.getCurrentPosition();
        }
        return -1;
    }
    
    public void setPos(int pos)
    {
        if (wmPlayer != null && wmPlayer.isPlaying())
        {
            wmPlayer.seekTo(pos);
        }
    }
    
    WMPlayer.OnPreparedListener mPreparedListener = new WMPlayer.OnPreparedListener()
    {
        @Override
        public void onPrepared(WMPlayer mp)
        {
            wmPlayer.start();
            replay_ll.setVisibility(View.GONE);
            replay_rl.setVisibility(View.GONE);
            LayoutParams params = new LayoutParams(mp.getVideoWidth() * play_preview.getHeight() / mp.getVideoHeight(), play_preview.getHeight());
            surfaceView.setLayoutParams(params);
            // wmPlayer.changeSurface(surfaceView.getHolder());
            timer = new Timer();
            task = new MyTimeTask();
            timer.schedule(task, 1000, 1000);
            controlIsShow = true;
            control_play.setImageResource(R.drawable.vedio_stop);
            play_seekbar.setMax(wmPlayer.getDuration() / 1000);
            playtime.setText(longTomin(wmPlayer.getDuration()));
            if (onGetViewListener != null)
            {
                onGetViewListener.OnView(mp.getVideoWidth(), mp.getVideoHeight());
            }
        }
    };
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        // if (wmPlayer != null && holder != null)
        // wmPlayer.changeSurface(holder);
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        if (wmPlayer != null)
        {
            if (wmPlayer.isPlaying())
            {
                wmPlayer.stop();
            }
            wmPlayer.release();
            wmPlayer = null;
        }
    }
    
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 10:
                    if (wmPlayer != null)
                    {
                        nowtime.setText(longTomin(wmPlayer.getCurrentPosition()));
                        play_seekbar.setProgress(wmPlayer.getCurrentPosition() / 1000);
                    }
                    break;
                default:
                    break;
            }
        };
    };
    
    public String longTomin(long time)
    {
        String mm = "";
        String ss = "";
        int nex = (int)(time / 1000);
        
        if (nex / 60 >= 10)
        {
            mm = nex / 60 + "";
        }
        else
        {
            mm = "0" + nex / 60;
        }
        
        if (nex % 60 >= 10)
        {
            ss = nex % 60 + "";
        }
        else
        {
            ss = "0" + nex % 60;
        }
        
        return mm + ":" + ss;
    }
    
    class MyTimeTask extends TimerTask
    {
        
        @Override
        public void run()
        {
            handler.sendEmptyMessage(10);
        }
    }
    
    public void setOnVedioChangeListener(OnChangeClickListener onChangeClickListener)
    {
        this.onChangeClickListener = onChangeClickListener;
    }
    
    public interface OnChangeClickListener
    {
        void OnClick(View view);
    }
    
    OnGetViewListener onGetViewListener;
    
    public void setOnGetViewListener(OnGetViewListener onGetViewListener)
    {
        this.onGetViewListener = onGetViewListener;
    }
    
    public interface OnGetViewListener
    {
        void OnView(int width, int hegiht);
    }
    
}
