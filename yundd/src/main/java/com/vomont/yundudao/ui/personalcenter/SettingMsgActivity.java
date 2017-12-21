package com.vomont.yundudao.ui.personalcenter;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.view.BaseActivity;

public class SettingMsgActivity extends BaseActivity
{
    
    private ImageView go_back;
    
    private TextView  top_name;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_msg);
        go_back=(ImageView)findViewById(R.id.go_back);
        top_name=(TextView)findViewById(R.id.top_name);
        go_back.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
           finish();     
            }
        });
        Appcation.getInstance().addActivity(this);
        top_name.setText(R.string.setting_message_new);
    }
}
