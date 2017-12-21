package com.vomont.yundudao.ui.manual;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.ManualBean;
import com.vomont.yundudao.bean.PicTimeInfo;
import com.vomont.yundudao.ui.createproblem.CreateProblemActivity;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.CommonUtil;
import com.vomont.yundudao.utils.Playutil;
import com.vomont.yundudao.utils.Playutil.PlayListener;
import com.vomont.yundudao.utils.ScreenListener;
import com.vomont.yundudao.utils.ScreenListener.ScreenStateListener;
import com.wmclient.clientsdk.WMClientSdk;
import com.wmclient.clientsdk.WMDeviceInfo;

@SuppressLint("HandlerLeak")
public class ManualPlayActivity extends Activity implements OnClickListener
{
    private ProgressBar vedio_progressbar;
    
    private ImageView manual_video_back;
    
    private TextView play_device_name;
    
    private TextView play_next_device_name;
    
    private ImageView manual_video_control;
    
    private TextView count_down;
    
    private TextView count_all;
    
    private TextView manual_video_setting;
    
    private ImageView manual_video_camera;
    
    private ImageView manual_video_kuai;
    
    private RelativeLayout buttom_re;
    
    private RelativeLayout top_re;
    
    private RelativeLayout manual_play;
    
    private ManualBean manualBean;
    
    private Playutil playutil;
    
    private int TYPE = 1;
    
    private List<WMDeviceInfo> mlist_device;
    
    private int type;
    
    private int playPosition;
    
    private boolean isPlaying;
    
    private int playTime;
    
    private Timer timer;
    
    private LinearLayout control_stream;
    
    private boolean isShow;
    
    private TextView stream_type_sub;
    
    private TextView stream_type_main;
    
    private int factoryid = 0;
    
    private boolean isOnStop;
    
    private boolean uiIsHidden;
    
    // private ShareUtil shareUtil;
    
    private boolean isFinish = true;
    
    private boolean canFinish;
    
    private ScreenListener screenListener;
    
    private int subFactoryid, devceid;
    
