package com.vomont.yundudao.ui.createproblem.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.calendar.KCalendar;
import com.vomont.yundudao.ui.calendar.KCalendar.OnCalendarClickListener;
import com.vomont.yundudao.ui.calendar.KCalendar.OnCalendarDateChangedListener;

public class DatePopuWindow extends PopupWindow
{
    private View mMenuView;
    
    private KCalendar send_img_date;
    
    private String date;
    
    private RelativeLayout popupwindow_calendar_date_left;
    
    private RelativeLayout popupwindow_calendar_date_right;
    
    private TextView popupwindow_calendar_datetime;
    
    private SetClickBack setClickBack;
    
    private int day;
    
    @SuppressLint({"SimpleDateFormat", "InflateParams"})
    public DatePopuWindow(Activity context)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.date_alert_dialog, null);
        send_img_date = (KCalendar)mMenuView.findViewById(R.id.send_img_date);
        popupwindow_calendar_date_left = (RelativeLayout)mMenuView.findViewById(R.id.popupwindow_calendar_date_left);
        popupwindow_calendar_date_right = (RelativeLayout)mMenuView.findViewById(R.id.popupwindow_calendar_date_right);
        popupwindow_calendar_datetime = (TextView)mMenuView.findViewById(R.id.popupwindow_calendar_datetime);
        
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = sFormat.format(new Date());
        
        if (null != date)
        {
            int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
            int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
            day = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1, date.length()));
            popupwindow_calendar_datetime.setText(years + "/" + timeShow(month));
            send_img_date.showCalendar(years, month);
            send_img_date.setCalendarDayBgColor(date, R.drawable.new_dot_three);
        }
        
        // 监听所选中的日期
        send_img_date.setOnCalendarClickListener(new OnCalendarClickListener()
        {
            public void onCalendarClick(int row, int col, String dateFormat)
            {
                int year = Integer.parseInt(dateFormat.substring(0, dateFormat.indexOf("-")));
                int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));
                
                if (send_img_date.getCalendarMonth() - month == 1// 跨年跳转
                    || send_img_date.getCalendarMonth() - month == -11)
                {
                    send_img_date.lastMonth();
                }
                else if (month - send_img_date.getCalendarMonth() == 1 // 跨年跳转
                    || month - send_img_date.getCalendarMonth() == -11)
                {
                    send_img_date.nextMonth();
                    
                }
                else
                {
                    send_img_date.removeAllBgColor();
                    send_img_date.setCalendarDayBgColor(dateFormat, R.drawable.new_dot_three);
                    date = dateFormat;// 最后返回给全局 date
                    day = Integer.parseInt(dateFormat.substring(dateFormat.lastIndexOf("-") + 1, dateFormat.length()));
                    popupwindow_calendar_datetime.setText(year + "/" + timeShow(month));
                    setClickBack.getDateString(year + "/" + month + "/" + day);
                    dismiss();
                }
            }
        });
        
        popupwindow_calendar_date_left.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                send_img_date.lastMonth();
            }
        });
        
        send_img_date.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener()
        {
            @Override
            public void onCalendarDateChanged(int year, int month)
            {
                popupwindow_calendar_datetime.setText(year + "/" + timeShow(month));
            }
        });
        popupwindow_calendar_date_right.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                send_img_date.nextMonth();
            }
        });
        initView();
    }
    
    public String timeShow(int time)
    {
        if (time < 10)
        {
            return "0" + time;
        }
        return time + "";
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("deprecation")
    private void initView()
    {
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AlertDialogStyle);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener()
        {
            
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                
                if (mMenuView.findViewById(R.id.pop_layout) != null)
                {
                    int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                    int y = (int)event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        if (y < height)
                        {
                            dismiss();
                        }
                    }
                }
                return true;
            }
        });
        
    }
    
    public interface SetClickBack
    {
        
        void getDateString(String date);
        
    }
    
    public void setOnClickBack(SetClickBack setClickBack)
    {
        this.setClickBack = setClickBack;
    }
    
}
