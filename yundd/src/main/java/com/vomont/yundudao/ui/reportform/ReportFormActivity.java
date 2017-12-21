package com.vomont.yundudao.ui.reportform;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.xclcharts.chart.PieData;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FormBean;
import com.vomont.yundudao.bean.FormFactory;
import com.vomont.yundudao.bean.FormType;
import com.vomont.yundudao.mvpview.reportform.IReportFormView;
import com.vomont.yundudao.presenter.reportform.ReportFormPresenter;
import com.vomont.yundudao.ui.reportform.adapter.ReportFormAdapter;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.pieChart.PieView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportFormActivity extends Activity implements OnClickListener, IReportFormView
{
    // gridview_linearlayout
    
    private PieView piechartview;
    
    private TextView week_data;
    
    private TextView mouth_data;
    
    private TextView top_name;
    
    private ImageView go_back;
    
    private LinearLayout most_layout;
    
    private ScrollView scrollview;
    
    private TextView most_type_name;
    
    private List<PieData> chartData;
    
    private ListView list_data;
    
    private ReportFormAdapter adapter;
    
    private TextView more_data;
    
    private ReportFormPresenter presenter;
    
    private long startTime, endTime;
    
    private ShareUtil shareUtil;
    
    private boolean isMouth = false;
    
    private LinearLayout reportform_data_layout;
    
    private TextView no_data_reportform;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportfrom);
        reportform_data_layout=(LinearLayout)findViewById(R.id.reportform_data_layout);
        no_data_reportform=(TextView)findViewById(R.id.no_data_reportform);
        piechartview = (PieView)findViewById(R.id.piechartview);
        list_data = (ListView)findViewById(R.id.list_data);
        more_data = (TextView)findViewById(R.id.more_data);
        week_data = (TextView)findViewById(R.id.week_data);
        mouth_data = (TextView)findViewById(R.id.mouth_data);
        top_name = (TextView)findViewById(R.id.top_name);
        go_back = (ImageView)findViewById(R.id.go_back);
        most_layout = (LinearLayout)findViewById(R.id.most_layout);
        most_type_name = (TextView)findViewById(R.id.most_type_name);
        scrollview=(ScrollView)findViewById(R.id.scrollview);
        presenter = new ReportFormPresenter(this);
        chartData = new ArrayList<PieData>();
        adapter = new ReportFormAdapter(this);
        list_data.setAdapter(adapter);
        more_data.setOnClickListener(this);
        week_data.setOnClickListener(this);
        mouth_data.setOnClickListener(this);
        go_back.setOnClickListener(this);
        top_name.setText("报表");
        piechartview.setVisibility(View.GONE);
        most_layout.setVisibility(View.GONE);
        shareUtil = new ShareUtil(this);
        most_type_name.setText("");
        getWeek();
        presenter.getForm(shareUtil.getShare().getUser_id() + "", startTime, endTime);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.more_data:
                Intent intent = new Intent(ReportFormActivity.this, FormTypeActivity.class);
                startActivity(intent);
                break;
            case R.id.go_back:
                finish();
                break;
            case R.id.week_data:
                if (isMouth)
                {
                    upDataView(week_data, mouth_data);
                    getWeek();
                    presenter.getForm(shareUtil.getShare().getUser_id() + "", startTime, endTime);
                    isMouth = !isMouth;
                }
                break;
            case R.id.mouth_data:
                if (!isMouth)
                {
                    upDataView(mouth_data, week_data);
                    getMoth();
                    presenter.getForm(shareUtil.getShare().getUser_id() + "", startTime, endTime);
                    isMouth = !isMouth;
                }
                break;
            default:
                break;
        }
    }
    
    public void upDataView(TextView textView1, TextView textView2)
    {
        textView1.setBackgroundResource(R.drawable.textview_suface);
        textView1.setTextColor(getResources().getColor(R.color.main_color));
        
        textView2.setBackgroundResource(R.color.back_color);
        textView2.setTextColor(getResources().getColor(R.color.biantai_gray));
    }
    
    @Override
    public void getSucess(FormBean formBean)
    {
        if (formBean != null && formBean.getResult() == 0)
        {
            reportform_data_layout.setVisibility(View.VISIBLE);
            no_data_reportform.setVisibility(View.GONE);
            chartData.clear();
            // piechartview.setVisibility(View.GONE);
            most_layout.setVisibility(View.VISIBLE);
            List<FormType> types = formBean.getTypestatistic();
            if (types != null && types.size() != 0)
            {
                Collections.sort(types, new Comparator<FormType>()
                {
                    @Override
                    public int compare(FormType o1, FormType o2)
                    {
                        FormType stu1=(FormType)o1;  
                        FormType stu2=(FormType)o2;  
                        if(stu1.getProblemcnt()>stu2.getProblemcnt()){  
                            return 1;  
                        }else if(stu1.getProblemcnt()==stu2.getProblemcnt()){  
                            return 0;  
                        }else{  
                            return -1;  
                        }  
                    }
                });
                int max = 0;
                Collections.reverse(types);
                for (int i = 0; i < types.size(); i++)
                {
                    if (i > 3)
                    {
                        types.remove(i);
                    }
                    else
                    {
                        max = max + types.get(i).getProblemcnt();
                    }
                }
                most_type_name.setText(types.get(0).getTypename());
                int sum = 0;
                for (int i = 0; i < types.size(); i++)
                {
                    int color = Color.rgb(22, 160, 232);
                    if (i == 0)
                    {
                        color = Color.rgb(22, 160, 232);
                    }
                    else if (i == 1)
                    {
                        color = Color.rgb(252, 131, 42);
                    }
                    else if (i == 2)
                    {
                        color = Color.rgb(247, 116, 116);
                    }
                    else if (i == 3)
                    {
                        //55, 178, 130
                        color = Color.rgb(55, 178, 130);
                    }
                    int m = types.get(i).getProblemcnt() * 100 / max;
                    if (i == (types.size() - 1))
                    {
                        if ((sum + m) < 100)
                        {
                            chartData.add(new PieData(types.get(i).getTypename(), 100 - sum + "%", 100 - sum, color));
                        }
                        else
                        {
                            chartData.add(new PieData(types.get(i).getTypename(), m + "%", m, color));
                        }
                    }
                    else
                    {
                        chartData.add(new PieData(types.get(i).getTypename(), m + "%", m, color));
                    }
                    sum = sum + m;
                }
            }
            else
            {
                piechartview.setVisibility(View.GONE);
                most_layout.setVisibility(View.GONE);
                most_type_name.setText("");
                adapter.setData(null);
                adapter.notifyDataSetChanged();
            }
            piechartview.setData(chartData);
            List<FormFactory> factories = formBean.getStatusstatistic();
            if (factories != null && factories.size() != 0)
            {
                adapter.setData(factories);
                adapter.notifyDataSetChanged();
                piechartview.setVisibility(View.VISIBLE);
                setListViewHeightBasedOnChildren(list_data);
                scrollview.smoothScrollBy(0, 0);
            }
            
            if((types == null || types.size() == 0)&&(factories == null || factories.size() == 0))
            {
                reportform_data_layout.setVisibility(View.GONE);
                no_data_reportform.setVisibility(View.VISIBLE);
                no_data_reportform.setText("当前期间内无数据!");
            }
        }
        else
        {
            reportform_data_layout.setVisibility(View.GONE);
            no_data_reportform.setVisibility(View.VISIBLE);
            no_data_reportform.setText("当前期间内无数据!");
        }
    }
    
    @Override
    public void getError()
    {
        reportform_data_layout.setVisibility(View.GONE);
        no_data_reportform.setVisibility(View.VISIBLE);
        Toast.makeText(this, "请检测网络!", Toast.LENGTH_SHORT).show();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // cal.add(Calendar.DATE, 6);
        // String imptimeEnd = sdf.format(cal.getTime());
        
        // int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        // if (1 == dayWeek) {
        // cal.add(Calendar.DAY_OF_MONTH, -1);
        // }
        // cal.setFirstDayOfWeek(Calendar.MONDAY);
        // // cal.set(Calendar.DAY_OF_WEEK, 1);
        // String start = dateFormater.format(cal.getTime()) + " 00:00:00";
        // cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        // // String end = dateFormater.format(cal.getTime()) + " 24:00:00";
        // try
        // {
        // startTime = format.parse(start).getTime();
        // endTime = new Date().getTime();
        // }
        // catch (ParseException e)
        // {
        // e.printStackTrace();
        // }
        
        
//        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        cal.setFirstDayOfWeek(Calendar.MONDAY); 
//        cal.set(Calendar.DAY_OF_WEEK, 1);
//        String start = dateFormater.format(cal.getTime()) + " 00:00:00";
//        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
//        String end = dateFormater.format(cal.getTime()) + " 23:59:59";
//        try
//        {
//            startTime = format.parse(start).getTime();
//            endTime = format.parse(end).getTime();
//        }
//        catch (ParseException e)
//        {
//            e.printStackTrace();
//        }
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
}
