package com.vomont.yundudao.view.pieChart;

import java.util.List;

import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.XEnum.LineStyle;
import org.xclcharts.renderer.plot.PlotLegend;
import org.xclcharts.view.ChartView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

public class ItemChartView extends ChartView implements Runnable
{
    private PieChart chart = new PieChart();
    
    private List<PieData> chartData;
    
    private String TAG = "PieChartView";
    
    private int width;
    
    private int height;
    
    public ItemChartView(Context context)
    {
        super(context);
        initView();
    }
    
    public ItemChartView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }
    
    public ItemChartView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView();
    }
    
    public void setData(List<PieData> chartData)
    {
//        this.chartData = chartData;
        chart.setDataSource(chartData);
        invalidate();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        // 图所占范围大小
        chart.setChartRange(DensityUtil.dip2px(getContext(), 180), DensityUtil.dip2px(getContext(), 180));
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取宽度大小
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
    
    private void initView()
    {
        // chartDataSet();
        chartRender();
        // 綁定手势滑动事件
        // this.bindTouch(this,chart);
         new Thread(this).start();
    }
    
    private void chartRender()
    {
        try
        {
            // 设置绘图区默认缩进px值
            chart.setPadding(0, 0, width, height);
            // 设置起始偏移角度(即第一个扇区从哪个角度开始绘制)
            // 标签显示(隐藏，显示在中间，显示在扇区外面)
            chart.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);
            chart.getLabelPaint().setColor(Color.WHITE);
            chart.getLabelPaint().setTextSize(22);
            chart.getArcBorderPaint().setColor(Color.WHITE);
            chart.getArcBorderPaint().setStrokeWidth(2);
            
//            chart.setDataSource(chartData);
            // 显示图例
            PlotLegend legend = chart.getPlotLegend();
            legend.hide();
            legend.getPaint().setTextSize(25);
            legend.setType(XEnum.LegendType.COLUMN);
            legend.setHorizontalAlign(XEnum.HorizontalAlign.RIGHT);
            legend.setVerticalAlign(XEnum.VerticalAlign.MIDDLE);
            legend.getBox().setBorderLineStyle(LineStyle.DOT);
            legend.getBox().setBorderLineColor(Color.WHITE);
            legend.showBox();
            // legend.hideBox();
            legend.hideBorder();
        }
        catch (Exception e)
        {
            Log.e(TAG, e.toString());
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
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void run()
    {
        try {          
            chartAnimation();           
         }
         catch(Exception e) {
             Thread.currentThread().interrupt();
         }  
    }
    
    private void chartAnimation()
    {
          try {       
//                chart.setDataSource(chartData);
                int count = 360 / 10;
                
                for(int i=1;i<count ;i++)
                {
                    Thread.sleep(40);
                    
                    chart.setTotalAngle(10 * i);
                    
//                    //激活点击监听
                    if(count - 1 == i)
                    {
                        chart.setTotalAngle(360);
//                        
//                        chart.ActiveListenItemClick();
////                        //显示边框线，并设置其颜色
////                        chart.getArcBorderPaint().setColor(Color.YELLOW);
////                        chart.getArcBorderPaint().setStrokeWidth(3);
                    }
//                    
                    postInvalidate();                                                   
              }
              
          }
          catch(Exception e) {
              Thread.currentThread().interrupt();
          }       
          
    }
}
