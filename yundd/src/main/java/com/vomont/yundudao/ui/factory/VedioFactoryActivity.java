package com.vomont.yundudao.ui.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DetalBean;
import com.vomont.yundudao.bean.DetalInfo;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.PicTimeBean;
import com.vomont.yundudao.bean.PicTimeInfo;
import com.vomont.yundudao.bean.ProblemDetailBean;
import com.vomont.yundudao.bean.ProblemDetailInfo;
import com.vomont.yundudao.bean.ProblemListBean;
import com.vomont.yundudao.bean.ProblemListlInfo;
import com.vomont.yundudao.bean.ProblemTypeBean;
import com.vomont.yundudao.bean.ProblemTypeInfo;
import com.vomont.yundudao.mvpview.detal.IDetalView;
import com.vomont.yundudao.mvpview.problem.ICenterProblemView;
import com.vomont.yundudao.presenter.detal.DetalPresenter;
import com.vomont.yundudao.presenter.problem.ICenterProPresenter;
import com.vomont.yundudao.ui.createproblem.CenterProblemActivity;
import com.vomont.yundudao.ui.createproblem.CreateProblemActivity;
import com.vomont.yundudao.ui.createproblem.ProblemDetailActivity;
import com.vomont.yundudao.ui.factory.adapter.VedioProblemAdapter;
import com.vomont.yundudao.ui.pic.AllPicActivity;
import com.vomont.yundudao.ui.pic.PicCenterActivity;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.CommonUtil;
import com.vomont.yundudao.utils.DensityUtil;
import com.vomont.yundudao.utils.ImageUtils;
import com.vomont.yundudao.utils.Playutil;
import com.vomont.yundudao.utils.Playutil.PlayListener;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.BaseActivity;
import com.wmclient.clientsdk.Constants;
import com.wmclient.clientsdk.WMDeviceInfo;

@SuppressLint({"HandlerLeak", "CutPasteId", "SimpleDateFormat", "ClickableViewAccessibility"})
public class VedioFactoryActivity extends BaseActivity implements OnClickListener, ICenterProblemView, IDetalView, OnTouchListener
{
    
    protected static final float FLIP_DISTANCE = 50;
    
    // 标题布局
    private RelativeLayout vedio_top;
    
    private LinearLayout vedio_phone, vedio_playback;
    
    private LinearLayout phone_img;
    
    private ImageView vedio_change;
    
    private LinearLayout scroll_manager;
    
    // 图片的路径
    private List<String> mlist_name;
    
    private TextView top_name;
    
    // 返回 全屏的按钮
    private ImageView go_back;
    
    private RelativeLayout vedio_all;
    
    // surfaceview的容器
    private RelativeLayout surfaceview_layout;
    
    // 播放的接口
    private Playutil playutil;
    
    private Intent intent;
    
    private Context context;
    
    private List<WMDeviceInfo> mlist_device;
    
    private List<FactoryInfo> mlistBean;
    
    private int deviceid, channalid, type, veyeid;
    
    // 判断是否是横屏
    private boolean isLandscape = false;
    
    private int factoryid;
    
    private int subfactoryid;
    
    private int width, patrol_re_video_height;
    
    private ImageView vedio_to_pic;
    
    private ProgressBar vedio_progressbar;
    
    private TextView empty_img;
    
    private boolean isPlaying;
    
    private ImageView vedio_control_play;
    
    private ImageView vedio_pic_create;
    
    private ImageView vedio_yuntai;
    
    private static int STREAM_TYPE = 1;
    
    private TextView stream_type;
    
    private ImageView vedio_take_photo, vedio_snapshot;
    
    private ShareUtil shareUtil;
    
    private ICenterProPresenter iCenterProPresenter;
    
    private TextView empty_problem;
    
    private ListView vedio_problem_list;
    
    private Dialog dialog;
    
    private List<ProblemTypeInfo> prblem_type_mlist;
    
    private List<ProblemListlInfo> problem_list;
    
    private DetalPresenter detalPresenter;
    
    private int intentPostion;
    
    private List<DetalInfo> man_List;
    
    private ProblemDetailInfo problemDetailInfo;
    
    private RelativeLayout play_statue;
    
    private ImageView vedio_problem;
    
    private LinearLayout control_stream;
    
    private boolean isShow;
    
    private TextView stream_type_sub;
    
    private TextView stream_type_main;
    
