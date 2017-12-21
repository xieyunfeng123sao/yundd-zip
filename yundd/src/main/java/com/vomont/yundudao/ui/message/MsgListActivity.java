package com.vomont.yundudao.ui.message;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.view.BaseActivity;

public class MsgListActivity extends BaseActivity implements OnClickListener
{
    

    
    private TextView top_name;
    
    private ImageView go_back;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);
        top_name = (TextView)findViewById(R.id.top_name);
       String name=getIntent().getStringExtra("name");
       if(name!=null)
       {
           top_name.setText(name);
       }
       else
       {
           top_name.setText("消息");
       }
      
        go_back = (ImageView)findViewById(R.id.go_back);
        go_back.setOnClickListener(this);
//        mlist = new ArrayList<Message>();
//        mHelp = new MySqliteHelp(this);
//        mHelp.getWritableDatabase();
//        mHelp.getInfo(mlist);
//        mHelp.changeStatue();
//        mlist_new = new ArrayList<Message>();
//        
//        for (int i = mlist.size(); i > 0; i--)
//        {
//            mlist_new.add(mlist.get(i - 1));
//        }
//        
//        MsgAdapter msgAdapter = new MsgAdapter(this, mlist_new);
//        pullview.setAdapter(msgAdapter);
    }
    
    @Override
    public void onClick(View arg0)
    {
        switch (arg0.getId())
        {
            case R.id.go_back:
                finish();
                break;
            default:
                break;
        }
    }
}
