package com.vomont.yundudao.ui.reportform;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.bean.TagBean;
import com.vomont.yundudao.bean.TagInfo;
import com.vomont.yundudao.mvpview.tag.ITagView;
import com.vomont.yundudao.presenter.tag.TagPresenter;
import com.vomont.yundudao.ui.reportform.adapter.FormDevAdapter;
import com.vomont.yundudao.ui.reportform.adapter.GridTagAdapter;
import com.vomont.yundudao.ui.reportform.adapter.GridTagAdapter.OnCheckClickListener;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.BaseActivity;
import com.vomont.yundudao.view.NoScrollGridView.NoScrollGridView;

public class FormTypeActivity extends BaseActivity implements OnClickListener, ITagView
{
    private TextView top_name;
    
    private ImageView go_back;
    
    private TextView select_date1, select_date2, select_date3;
    
    private CheckBox select_type_all;
    
    private NoScrollGridView form_type_grid;
    
    private TextView select_factory_form;
    
    private ListView selected_factory_list;
    
    private Button start_get_result;
    
    private ACache aCache;
    
    private List<TagInfo> tagInfos;
    
    private GridTagAdapter adapter;
    
    private TagPresenter tagPresenter;
    
    private ShareUtil shareUtil;
    
    private long startTime, endTime;
    
    private int state = 0;
    
    public final int intent_action = 55;
    
    
    private List<TagInfo> sendTags;
    
    private List<FactoryInfo> mlist;
    
    private List<SubFactory> subFactories;
    
