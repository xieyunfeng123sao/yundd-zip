package com.vomont.yundudao.ui.reportform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FormBean;
import com.vomont.yundudao.bean.FormSubBean;
import com.vomont.yundudao.bean.FormSubInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.bean.TagInfo;
import com.vomont.yundudao.mvpview.reportform.IReportMoreView;
import com.vomont.yundudao.presenter.reportform.ReportMorePresenter;
import com.vomont.yundudao.ui.reportform.adapter.MoreListAdapter;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.flow.FlowLayout;
import com.vomont.yundudao.view.flow.TagAdapter;
import com.vomont.yundudao.view.flow.TagFlowLayout;
import com.vomont.yundudao.view.flow.TagFlowLayout.OnTagClickListener;

public class ReportMoreActivity extends Activity implements IReportMoreView
{
    private TagFlowLayout tag_report;
    
    private ImageView go_back;
    
    private TextView top_name;
    
    private ListView list_report;
    
    private MoreListAdapter adapter;
    
    private List<TagInfo> allInfos;
    
    private List<TagInfo> sendTags;
    
    private List<TagInfo> needTags;
    
    private List<Boolean> needList;
    
    private List<TextView> tvList;
    
    private long startTime, endTime;
    
    private List<SubFactory> subFactories;
    
    private TextView report_starttime;
    
    private TextView report_endtime;
    
    private ScrollView scrollview;
    
    private long weekLength = 7 * 24 * 60 * 60 * 1000;
    
    private long oneDayLength = 24 * 60 * 60 * 1000;
    
    private ReportMorePresenter presenter;
    
    private ShareUtil shareUtil;
    
    private String tagsId;
    
    private String subsId;
    
    // private List<FormSubInfo> mlistFInfos;
    
    private HashMap<Integer, List<FormSubInfo>> map = new HashMap<Integer, List<FormSubInfo>>();
    
    private Dialog dialog;
    
