package com.vomont.yundudao.ui.personalcenter;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.utils.DataCleanManager;
import com.vomont.yundudao.view.BaseActivity;

public class SettingActivity extends BaseActivity implements OnClickListener
{
    
    private Context context;
    
    private ImageView go_back;
    
    private TextView top_name, crash_size;
    
    private LinearLayout new_msg_action, setting_crash;
    
    private DataCleanManager daManager;
    
    private  Intent intent;
     
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Appcation.getInstance().addActivity(this);
        initView();
        initData();
        initListener();
    }
    
    private void initView()
    {
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        crash_size = (TextView)findViewById(R.id.crash_size);
        new_msg_action = (LinearLayout)findViewById(R.id.new_msg_action);
        setting_crash = (LinearLayout)findViewById(R.id.setting_crash);
    }
    
    @SuppressWarnings("static-access")
    private void initData()
    {
        context = this;
        top_name.setText(R.string.setting_top);
        daManager = new DataCleanManager();
        File file = new File(context.getExternalCacheDir().getAbsolutePath());
        
        try
        {
            String siz = daManager.getCacheSize(file);
            
            crash_size.setText(siz);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
        new_msg_action.setOnClickListener(this);
        setting_crash.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.go_back:
                finish();
                break;
            case R.id.new_msg_action:
                intent=new Intent();
                intent.setAction(SETTING_MSG_ACTION);
                startActivity(intent);
                break;
            case R.id.setting_crash:
                
                break;
            default:
                break;
        }
    }
}
