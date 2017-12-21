package com.vomont.yundudao.view.pieChart;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.common.IFormatterDoubleCallBack;
import org.xclcharts.common.IFormatterTextCallBack;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.view.ChartView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FormSubCount;
import com.vomont.yundudao.bean.FormSubInfo;
import com.vomont.yundudao.bean.TagInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LineChartView extends ChartView implements Runnable
{
//    private String TAG = "LineChart02View";
    
    private LineChart chart = new LineChart();
    
    // 标签集合
    private LinkedList<String> labels = new LinkedList<String>();
    
    private LinkedList<LineData> chartData = new LinkedList<LineData>();
    
    private List<CustomLineData> mCustomLineDataset = new LinkedList<CustomLineData>();
    
    private long weekLength = 7 * 24 * 60 * 60 * 1000;
    
    private long oneDayLength = 24 * 60 * 60 * 1000;
    
    private int size;
    
    private List<Long> days = new ArrayList<Long>();
    
    public LineChartView(Context context)
    {
        super(context);
        initView();
    }
    
    public LineChartView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }
    
    public LineChartView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView();
    }
    
    private void initView()
    {
        // chartLabels(null);
        // chartRender();
        // chartDataSet(null);
        // 綁定手势滑动事件
        // this.bindTouch(this, chart);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        // 图所占范围大小
        chart.setChartRange(w, h);
    }
    
    private void chartRender()
    {
        try
        {
            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            chart.setPadding(DensityUtil.dip2px(getContext(), 45), ltrb[1], ltrb[2], ltrb[3]);
            // 设定数据源
            chart.setCategories(labels);
            
            chart.getCategoryAxis().setTickLabelMargin(DensityUtil.dip2px(getContext(), 15));
            //设置日期的文字的大小
            chart.getCategoryAxis().getTickLabelPaint().setTextSize(DensityUtil.dip2px(getContext(),10));
            // 指隔多少个轴刻度(即细刻度)后为主刻度
            // chart.getDataAxis().setDetailModeSteps(1);
            
            // 底部的标题
            // chart.getAxisTitle().setLeftTitle("问题数量");
            // chart.getAxisTitle().getLeftTitlePaint().setColor(Color.GRAY);
            // chart.getAxisTitle().getLeftTitlePaint().setTextSize(DensityUtil.dip2px(getContext(),
            // 10));
            // 左边的标题
            // chart.getAxisTitle().setLowerTitle("时间");
            // chart.getAxisTitle().getLowerTitlePaint().setColor(Color.GRAY);
            // chart.getAxisTitle().getLowerTitlePaint().setTextSize(DensityUtil.dip2px(getContext(),
            // 10));
            
            // 背景网格
            chart.getPlotGrid().showHorizontalLines();
            chart.getPlotGrid().getHorizontalLinePaint().setColor(getResources().getColor(R.color.gray_qian));
            chart.getPlotGrid().getHorizontalLinePaint().setStrokeWidth(0.1f);
            chart.getPlotGrid().showVerticalLines();
            chart.getPlotGrid().getVerticalLinePaint().setColor(getResources().getColor(R.color.gray_qian));
            chart.getPlotGrid().getVerticalLinePaint().setStrokeWidth(0.1f);
            // 设置轴风格
            // chart.getDataAxis().setTickMarksVisible(false);
            chart.getDataAxis().getAxisPaint().setStrokeWidth(2);
            chart.getDataAxis().getTickMarksPaint().setStrokeWidth(2);
            // chart.getDataAxis().showAxisLabels();
            chart.getDataAxis().getTickLabelPaint().setColor(getResources().getColor(R.color.gray_shen));
            chart.getDataAxis().getAxisPaint().setColor(getResources().getColor(R.color.gray_shen));
            chart.getDataAxis().hideTickMarks();
            
            chart.getCategoryAxis().getAxisPaint().setStrokeWidth(2);
            chart.getCategoryAxis().getTickLabelPaint().setColor(getResources().getColor(R.color.gray_shen));
            chart.getCategoryAxis().getAxisPaint().setColor(getResources().getColor(R.color.gray_shen));
            chart.getCategoryAxis().hideTickMarks();
                //折线图备注
            chart.getPlotLegend().getPaint().setTextSize(DensityUtil.dip2px(getContext(), 1));
            chart.getPlotLegend().hide();
            // 定义数据轴标签显示格式
            chart.getDataAxis().setLabelFormatter(new IFormatterTextCallBack()
            {
                @Override
                public String textFormatter(String value)
                {
                    Double tmp = Double.parseDouble(value);
                    DecimalFormat df = new DecimalFormat("#0");
                    String label = df.format(tmp).toString();
                    return (label);
                }
            });
            
            // 定义线上交叉点标签显示格式
            chart.setItemLabelFormatter(new IFormatterDoubleCallBack()
            {
                @Override
                public String doubleFormatter(Double value)
                {
                    DecimalFormat df = new DecimalFormat("#1");
                    String label = df.format(value).toString();
                    return label;
                }
            });
            
            // 允许线与轴交叉时，线会断开
            chart.setLineAxisIntersectVisible(false);
            
            // 动态线
            chart.showDyLine();
            // 不封闭
            chart.setAxesClosed(false);
            // 扩展绘图区右边分割的范围，让定制线的说明文字能显示出来
            chart.getClipExt().setExtRight(150.f);
            // 设置标签交错换行显示
            chart.getCategoryAxis().setLabelLineFeed(XEnum.LabelLineFeed.NORMAL);
            // 禁止滑动
            chart.disablePanMode();
        }
        catch (Exception e)
        {
//            Log.e(TAG, e.toString());
        }
    }
    
    @SuppressLint("SimpleDateFormat")
    public void setDataChart(List<FormSubInfo> mlist, long startTime, long endTime, List<TagInfo> tagInfos)
    {
        labels.clear();
        days.clear();
        chartData.clear();
        if ((endTime - startTime) > weekLength)
        {
            // 大于一周
            size = (int)((endTime - startTime) / (weekLength)+((endTime - startTime) % weekLength == 0 ? 0 : 1));
            for (int i = 0; i < 6; i++)
            {
                long start = startTime + i * oneDayLength*size;
                long end = (i == 5) ? endTime : (start +  oneDayLength*size);
                if (i == 0)
                {
                    labels.add(new SimpleDateFormat("MM-dd").format(new Date(start)));
                    days.add(start);
                    labels.add(new SimpleDateFormat("MM-dd").format(new Date(end-1000)));
                    days.add(end);
                }
                else
                {
                    labels.add(new SimpleDateFormat("MM-dd").format(new Date(end)));
                    days.add(end);
                }
            }
        }
        else
        {
            // 一周以内
            size = (int)((endTime - startTime) / oneDayLength + ((endTime - startTime) % oneDayLength == 0 ? 0 : 1));
            for (int i = 0; i < size; i++)
            {
                long start = startTime + i * oneDayLength;
                long end = (i == (size - 1)) ? endTime : (start + oneDayLength);
                if (i == 0)
                {
                    labels.add("");
                    days.add(start);
                    labels.add(new SimpleDateFormat("MM-dd").format(new Date(end-1000)));
                    days.add(end);
                }
                else
                {
                    labels.add(new SimpleDateFormat("MM-dd").format(new Date(end-1000)));
                    days.add(end);
                }
            }
            addLab();
        }
        cavLineWeek(mlist, tagInfos);
        chartRender();
        new Thread(this).start();
        postInvalidate();
    }
    
    
    @SuppressLint("UseSparseArrays")
    private void cavLineWeek(List<FormSubInfo> mlist, List<TagInfo> tagInfos)
    {
        HashMap<Long, FormSubInfo> map = new HashMap<Long, FormSubInfo>();
        for (int i = 0; i < days.size(); i++)
        {
            for (int j = 0; j < mlist.size(); j++)
            {
                if (days.get(i) == mlist.get(j).getEndTime())
                {
                    map.put(days.get(i), mlist.get(j));
                }
            }
        }
        
        int max = 0;
        for (int i = 0; i < tagInfos.size(); i++)
        {
            LinkedList<Double> dataSeries = new LinkedList<Double>();
            for (int j = 0; j < days.size(); j++)
            {
                if (map.get(days.get(j)) != null)
                {
                    boolean hasAdd = false;
                    for (FormSubCount subCount : map.get(days.get(j)).getTypestatistic())
                    {
                        if (tagInfos.get(i).getTypeid() == subCount.getTypeid())
                        {
                            hasAdd = true;
                            dataSeries.add(Double.parseDouble(subCount.getCount() + ""));
                            if (subCount.getCount() > max)
                            {
                                max = subCount.getCount();
                            }
                            break;
                        }
                    }
                    if (!hasAdd)
                        dataSeries.add(0.01d);
                }
                else
                {
                    dataSeries.add(0.01d);
                }
            }
         
            int num = 0;
            num = (max / 5 + 1) * 5;
            chart.getDataAxis().setAxisMax(num);
            // 数据轴刻度间隔
            chart.getDataAxis().getTickLabelPaint().setTextSize(DensityUtil.dip2px(getContext(), 11));
//            chart.getDataAxis().getTickMarksPaint().setTextSize(DensityUtil.dip2px(getContext(), 13));
//            chart.getDataAxis().getAxisPaint().setTextSize(DensityUtil.dip2px(getContext(), 13));
//            chart.getAnchorDataPoint().get(i).setTextSize(100);
//            chart.getCategoryAxis().
//            chart.getDataSource().get(i).get
            
            chart.getDataAxis().setAxisSteps(num / 5);
            LineData lineData = new LineData(tagInfos.get(i).getTypename(), dataSeries, tagInfos.get(i).getColor());
            lineData.setDotStyle(XEnum.DotStyle.RING);
            lineData.getLinePaint().setStrokeWidth(3);
//            chart.getDataAxis()
            chartData.add(lineData);
        }
       
//    
    }
    
    private void addLab()
    {
        if (labels.size() < 8)
        {
            labels.add("");
            addLab();
        }
    }

    
    @Override
    public void render(Canvas canvas)
    {
        try
        {
            chart.render(canvas);
        }
        catch (Exception e)
        {
//            Log.e(TAG, e.toString());
        }
    }
    
    @Override
    public void run()
    {
        try
        {
            chartAnimation();
        }
        catch (Exception e)
        {
            Thread.currentThread().interrupt();
        }
    }
    
    private void chartAnimation()
    {
        try
        {
            
            if (chartData != null && chartData.size() != 0)
            {
                int count = chartData.size();
                for (int i = 0; i < count; i++)
                {
                    Thread.sleep(200);
                    LinkedList<LineData> animationData = new LinkedList<LineData>();
                    for (int j = 0; j <= i; j++)
                    {
                        animationData.add(chartData.get(j));
                    }
                    // Log.e(TAG,"size = "+animationData.size());
                    chart.setDataSource(animationData);
                    if (i == count - 1)
                    {
                        chart.getDataAxis().show();
                        chart.getDataAxis().showAxisLabels();
                        chart.setCustomLines(mCustomLineDataset);
                    }
                    postInvalidate();
                }
            }
            else
            {
                chart.setDataSource(chartData);
            }
        }
        catch (Exception e)
        {
            Thread.currentThread().interrupt();
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        
        super.onTouchEvent(event);
        
        // if(event.getAction() == MotionEvent.ACTION_UP)
        // {
        // //交叉线
        // if(chart.getDyLineVisible())
        // {
        // chart.getDyLine().setCurrentXY(event.getX(),event.getY());
        // if(chart.getDyLine().isInvalidate())this.invalidate();
        // }
        // }
        return true;
    }
    
    // Demo中bar chart所使用的默认偏移值。
    // 偏移出来的空间用于显示tick,axistitle....
    protected int[] getBarLnDefaultSpadding()
    {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 40); // left
        ltrb[1] = DensityUtil.dip2px(getContext(), 60); // top
        ltrb[2] = DensityUtil.dip2px(getContext(), 20); // right
        ltrb[3] = DensityUtil.dip2px(getContext(), 40); // bottom
        return ltrb;
    }
    
    protected int[] getPieDefaultSpadding()
    {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), 20); // left
        ltrb[1] = DensityUtil.dip2px(getContext(), 65); // top
        ltrb[2] = DensityUtil.dip2px(getContext(), 20); // right
        ltrb[3] = DensityUtil.dip2px(getContext(), 20); // bottom
        return ltrb;
    }
}
