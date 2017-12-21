package com.vomont.yundudao.ui.manual;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.ui.createproblem.adapter.DevExpandAdapter;
import com.vomont.yundudao.ui.createproblem.adapter.PopuFactoryAdapter;
import com.vomont.yundudao.utils.ACache;

@SuppressLint("InflateParams")
public class ManualSelDevActivity extends Activity implements OnClickListener
{
    
    private RelativeLayout select_factoty;
    
    private PopupWindow popupWindow;
    
    private View view;
    
    private ImageView go_back;
    
    private TextView top_name;
    
    private ListView popu_select_factory;
    
    private ExpandableListView select_device;
    
    private PopuFactoryAdapter adapter;
    
    private DevExpandAdapter devAdapter;
    
    private List<FactoryInfo> mlist;
    
    private TextView jump;
    
    private List<SubFactory> sub_mlist;
    
    private TextView factory_name;
    
    private ImageView show_view;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_factory);
        initView();
        initData();
        initListener();
    }
    
    private void initView()
    {
        show_view = (ImageView)findViewById(R.id.show_view);
        select_factoty = (RelativeLayout)findViewById(R.id.select_factoty);
        go_back = (ImageView)findViewById(R.id.go_back);
        factory_name = (TextView)findViewById(R.id.factory_name);
        top_name = (TextView)findViewById(R.id.top_name);
        jump = (TextView)findViewById(R.id.jump);
        select_device = (ExpandableListView)findViewById(R.id.patrol_select_subfatory);
        sub_mlist = new ArrayList<SubFactory>();
        top_name.setText("选择监控点");
        jump.setVisibility(View.VISIBLE);
        jump.setText("确定");
        ACache aCache = ACache.get(this);
        FactoryBean factoryBean = (FactoryBean)aCache.getAsObject("factoryBean");
        addFactoryInfo(factoryBean);
        FactoryInfo factoryInfo = new FactoryInfo();
        factoryInfo.setFactoryname("全部");
        mlist.add(0, factoryInfo);
        loadDataToList(0);
    }
    
    private void initData()
    {
        top_name.setText(R.string.select_fatory);
        select_device.setGroupIndicator(null);
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
        select_factoty.setOnClickListener(this);
        jump.setOnClickListener(this);
        select_device.setOnChildClickListener(new OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                devAdapter.setSelectedItem(groupPosition, childPosition);
                devAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.select_factoty:
                initPopupWindow();
                break;
            case R.id.go_back:
                finish();
                break;
            case R.id.jump:
                if (devAdapter.getSelectedDev() != null && devAdapter.getSelectedDev().size() != 0)
                {
                    Intent intent = getIntent();
                    intent.putExtra("dev", (Serializable)devAdapter.getSelectedDev());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(ManualSelDevActivity.this, "请选择设备!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @see [类、类#方法、类#成员]
     */
    private void initPopupWindow()
    {
        if (popupWindow == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.layout_popu_selectfactory, null);
            popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            popu_select_factory = (ListView)view.findViewById(R.id.popu_select_factory);
            popu_select_factory.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    factory_name.setTextColor(getResources().getColor(R.color.text_color));
                    factory_name.setText(mlist.get(position).getFactoryname());
                    popupWindow.dismiss();
                    loadDataToList(position);
                    popupWindow.dismiss();
                    show_view.setPivotX(show_view.getWidth() / 2);
                    show_view.setPivotY(show_view.getHeight() / 2);// 支点在图片中心
                    show_view.setRotation(0);
                    
                }
            });
            show_view.setPivotX(show_view.getWidth() / 2);
            show_view.setPivotY(show_view.getHeight() / 2);// 支点在图片中心
            show_view.setRotation(180);
        }
        if (popupWindow.isShowing())
        {
            factory_name.setTextColor(getResources().getColor(R.color.text_color));
            popupWindow.dismiss();
            show_view.setPivotX(show_view.getWidth() / 2);
            show_view.setPivotY(show_view.getHeight() / 2);// 支点在图片中心
            show_view.setRotation(0);
        }
        else
        {
            factory_name.setTextColor(getResources().getColor(R.color.main_color));
            adapter = new PopuFactoryAdapter(this, mlist);
            popu_select_factory.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            popupWindow.showAsDropDown(select_factoty, popupWindow.getWidth(), 18);
            show_view.setPivotX(show_view.getWidth() / 2);
            show_view.setPivotY(show_view.getHeight() / 2);// 支点在图片中心
            show_view.setRotation(180);
        }
    }
    
    private void loadDataToList(int position)
    {
        sub_mlist.clear();
        if (position == 0)
        {
            if (mlist != null && mlist.size() != 0)
            {
                for (FactoryInfo info : mlist)
                {
                    if (info.getMlist() != null && info.getMlist().size() != 0)
                    {
                        for (SubFactory subFactory : info.getMlist())
                        {
                            sub_mlist.add(subFactory);
                        }
                    }
                }
            }
        }
        else
        {
            if ((mlist != null) && (mlist.size() != 0) && (mlist.get(position).getMlist() != null) && (mlist.get(position).getMlist().size() != 0))
                sub_mlist.addAll(mlist.get(position).getMlist());
        }
        devAdapter = new DevExpandAdapter(ManualSelDevActivity.this, sub_mlist);
        select_device.setAdapter(devAdapter);
    }
    
    public void addFactoryInfo(FactoryBean factoryBean)
    {
        mlist = new ArrayList<FactoryInfo>();
        if (factoryBean != null)
        {
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
