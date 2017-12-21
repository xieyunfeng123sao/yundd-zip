package com.vomont.yundudao.ui.police;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.vomont.yundudao.R;

public class CallPoliceActivity extends Activity
{
    private ImageView call_police_back;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callpolice);
        call_police_back = (ImageView)findViewById(R.id.call_police_back);
        call_police_back.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
}
