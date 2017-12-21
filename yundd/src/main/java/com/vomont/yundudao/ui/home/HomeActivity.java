package com.vomont.yundudao.ui.home;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;
import com.vomont.fileloadsdk.WMFileLoadSdk;
import com.vomont.fileloadsdk.WMFileLoadSdk.EventBigFileCallback;
import com.vomont.fileloadsdk.WMFileLoadSdk.EventCallback;
import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.ui.home.fragment.FactoryFragment;
import com.vomont.yundudao.ui.home.fragment.HomeFragment;
import com.vomont.yundudao.ui.home.fragment.HomeFragment.GetSysMessage;
import com.vomont.yundudao.ui.home.fragment.MangerFragment;
import com.vomont.yundudao.ui.home.fragment.MeFragment;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoHelpter;
import com.vomont.yundudao.upload.VideoUpService;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.CashActivty;
import com.vomont.yundudao.utils.Playutil;
import com.vomont.yundudao.utils.ShareUtil;
import com.wmclient.clientsdk.WMClientSdk;

public class HomeActivity extends FragmentActivity implements OnClickListener
{
    private FragmentManager fragmentManager;
    
    private HomeFragment homeFragment;
    
    private FactoryFragment factoryFragment;
    
    private MangerFragment mangerFragment;
    
    private MeFragment meFragment;
    
    private FragmentTransaction fragmentTransaction;
    
    private RadioButton radio_msg, radio_factory, radio_manager, radio_me;
    
    private Playutil playutil;
    
    private ShareUtil shareUtil;
    
    private long exitTime = 0;
    
    private boolean needLogin = true;
    
    private int result = -1;
    
    public static String name = "";
    
    private VideoHelpter videoHelpter;
    