    private boolean isClear = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_select_type);
        initView();
        initData();
        initListener();
    }
    
    private void initView()
    {
        top_name = (TextView)findViewById(R.id.top_name);
        go_back = (ImageView)findViewById(R.id.go_back);
        select_date1 = (TextView)findViewById(R.id.select_date1);
        select_date2 = (TextView)findViewById(R.id.select_date2);
        select_date3 = (TextView)findViewById(R.id.select_date3);
        select_type_all = (CheckBox)findViewById(R.id.select_type_all);
        form_type_grid = (NoScrollGridView)findViewById(R.id.form_type_grid);
        select_factory_form = (TextView)findViewById(R.id.select_factory_form);
        selected_factory_list = (ListView)findViewById(R.id.selected_factory_list);
        start_get_result = (Button)findViewById(R.id.start_get_result);
    }
    
    @SuppressWarnings("unchecked")
    private void initData()
    {
        top_name.setText("自定义分析");
        aCache = ACache.get(this);
        tagInfos = (List<TagInfo>)aCache.getAsObject("tag");
        shareUtil = new ShareUtil(this);
        tagPresenter = new TagPresenter(this);
        tagPresenter.getTag(shareUtil.getShare().getUser_id() + "");
        adapter = new GridTagAdapter(this);
        form_type_grid.setAdapter(adapter);
        FactoryBean factoryBean = (FactoryBean)aCache.getAsObject("factoryBean");
        addFactoryInfo(factoryBean);
        subFactories = new ArrayList<SubFactory>();
        sendTags = new ArrayList<TagInfo>();
        adapter.setCheckBoxOnClickListener(new OnCheckClickListener()
        {
            @Override
            public void onChange(List<TagInfo> mlist1, boolean isChecked)
            {
                
                if(sendTags.size()==tagInfos.size())
                {
                    if(mlist1.size()<sendTags.size())
                    {
                        isClear=true;
                        select_type_all.setChecked(false);
                    }
                }
                else if((mlist1.size()==tagInfos.size())&&(sendTags.size()!=mlist1.size()))
                {
                    isClear=true;
                    select_type_all.setChecked(true);
                }
                isClear=false;
                sendTags.clear();
                sendTags.addAll(mlist1);
            }
        });
        
        select_type_all.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(!isClear)
                {
                    adapter.setClearCheck(isChecked);
                }
            }
        });
        getWeek();
    }
    
    public void selectChangeUI(TextView textView1, TextView textView2, TextView textView3)
    {
        textView1.setTextColor(getResources().getColor(R.color.main_color));
        textView1.setBackgroundResource(R.drawable.textview_suface);
        
        textView2.setTextColor(getResources().getColor(R.color.biantai_gray));
        textView2.setBackgroundResource(R.drawable.textview_suface_no);
        
        textView3.setTextColor(getResources().getColor(R.color.biantai_gray));
        textView3.setBackgroundResource(R.drawable.textview_suface_no);
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
        select_date1.setOnClickListener(this);
        select_date2.setOnClickListener(this);
        select_date3.setOnClickListener(this);
        select_factory_form.setOnClickListener(this);
        start_get_result.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.go_back:
                finish();
                break;
            case R.id.select_date1:
                if (state != 0)
                {
                    selectChangeUI(select_date1, select_date2, select_date3);
                    getWeek();
                }
                state = 0;
                break;
            case R.id.select_date2:
                if (state != 1)
                {
                    selectChangeUI(select_date2, select_date1, select_date3);
                    getMoth();
                }
                state = 1;
                break;
            case R.id.select_date3:
                if (state != 2)
                {
                    selectChangeUI(select_date3, select_date2, select_date1);
                    getLastMoth();
                }
                state = 2;
                break;
            case R.id.select_factory_form:
                Intent intent = new Intent(FormTypeActivity.this, FormSubActivity.class);
                startActivityForResult(intent, intent_action);
                break;
            case R.id.start_get_result:
                if (sendTags == null || sendTags.size() == 0)
                {
                    Toast.makeText(FormTypeActivity.this, "请选择问题类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (subFactories == null || subFactories.size() == 0)
                {
                    Toast.makeText(FormTypeActivity.this, "请选择巡视点", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent2 = new Intent(FormTypeActivity.this, ReportMoreActivity.class);
                intent2.putExtra("starttime", startTime);
                intent2.putExtra("endtime", endTime);
                intent2.putExtra("tags", (Serializable)tagInfos);
                intent2.putExtra("types", (Serializable)sendTags);
                intent2.putExtra("sub", (Serializable)subFactories);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void getTag(TagBean tagBean)
    {
        tagInfos = new ArrayList<TagInfo>();
        if (tagBean.getProblemtypes() != null)
        {
            for (int i = 0; i < tagBean.getProblemtypes().size(); i++)
            {
                if (tagBean.getProblemtypes().get(i).getOwnertypeid() == 0)
                {
                    tagInfos.add(tagBean.getProblemtypes().get(i));
                }
            }
            for (int i = 0; i < tagInfos.size(); i++)
            {
                List<TagInfo> item = new ArrayList<TagInfo>();
                for (int j = 0; j < tagBean.getProblemtypes().size(); j++)
                {
                    if (tagInfos.get(i).getTypeid() == tagBean.getProblemtypes().get(j).getOwnertypeid())
                    {
                        item.add(tagBean.getProblemtypes().get(j));
                    }
                }
                tagInfos.get(i).setMlist(item);
            }
        }
        if (tagInfos != null && tagInfos.size() != 0)
        {
            adapter.setData(tagInfos);
            adapter.notifyDataSetChanged();
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case intent_action:
                if (data != null)
                {
//                    subFactories.clear();
                    List<SubFactory> addSub=(List<SubFactory>)data.getSerializableExtra("subfatory");
                    for(SubFactory factory:addSub)
                    {
                        if(!subFactories.contains(factory))
                        {
                            subFactories.add(factory);
                        }
                    }
                    FormDevAdapter adapter = new FormDevAdapter(FormTypeActivity.this);
                    adapter.setData(mlist, subFactories);
                    // adapter.setData(map);
                    selected_factory_list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(selected_factory_list);
                }
                break;
            default:
                break;
        }
    }
    
    @SuppressLint("SimpleDateFormat")
    public void getWeek()
    {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek)
        {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = dateFormater.format(cal.getTime()) + " 00:00:00";
        // System.out.println("所在周星期一的日期：" + imptimeBegin);
        try
        {
            startTime = format.parse(imptimeBegin).getTime();
            endTime = new Date().getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
     
    }
    
    @SuppressLint("SimpleDateFormat")
    public void getMoth()
    {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String start = dateFormater.format(cal.getTime()) + " 00:00:00";
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        // String end = dateFormater.format(cal.getTime()) + " 24:00:00";
        try
        {
            startTime = format.parse(start).getTime();
            endTime = new Date().getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
    
    @SuppressLint("SimpleDateFormat")
    public void getLastMoth()
    {
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1); // set the date to be 1
        cal.add(Calendar.MONTH, -1);// reduce a month to be last month
        String start = dateFormater.format(cal.getTime()) + " 00:00:00";
        
        cal.roll(Calendar.DATE, -1);//
        String end = dateFormater.format(cal.getTime()) + " 23:59:59";
        try
        {
            startTime = format.parse(start).getTime();
            endTime = format.parse(end).getTime();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
    
    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++)
        {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
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
