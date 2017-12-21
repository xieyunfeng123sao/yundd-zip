package com.vomont.yundudao.ui.personalcenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.view.BaseActivity;

public class AboutActivity extends BaseActivity
{
    
    private ImageView go_back;
    
    private TextView top_name, about_vesion;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        about_vesion = (TextView)findViewById(R.id.about_vesion);
        go_back.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        top_name.setText(R.string.about_us);
        try
        {
            about_vesion.setText("版本号：v "+getAppVersion());
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        Appcation.getInstance().addActivity(this);
    }
    
    /** 获取单个App版本号 **/
    public String getAppVersion()
        throws NameNotFoundException
    {
        
        PackageManager pManager = getPackageManager();
        PackageInfo packageInfo = pManager.getPackageInfo(getPackageName(), 0);
        String appVersion = packageInfo.versionName;
        return appVersion;
    }
    // 版本号
    public static int getVersionCode(Context context)
    {
        return getPackageInfo(context).versionCode;
    }
    
    private static PackageInfo getPackageInfo(Context context)
    {
        PackageInfo pi = null;
        try
        {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return pi;
    }
    
}
