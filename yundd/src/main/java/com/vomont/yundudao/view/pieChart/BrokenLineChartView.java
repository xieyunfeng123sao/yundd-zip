package com.vomont.yundudao.view.pieChart;

import java.util.List;

import org.xclcharts.chart.PieData;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FormSubInfo;
import com.vomont.yundudao.bean.TagInfo;
import com.vomont.yundudao.view.NoScrollGridView.NoScrollGridView;
import com.vomont.yundudao.view.pieChart.adapter.ItemTagAdapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class BrokenLineChartView extends LinearLayout
{
    
    private LineChartView lineChartView;
    
    private NoScrollGridView tag_name;
    
    public BrokenLineChartView(Context context)
    {
        super(context);
        initView();
    }
    
    public BrokenLineChartView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }
    
    public BrokenLineChartView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView();
    }
    
    public void setData(List<FormSubInfo> mlist, long startTime, long endTime, List<TagInfo> tagInfos)
    {
        
        lineChartView.setDataChart(mlist, startTime, endTime, tagInfos);
        // addview.removeAllViews();
        // for (int i = 0; i < mlist.size(); i++)
        // {
        // View item = LayoutInflater.from(getContext()).inflate(R.layout.piechart_item, null);
        // View item_color = item.findViewById(R.id.item_color);
        // TextView item_text = (TextView)item.findViewById(R.id.item_name);
        // item_color.setBackgroundColor(mlist.get(i).getSliceColor());
        // item_text.setText(mlist.get(i).getKey());
        // addview.addView(item);
        // }
        ItemTagAdapter adapter = new ItemTagAdapter(getContext(), tagInfos);
        tag_name.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        invalidate();
    }
    
    private void initView()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.brokenlinechartview, this, true);
        lineChartView = (LineChartView)findViewById(R.id.item_view);
        tag_name = (NoScrollGridView)findViewById(R.id.tag_name);
    }
}