    @SuppressLint("SimpleDateFormat")
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_more);
        tag_report = (TagFlowLayout)findViewById(R.id.tag_report);
        list_report = (ListView)findViewById(R.id.list_report);
        report_starttime = (TextView)findViewById(R.id.report_starttime);
        report_endtime = (TextView)findViewById(R.id.report_endtime);
        scrollview = (ScrollView)findViewById(R.id.scrollview);
        allInfos = (List<TagInfo>)getIntent().getSerializableExtra("tags");
        sendTags = (List<TagInfo>)getIntent().getSerializableExtra("types");
        needTags = new ArrayList<TagInfo>();
        needList = new ArrayList<Boolean>();
        tvList = new ArrayList<TextView>();
        for (int i = 0; i < sendTags.size(); i++)
        {
            int color = 0;
            switch (i)
            {
                case 0:
                    color = Color.rgb(55, 178, 130);
                    break;
                case 1:
                    color = Color.rgb(252, 131, 42);
                    break;
                case 2:
                    color = Color.rgb(22, 160, 232);
                    break;
                case 3:
                    color = Color.rgb(247, 116, 116);
                    break;
                case 4:
                    color = Color.rgb(0, 191, 255);
                    break;
                default:
                    break;
            }
            sendTags.get(i).setColor(color);
        }
        
        needTags.addAll(sendTags);
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        startTime = getIntent().getLongExtra("starttime", 0);
        endTime = getIntent().getLongExtra("endtime", 0);
        go_back.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        top_name.setText("分析结果");
        
        subFactories = (List<SubFactory>)getIntent().getSerializableExtra("sub");
        tagsId = "";
        if (sendTags != null)
        {
            for (TagInfo tagInfo : sendTags)
            {
                tagsId = tagsId + tagInfo.getTypeid() + ";";
            }
        }
        subsId = "";
        if (subFactories != null)
        {
            for (SubFactory subFactory : subFactories)
            {
                List<FormSubInfo> forInfos = new ArrayList<FormSubInfo>();
                map.put(subFactory.getSubfactoryid(), forInfos);
                subsId = subsId + subFactory.getSubfactoryid() + ";";
            }
        }
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        report_starttime.setText(format.format(new Date(startTime)));
        report_endtime.setText(format.format(new Date(endTime)));
        final LayoutInflater mInflater = LayoutInflater.from(this);
        tag_report.setAdapter(new TagAdapter<TagInfo>(sendTags)
        {
            @Override
            public View getView(FlowLayout parent, int position, TagInfo t)
            {
                TextView tv = (TextView)mInflater.inflate(R.layout.item_report_more_gridview, parent, false);
                tv.setText(t.getTypename());
                tv.setTextColor(getResources().getColor(R.color.text_color));
                for (int i = 0; i < sendTags.size(); i++)
                {
                    if (t.getTypeid() == sendTags.get(i).getTypeid())
                    {
                        tv.setBackgroundResource(R.drawable.report_select_no);
                        tv.setTextColor(getResources().getColor(R.color.white));
                    }
                }
                tvList.add(tv);
                needList.add(true);
                return tv;
            }
        });
        tag_report.setOnTagClickListener(new OnTagClickListener()
        {
            
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent)
            {
                if (needList.get(position))
                {
                    needList.set(position, false);
                    needTags.remove(sendTags.get(position));
                    tvList.get(position).setBackgroundResource(R.drawable.report_select_sure);
                    tvList.get(position).setTextColor(getResources().getColor(R.color.main_color));
                }
                else
                {
                    needList.set(position, true);
                    needTags.add(sendTags.get(position));
                    tvList.get(position).setBackgroundResource(R.drawable.report_select_no);
                    tvList.get(position).setTextColor(getResources().getColor(R.color.white));
                }
                adapter.setData(map, startTime, endTime, needTags);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        adapter = new MoreListAdapter(this);
        list_report.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        scrollview.smoothScrollTo(0, 0);
        presenter = new ReportMorePresenter(this);
        shareUtil = new ShareUtil(this);
        dialog = ProgressDialog.createLoadingDialog(this, "");
        dialog.show();
        new Thread(runnable).start();
    }
    
    Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            getData();
        }
    };
    
    public void getData()
    {
        // mlistFInfos = new ArrayList<FormSubInfo>();''
        if ((endTime - startTime) > weekLength)
        {
            // 大于一周
            int size = (int)((endTime - startTime) / (weekLength) + ((endTime - startTime) % weekLength == 0 ? 0 : 1));
            for (int i = 0; i < 6; i++)
            {
                long start = startTime + i * oneDayLength * size;
                long end;
                if (i == 5)
                {
                    end = endTime;
                }
                else
                {
                    end = start + size * oneDayLength;
                }
                String result = presenter.getFormMore(shareUtil.getShare().getUserid() + "", start, end, tagsId, subsId);
                if (result != null)
                {
                    logicData(start, end, result);
                }
            }
        }
        else
        {
            // 一周以内
            int size = (int)((endTime - startTime) / oneDayLength + ((endTime - startTime) % oneDayLength == 0 ? 0 : 1));
            for (int i = 0; i < size; i++)
            {
                long start = startTime + i * oneDayLength;
                long end = (i == (size - 1)) ? endTime : (start + oneDayLength);
                String result = presenter.getFormMore(shareUtil.getShare().getUserid() + "", start, end, tagsId, subsId);
                if (result != null)
                {
                    logicData(start, end, result);
                }
            }
        }
        handler.sendEmptyMessage(20);
    }
    
    /**
     * 处理返回数据的逻辑方法
     * 
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param result
     *            返回的参数
     */
    private void logicData(long startTime, long endTime, String result)
    {
        Gson gson = new Gson();
        FormSubBean call = gson.fromJson(result, FormSubBean.class);
        List<FormSubInfo> subFormBeans = call.getStatistic();
        int resultCall = call.getResult();
        if (resultCall == 0)
        {
            if (subFormBeans != null)
            {
                for (FormSubInfo subInfo : subFormBeans)
                {
                    subInfo.setStartTime(startTime);
                    subInfo.setEndTime(endTime);
                    // 判断是否已经有了该厂区的数据
                    if (map.containsKey(subInfo.getSubfactoryid()))
                    {
                        map.get(subInfo.getSubfactoryid()).add(subInfo);
                    }
                }
            }
        }
    }
    
    Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 20:
                    adapter.setData(map, startTime, endTime, needTags);
                    adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(list_report);
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        };
    };
    
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
        listView.setLayoutParams(params);
    }
    
    @Override
    public void getSucess(FormBean formBean)
    {
     
    }
    
    @Override
    public void getError()
    {
        
    }
    
}
