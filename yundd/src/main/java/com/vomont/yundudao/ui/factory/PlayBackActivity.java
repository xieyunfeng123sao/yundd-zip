package com.vomont.yundudao.ui.factory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.PicTimeInfo;
import com.vomont.yundudao.ui.calendar.KCalendar;
import com.vomont.yundudao.ui.calendar.KCalendar.OnCalendarClickListener;
import com.vomont.yundudao.ui.createproblem.CreateProblemActivity;
import com.vomont.yundudao.utils.CommonUtil;
import com.vomont.yundudao.utils.PlayBackUtil;
import com.vomont.yundudao.utils.PlayBackUtil.PlayBackListener;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.view.playback.PlayBackSeekBar;
import com.vomont.yundudao.view.playback.PlayBackSeekBar.OnPlaySeekBarListener;
import com.wmclient.clientsdk.StreamPlayer;
import com.wmclient.clientsdk.WMFileInfo;

@SuppressLint({"SimpleDateFormat", "HandlerLeak"})
public class PlayBackActivity extends Activity implements OnClickListener
{
    // 返回键
    private ImageView go_back;
    
    // 标题名称
    private TextView top_name;
    
    // 播放窗口
    private RelativeLayout playback_layout;
    
    // 日期时间
    private TextView popupwindow_calendar_month;
    
    // 1天 1小时 5分钟时间切换的button
    private Button one_day, one_hour, five_min;
    
    // 日期控件
    private KCalendar calendar;
    
    // 日期
    private String date = "";
    
    private int on_day;
    
    // 回放接口
    private PlayBackUtil playBackUtil;
    
    // 回放的文件
    private List<WMFileInfo> mlFileInfos;
    
    // 回放的类型
    private int type;
    
    // 回放的结束时间
    long endTime = 0;
    
    // 回放的开始时间
    long startTime = 0;
    
    // 时间轴开始时间
    long seekStartTime = 0;
    
    // 时间轴结束时间
    long seekEndTime = 0;
    
    private Handler handler;
    
    private int deviceId, devChannelId;
    
    // 回放的时间轴
    private PlayBackSeekBar playbackbar;
    
    // 流媒体播放器
    private StreamPlayer streamPlayer;
    
    // 定时器
    private Timer timer;
    
    private MyTimeTask myTimeTask;
    
    //
    private ImageView play_back_change;
    
    private ImageView play_back_stop;
    
    // 是否横竖屏
    private boolean isLandscape;
    
    private RelativeLayout playback_top;
    
    private ScrollView play_back_scroll;
    
    private int width, patrol_re_video_height;
    
    private RelativeLayout play_back_all;
    
    private String topname;
    
    private ImageView play_back_zhua, play_back_kuai;
    
    private ProgressBar playback_progressbar;
    
    private int factoryid, subfactoryid, deviceid;
    
    private RelativeLayout play_back_control_rl;
    
    // 播放窗口中的状态栏是否显示
    private boolean controlIsShow;
    
    private Dialog dialog;
    
    // 播放的pos点
    private int nPos;
    
    // 播放的片段
    private int index;
    
    private int seekBar_type = 0;
    
    // 判断是否滑动到可播放的区域
    private boolean isScrollToPlay = false;
    
    // 当前是否在播放
    private boolean isPlayIng = false;
    
