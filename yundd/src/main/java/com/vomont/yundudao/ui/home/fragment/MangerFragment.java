package com.vomont.yundudao.ui.home.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.ui.home.fragment.adapter.GridManganerAdapter;
import com.vomont.yundudao.ui.manual.ManualActivity;
import com.vomont.yundudao.ui.newproblem.NewProblemActivity;
import com.vomont.yundudao.ui.patrol.MediaActivity;
import com.vomont.yundudao.ui.patrol.PatrolListActivity;
import com.vomont.yundudao.ui.pic.AllPicActivity;
import com.vomont.yundudao.ui.police.CallPoliceActivity;
import com.vomont.yundudao.ui.reportform.ReportFormActivity;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.FactoryBeanUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class MangerFragment extends Fragment
{
    
    private GridView gridView;
    
    private ImageView go_back;
    
    // 除了标题和底部菜单中间部分的布局 用于计算gridview的高度 达到铺满全屏
    private LinearLayout fragment_manger_layout;
    
    private View view;
    
    // 标题
    private TextView top_name;
    
    private List<Map<String, Object>> data_list;
    
    // item的图片 R.drawable.manage_hand,
    private int[] icon = {R.drawable.manage_auto_tour, R.drawable.manage_picture, R.drawable.yundd_question, R.drawable.manage_scence_tour, R.drawable.manage_task_center,
        R.drawable.manage_port, R.drawable.manage_alarm_center,R.drawable.manage_alarm_center,R.drawable.manage_alarm_center};
    
    // item的标题 "手动巡视",
    private String[] iconName = {"智能巡视", "图片", "问题", "现场巡查", "巡检视频", "报表", "报警","",""};
    
    private List<FactoryInfo> mlist;
    
    private boolean isFirst=true;
    
    public MangerFragment()
    {
    }
    
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_mangager, null);
        initView();
        mlist = FactoryBeanUtil.addFactoryInfo(getActivity());
        initData();
        return view;
    }
    
    private void initView()
    {
        gridView = (GridView)view.findViewById(R.id.manger_button);
        go_back = (ImageView)view.findViewById(R.id.go_back);
        go_back.setVisibility(View.GONE);
        top_name = (TextView)view.findViewById(R.id.top_name);
        fragment_manger_layout = (LinearLayout)view.findViewById(R.id.fragment_manger_layout);
        
    }
    
    private void initData()
    {
        top_name.setText("管理");
        
        // 新建List
        data_list = new ArrayList<Map<String, Object>>();
        // 获取数据
        getData();
        gridView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == 0)
                {
                    Intent intent = new Intent(getActivity(), ManualActivity.class);
                    intent.putExtra("factoryBean", (Serializable)mlist);
                    startActivity(intent);
                }
                if (position == 1)
                {
                    Intent intent = new Intent(getActivity(), AllPicActivity.class);
                    intent.putExtra("factoryBean", (Serializable)mlist);
                    startActivity(intent);
                }

                if (position == 2)
                {
                    Intent intent = new Intent(getActivity(), NewProblemActivity.class);
                  startActivity(intent);
                }
                if (position == 3)
                {
                    Intent intent = new Intent(getActivity(), MediaActivity.class);
                    startActivity(intent);
                }
                
                if (position == 4)
                {
                    Intent intent = new Intent(getActivity(), PatrolListActivity.class);
                    ACache aCache = ACache.get(getActivity());
                    intent.putExtra("factoryBean",(FactoryBean)aCache.getAsObject("factoryBean"));
                    startActivity(intent);
                }
                if (position == 5)
                {
                    Intent intent = new Intent(getActivity(), ReportFormActivity.class);
                    startActivity(intent);
                }
                
                if (position == 6)
                {
                    Intent intent = new Intent(getActivity(), CallPoliceActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    
    public List<Map<String, Object>> getData()
    {
        for (int i = 0; i < icon.length; i++)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        if(isFirst)
        {
            // view绘制完成后 获取gridview的高度 从而设置每一个item的高度铺满全屏
            view.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    GridManganerAdapter adapter = new GridManganerAdapter(data_list, getActivity(), fragment_manger_layout.getHeight());
                    gridView.setAdapter(adapter);
                }
            }, 100); 
            isFirst=false;
        }
    }
    
}
