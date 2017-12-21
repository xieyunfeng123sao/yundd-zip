package com.vomont.yundudao.ui.personalcenter;

import com.vomont.yundudao.R;
import com.vomont.yundudao.utils.ACache;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoSettingActivity extends Activity implements OnClickListener
{
    
    private ImageView video_setting_go_back;
    
    private TextView video_setting_sure;
    
    private CheckBox low_av;
    
    private CheckBox high_av;
    
    private CheckBox top_av;
    
    private LinearLayout low_ll,high_ll,top_ll;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_setting);
        initView();
        initListener();
    }
    
    private void initListener()
    {
        video_setting_go_back.setOnClickListener(this);
        video_setting_sure.setOnClickListener(this);
        
        
        low_av.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                high_av.setChecked(false);
                top_av.setChecked(false);
                low_av.setChecked(true);
            }
        });
        low_ll.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                high_av.setChecked(false);
                top_av.setChecked(false);
                low_av.setChecked(true);
            }
        });
        
        high_av.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                high_av.setChecked(true);
                top_av.setChecked(false);
                low_av.setChecked(false);
                
            }
        });
        
        high_ll.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                high_av.setChecked(true);
                top_av.setChecked(false);
                low_av.setChecked(false);
            }
        });
        top_av.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                high_av.setChecked(false);
                top_av.setChecked(true);
                low_av.setChecked(false);
                
            }
        });
        
        top_ll.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                high_av.setChecked(false);
                top_av.setChecked(true);
                low_av.setChecked(false);
            }
        });
        
        ACache aCache = ACache.get(this);
        String data = aCache.getAsString("videosetting");
        if (data != null)
        {
            if (data.equals("0"))
            {
                low_av.setChecked(true);
            }
            else if (data.equals("1"))
            {
                high_av.setChecked(true);
            }
            else
            {
                top_av.setChecked(true);
            }
        }
        else
        {
            low_av.setChecked(true);
        }
    }
    
    private void initView()
    {
        video_setting_go_back = (ImageView)findViewById(R.id.video_setting_go_back);
        video_setting_sure = (TextView)findViewById(R.id.video_setting_sure);
        low_av = (CheckBox)findViewById(R.id.low_av);
        high_av = (CheckBox)findViewById(R.id.high_av);
        top_av = (CheckBox)findViewById(R.id.top_av);
        low_ll=(LinearLayout)findViewById(R.id.low_ll);
        high_ll=(LinearLayout)findViewById(R.id.high_ll);
        top_ll=(LinearLayout)findViewById(R.id.top_ll);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.video_setting_go_back:
                finish();
                break;
            case R.id.video_setting_sure:
                if (high_av.isChecked() || low_av.isChecked() || top_av.isChecked())
                {
                    if (low_av.isChecked())
                    {
                        ACache aCache = ACache.get(this);
                        aCache.put("videosetting", "0");
                    }
                    else if (high_av.isChecked())
                    {
                        ACache aCache = ACache.get(this);
                        aCache.put("videosetting", "1");
                    }
                    else
                    {
                        ACache aCache = ACache.get(this);
                        aCache.put("videosetting", "2");
                    }
                    finish();
                }
                else
                {
                    
                }
                
                break;
            default:
                break;
        }
    }
    
}