    // 界面是否跳到后台
    private boolean isStop = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
        controlIsShow = true;
        factoryid = getIntent().getIntExtra("factoryid", -1);
        subfactoryid = getIntent().getIntExtra("subfactoryid", -1);
        deviceid = getIntent().getIntExtra("deviceid", -1);
        topname = getIntent().getStringExtra("topname");
        deviceId = getIntent().getIntExtra("deviceId", 0);
        devChannelId = getIntent().getIntExtra("devChannelId", 0);
        type = getIntent().getIntExtra("type", 0);
        playBackUtil = new PlayBackUtil(this);
        playBackUtil.setPlayBackListener(new PlayBackListener()
        {
            @Override
            public void onSucess()
            {
                handler.sendEmptyMessage(30);
            }
            
            @Override
            public void onFail()
            {
                handler.sendEmptyMessage(40);
            }
            
            @Override
            public void onConnectTimeOut()
            {
                handler.sendEmptyMessage(50);
            }
        });
        playbackbar.setScrollview(play_back_scroll);
        playbackbar.setOnScrollListener(new OnPlaySeekBarListener()
        {
            @Override
            public void onScrolling()
            {
                stopTimer();
            }
            
            @Override
            public void onScrolled(long nowTime)
            {
                if (mlFileInfos != null && mlFileInfos.size() != 0)
                {
                    for (int i = 0; i < mlFileInfos.size(); i++)
                    {
                        if (nowTime >= mlFileInfos.get(i).getStartTime() && nowTime <= mlFileInfos.get(i).getEndTime())
                        {
                            isScrollToPlay = true;
                            isPlayIng = false;
                            // 滑动到的地方有视频
                            index = i;
                            nPos = (int)((nowTime - mlFileInfos.get(i).getStartTime()) * 10000 / (mlFileInfos.get(i).getEndTime() - mlFileInfos.get(i).getStartTime()));
                            startPlayVideo();
                            // 滑动到最左边 或者最右边
                            if (seekBar_type != 0 && nowTime == seekStartTime)
                            {
                                updataSeek(seekBar_type, nowTime);
                                
                            }
                            else if (seekBar_type != 0 && nowTime == seekEndTime)
                            {
                                updataSeek(seekBar_type, nowTime);
                            }
                            break;
                        }
                    }
                }
                if (isPlayIng && !isScrollToPlay)
                {
                    startTimer();
                    isScrollToPlay = false;
                }
            }
        });
        
