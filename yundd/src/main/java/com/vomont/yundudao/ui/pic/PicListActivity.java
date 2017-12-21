package com.vomont.yundudao.ui.pic;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.pic.adapter.PicListAdapter;
import com.vomont.yundudao.utils.addpic.LocalMediaFolder;

public class PicListActivity extends Activity
{
    private TextView picture_list_right;
    
    private ListView pic_listview;
    
    private List<LocalMediaFolder> mlist;
    
    private PicListAdapter adapter;
    
    private Intent intent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piclist);
        initView();
        initData();
    }
    
    @SuppressWarnings("unchecked")
    private void initData()
    {
        intent = getIntent();
        mlist = (List<LocalMediaFolder>)intent.getSerializableExtra("LocalMediaFolder");
        adapter = new PicListAdapter(this, mlist);
        pic_listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        pic_listview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                intent.putExtra("showType", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        
        picture_list_right.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
    
    private void initView()
    {
        picture_list_right = (TextView)findViewById(R.id.picture_list_right);
        pic_listview = (ListView)findViewById(R.id.pic_listview);
        
    }
    
}
