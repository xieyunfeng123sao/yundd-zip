package com.vomont.yundudao.ui.manual;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.ManualBean;
import com.vomont.yundudao.db.Db_Manual;
import com.vomont.yundudao.ui.manual.adapter.ManualAdapter;
import com.vomont.yundudao.ui.manual.adapter.ManualAdapter.OnEditListener;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.view.ios.ActionEditDialog;
import com.vomont.yundudao.view.ios.ActionEditDialog.OnSheetItemClickListener;
import com.vomont.yundudao.view.ios.ActionEditDialog.SheetItemColor;

public class ManualActivity extends Activity implements OnClickListener
{
    private ImageView manual_go_back;
    
    private ImageView manual_add;
    
    private ListView manual_list;
    
    private LinearLayout empty_manual;
    
    private ManualAdapter adapter;
    
    private List<FactoryInfo> mlist;
    
    private List<ManualBean> ma_list;
    
    private Db_Manual db_Manual;
    
    private FactoryBean factoryBean;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_inspection);
        factoryBean = (FactoryBean)ACache.get(this).getAsObject("factoryBean");
        initView();
        initListener();
        initData();
        
    }
    
    @SuppressWarnings("unchecked")
    private void initData()
    {
        mlist = (List<FactoryInfo>)getIntent().getSerializableExtra("factoryBean");
        ma_list = new ArrayList<ManualBean>();
        adapter = new ManualAdapter(this, ma_list);
        manual_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnEditListener(new OnEditListener()
        {
            @Override
            public void onEdit(final int position)
            {
                new ActionEditDialog(ManualActivity.this).builder()
                    .setCancelable(true)
                    .setCanceledOnTouchOutside(true)
                    .addSheetItem("编辑", SheetItemColor.GRAY, R.drawable.handtour_edit, new OnSheetItemClickListener()
                    {
                        @Override
                        public void onClick(int which)
                        {
                            Intent intent = new Intent(ManualActivity.this, ManualDetailActivity.class);
                            intent.putExtra("manualdetail", ma_list.get(position));
                            intent.putExtra("factoryBean", (Serializable)mlist);
                            startActivity(intent);
                        }
                    })
                    .addSheetItem("删除", SheetItemColor.Red, R.drawable.handtour_delete, new OnSheetItemClickListener()
                    {
                        @Override
                        public void onClick(int which)
                        {
                            db_Manual.deleteManual(ma_list.get(position).getId());
                            ma_list.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    })
                    .show();
            }
        });
        
        manual_list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ManualActivity.this, ManualPlayActivity.class);
                intent.putExtra("manualdetail", ma_list.get(position));
                startActivity(intent);                
            }
        });
    }
    
    private void initListener()
    {
        manual_go_back.setOnClickListener(this);
        manual_add.setOnClickListener(this);
        db_Manual = new Db_Manual(this);
    }
    
    private void initView()
    {
        manual_go_back = (ImageView)findViewById(R.id.manual_go_back);
        manual_add = (ImageView)findViewById(R.id.manual_add);
        manual_list = (ListView)findViewById(R.id.manual_list);
        empty_manual = (LinearLayout)findViewById(R.id.empty_manual);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.manual_go_back:
                finish();
                break;
            case R.id.manual_add:
                Intent intent = new Intent(ManualActivity.this, ManualDetailActivity.class);
                intent.putExtra("factoryBean", (Serializable)mlist);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        ma_list.clear();
        if (db_Manual.selectAll() != null)
        {
            empty_manual.setVisibility(View.GONE);
            ma_list.addAll(db_Manual.selectAll());
            if (factoryBean.getDevices() != null && factoryBean.getDevices().size() != 0)
            {
                for (int i = 0; i < ma_list.size(); i++)
                {
                    List<DeviceInfo> devlist = new ArrayList<DeviceInfo>();
                    String[] idList = ma_list.get(i).getDeviceIdList().split(",");
                    for (int j = 0; j < idList.length; j++)
                    {
                        DeviceInfo deviceInfo = new DeviceInfo();
                        for (int m = 0; m < factoryBean.getDevices().size(); m++)
                        {
                            if (factoryBean.getDevices().get(m).getDeviceid() == Integer.parseInt(idList[j]))
                            {
                                deviceInfo = factoryBean.getDevices().get(m);
                                devlist.add(deviceInfo);
                                continue;
                            }
                        }
                    }
                    ma_list.get(i).setDevList(devlist);
                }
            }
            adapter.notifyDataSetChanged();
        }
        else
        {
            empty_manual.setVisibility(View.VISIBLE);
        }
        
    }
    
}