    private String factoryname;
    
    private Toast toast;
    
    private RelativeLayout play_statue_line;
    
    private boolean controlIsShow;
    
    private int m;
    
    private List<String> nameList;
    
    private boolean reStartPlay;
    
    private boolean canClick;
    
    private ImageView finish_yuntai;
    
    private RelativeLayout rl_yuntai;
    
    private ImageView contrl_yuntai;
    
    private GestureDetector mGestureDetector;
    
    private int ptz;
    
    int ptz_control = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_factory);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        nameList = new ArrayList<String>();
        InitView();
        initListener();
        initData();
    }
    
    @SuppressWarnings("unchecked")
    private void initData()
    {
        controlIsShow = true;
        playutil = new Playutil(this);
        playutil.addPlayListener(new PlayListener()
        {
            @Override
            public void onPlaySuccess()
            {
                handler.sendEmptyMessage(20);
                canClick = true;
            }
            
            @Override
            public void onPlayFailed()
            {
                handler.sendEmptyMessage(30);
                canClick = true;
            }
            
            @Override
            public void onPlayConnectTimeOut()
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        playutil.stopIng();
                        isPlaying = false;
                        vedio_control_play.setImageResource(R.drawable.icon_play);
                        vedio_progressbar.setVisibility(View.GONE);
                        toast = Toast.makeText(context, "连接超时!", Toast.LENGTH_SHORT);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, DensityUtil.dip2px(context, 50) / 2 + patrol_re_video_height / 2);
                        toast.show();
                        canClick = true;
                    }
                });
            }
        });
        intent = getIntent();
        factoryname = intent.getStringExtra("factoryname");
        veyeid = intent.getIntExtra("veyeid", -1);
        factoryid = intent.getIntExtra("factoryid", -1);
        subfactoryid = intent.getIntExtra("subfactoryid", -1);
        deviceid = intent.getIntExtra("deviceid", -1);
        channalid = intent.getIntExtra("channalid", -1);
        ptz = intent.getIntExtra("ptz", 0);
        mlistBean = (List<FactoryInfo>)intent.getSerializableExtra("factoryBean");
        top_name.setText(factoryname + "-" + intent.getStringExtra("name"));
        mlist_device = new ArrayList<WMDeviceInfo>();
        initHandler();
        getViewwidth();
        shareUtil = new ShareUtil(this);
        iCenterProPresenter = new ICenterProPresenter(this);
    }
    
