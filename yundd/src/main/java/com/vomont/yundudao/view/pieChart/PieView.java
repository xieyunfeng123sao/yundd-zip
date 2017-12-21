package com.vomont.yundudao.view.pieChart;

import java.util.List;

import org.xclcharts.chart.PieData;

import com.vomont.yundudao.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PieView extends RelativeLayout
{
    
    private List<PieData> mlist;
    
    private ItemChartView itemChartView;
    
    private LinearLayout addview;
    
    public PieView(Context context)
    {
        super(context);
        initView();
    }
    
    public PieView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }
    
    public PieView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView();
    }
    
    public void setData(List<PieData> mlist)
    {
        this.mlist = mlist;
        itemChartView.setData(mlist);
        addview.removeAllViews();
        for (int i = 0; i < mlist.size(); i++)
        {
            View item = LayoutInflater.from(getContext()).inflate(R.layout.piechart_item, null);
            View item_color = item.findViewById(R.id.item_color);
            TextView item_text = (TextView)item.findViewById(R.id.item_name);
            item_color.setBackgroundColor(mlist.get(i).getSliceColor());
            item_text.setText(mlist.get(i).getKey());
            addview.addView(item);
        }
        invalidate();
    }
    
    private void initView()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.piechartview, this, true);
        itemChartView = (ItemChartView)findViewById(R.id.itemchartview);
        addview = (LinearLayout)findViewById(R.id.addview);
    }
}
