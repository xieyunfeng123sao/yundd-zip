package com.vomont.yundudao.ui.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.Message;
import com.vomont.yundudao.view.BaseActivity;

public class MsgAllActivity extends BaseActivity implements OnClickListener
{
    private ImageView go_back;
    
    private TextView top_name;
    
    private Message message;
    
    private TextView msg_all_title, msg_all_time, msg_all_content;
    
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_all);
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        msg_all_title = (TextView)findViewById(R.id.msg_all_title);
        msg_all_time = (TextView)findViewById(R.id.msg_all_time);
        msg_all_content = (TextView)findViewById(R.id.msg_all_content);
        top_name.setText(R.string.msg_all);
        message = (Message)getIntent().getSerializableExtra("info");
        msg_all_title.setText(message.getMsgtitle());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        
        msg_all_time.setText(sDateFormat.format(new Date(message.getMsgpubtime() * 1000 + 0)));
        msg_all_content.setText(message.getMsgcontent());
        initListener();
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.go_back:
                finish();
                break;
            default:
                break;
        }
    }
}