    @Override
    protected void onCreate(@Nullable Bundle arg0)
    {
        super.onCreate(arg0);
        setContentView(R.layout.activity_home);
        name = new ShareUtil(this).getShare().getNum();
        CashActivty.finishActivity();
        radio_msg = (RadioButton)findViewById(R.id.radio_msg);
        radio_factory = (RadioButton)findViewById(R.id.radio_factory);
        radio_manager = (RadioButton)findViewById(R.id.radio_manager);
        radio_me = (RadioButton)findViewById(R.id.radio_me);
        Appcation.getInstance().addActivity(this);
        playutil = new Playutil(this);
        shareUtil = new ShareUtil(this);
        if (shareUtil != null && shareUtil.getShare() != null && shareUtil.getShare().getVfilesvr() != null)
        {
            WMFileLoadSdk.getInstance().WM_VFile_Init(shareUtil.getShare().getVfilesvr().getVfilesvrip(),
                shareUtil.getShare().getVfilesvr().getVfilesvrport(),
                shareUtil.getShare().getAccountid(),
                "",
                this);
            WMFileLoadSdk.getInstance().setEventBigFileCallback(new EventBigFileCallback()
            {
                @Override
                public void OnSucess(final int arg0, final String arg1)
                {
                    Log.e("insert","====OnSucess=====");
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Intent intent = new Intent();
                            intent.setAction("com.vomont.load.info");
                            intent.putExtra("type", 0);
                            intent.putExtra("fileId", arg0);
                            intent.putExtra("videourl", arg1);
                            sendBroadcast(intent);
                        }
                    });
                }
                
                @Override
                public void OnProgress(final int arg0, final int arg1)
                {
                    Log.e("insert","====OnProgress=====");
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Intent intent = new Intent();
                            intent.setAction("com.vomont.load.info");
                            intent.putExtra("type", 1);
                            intent.putExtra("fileId", arg0);
                            intent.putExtra("progress", arg1);
                            sendBroadcast(intent);
                        }
                    });
                }
                
                @Override
                public void OnFail(final int arg0, final int arg1)
                {
                    Log.e("insert","====OnFail=====");
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Intent intent = new Intent();
                            intent.setAction("com.vomont.load.info");
                            intent.putExtra("type", 1);
                            intent.putExtra("fileId", arg0);
                            intent.putExtra("errorid", arg1);
                            sendBroadcast(intent);
                        }
                    });
                }
            });
            WMFileLoadSdk.getInstance().setEventCallback(new EventCallback()
            {
                
                @Override
                public void OnSucess(final int fileIdx, final String fileUri)
                {
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Intent intent = new Intent();
                            intent.setAction("com.vomont.load.info");
                            intent.putExtra("type", 0);
                            intent.putExtra("fileId", fileIdx);
                            intent.putExtra("videourl", fileUri);
                            sendBroadcast(intent);
                        }
                    });
                    
                }
                
                @Override
                public void OnFail(final int fileIdx, final int errorId)
                {
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Intent intent = new Intent();
                            intent.setAction("com.vomont.load.info");
                            intent.putExtra("type", 1);
                            intent.putExtra("fileId", fileIdx);
                            intent.putExtra("errorid", errorId);
                            sendBroadcast(intent);
                        }
                    });
                    
                }
            });
        }
        initFragment();
        homeFragment.initHandler(new GetSysMessage()
        {
            @Override
            public void onEnd()
            {
                
            }
        });
        
        new Thread(new Runnable()
        {
            public void run()
            {
                while (needLogin)
                {
                    if (result != 0)
                    {
                        result =
                            playutil.authenticate(shareUtil.getShare().getVeyeuserid(), shareUtil.getShare().getVeyekey(), shareUtil.getShare().getVeyesvrip(), shareUtil.getShare().getVeyesvrport());
                    }
                    else if (result != -1)
                    {
                        needLogin = false;
                    }
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        
        // 绑定服务在主界面 后面的界面调用 解绑service后 不会销毁该服务
        Intent intent = new Intent(this, VideoUpService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        
        // // 防止由于崩溃等原因导致在上传过程中 断掉上传 而出现上传状态的错误的更新
        // videoHelpter.updataLoad();
        initListener();
        videoHelpter = new VideoHelpter(this);
        if (videoHelpter.selectNoPack() != null)
        {
            List<VideoBean> mlist = new ArrayList<VideoBean>();
            mlist.addAll(videoHelpter.selectNoPack());
            for(VideoBean bean:mlist)
            {
                bean.setLoadstate(0);
                videoHelpter.updateBean(bean.getName(), bean);
                if(bean.getFileId()!=0)
                WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(bean.getFileId(), true);
            }
        }
    }
    
    private ServiceConnection conn = new ServiceConnection()
    {
        /** 获取服务对象时的操作 */
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            // 返回一个MsgService对象
            // myService = ((MyService.MsgBinder) service).getService();
        }
        
        /** 无法获取到服务对象时的操作 */
        public void onServiceDisconnected(ComponentName name)
        {
            // 、 myService = null;
        }
    };
    
    private void initFragment()
    {
        // 初始化 默认第一个页面
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        homeFragment = new HomeFragment();
        fragmentTransaction.add(R.id.activity_home_frame, homeFragment);
        if (factoryFragment != null)
        {
            fragmentTransaction.hide(factoryFragment);
        }
        
        if (mangerFragment != null)
        {
            fragmentTransaction.hide(mangerFragment);
        }
        
        if (meFragment != null)
        {
            fragmentTransaction.hide(meFragment);
        }
        fragmentTransaction.commit();
        WMClientSdk.getInstance().init(63);
    }
    
    private void initListener()
    {
        radio_msg.setOnClickListener(this);
        radio_factory.setOnClickListener(this);
        radio_manager.setOnClickListener(this);
        radio_me.setOnClickListener(this);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            exit();
        }
        return true;
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.radio_msg:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (homeFragment == null)
                {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.activity_home_frame, homeFragment);
                }
                else
                {
                    fragmentTransaction.show(homeFragment);
                }
                
                if (factoryFragment != null)
                {
                    fragmentTransaction.hide(factoryFragment);
                }
                
                if (mangerFragment != null)
                {
                    fragmentTransaction.hide(mangerFragment);
                }
                
                if (meFragment != null)
                {
                    fragmentTransaction.hide(meFragment);
                }
                fragmentTransaction.commit();
                break;
            case R.id.radio_factory:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (factoryFragment == null)
                {
                    factoryFragment = new FactoryFragment();
                    fragmentTransaction.add(R.id.activity_home_frame, factoryFragment);
                }
                else
                {
                    fragmentTransaction.show(factoryFragment);
                }
                
                if (homeFragment != null)
                {
                    fragmentTransaction.hide(homeFragment);
                }
                
                if (mangerFragment != null)
                {
                    fragmentTransaction.hide(mangerFragment);
                }
                
                if (meFragment != null)
                {
                    fragmentTransaction.hide(meFragment);
                }
                fragmentTransaction.commit();
                
                break;
            case R.id.radio_manager:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (mangerFragment == null)
                {
                    mangerFragment = new MangerFragment();
                    fragmentTransaction.add(R.id.activity_home_frame, mangerFragment);
                }
                else
                {
                    fragmentTransaction.show(mangerFragment);
                }
                
                if (homeFragment != null)
                {
                    fragmentTransaction.hide(homeFragment);
                }
                
                if (factoryFragment != null)
                {
                    fragmentTransaction.hide(factoryFragment);
                }
                
                if (meFragment != null)
                {
                    fragmentTransaction.hide(meFragment);
                }
                fragmentTransaction.commit();
                break;
            case R.id.radio_me:
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                if (meFragment == null)
                {
                    meFragment = new MeFragment();
                    fragmentTransaction.add(R.id.activity_home_frame, meFragment);
                }
                else
                {
                    fragmentTransaction.show(meFragment);
                }
                
                if (homeFragment != null)
                {
                    fragmentTransaction.hide(homeFragment);
                }
                
                if (factoryFragment != null)
                {
                    fragmentTransaction.hide(factoryFragment);
                }
                
                if (mangerFragment != null)
                {
                    fragmentTransaction.hide(mangerFragment);
                }
                fragmentTransaction.commit();
                break;
            default:
                break;
        }
    }
    
    public void exit()
    {
        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else
        {
            ACache aCache = ACache.get(this);
            aCache.put("factoryBean", "");
            aCache.put("problemDetailInfo", "");
            finish();
            System.exit(0);
        }
    }
    
    // 改界面销毁 就是app销毁 结束该service的绑定
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unbindService(conn);
    }
}
