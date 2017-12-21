package com.vomont.yundudao.ui.patrol;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.mvpview.fatory.IFactoryView;
import com.vomont.yundudao.presenter.factory.FactoryPresenter;
import com.vomont.yundudao.ui.patrol.adapter.SubAdapter;
import com.vomont.yundudao.utils.ShareUtil;

public class SelectSubActivity extends Activity implements IFactoryView
{
    
    private ImageView patrol_sub_back;
    
    private ExpandableListView select_device;
    
    private ShareUtil shareUtil;
    
    private FactoryPresenter factoryPresenter;
    
    private List<FactoryInfo> mlist;
    
    private TextView jump;
    
    private int seletedDaPosition = -1;
    
    private int seletedPosition = -1;
    
    private SubAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_fatory);
        initView();
        initData();
    }
    
    private void initData()
    {
        shareUtil = new ShareUtil(this);
        factoryPresenter = new FactoryPresenter(this);
        factoryPresenter.getFactory(shareUtil.getShare().getUser_id() + "");
    }
    
    private void initView()
    {
        jump = (TextView)findViewById(R.id.jump);
        patrol_sub_back = (ImageView)findViewById(R.id.patrol_sub_back);
        select_device = (ExpandableListView)findViewById(R.id.select_device);
        // 去掉箭头
        select_device.setGroupIndicator(null);
        patrol_sub_back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        
        jump.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (seletedDaPosition != -1 && seletedPosition != -1)
                {
                    Intent intent = getIntent();
                    intent.putExtra("fatory", mlist.get(seletedDaPosition));
                    intent.putExtra("subfatory", mlist.get(seletedDaPosition).getMlist().get(seletedPosition));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(SelectSubActivity.this, "选择巡视点", Toast.LENGTH_LONG).show();
                }
                
            }
        });
        
    }
    
    @Override
    public void getFactory(FactoryBean factoryBean)
    {
        if (factoryBean != null)
        {
            addFactoryInfo(factoryBean);
            adapter = new SubAdapter(this, mlist);
            select_device.setAdapter(adapter);
            select_device.setOnChildClickListener(new OnChildClickListener()
            {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
                {
                    seletedDaPosition = groupPosition;
                    seletedPosition = childPosition;
                    adapter.setSelectedItem(groupPosition, childPosition);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            });
        }
    }
    
    @Override
    public void onFail()
    {
        
    }
    
    private void addFactoryInfo(FactoryBean factoryBean)
    {
        if (factoryBean != null)
        {
            mlist = new ArrayList<FactoryInfo>();
            if (factoryBean.getFactorys() != null)
            {
                // 18551768363
                mlist.addAll(factoryBean.getFactorys());
                if (factoryBean.getSubfactorys() != null)
                {
                    if (mlist != null && mlist.size() != 0)
                    {
                        for (int i = 0; i < mlist.size(); i++)
                        {
                            List<SubFactory> sub_list = new ArrayList<SubFactory>();
                            for (int j = 0; j < factoryBean.getSubfactorys().size(); j++)
                            {
                                if (mlist.get(i).getFactoryid() == factoryBean.getSubfactorys().get(j).getOwnerfactoryid())
                                {
                                    sub_list.add(factoryBean.getSubfactorys().get(j));
                                }
                            }
                            mlist.get(i).setMlist(sub_list);
                        }
                    }
                    
                }
                if (factoryBean.getDevices() != null)
                {
                    if (mlist != null && mlist.size() != 0)
                    {
                        for (int i = 0; i < mlist.size(); i++)
                        {
                            if (mlist.get(i).getMlist() != null && mlist.get(i).getMlist().size() != 0)
                            {
                                
                                for (int j = 0; j < mlist.get(i).getMlist().size(); j++)
                                {
                                    List<DeviceInfo> dev_list = new ArrayList<DeviceInfo>();
                                    for (int k = 0; k < factoryBean.getDevices().size(); k++)
                                    {
                                        if (mlist.get(i).getMlist().get(j).getSubfactoryid() == factoryBean.getDevices().get(k).getSubfactoryid())
                                        {
                                            dev_list.add(factoryBean.getDevices().get(k));
                                        }
                                    }
                                    mlist.get(i).getMlist().get(j).setMlist_device(dev_list);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
