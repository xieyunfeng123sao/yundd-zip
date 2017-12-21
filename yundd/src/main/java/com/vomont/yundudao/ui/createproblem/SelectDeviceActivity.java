package com.vomont.yundudao.ui.createproblem;

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
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.ui.createproblem.adapter.DevExpandAdapter;
import com.vomont.yundudao.ui.createproblem.adapter.PopuFactoryAdapter;
import com.vomont.yundudao.ui.createproblem.adapter.SubExpandAdapter;

@SuppressLint("InflateParams")
public class SelectDeviceActivity extends Activity implements OnClickListener
{
    
    private RelativeLayout select_factoty;
    
    private TextView factory_name;
    
    private PopupWindow popupWindow;
    
    private View view;
    
    private ImageView go_back;
    
    private TextView top_name;
    
    private ListView popu_select_factory;
    
    private ExpandableListView select_device;
    
    private PopuFactoryAdapter adapter;
    
    private DevExpandAdapter devAdapter;
    
    private List<FactoryInfo> mlist;
    
    private List<SubFactory> updataMlist;
    
    private String type;
    
    private ImageView show_view;
    
    private TextView jump;
    
    private int groupP = -1;
    
    private int childP = -1;
    
    private SubExpandAdapter subExpandAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_factory);
        initView();
        initData();
        initListener();
    }
    
    @SuppressWarnings("unchecked")
    private void initView()
    {
        jump = (TextView)findViewById(R.id.jump);
        factory_name = (TextView)findViewById(R.id.factory_name);
        select_factoty = (RelativeLayout)findViewById(R.id.select_factoty);
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        select_device = (ExpandableListView)findViewById(R.id.patrol_select_subfatory);
        show_view = (ImageView)findViewById(R.id.show_view);
        jump.setVisibility(View.VISIBLE);
        jump.setText("确定");
        type = getIntent().getStringExtra("type");
        updataMlist = new ArrayList<SubFactory>();
        if (type.equals("subfatory"))
        {
            select_factoty.setVisibility(View.GONE);
        }
        mlist = (List<FactoryInfo>)getIntent().getSerializableExtra("factory");
        
        if (mlist != null && mlist.size() != 0)
        {
            FactoryInfo factoryInfo = new FactoryInfo();
            factoryInfo.setFactoryname("全部");
            mlist.add(0, factoryInfo);
        }
        loadDataToList(0);
    }
    
    private void initData()
    {
        top_name.setText(R.string.select_fatory);
        select_device.setGroupIndicator(null);
    }
    
    private void initListener()
    {
        jump.setOnClickListener(this);
        go_back.setOnClickListener(this);
        select_factoty.setOnClickListener(this);
        select_device.setOnChildClickListener(new OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                groupP = groupPosition;
                childP = childPosition;
                if (devAdapter != null)
                {
                    devAdapter.setNotify(groupPosition, childPosition);
                }
                if (subExpandAdapter != null)
                {
                    subExpandAdapter.setNotify(groupPosition, childPosition);
                }
                return false;
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
                Intent intent = getIntent();
                if (groupP != -1 && childP != -1)
                {
                    if (type.equals("info"))
                    {
                        intent.putExtra("type", "info");
                        intent.putExtra("dev", updataMlist.get(groupP).getMlist_device().get(childP));
                        intent.putExtra("subid", updataMlist.get(groupP).getSubfactoryid());
                        setResult(RESULT_OK, intent);
                    }
                    else
                    {
                        intent.putExtra("type", "subfatory");
                        intent.putExtra("subid", mlist.get(groupP).getMlist().get(childP).getSubfactoryid());
                        setResult(RESULT_OK, intent);
                    }
                    finish();
                }
                else
                {
                    Toast.makeText(SelectDeviceActivity.this, "请选择设备", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    
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
                    factory_name.setText(mlist.get(position).getFactoryname());
                    popupWindow.dismiss();
                    popupWindow.dismiss();
                    loadDataToList(position);
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
            popupWindow.dismiss();
            show_view.setPivotX(show_view.getWidth() / 2);
            show_view.setPivotY(show_view.getHeight() / 2);// 支点在图片中心
            show_view.setRotation(0);
        }
        else
        {
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
        if (mlist != null && mlist.size() != 0 && type.equals("info"))
        {
            updataMlist.clear();
            if (position == 0)
            {
                for (FactoryInfo info : mlist)
                {
                    if (info.getMlist() != null)
                    {
                        updataMlist.addAll(info.getMlist());
                    }
                    
                }
            }
            else
            {
                if (mlist.get(position).getMlist() != null)
                    updataMlist.addAll(mlist.get(position).getMlist());
            }
            devAdapter = new DevExpandAdapter(SelectDeviceActivity.this, updataMlist);
            select_device.setAdapter(devAdapter);
        }
        else
        {
            subExpandAdapter = new SubExpandAdapter(SelectDeviceActivity.this, mlist);
            select_device.setAdapter(subExpandAdapter);
        }
        
    }
    
}