//    private void getProblemList()
//    {
//        iCenterProPresenter.getProblemType(shareUtil.getShare().getUser_id() + "");
//    }
    
    private void initListener()
    {
        vedio_snapshot.setOnClickListener(this);
        vedio_take_photo.setOnClickListener(this);
        stream_type.setOnClickListener(this);
        vedio_pic_create.setOnClickListener(this);
        vedio_to_pic.setOnClickListener(this);
        vedio_change.setOnClickListener(this);
        vedio_phone.setOnClickListener(this);
        go_back.setOnClickListener(this);
        vedio_playback.setOnClickListener(this);
        vedio_control_play.setOnClickListener(this);
        vedio_problem.setOnClickListener(this);
        surfaceview_layout.setOnClickListener(this);
        vedio_yuntai.setOnClickListener(this);
        finish_yuntai.setOnClickListener(this);
        vedio_problem_list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (canClick)
                {
                    detalPresenter = new DetalPresenter(VedioFactoryActivity.this);
                    detalPresenter.getDetal(shareUtil.getShare().getUser_id() + "");
                    dialog = ProgressDialog.createLoadingDialog(VedioFactoryActivity.this, "");
                    dialog.show();
                    intentPostion = position;
                }
            }
        });
    }
    
    /*
     * 初始化标签名
     */
    @SuppressWarnings("deprecation")
    public void InitView()
    {
        top_name = (TextView)findViewById(R.id.top_name);
        go_back = (ImageView)findViewById(R.id.go_back);
        surfaceview_layout = (RelativeLayout)findViewById(R.id.surfaceview_layout);
        vedio_top = (RelativeLayout)findViewById(R.id.vedio_top);
        vedio_phone = (LinearLayout)findViewById(R.id.vedio_phone);
        vedio_playback = (LinearLayout)findViewById(R.id.vedio_playback);
        phone_img = (LinearLayout)findViewById(R.id.phone_img);
        vedio_change = (ImageView)findViewById(R.id.vedio_change);
        scroll_manager = (LinearLayout)findViewById(R.id.scroll_manager);
        vedio_to_pic = (ImageView)findViewById(R.id.vedio_to_pic);
        vedio_all = (RelativeLayout)findViewById(R.id.vedio_all);
        vedio_progressbar = (ProgressBar)findViewById(R.id.vedio_progressbar);
        empty_img = (TextView)findViewById(R.id.empty_img);
        vedio_control_play = (ImageView)findViewById(R.id.vedio_control_play);
        vedio_pic_create = (ImageView)findViewById(R.id.vedio_pic_create);
        stream_type = (TextView)findViewById(R.id.stream_type);
        vedio_take_photo = (ImageView)findViewById(R.id.vedio_take_photo);
        vedio_snapshot = (ImageView)findViewById(R.id.vedio_snapshot);
        empty_problem = (TextView)findViewById(R.id.empty_problem);
        vedio_problem_list = (ListView)findViewById(R.id.vedio_problem_list);
        vedio_problem = (ImageView)findViewById(R.id.vedio_problem);
        play_statue = (RelativeLayout)findViewById(R.id.play_statue_line);
        control_stream = (LinearLayout)findViewById(R.id.control_stream);
        stream_type_sub = (TextView)findViewById(R.id.stream_type_sub);
        stream_type_main = (TextView)findViewById(R.id.stream_type_main);
        play_statue_line = (RelativeLayout)findViewById(R.id.play_statue_line);
        vedio_yuntai = (ImageView)findViewById(R.id.vedio_yuntai);
        finish_yuntai = (ImageView)findViewById(R.id.finish_yuntai);
        rl_yuntai = (RelativeLayout)findViewById(R.id.rl_yuntai);
        contrl_yuntai = (ImageView)findViewById(R.id.contrl_yuntai);
        contrl_yuntai.setOnTouchListener(this);
        contrl_yuntai.setFocusable(true);
        contrl_yuntai.setClickable(true);
        contrl_yuntai.setLongClickable(true);
        mGestureDetector = new GestureDetector(new GestureListener()); // 使用派生自
    }
    
    @SuppressWarnings("unchecked")
    private void initHandler()
    {
        ACache aCache = ACache.get(this);
        mlist_device.addAll((List<WMDeviceInfo>)aCache.getAsObject("deviceinfo"));
        for (int i = 0; i < mlist_device.size(); i++)
        {
            if (mlist_device.get(i).getDevId() == veyeid)
            {
                type = mlist_device.get(i).getDevType();
            }
        }
        canClick = false;
        play_statue_line.postDelayed(new Runnable()
        {
            public void run()
            {
                playutil.getStreamPlayer(type, surfaceview_layout, veyeid, channalid, STREAM_TYPE);
            }
        }, 400);
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        playutil.stopIng();
        if (toast != null)
        {
            toast.cancel();
        }
        canClick = false;
        reStartPlay = true;
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        STREAM_TYPE = 1;
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        showView();
        // scroll_manager.smoothScrollTo(0, 0);
        if (reStartPlay)
        {
            playutil.getStreamPlayer(type, surfaceview_layout, veyeid, channalid, STREAM_TYPE);
            reStartPlay = false;
        }
    }
    
    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 10:
                    canClick = false;
                    playutil.getStreamPlayer(type, surfaceview_layout, veyeid, channalid, STREAM_TYPE);
                    break;
                case 20:
                    isPlaying = true;
                    vedio_control_play.setImageResource(R.drawable.icon_pause);
                    vedio_progressbar.setVisibility(View.GONE);
                    break;
                case 30:
                    playutil.stopIng();
                    isPlaying = false;
                    vedio_control_play.setImageResource(R.drawable.icon_play);
                    vedio_progressbar.setVisibility(View.GONE);
                    toast = Toast.makeText(context, "打开失败!", Toast.LENGTH_LONG);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, DensityUtil.dip2px(context, 50) / 2 + patrol_re_video_height / 2);
                    toast.show();
                    break;
                default:
                    break;
            }
        };
    };
    
    private void showView()
    {
        vedio_progressbar.setProgress(100);
        vedio_control_play.setImageResource(R.drawable.icon_play);
        vedio_progressbar.setVisibility(View.VISIBLE);
        addPhoneToView();
        // getProblemList();
        vedio_progressbar.setVisibility(View.VISIBLE);
        
    }
    
    @Override
    public void onClick(View v)
    {
        if (canClick)
        {
            switch (v.getId())
            {
                case R.id.go_back:
                    // 返回
                    playutil.stopIng();
                    finish();
                    break;
                case R.id.vedio_take_photo:
                    // 抓拍
                    String onename = playutil.saveSnapshot(factoryid, subfactoryid, deviceid);
                    if (onename != null)
                    {
                        Toast.makeText(context, "抓拍成功！", Toast.LENGTH_SHORT).show();
                    }
                    addPhoneToView();
                    break;
                case R.id.vedio_phone:
                    // 抓拍
                    String nextname = playutil.saveSnapshot(factoryid, subfactoryid, deviceid);
                    if (nextname != null)
                    {
                        Toast.makeText(context, "抓拍成功！", Toast.LENGTH_SHORT).show();
                    }
                    addPhoneToView();
                    break;
                case R.id.vedio_change:
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
                case R.id.vedio_playback:
                    // 回放
                    intent = new Intent(VedioFactoryActivity.this, PlayBackActivity.class);
                    
                    intent.putExtra("factoryid", factoryid);
                    intent.putExtra("subfactoryid", subfactoryid);
                    intent.putExtra("deviceid", deviceid);
                    intent.putExtra("topname", top_name.getText().toString());
                    intent.putExtra("deviceId", veyeid);
                    intent.putExtra("devChannelId", channalid);
                    intent.putExtra("type", type);
                    startActivity(intent);
                    break;
                case R.id.vedio_to_pic:
                    // 所有图片
                    intent = new Intent(VedioFactoryActivity.this, AllPicActivity.class);
                    intent.putExtra("factoryBean", (Serializable)mlistBean);
                    startActivity(intent);
                    break;
                case R.id.vedio_control_play:
                    // 是否暂停
                    if (isPlaying)
                    {
                        vedio_control_play.setImageResource(R.drawable.icon_play);
                        playutil.stopIng();
                    }
                    else
                    {
                        vedio_progressbar.setVisibility(View.VISIBLE);
                        vedio_control_play.setImageResource(R.drawable.icon_pause);
                        // showView();
                        canClick = false;
                        playutil.getStreamPlayer(type, surfaceview_layout, veyeid, channalid, STREAM_TYPE);
                    }
                    isPlaying = !isPlaying;
                    break;
                case R.id.vedio_snapshot:
                    // 快拍
                    String name = playutil.saveSnapshot(factoryid, subfactoryid, deviceid);
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
                        Intent intent = new Intent(VedioFactoryActivity.this, CreateProblemActivity.class);
                        intent.putExtra("picinfo", picTimeInfo);
                        startActivity(intent);
                    }
                    break;
                case R.id.vedio_pic_create:
                    // 快拍
                    String namenext = playutil.saveSnapshot(factoryid, subfactoryid, deviceid);
                    if (namenext != null)
                    {
                        String string = new String(namenext);
                        String[] info = string.split("-");
                        PicTimeInfo picTimeInfo = new PicTimeInfo();
                        picTimeInfo.setDeviceid(Integer.parseInt(info[3]));
                        picTimeInfo.setSubfactoryid(Integer.parseInt(info[2]));
                        picTimeInfo.setFactoryid(Integer.parseInt(info[1]));
                        picTimeInfo.setPath(playutil.getPath());
                        picTimeInfo.setTime(info[0]);
                        picTimeInfo.setName(namenext + ".jpg");
                        Intent intent = new Intent(VedioFactoryActivity.this, CreateProblemActivity.class);
                        intent.putExtra("picinfo", picTimeInfo);
                        startActivity(intent);
                    }
                    break;
                case R.id.stream_type:
                    // 标清和高清
                    addPopupWindow();
                    break;
                case R.id.vedio_problem:
                    Intent intent = new Intent(this, CenterProblemActivity.class);
                    intent.putExtra("factoryBean", (Serializable)mlistBean);
                    startActivity(intent);
                    break;
                case R.id.surfaceview_layout:
                    if (controlIsShow)
                    {
                        play_statue_line.setVisibility(View.GONE);
                        control_stream.setVisibility(View.GONE);
                        isShow = false;
                    }
                    else
                    {
                        play_statue_line.setVisibility(View.VISIBLE);
                    }
                    controlIsShow = !controlIsShow;
                    break;
                case R.id.vedio_yuntai:
                    // 云台功能
                    if (ptz == 1)
                    {
                        scroll_manager.setVisibility(View.GONE);
                        rl_yuntai.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        Toast.makeText(context, "该设备不支持云台", Toast.LENGTH_SHORT).show();
                    }
                    
                    break;
                case R.id.finish_yuntai:
                    scroll_manager.setVisibility(View.VISIBLE);
                    rl_yuntai.setVisibility(View.GONE);
                    // scroll_manager.smoothScrollTo(0, 0);
                    break;
                default:
                    break;
            }
        }
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
                vedio_progressbar.setVisibility(View.VISIBLE);
                playutil.stopIng();
                STREAM_TYPE = 1;
                vedio_progressbar.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        canClick = false;
                        playutil.getStreamPlayer(type, surfaceview_layout, veyeid, channalid, STREAM_TYPE);
                    }
                }, 300);
                
                stream_type.setText("标清");
                control_stream.setVisibility(View.GONE);
            }
        });
        stream_type_main.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                vedio_progressbar.setVisibility(View.VISIBLE);
                playutil.stopIng();
                STREAM_TYPE = 0;
                vedio_progressbar.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        canClick = false;
                        playutil.getStreamPlayer(type, surfaceview_layout, veyeid, channalid, STREAM_TYPE);
                    }
                }, 300);
                stream_type.setText("高清");
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
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            
            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            
            control_stream.measure(width, height);
            
            int control_stream_height = control_stream.getMeasuredHeight();
            
            params.setMargins(stream_type.getLeft(), play_statue.getTop() - control_stream_height, 0, 0);
            control_stream.setGravity(Gravity.BOTTOM);
            control_stream.setLayoutParams(params);
        }
        isShow = !isShow;
    }
    
    /**
     * 横竖屏切换的监听
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        isLandscape = !isLandscape;
        try
        {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                // land
                rl_yuntai.setVisibility(View.GONE);
                vedio_top.setVisibility(View.GONE);
                scroll_manager.setVisibility(View.GONE);
                CommonUtil.setFullScreen(VedioFactoryActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width);
                vedio_all.setLayoutParams(params);
            }
            else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                // port
                CommonUtil.cancelFullScreen(VedioFactoryActivity.this);
                vedio_top.setVisibility(View.VISIBLE);
                scroll_manager.setVisibility(View.VISIBLE);
                rl_yuntai.setVisibility(View.GONE);
                // scroll_manager.smoothScrollTo(0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, patrol_re_video_height);
                vedio_all.setLayoutParams(params);
            }
        }
        catch (Exception ex)
        {
        }
    }
    
    private void backPicInfo(int index)
    {
        if (playutil.getImageName().size() >= (index + 1))
        {
            String name = playutil.getImageName().get(index);
            String newname = name.substring(0, name.length() - 4);
            System.out.println(newname);
            String string = new String(newname);
            String[] info = string.split("-");
            PicTimeInfo picTimeInfo = new PicTimeInfo();
            picTimeInfo.setDeviceid(Integer.parseInt(info[3]));
            picTimeInfo.setSubfactoryid(Integer.parseInt(info[2]));
            picTimeInfo.setFactoryid(Integer.parseInt(info[1]));
            picTimeInfo.setPath(playutil.getPath());
            picTimeInfo.setTime(info[0]);
            picTimeInfo.setName(name);
            PicTimeBean timeBean = new PicTimeBean();
            List<PicTimeInfo> info_mlist = new ArrayList<PicTimeInfo>();
            info_mlist.add(picTimeInfo);
            timeBean.setPaths(info_mlist);
            Intent intent = new Intent(this, PicCenterActivity.class);
            intent.putExtra("picinfo", timeBean);
            intent.putExtra("postion", 0);
            startActivity(intent);
        }
    }
    
    /**
     * 显示图片列表
     */
    public void addPhoneToView()
    {
        if (playutil.getImageName() != null && playutil.getImageName().size() != 0)
        {
            mlist_name = playutil.getImageName();
            Collections.reverse(mlist_name);
            empty_img.setVisibility(View.GONE);
        }
        else
        {
            empty_img.setVisibility(View.VISIBLE);
            mlist_name = new ArrayList<String>();
            return;
        }
        phone_img.removeAllViews();
        List<ImageView> list_img = new ArrayList<ImageView>();
        if (mlist_name.size() != 0)
        {
            int view_width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int view_height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            phone_img.measure(view_width, view_height);
            for (int i = 0; i < 4; i++)
            {
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ScaleType.FIT_XY);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((width - 60) / 4, (width - 60) / 4);
                if (i != 3)
                {
                    layoutParams.setMargins(15, 0, 0, 0);
                }
                else
                {
                    layoutParams.setMargins(10, 0, 0, 15);
                }
                
                imageView.setLayoutParams(layoutParams);
                list_img.add(imageView);
                phone_img.addView(imageView);
            }
            phone_img.getChildAt(0).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (canClick)
                    {
                        backPicInfo(0);
                    }
                }
            });
            phone_img.getChildAt(1).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (canClick)
                    {
                        
                        backPicInfo(1);
                    }
                }
            });
            phone_img.getChildAt(2).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (canClick)
                    {
                        backPicInfo(2);
                    }
                }
            });
            phone_img.getChildAt(3).setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    if (canClick)
                    {
                        backPicInfo(3);
                    }
                }
            });
            if (mlist_name.size() > 0)
            {
                for (int i = 0; i < mlist_name.size(); i++)
                {
                    Glide.with(VedioFactoryActivity.this).load(new File(playutil.getPath() + File.separator + mlist_name.get(mlist_name.size() - 1))).into(list_img.get(0));
                    // LoadLocalImageUtil.getInstance().displayFromSDCard(playutil.getPath() + File.separator +
                    // mlist_name.get(mlist_name.size() - 1), list_img.get(0));
                    if ((mlist_name.size() - 2) >= 0)
                    {
                        Glide.with(VedioFactoryActivity.this).load(new File(playutil.getPath() + File.separator + mlist_name.get(mlist_name.size() - 2))).into(list_img.get(1));
                        // LoadLocalImageUtil.getInstance().displayFromSDCard(playutil.getPath() + File.separator +
                        // mlist_name.get(mlist_name.size() - 2), list_img.get(1));
                    }
                    if ((mlist_name.size() - 3) >= 0)
                    {
                        Glide.with(VedioFactoryActivity.this).load(new File(playutil.getPath() + File.separator + mlist_name.get(mlist_name.size() - 3))).into(list_img.get(2));
                        // LoadLocalImageUtil.getInstance().displayFromSDCard(playutil.getPath() + File.separator +
                        // mlist_name.get(mlist_name.size() - 3), list_img.get(2));
                    }
                    if ((mlist_name.size() - 4) >= 0)
                    {
                        Glide.with(VedioFactoryActivity.this).load(new File(playutil.getPath() + File.separator + mlist_name.get(mlist_name.size() - 4))).into(list_img.get(3));
                        // LoadLocalImageUtil.getInstance().displayFromSDCard(playutil.getPath() + File.separator +
                        // mlist_name.get(mlist_name.size() - 4), list_img.get(3));
                    }
                }
            }
        }
        
    }
    
    /**
     * 加载本地图片
     * 
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url)
    {
        try
        {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    @SuppressWarnings("deprecation")
    private void getViewwidth()
    {
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        
        int view_width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        
        int view_height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        
        vedio_all.measure(view_width, view_height);
        
        patrol_re_video_height = vedio_all.getMeasuredHeight();
        Log.e("insert", "===patrol_re_video_height====" + patrol_re_video_height);
    }
    
    @Override
    public void getDetalman(DetalBean detalBean)
    {
        man_List = new ArrayList<DetalInfo>();
        if (detalBean != null && detalBean.getAccounts() != null)
        {
            man_List.addAll(detalBean.getAccounts());
        }
        else
        {
            Toast.makeText(this, "详情获取失败！", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
        iCenterProPresenter.getProblemDetail(shareUtil.getShare().getUser_id() + "", problem_list.get(intentPostion).getProblemid() + "");
    }
    
    @Override
    public void getFailed()
    {
        Toast.makeText(this, "详情获取失败！", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
    
    @Override
    public void getList(ProblemListBean bean)
    {
        problem_list = new ArrayList<ProblemListlInfo>();
        if (bean != null && bean.getProblems() != null)
        {
            empty_problem.setVisibility(View.GONE);
            vedio_problem_list.setVisibility(View.VISIBLE);
            problem_list.addAll(bean.getProblems());
            VedioProblemAdapter adapter = new VedioProblemAdapter(this, problem_list, prblem_type_mlist);
            vedio_problem_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(vedio_problem_list);
        }
        else
        {
            vedio_problem_list.setVisibility(View.GONE);
            empty_problem.setVisibility(View.VISIBLE);
        }
    }
    
    @Override
    public void getType(ProblemTypeBean result)
    {
        prblem_type_mlist = new ArrayList<ProblemTypeInfo>();
        if (result != null && result.getProblemtypes() != null)
        {
            prblem_type_mlist.addAll(result.getProblemtypes());
        }
        iCenterProPresenter.getProblemDetailList(shareUtil.getShare().getUser_id() + "", "1");
    }
    
    @Override
    public void getProbleDtail(ProblemDetailBean problemBean)
    {
        if (problemBean != null && problemBean.getResult() == 0)
        {
            problemDetailInfo = problemBean.getProblem();
            
            for (int i = 0; i < problemDetailInfo.getImagecontents().size(); i++)
            {
                asyncloadImage(problemDetailInfo.getImagecontents().get(i).getImagecontent(), i);
            }
        }
        dialog.dismiss();
    }
    
    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++)
        {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (canClick && keyCode == KeyEvent.KEYCODE_BACK)
        {
            // 横竖屏切换
            if (isLandscape)
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else
            {
                finish();
            }
        }
        return true;
    }
    
    private synchronized void asyncloadImage(final String imagecontent, final int position)
    {
        final Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                if (msg.what == 1)
                {
                    m++;
                    String name = (String)msg.obj;
                    nameList.add(name);
                    if (m == problemDetailInfo.getImagecontents().size())
                    {
                        Intent intent = new Intent(VedioFactoryActivity.this, ProblemDetailActivity.class);
                        problemDetailInfo.setImagecontents(null);
                        problemDetailInfo.setImgeNames(nameList);
                        intent.putExtra("problemDetailInfo", (Serializable)problemDetailInfo);
                        intent.putExtra("delman", (Serializable)man_List);
                        intent.putExtra("factorylist", (Serializable)mlistBean);
                        startActivity(intent);
                        m = 0;
                        nameList = new ArrayList<String>();
                        dialog.dismiss();
                    }
                }
            }
        };
        // 子线程，开启子线程去下载或者去缓存目录找图片，并且返回图片在缓存目录的地址
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String mPath = VideoManager.detail_img_cash + "/" + name + position + ".jpg";
                    File file = new File(mPath);
                    // 这个URI是图片下载到本地后的缓存目录中的URI
                    if (imagecontent != null && !file.exists())
                    {
                        ImageUtils.byte2File(Base64.decode(URLDecoder.decode(imagecontent, "utf-8"), Base64.DEFAULT), VideoManager.detail_img_cash, name + position + ".jpg");
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = name + position;
                        mHandler.sendMessage(msg);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
    
    class GestureListener implements GestureDetector.OnGestureListener
    {
        
        @Override
        public boolean onDown(MotionEvent e)
        {
            return false;
        }
        
        @Override
        public void onShowPress(MotionEvent e)
        {
            
        }
        
        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            return false;
        }
        
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            
            return false;
        }
        
        @Override
        public void onLongPress(MotionEvent e)
        {
        }
        
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            if (e1.getX() - e2.getX() > FLIP_DISTANCE)
            {
                playutil.ptzControlStart(Constants.WMPtzCommand_Left);
                ptz_control = Constants.WMPtzCommand_Left;
            }
            if (e2.getX() - e1.getX() > FLIP_DISTANCE)
            {
                playutil.ptzControlStart(Constants.WMPtzCommand_Right);
                ptz_control = Constants.WMPtzCommand_Right;
            }
            if (e1.getY() - e2.getY() > FLIP_DISTANCE)
            {
                playutil.ptzControlStart(Constants.WMPtzCommand_Up);
                ptz_control = Constants.WMPtzCommand_Up;
            }
            if (e2.getY() - e1.getY() > FLIP_DISTANCE)
            {
                playutil.ptzControlStart(Constants.WMPtzCommand_Down);
                ptz_control = Constants.WMPtzCommand_Down;
            }
            return false;
        }
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        return mGestureDetector.onTouchEvent(event);
    }
}