        top_name.setText(topname);
        handlerMessage();
        initData();
    }
    
    private void initData()
    {
        dialog = ProgressDialog.createLoadingDialog(this, "");
        dialog.show();
        
        mlFileInfos.clear();
        stopPlayVideo();
        new Thread(new Runnable()
        {
            public void run()
            {
                mlFileInfos = new ArrayList<WMFileInfo>();
                playBackUtil.searchFrontEndFileList(mlFileInfos, deviceId, devChannelId, startTime, endTime);
                Message message = new Message();
                message.what = 100;
                message.obj = mlFileInfos;
                handler.sendMessage(message);
            }
        }).start();
    }
    
    private void initView()
    {
        one_day = (Button)findViewById(R.id.one_day);
        one_hour = (Button)findViewById(R.id.one_hour);
        five_min = (Button)findViewById(R.id.five_min);
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        playback_layout = (RelativeLayout)findViewById(R.id.playback_layout);
        playbackbar = (PlayBackSeekBar)findViewById(R.id.playbackbar);
        play_back_change = (ImageView)findViewById(R.id.play_back_change);
        playback_top = (RelativeLayout)findViewById(R.id.playback_top);
        play_back_scroll = (ScrollView)findViewById(R.id.play_back_scroll);
        play_back_all = (RelativeLayout)findViewById(R.id.play_back_all);
        play_back_stop = (ImageView)findViewById(R.id.play_back_stop);
        play_back_zhua = (ImageView)findViewById(R.id.play_back_zhua);
        play_back_kuai = (ImageView)findViewById(R.id.play_back_kuai);
        playback_progressbar = (ProgressBar)findViewById(R.id.playback_progressbar);
        play_back_control_rl = (RelativeLayout)findViewById(R.id.play_back_control_rl);
        playback_layout.setOnClickListener(this);
        play_back_change.setOnClickListener(this);
        play_back_stop.setOnClickListener(this);
        play_back_zhua.setOnClickListener(this);
        play_back_kuai.setOnClickListener(this);
        go_back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                playback_progressbar.setVisibility(View.GONE);
                playBackUtil.stopPlay();
                stopTimer();
                finish();
            }
        });
        one_day.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                one_day.setBackgroundResource(R.drawable.time_button_bg);
                one_day.setTextColor(getResources().getColor(R.color.white));
                
                one_hour.setBackgroundResource(R.drawable.time_button_bg_pressed);
                one_hour.setTextColor(getResources().getColor(R.color.text_color));
                
                five_min.setBackgroundResource(R.drawable.time_button_bg_pressed);
                five_min.setTextColor(getResources().getColor(R.color.text_color));
                
                // 更新状态 已经改变时间轴的起始和结束时间
                seekBar_type = 0;
                updataSeek(seekBar_type, getPosTime());
            }
        });
        
        one_hour.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                one_hour.setBackgroundResource(R.drawable.time_button_bg);
                one_hour.setTextColor(getResources().getColor(R.color.white));
                
                one_day.setBackgroundResource(R.drawable.time_button_bg_pressed);
                one_day.setTextColor(getResources().getColor(R.color.text_color));
                
                five_min.setBackgroundResource(R.drawable.time_button_bg_pressed);
                five_min.setTextColor(getResources().getColor(R.color.text_color));
                
                seekBar_type = 1;
                
                updataSeek(seekBar_type, getPosTime());
            }
        });
        
        five_min.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                five_min.setBackgroundResource(R.drawable.time_button_bg);
                five_min.setTextColor(getResources().getColor(R.color.white));
                one_hour.setBackgroundResource(R.drawable.time_button_bg_pressed);
                one_hour.setTextColor(getResources().getColor(R.color.text_color));
                one_day.setBackgroundResource(R.drawable.time_button_bg_pressed);
                one_day.setTextColor(getResources().getColor(R.color.text_color));
                seekBar_type = 2;
                updataSeek(seekBar_type, getPosTime());
            }
        });
        top_name.setText("录像回放");
        popupwindow_calendar_month = (TextView)findViewById(R.id.popupwindow_calendar_month);
        calendar = (KCalendar)findViewById(R.id.popupwindow_calendar);
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = sFormat.format(new Date());
        mlFileInfos = new ArrayList<WMFileInfo>();
        if (null != date)
        {
            int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
            int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
            int day = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1, date.length()));
            popupwindow_calendar_month.setText(years + "/" + time0(month) + "/" + time0(day));
            calendar.showCalendar(years, month);
            calendar.setCalendarDayBgColor(date, R.drawable.new_dot_three);
            try
            {
                startTime = sFormat.parse(date + " 00:00:00").getTime();
                endTime = startTime + 86400000;
                seekStartTime = startTime;
                seekEndTime = endTime;
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        // 监听所选中的日期
        calendar.setOnCalendarClickListener(new OnCalendarClickListener()
        {
            public void onCalendarClick(int row, int col, String dateFormat)
            {
                int year = Integer.parseInt(dateFormat.substring(0, dateFormat.indexOf("-")));
                int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));
                if (calendar.getCalendarMonth() - month == 1// 跨年跳转
                    || calendar.getCalendarMonth() - month == -11)
                {
                    calendar.lastMonth();
                }
                else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
                    || month - calendar.getCalendarMonth() == -11)
                {
                    calendar.nextMonth();
                }
                else
                {
                    calendar.removeAllBgColor();
                    calendar.setCalendarDayBgColor(dateFormat, R.drawable.new_dot_three);
                    date = dateFormat;// 最后返回给全局 date
                    on_day = Integer.parseInt(dateFormat.substring(dateFormat.lastIndexOf("-") + 1, dateFormat.length()));
                    popupwindow_calendar_month.setText(year + "/" + time0(month) + "/" + time0(on_day));
                    try
                    {
                        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
                        startTime = sFormat.parse(date + " 00:00:00").getTime();
                        endTime = startTime + 86400000;
                        // 切換日期后 初始化所有数据
                        seekStartTime = startTime;
                        seekEndTime = endTime;
                        index = 0;
                        nPos = 0;
                        one_day.setBackgroundResource(R.drawable.time_button_bg);
                        one_day.setTextColor(getResources().getColor(R.color.white));
                        
                        one_hour.setBackgroundResource(R.drawable.time_button_bg_pressed);
                        one_hour.setTextColor(getResources().getColor(R.color.text_color));
                        
                        five_min.setBackgroundResource(R.drawable.time_button_bg_pressed);
                        five_min.setTextColor(getResources().getColor(R.color.text_color));
                        seekBar_type = 0;
                        nPos = 0;
                        stopTimer();
                        initData();
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        // 上月监听按钮
        RelativeLayout popupwindow_calendar_last_month = (RelativeLayout)findViewById(R.id.popupwindow_calendar_last_month);
        popupwindow_calendar_last_month.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                calendar.lastMonth();
            }
        });
        
        // 下月监听按钮
        RelativeLayout popupwindow_calendar_next_month = (RelativeLayout)findViewById(R.id.popupwindow_calendar_next_month);
        popupwindow_calendar_next_month.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                calendar.nextMonth();
            }
        });
        getViewwidth();
    }
    
    /**
     * 根据当前时间更新时间轴时间
     * <功能详细描述>
     * @param seektype 时间轴的类型
     * @param nowTime 当前时间
     * @see [类、类#方法、类#成员]
     */
    private void updataSeek(int seektype, long nowTime)
    {
        if (seektype == 0)
        {
            seekStartTime = startTime;
            seekEndTime = endTime;
        }
        else
        {
            int longTime = 0;
            if (seektype == 1)
            {
                longTime = (int)(1000 * 60 * 60 * 0.5);
                
            }
            else
            {
                longTime = (int)(1000 * 60 * 2.5);
            }
            if ((nowTime - startTime) >= longTime && (endTime - getPosTime()) >= longTime)
            {
                
                seekStartTime = (long)(nowTime - longTime);
                seekEndTime = (long)(nowTime + longTime);
            }
            else if ((getPosTime() - startTime) < longTime)
            {
                seekStartTime = startTime;
                seekEndTime = (long)(startTime + longTime * 2);
            }
            else
            {
                // 时间大于当天的23:57:30
                seekStartTime = (long)(endTime - longTime * 2);
                seekEndTime = endTime;
            }
        }
        playbackbar.setTime(seekStartTime, seekEndTime, seekBar_type);
        playbackbar.setScroll(getPosTime());
    }
    
    private String time0(int time)
    {
        if (time < 10)
        {
            return "0" + time;
        }
        return time + "";
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    
    public void handlerMessage()
    {
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch (msg.what)
                {
                    case 100:
                        if (mlFileInfos != null && mlFileInfos.size() != 0)
                        {
                            playbackbar.setTime(seekStartTime, seekEndTime, seekBar_type);
                            playbackbar.setTimeFile(mlFileInfos);
                            if (mlFileInfos.get(index).getStartTime() < seekStartTime)
                            {
                                nPos = (int)((seekStartTime - mlFileInfos.get(index).getStartTime()) * 10000 / (mlFileInfos.get(index).getEndTime() - mlFileInfos.get(index).getStartTime()));
                            }
                            startPlayVideo();
                        }
                        else
                        {
                            // 如果没有数据
                            playbackbar.setTime(startTime, endTime, seekBar_type);
                            Toast.makeText(PlayBackActivity.this, "没有数据！", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        break;
                    case 20:
                        new Thread(new Runnable()
                        {
                            public void run()
                            {
                                final int now = playBackUtil.getFrontEndFilePlayPos();
                                if (now != 0)
                                {
                                    nPos = now;
                                }
                                runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        if (now == 10000)
                                        {
                                            // 如果這一小段播放結束 则判断是否还有下一个片段 如果有就继续播放
                                            if ((index + 1) <= (mlFileInfos.size() - 1))
                                            {
                                                index = index + 1;
                                                nPos = 0;
                                                startPlayVideo();
                                                // 如果播放的片段的起始时间不在该片段内则更新时间轴的时间
                                                if (mlFileInfos.get(index).getStartTime() >= seekEndTime)
                                                {
                                                    updataSeek(seekBar_type, mlFileInfos.get(index).getStartTime());
                                                }
                                            }
                                            else
                                            {
                                                // 没有新的片段刷新界面的效果
                                                stopPlayVideo();
                                            }
                                            return;
                                        }
                                        else
                                        {
                                            // 如果播放的时间超过了时间轴的最右边 则刷新时间轴
                                            if (getPosTime() >= seekEndTime && getPosTime() <= endTime)
                                            {
                                                updataSeek(seekBar_type, seekEndTime);
                                            }
                                            else if (getPosTime() > endTime)
                                            {
                                                stopPlayVideo();
                                            }
                                            else
                                            { // 没超过 就移动滑块
                                                playbackbar.setScroll(getPosTime());
                                            }
                                        }
                                    }
                                });
                            }
                        }).start();
                        break;
                    case 30:
                        isScrollToPlay = false;
                        isPlayIng = true;
                        play_back_stop.setImageResource(R.drawable.icon_pause);
                        startTimer();
                        break;
                    case 40:
                        isPlayIng = false;
                        Toast.makeText(PlayBackActivity.this, "打开失败！", Toast.LENGTH_SHORT).show();
                        break;
                    case 50:
                        isPlayIng = false;
                        Toast.makeText(PlayBackActivity.this, "连接超时！", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    
    private void stopPlayVideo()
    {
        isPlayIng = false;
        isScrollToPlay = false;
        playBackUtil.stopPlay();
        playback_progressbar.setVisibility(View.GONE);
        isPlayIng = false;
        play_back_stop.setImageResource(R.drawable.icon_play);
        stopTimer();
    }
    
    private void startPlayVideo()
    {
        stopPlayVideo();
        playback_progressbar.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable()
        {
            public void run()
            {
                // 加点延时 可以显示转圈 不然由于播放器的创建 转圈效果就没了
                streamPlayer = playBackUtil.getStreamPlayer(type, playback_layout, deviceId, devChannelId);
                playBackUtil.startFrontEndFilePlay(nPos, mlFileInfos.get(index), streamPlayer);
            }
        }, 300);
        
    }
    
    /**
     * 
     * 获取当前播放的时间节点 单位是毫秒
     * 通过pos点获取当前播放的时间节点
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    public long getPosTime()
    {
        if (mlFileInfos != null && mlFileInfos.size() != 0)
        {
            long time = mlFileInfos.get(index).getStartTime() + (mlFileInfos.get(index).getEndTime() - mlFileInfos.get(index).getStartTime()) * nPos / 10000;
            return time;
        }
        return 0;
    }
    
    /**
     * 
     * 停止计时
     * 播放器关闭时停止计时 不需要在获取pos点
     * 
     * @see [类、类#方法、类#成员]
     */
    private void stopTimer()
    {
        if (timer != null)
        {
            myTimeTask.cancel();
            myTimeTask = null;
            timer.cancel();
            timer = null;
        }
    }
    
    /**
     * 
     * 開始計時
     * 当回放视频开始播放时，获取当前播放pos 用于时间轴上滑块的位置的实时更新
     * 
     * @see [类、类#方法、类#成员]
     */
    private void startTimer()
    {
        stopTimer();
        timer = new Timer();
        myTimeTask = new MyTimeTask();
        timer.schedule(myTimeTask, 0, 1000);
    }
    
    class MyTimeTask extends TimerTask
    {
        @Override
        public void run()
        {
            handler.sendEmptyMessage(20);
        }
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        isStop = true;
        stopPlayVideo();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            
            if (isLandscape)
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else
            {
                if (dialog != null)
                {
                    dialog.dismiss();
                }
                playback_progressbar.setVisibility(View.GONE);
                stopPlayVideo();
                finish();
            }
        }
        return true;
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        if (isStop)
        {
            startPlayVideo();
            isStop = false;
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.play_back_change:
                // 横竖屏切换
                if (isLandscape)
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                else
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;
            case R.id.play_back_stop:
                if (isPlayIng)
                {
                    stopTimer();
                    playBackUtil.stopPlay();
                    playback_progressbar.setVisibility(View.GONE);
                    isPlayIng = false;
                    play_back_stop.setImageResource(R.drawable.icon_play);
                }
                else
                {
                    playback_progressbar.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            if(mlFileInfos!=null&&mlFileInfos.size()!=0)
                            {
                             // 加点延时 可以显示转圈 不然由于播放器的创建 转圈效果就没了
                                streamPlayer = playBackUtil.getStreamPlayer(type, playback_layout, deviceId, devChannelId);
                                playBackUtil.startFrontEndFilePlay(nPos, mlFileInfos.get(index), streamPlayer); 
                            }
                            else
                            {
                                playback_progressbar.setVisibility(View.GONE);
                            }
                        }
                    }, 300);
                }
                break;
            case R.id.play_back_zhua:
                // 抓拍
                String name1 = playBackUtil.saveSnapshot(factoryid, subfactoryid, deviceid);
                if (name1 != null)
                {
                    Toast.makeText(PlayBackActivity.this, "抓拍成功！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.play_back_kuai:
                // 快拍
                String name = playBackUtil.saveSnapshot(factoryid, subfactoryid, deviceid);
                if (name != null)
                {
                    String string = new String(name);
                    String[] info = string.split("-");
                    PicTimeInfo picTimeInfo = new PicTimeInfo();
                    picTimeInfo.setDeviceid(Integer.parseInt(info[3]));
                    picTimeInfo.setSubfactoryid(Integer.parseInt(info[2]));
                    picTimeInfo.setFactoryid(Integer.parseInt(info[1]));
                    picTimeInfo.setPath(playBackUtil.getPath());
                    picTimeInfo.setTime(info[0]);
                    picTimeInfo.setName(name + ".jpg");
                    Intent intent = new Intent(PlayBackActivity.this, CreateProblemActivity.class);
                    intent.putExtra("picinfo", picTimeInfo);
                    startActivity(intent);
                }
                break;
            case R.id.playback_layout:
                if (controlIsShow)
                {
                    play_back_control_rl.setVisibility(View.GONE);
                }
                else
                {
                    play_back_control_rl.setVisibility(View.VISIBLE);
                }
                controlIsShow = !controlIsShow;
                break;
            default:
                break;
        }
    }
    
    /**
     * 横竖屏切换的监听
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        isLandscape = !isLandscape;
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            // land
            playback_top.setVisibility(View.GONE);
            play_back_scroll.setVisibility(View.GONE);
            CommonUtil.setFullScreen(PlayBackActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width);
            play_back_all.setLayoutParams(params);
        }
        else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            // port
            playback_top.setVisibility(View.VISIBLE);
            play_back_scroll.setVisibility(View.VISIBLE);
            play_back_scroll.smoothScrollTo(0, 0);
            CommonUtil.cancelFullScreen(PlayBackActivity.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, patrol_re_video_height);
            play_back_all.setLayoutParams(params);
        }
        
    }
    
    @SuppressWarnings("deprecation")
    private void getViewwidth()
    {
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        
        int view_width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        
        int view_height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        
        play_back_all.measure(view_width, view_height);
        
        patrol_re_video_height = play_back_all.getMeasuredHeight();
    }
    
}