    private Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 10:
                    isPlaying = true;
                    manual_video_control.setImageResource(R.drawable.icon_pause);
                    vedio_progressbar.setVisibility(View.GONE);
                    timer = new Timer();
                    timer.schedule(new MyPlayTimeTask(), 1000, 1000);
                    if ((playPosition + 1) == manualBean.getDevList().size())
                    {
                        if (manualBean.getMode() == 1)
                        {
                            playPosition = 0;
                        }
                        else
                        {
                            playPosition++;
                        }
                    }
                    else
                    {
                        
                        playPosition++;
                    }
                    break;
                case 20:
                    Toast.makeText(ManualPlayActivity.this, "打开失败!", Toast.LENGTH_SHORT).show();
                    if ((playPosition + 1) == manualBean.getDevList().size())
                    {
                        if (manualBean.getMode() == 1)
                        {
                            playPosition = 0;
                        }
                        else
                        {
                            finish();
                        }
                    }
                    else
                    {
                        playPosition++;
                    }
                    isPlaying = true;
                    manual_video_control.setImageResource(R.drawable.icon_play);
                    vedio_progressbar.setVisibility(View.GONE);
                    if (timer != null)
                    {
                        timer.cancel();
                    }
                    playNextVideo();
                    break;
                case 30:
                    playTime = (Integer)msg.obj;
                    count_down.setText(playTime + "s");
                    if (playTime == 0)
                    {
                        isPlaying = false;
                        if (timer != null)
                        {
                            timer.cancel();
                        }
                        if (playPosition == manualBean.getDevList().size())
                        {
                            if (manualBean.getMode() == 1)
                            {
                                vedio_progressbar.setVisibility(View.VISIBLE);
                                playNextVideo();
                            }
                            else
                            {
                                playutil.stopIng();
                                finish();
                            }
                        }
                        else
                        {
                            vedio_progressbar.setVisibility(View.VISIBLE);
                            playNextVideo();
                        }
                    }
                    break;
                default:
                    break;
            }
        };
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_video);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        CommonUtil.setFullScreen(ManualPlayActivity.this);
        initView();
        initListener();
        initData();
        
    }
    
    private void initData()
    {
        playutil = new Playutil(this);
        // 循环的方案
        manualBean = (ManualBean)getIntent().getSerializableExtra("manualdetail");
        // 播放的时长
        count_all.setText(manualBean.getLength() + "s");
        count_down.setText(manualBean.getLength() + "s");
        mlist_device = new ArrayList<WMDeviceInfo>();
        // 获取播放列表
        WMClientSdk.getInstance().getDeviceList(mlist_device, false);
        // 当前播放的进度
        playTime = manualBean.getLength();
        // 获取播放的类型
        if (mlist_device != null && mlist_device.size() != 0 && manualBean.getDevList() != null && manualBean.getDevList().size() != 0)
        {
            for (int i = 0; i < mlist_device.size(); i++)
            {
                if (mlist_device.get(i).getDevId() == manualBean.getDevList().get(0).getVeyeid())
                {
                    type = mlist_device.get(i).getDevType();
                }
            }
        }
        else
        {
            Toast.makeText(ManualPlayActivity.this, "播放失败!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // 判断是否播放成功
        playutil.addPlayListener(new PlayListener()
        {
            @Override
            public void onPlaySuccess()
            {
                handler.sendEmptyMessage(10);
                canFinish = true;
            }
            
            @Override
            public void onPlayFailed()
            {
                handler.sendEmptyMessage(20);
                canFinish = true;
            }
            
            @Override
            public void onPlayConnectTimeOut()
            {
                canFinish = true;
            }
        });
        // 播放视频
        manual_play.postDelayed(new Runnable()
        {
            public void run()
            {
                vedio_progressbar.setVisibility(View.VISIBLE);
                subFactoryid = manualBean.getDevList().get(playPosition).getSubfactoryid();
                devceid = manualBean.getDevList().get(playPosition).getDeviceid();
                playutil.getStreamPlayer(type, manual_play, manualBean.getDevList().get(playPosition).getVeyeid(), manualBean.getDevList().get(playPosition).getVeyechannel(), TYPE);
                canFinish = false;
            }
        }, 200);
        showTopDev();
    }
    
    /**
     * 显示当前播放的设备以及下一个即将播放的设备
     */
    private void showTopDev()
    {
        if (playPosition <= (manualBean.getDevList().size() - 1))
        {
            // 当前播放的设备
            play_device_name.setText(getPlayDevice(manualBean.getDevList().get(playPosition)));
            // 下一个播放的设备
            if ((manualBean.getDevList().size() - 1) >= (playPosition + 1))
            {
                play_next_device_name.setText("即将:" + getPlayDevice(manualBean.getDevList().get(playPosition + 1)));
            }
            else
            {
                if (manualBean.getMode() == 1)
                {
                    play_next_device_name.setText("即将:" + getPlayDevice(manualBean.getDevList().get(0)));
                }
                else
                {
                    play_next_device_name.setText("");
                }
            }
        }
    }
    
    /**
     * 播放下一个设备
     */
    private void playNextVideo()
    {
        manual_video_control.setImageResource(R.drawable.icon_play);
        showTopDev();
        playutil.stopIng();
        playTime = manualBean.getLength();
        count_down.setText(playTime + "s");
        if (playPosition <= (manualBean.getDevList().size() - 1))
        {
            for (int i = 0; i < mlist_device.size(); i++)
            {
                if (mlist_device.get(i).getDevId() == manualBean.getDevList().get(playPosition).getVeyeid())
                {
                    type = mlist_device.get(i).getDevType();
                }
            }
            manual_play.postDelayed(new Runnable()
            {
                public void run()
                {
                    vedio_progressbar.setVisibility(View.VISIBLE);
                    subFactoryid = manualBean.getDevList().get(playPosition).getSubfactoryid();
                    devceid = manualBean.getDevList().get(playPosition).getDeviceid();
                    playutil.getStreamPlayer(type, manual_play, manualBean.getDevList().get(playPosition).getVeyeid(), manualBean.getDevList().get(playPosition).getVeyechannel(), TYPE);
                    canFinish = false;
                }
            }, 200);
        }
    }
    
    /**
     * 获取播放设备的名称
     * 
     * @param deviceInfo
     * @return
     */
    private String getPlayDevice(DeviceInfo deviceInfo)
    {
        ACache aCache = ACache.get(this);
        FactoryBean factoryBean = (FactoryBean)aCache.getAsObject("factoryBean");
        String devName = deviceInfo.getDevicename();
        String subName = "";
        String fatoryName = "";
        
        if (factoryBean.getSubfactorys() != null && factoryBean.getSubfactorys().size() != 0)
        {
            for (int i = 0; i < factoryBean.getSubfactorys().size(); i++)
            {
                if (factoryBean.getSubfactorys().get(i).getSubfactoryid() == deviceInfo.getSubfactoryid())
                {
                    subName = factoryBean.getSubfactorys().get(i).getSubfactoryname();
                    factoryid = factoryBean.getSubfactorys().get(i).getOwnerfactoryid();
                    break;
                }
            }
        }
        
        if (factoryBean.getFactorys() != null && factoryBean.getFactorys().size() != 0)
        {
            for (int i = 0; i < factoryBean.getFactorys().size(); i++)
            {
                if (factoryBean.getFactorys().get(i).getFactoryid() == factoryid)
                {
                    fatoryName = factoryBean.getFactorys().get(i).getFactoryname();
                    break;
                }
            }
        }
        return fatoryName + "/" + subName + "/" + devName;
    }
    
    private void initListener()
    {
        manual_video_back.setOnClickListener(this);
        manual_video_control.setOnClickListener(this);
        manual_video_setting.setOnClickListener(this);
        manual_video_camera.setOnClickListener(this);
        manual_video_kuai.setOnClickListener(this);
        manual_play.setOnClickListener(this);
    }
    
    private void initView()
    {
        manual_video_back = (ImageView)findViewById(R.id.manual_video_back);
        play_device_name = (TextView)findViewById(R.id.play_device_name);
        play_next_device_name = (TextView)findViewById(R.id.play_next_device_name);
        manual_video_control = (ImageView)findViewById(R.id.manual_video_control);
        count_down = (TextView)findViewById(R.id.count_down);
        count_all = (TextView)findViewById(R.id.count_all);
        manual_video_setting = (TextView)findViewById(R.id.manual_video_setting);
        manual_video_camera = (ImageView)findViewById(R.id.manual_video_camera);
        manual_video_kuai = (ImageView)findViewById(R.id.manual_video_kuai);
        buttom_re = (RelativeLayout)findViewById(R.id.buttom_re);
        top_re = (RelativeLayout)findViewById(R.id.top_re);
        manual_play = (RelativeLayout)findViewById(R.id.manual_play);
        vedio_progressbar = (ProgressBar)findViewById(R.id.vedio_progressbar);
        control_stream = (LinearLayout)findViewById(R.id.control_stream);
        stream_type_sub = (TextView)findViewById(R.id.stream_type_sub);
        stream_type_main = (TextView)findViewById(R.id.stream_type_main);
        screenListener = new ScreenListener(this);
        screenListener.begin(new ScreenStateListener()
        {
            
            @Override
            public void onUserPresent()
            {
            }
            
            @Override
            public void onScreenOn()
            {
            }
            
            @Override
            public void onScreenOff()
            {
                Log.e("insert", "onScreenOff()");
                playutil.stopIng();
                if (timer != null)
                {
                    timer.cancel();
                }
                finish();
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        if (canFinish)
            switch (v.getId())
            {
                case R.id.manual_video_back:
                    playutil.stopIng();
                    if (timer != null)
                    {
                        timer.cancel();
                    }
                    finish();
                    break;
                case R.id.manual_video_control:
                    if (isPlaying)
                    {
                        playutil.stopIng();
                        if (timer != null)
                        {
                            timer.cancel();
                        }
                        manual_video_control.setImageResource(R.drawable.icon_play);
                    }
                    else
                    {
                        playPosition = playPosition - 1;
                        manual_video_control.setImageResource(R.drawable.icon_pause);
                        manual_play.postDelayed(new Runnable()
                        {
                            public void run()
                            {
                                vedio_progressbar.setVisibility(View.VISIBLE);
                                if (playPosition == -1)
                                {
                                    playPosition = manualBean.getDevList().size() - 1;
                                }
                                subFactoryid = manualBean.getDevList().get(playPosition).getSubfactoryid();
                                devceid = manualBean.getDevList().get(playPosition).getDeviceid();
                                playutil.getStreamPlayer(type, manual_play, manualBean.getDevList().get(playPosition).getVeyeid(), manualBean.getDevList().get(playPosition).getVeyechannel(), TYPE);
                                canFinish = false;
                            }
                        }, 200);
                    }
                    isPlaying = !isPlaying;
                    break;
                case R.id.manual_video_setting:
                    addPopupWindow();
                    break;
                case R.id.manual_video_camera:
                    String onename = playutil.saveSnapshot(factoryid, subFactoryid, devceid);
                    if (onename != null)
                    {
                        Toast.makeText(ManualPlayActivity.this, "抓拍成功！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.manual_video_kuai:
                    // 快拍
                    String name = playutil.saveSnapshot(factoryid, subFactoryid, devceid);
                    if (name != null)
                    {
                        String string = new String(name);
                        String[] info = string.split("-");
                        PicTimeInfo picTimeInfo = new PicTimeInfo();
                        picTimeInfo.setDeviceid(Integer.parseInt(info[3]));
                        picTimeInfo.setSubfactoryid(Integer.parseInt(info[2]));
                        picTimeInfo.setFactoryid(Integer.parseInt(info[1]));
                        picTimeInfo.setPath(playutil.getPath());
                        picTimeInfo.setTime(info[0]);
                        picTimeInfo.setName(name + ".jpg");
                        Intent intent = new Intent(ManualPlayActivity.this, CreateProblemActivity.class);
                        intent.putExtra("picinfo", picTimeInfo);
                        startActivity(intent);
                        isFinish = false;
                    }
                    break;
                case R.id.manual_play:
                    if (uiIsHidden)
                    {
                        top_re.setVisibility(View.VISIBLE);
                        buttom_re.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        top_re.setVisibility(View.GONE);
                        buttom_re.setVisibility(View.GONE);
                        control_stream.setVisibility(View.GONE);
                    }
                    isShow = false;
                    uiIsHidden = !uiIsHidden;
                    break;
                default:
                    break;
            }
    }
    
    @Override
    protected void onRestart()
    {
        super.onRestart();
        isFinish = true;
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        if (!isFinish)
        {
            isOnStop = true;
            canFinish = false;
            playutil.stopIng();
            if (timer != null)
            {
                timer.cancel();
            }
            playPosition = playPosition - 1;
            manual_video_control.setImageResource(R.drawable.icon_play);
        }
        else
        {
            playutil.stopIng();
            if (timer != null)
            {
                timer.cancel();
            }
            finish();
        }
        
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        if (isOnStop)
        {
            vedio_progressbar.postDelayed(new Runnable()
            {
                public void run()
                {
                    canFinish = false;
                    vedio_progressbar.setVisibility(View.VISIBLE);
                    if (playPosition == -1)
                    {
                        playPosition = manualBean.getDevList().size() - 1;
                    }
                    subFactoryid = manualBean.getDevList().get(playPosition).getSubfactoryid();
                    devceid = manualBean.getDevList().get(playPosition).getDeviceid();
                    playutil.getStreamPlayer(type, manual_play, manualBean.getDevList().get(playPosition).getVeyeid(), manualBean.getDevList().get(playPosition).getVeyechannel(), TYPE);
                }
            }, 300);
            isOnStop = false;
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        screenListener.unregisterListener();
    }
    
    /**
     * 标清和高清的切换
     */
    private void addPopupWindow()
    {
        stream_type_sub.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                playPosition = playPosition - 1;
                playutil.stopIng();
                if (timer != null)
                {
                    timer.cancel();
                }
                manual_video_control.setImageResource(R.drawable.icon_play);
                vedio_progressbar.setVisibility(View.VISIBLE);
                TYPE = 1;
                vedio_progressbar.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        canFinish = false;
                        if (playPosition == -1)
                        {
                            playPosition = manualBean.getDevList().size() - 1;
                        }
                        subFactoryid = manualBean.getDevList().get(playPosition).getSubfactoryid();
                        devceid = manualBean.getDevList().get(playPosition).getDeviceid();
                        playutil.getStreamPlayer(type, manual_play, manualBean.getDevList().get(playPosition).getVeyeid(), manualBean.getDevList().get(playPosition).getVeyechannel(), TYPE);
                    }
                }, 200);
                manual_video_setting.setText("标清");
                control_stream.setVisibility(View.GONE);
            }
        });
        stream_type_main.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                playPosition = playPosition - 1;
                playutil.stopIng();
                if (timer != null)
                {
                    timer.cancel();
                }
                manual_video_control.setImageResource(R.drawable.icon_play);
                vedio_progressbar.setVisibility(View.VISIBLE);
                TYPE = 0;
                vedio_progressbar.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        canFinish = false;
                        if (playPosition == -1)
                        {
                            playPosition = manualBean.getDevList().size() - 1;
                        }
                        subFactoryid = manualBean.getDevList().get(playPosition).getSubfactoryid();
                        devceid = manualBean.getDevList().get(playPosition).getDeviceid();
                        playutil.getStreamPlayer(type, manual_play, manualBean.getDevList().get(playPosition).getVeyeid(), manualBean.getDevList().get(playPosition).getVeyechannel(), TYPE);
                    }
                }, 200);
                manual_video_setting.setText("高清");
                control_stream.setVisibility(View.GONE);
            }
        });
        if (isShow)
        {
            control_stream.setVisibility(View.GONE);
        }
        else
        {
            control_stream.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            int
            
            width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            
            int
            
            height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            
            control_stream.measure(width, height);
            int control_stream_height = control_stream.getMeasuredHeight();
            params.setMargins(manual_video_setting.getLeft(), buttom_re.getTop() - control_stream_height, 0, 0);
            control_stream.setGravity(Gravity.BOTTOM);
            control_stream.setLayoutParams(params);
        }
        isShow = !isShow;
    }
    
    class MyPlayTimeTask extends TimerTask
    {
        @Override
        public void run()
        {
            playTime--;
            Message message = new Message();
            message.what = 30;
            message.obj = playTime;
            handler.sendMessage(message);
            
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (canFinish)
            {
                playutil.stopIng();
                finish();
            }
        }
        return true;
    }
    
}
