package com.vomont.yundudao.view.playback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vomont.yundudao.R;
import com.vomont.yundudao.utils.DensityUtil;
import com.wmclient.clientsdk.WMFileInfo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

@SuppressLint("ClickableViewAccessibility")
public class PlayBackSeekBar extends View
{
    public int SEEK_STAE_24 = 0;// 一天
    
    public int SEEK_STAE_1 = 1;// 1小时
    
    public int SEEK_STAE_5 = 2;// 5分钟
    
    private int seek_state = 0;
    
    // 进度条的画笔
    Paint paint = new Paint();
    
    // 刻度的画笔
    Paint paint1 = new Paint();
    
    // 刻度值的画笔
    Paint paint2 = new Paint();
    
    // 未播放模块时间片段
    Paint paint3 = new Paint();
    
    // 已播放模块时间片段
    Paint paint4 = new Paint();
    
    // 滑块
    private Bitmap mImage;
    
    // 屏幕宽度
    private int width;
    
    // 除了初始的 后面刻度线的个数
    private int m = 6;
    
    // 起始时间
    private long startTime = 57600000;
    
    // 结束时间
    private long endTime = 144000000;
    
    // 时间的集合
    private List<String> list_time = new ArrayList<String>();
    
    // 回放的时间片段
    private List<WMFileInfo> mlist;
    
    // 用于记录时间片的位置信息
    private List<TLInfo> tl_mlist;
    
    // 时间轴的时间片段内播放的第一个文件
    private int firstPlayFile;
    
    // 时间轴的时间片段内播放的最后一个文件
    private int lastPlayFile;
    
    // 滑块的宽度
    private int mImage_width;
    
    // 滑块的当前位置
    private int mImage_left = DensityUtil.dip2px(getContext(), 20);
    
    private OnPlaySeekBarListener onPlaySeekBarListener;
    
    public PlayBackSeekBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView();
    }
    
    public PlayBackSeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }
    
    public PlayBackSeekBar(Context context)
    {
        super(context);
        initView();
    }
    
    /**
     * 
     * 传入时间轴的起始时间和结束时间
     * 设置是5分钟 还是1小时 还是1天的时间轴
     * 
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @see [类、类#方法、类#成员]
     */
    public void setTime(long startTime, long endTime, int state)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        seek_state = state;
        if (seek_state != SEEK_STAE_5)
        {
            m = 6;
        }
        else
        {
            m = 5;
        }
        mImage_left = DensityUtil.dip2px(getContext(), 20);
        invalidate();
    }
    
    public void setTimeFile(List<WMFileInfo> mlist)
    {
        this.mlist = mlist;
        mImage_left = DensityUtil.dip2px(getContext(), 20);
    }
    
    private void initView()
    {
        // 进度条的画笔
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.main_color));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        // 刻度的画笔
        paint1.setAntiAlias(true);
        paint1.setColor(getResources().getColor(R.color.main_color));
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(10);
        // 刻度值的画笔
        paint2.setAntiAlias(true);
        paint2.setColor(Color.GRAY);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setStrokeWidth(1);
        paint2.setTextSize(DensityUtil.sp2px(getContext(), 12));
        // 滑块
        mImage = BitmapFactory.decodeResource(getResources(), R.drawable.huakua);
        // 未播放模块时间片段
        paint3.setAntiAlias(true);
        paint3.setColor(getResources().getColor(R.color.text_color));
        paint3.setStyle(Paint.Style.FILL);
        paint3.setStrokeWidth(5);
        // 已播放模块时间片段
        paint4.setAntiAlias(true);
        paint4.setColor(getResources().getColor(R.color.main_color));
        paint4.setStyle(Paint.Style.FILL);
        paint4.setStrokeWidth(5);
    }
    
    /**
     * 重写方法
     * 
     * @param widthMeasureSpec
     *            获取到的 宽度
     * @param heightMeasureSpec
     *            获取到的高度
     * @see android.widget.LinearLayout#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // 打印 宽度 通过宽度的值 来总结一下
        setMeasuredDimension(setWidth(widthMeasureSpec), setHegith(heightMeasureSpec));
    }
    
    /**
     * 通过测量模式 修改宽度
     * 
     * @param widthMeasureSpec
     *            宽度
     * @return
     * @see [类、类#方法、类#成员]
     */
    private int setWidth(int widthMeasureSpec)
    {
        int result = 0;
        // 获取测量模式
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        // 获取宽度大小
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        width = specSize;
        
        if (specMode == MeasureSpec.EXACTLY)
        { // 若果是这个模式 大小不变
            result = specSize;
        }
        else
        {
            result = specSize;
            if (specMode == MeasureSpec.AT_MOST)
            {
                // 如果是这个模式 result 和specSize 取小的值
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    
    public void setScroll(long time)
    {
        int layout_left = (int)((width - DensityUtil.dip2px(getContext(), 20) * 2) * (time - startTime) / (endTime - startTime));
        mImage_left = layout_left + DensityUtil.dip2px(getContext(), 20);
        if (mImage_left > (width - DensityUtil.dip2px(getContext(), 20)))
        {
            mImage_left = width - DensityUtil.dip2px(getContext(), 20);
        }
        else if (mImage_left < DensityUtil.dip2px(getContext(), 20))
        {
            mImage_left = DensityUtil.dip2px(getContext(), 20);
        }
        invalidate();
    }
    
    /**
     * 通过测量模式 修改宽度
     * 
     * @param widthMeasureSpec
     *            宽度
     * @return
     * @see [类、类#方法、类#成员]
     */
    private int setHegith(int heightMeasureSpec)
    {
        int result = 0;
        // 获取测量模式
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        // 获取宽度大小
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        
        if (specMode == MeasureSpec.EXACTLY)
        { // 若果是这个模式 大小不变
            result = specSize;
        }
        else
        {
            result = DensityUtil.dip2px(getContext(), 50);
            if (specMode == MeasureSpec.AT_MOST)
            {
                // 如果是这个模式 result 和specSize 取小的值
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        drawLine(canvas);
        drawTime(canvas);
        drawHasPlayTime(canvas);
    }
    
    /**
     * 
     * 绘制刻度线 和显示的刻度值
     * 根据当前状态(seek_state)以及起始时间(startTime)和结束时间(endTime) 绘制刻度线
     * 
     * @param canvas
     * @see [类、类#方法、类#成员]
     */
    @SuppressLint("SimpleDateFormat")
    private void drawLine(Canvas canvas)
    {
        RectF re1 = new RectF(DensityUtil.dip2px(getContext(), 10), DensityUtil.dip2px(getContext(), 10), width - DensityUtil.dip2px(getContext(), 10), DensityUtil.dip2px(getContext(), 15));
        // 绘制圆角矩形
        // 进度条
        canvas.drawRoundRect(re1, 5, 5, paint);
        
        // 第一条刻度线
        canvas.drawLine((float)DensityUtil.dip2px(getContext(), 20),
            (float)DensityUtil.dip2px(getContext(), 2),
            (float)DensityUtil.dip2px(getContext(), 20),
            (float)DensityUtil.dip2px(getContext(), 10),
            paint);
        // 其他刻度线
        for (int i = 0; i < m; i++)
        {
            canvas.drawLine(DensityUtil.dip2px(getContext(), 20) + (i + 1) * (width - DensityUtil.dip2px(getContext(), 20) * 2) / m,
                (float)DensityUtil.dip2px(getContext(), 2),
                DensityUtil.dip2px(getContext(), 20) + (i + 1) * (width - DensityUtil.dip2px(getContext(), 20) * 2) / m,
                (float)DensityUtil.dip2px(getContext(), 10),
                paint);
        }
        // 如果是5分钟 那么刻度线就少一个
        if (seek_state != SEEK_STAE_5)
        {
            list_time.removeAll(list_time);
            list_time.add(longToString(startTime));
            for (int i = 0; i < 5; i++)
            {
                list_time.add(longToString(startTime + (endTime - startTime) / m * (i + 1)));
            }
            if (longToString(endTime).equals("00:00"))
            {
                list_time.add("24:00");
            }
            else
            {
                list_time.add(longToString(endTime));
            }
        }
        else
        {
            list_time.removeAll(list_time);
            list_time.add(longToString(startTime));
            for (int i = 0; i < 4; i++)
            {
                list_time.add(longToString(startTime + (endTime - startTime) / m * (i + 1)));
            }
            list_time.add(longToString(endTime));
        }
        // 刻度值
        for (int i = 0; i < list_time.size(); i++)
        {
            canvas.drawText(list_time.get(i), DensityUtil.dip2px(getContext(), 8) + i * (width - DensityUtil.dip2px(getContext(), 20) * 2) / m, DensityUtil.dip2px(getContext(), 40), paint2);
        }
    }
    
    /**
     * 绘制时间片段
     * 根据当前起始时间和结束时间 以及通过获取到的回放的时间片段 绘制时间片段
     * 
     * @param canvas
     */
    private void drawTime(Canvas canvas)
    {
        tl_mlist = new ArrayList<PlayBackSeekBar.TLInfo>();
        if (mlist != null && mlist.size() != 0)
        {
            // 获取起始播放的第一个片段
            for (int i = 0; i < mlist.size(); i++)
            {
                // 播放文件的起始时间小于时间轴的起始时间
                if (mlist.get(i).getStartTime() <= startTime && mlist.get(i).getEndTime() >= startTime)
                {
                    firstPlayFile = i;
                    break;
                }
                // 播放文件的起始时间大于时间轴的起始时间
                else if (mlist.get(i).getStartTime() >= startTime && mlist.get(i).getStartTime() <= endTime)
                {
                    firstPlayFile = i;
                    break;
                }
            }
            for (int i = 0; i < mlist.size(); i++)
            {
                // 播放文件的时间有部分在时间轴之内
                if (mlist.get(i).getStartTime() < endTime && mlist.get(i).getEndTime() > startTime)
                {
                    lastPlayFile = i;
                }
            }
            
            for (int i = firstPlayFile; i < (lastPlayFile + 1); i++)
            {
                TLInfo info = new TLInfo();
                
                if (mlist.get(i).getStartTime() <= startTime)
                {
                    // 该判断主要用于当前时间段中第一个时间片段
                    // 如果第一个片段的起始时间小于时间轴的起始时间 那么起始位置就是最左边
                    info.left = DensityUtil.dip2px(getContext(), 20);
                }
                else
                {
                    info.left = DensityUtil.dip2px(getContext(), 20) + (mlist.get(i).getStartTime() - startTime) * (width - DensityUtil.dip2px(getContext(), 20) * 2) / (endTime - startTime);
                }
                
                if (mlist.get(i).getEndTime() >= endTime)
                {
                    info.rigth = width - DensityUtil.dip2px(getContext(), 20);
                }
                else
                {
                    // 该判断主要用于当前时间段中最后一个时间片段
                    // 如果最后一个片段的结束时间大于时间轴的结束时间 那么结束位置就是最右边
                    info.rigth = (mlist.get(i).getEndTime() - startTime) * (width - DensityUtil.dip2px(getContext(), 20) * 2) / (endTime - startTime) + DensityUtil.dip2px(getContext(), 20);
                }
                info.index = i;
                tl_mlist.add(info);
                RectF re1 = new RectF(info.getLeft(), (float)DensityUtil.dip2px(getContext(), 11), info.getRigth(), (float)DensityUtil.dip2px(getContext(), 14));
                canvas.drawRoundRect(re1, 0, 0, paint3);
            }
        }
    }
    
    /**
     * 
     * 绘制已经播放的片段的時間轴
     * 根据当前滑块的位置 绘制播放过的片段的时间轴
     * 
     * @param canvas
     * @see [类、类#方法、类#成员]
     */
    private void drawHasPlayTime(Canvas canvas)
    {
        mImage_width = mImage.getScaledWidth(canvas);
        canvas.drawBitmap(mImage, mImage_left - mImage_width / 2, 0, paint);
        int playIndex = 0;
        if (tl_mlist != null && tl_mlist.size() != 0)
        {
            // 当前播放到了第几个时间轴上的片段 不是所有回放文件的片段 这里要却别开来
            for (int i = 0; i < tl_mlist.size(); i++)
            {
                if (tl_mlist.get(i).getLeft() <= (mImage_left) && tl_mlist.get(i).getRigth() >= (mImage_left))
                {
                    playIndex = i + 1;
                }
                else if (mImage_left >= (tl_mlist.get(tl_mlist.size() - 1).getRigth()))
                {
                    playIndex = tl_mlist.size();
                }
            }
            // 根据播放的片段绘制时间轴
            for (int i = 0; i < playIndex; i++)
            {
                if (i == (playIndex - 1))
                {
                    if (mImage_left <= tl_mlist.get(i).getRigth())
                    {
                        RectF re1 = new RectF(tl_mlist.get(i).getLeft(), (float)DensityUtil.dip2px(getContext(), 11), mImage_left, (float)DensityUtil.dip2px(getContext(), 14));
                        // 绘制圆角矩形
                        // 进度条
                        canvas.drawRoundRect(re1, 0, 0, paint4);
                    }
                    else
                    {
                        RectF re1 = new RectF(tl_mlist.get(i).getLeft(), (float)DensityUtil.dip2px(getContext(), 11), tl_mlist.get(i).getRigth(), (float)DensityUtil.dip2px(getContext(), 14));
                        // 绘制圆角矩形
                        // 进度条
                        canvas.drawRoundRect(re1, 0, 0, paint4);
                    }
                }
                else
                {
                    RectF re1 = new RectF(tl_mlist.get(i).getLeft(), (float)DensityUtil.dip2px(getContext(), 11), tl_mlist.get(i).getRigth(), (float)DensityUtil.dip2px(getContext(), 14));
                    // 绘制圆角矩形
                    // 进度条
                    canvas.drawRoundRect(re1, 0, 0, paint4);
                }
            }
        }
    }
    
    ScrollView scrollView;
    public void setScrollview(ScrollView scrollView)
    {
        this.scrollView=scrollView;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_UP) {  
            scrollView.requestDisallowInterceptTouchEvent(false);  
        } else {  
            scrollView.requestDisallowInterceptTouchEvent(true);  
        }  
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                moveLogic(event);
                if (onPlaySeekBarListener != null)
                {
                    onPlaySeekBarListener.onScrolling();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                moveLogic(event);
                break;
            case MotionEvent.ACTION_UP:
                moveLogic(event);
                if (onPlaySeekBarListener != null)
                {
                    onPlaySeekBarListener.onScrolled(getScrollToTime());
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (onPlaySeekBarListener != null)
                {
                    onPlaySeekBarListener.onScrolled(getScrollToTime());
                }
                break;
            default:
                break;
        }
        return true;
    }
    
    /**
     * 
     * 获取当前滑动到的地方的时间
     * 滑块滑动后移动到的地方的时间
     * 
     * @return
     * @see [类、类#方法、类#成员]
     */
    private long getScrollToTime()
    {
        long scrolltime = startTime + (mImage_left - DensityUtil.dip2px(getContext(), 20)) * (endTime - startTime) / (width - DensityUtil.dip2px(getContext(), 20) * 2);
        return scrolltime;
    }
    
    /**
     * 
     * 滑块滑动的逻辑
     * 
     * @param event
     * @see [类、类#方法、类#成员]
     */
    private void moveLogic(MotionEvent event)
    {
        if (((event.getX() - DensityUtil.dip2px(getContext(), 20)) >= 0) && ((width - DensityUtil.dip2px(getContext(), 20)) >= event.getX()))
        {
            mImage_left = (int)event.getX();
        }
        else if (((event.getX() - DensityUtil.dip2px(getContext(), 20)) <= 0))
        {
            mImage_left = DensityUtil.dip2px(getContext(), 20);
        }
        else
        {
            mImage_left = width - DensityUtil.dip2px(getContext(), 20);
        }
        invalidate();
    }
    
    /**
     * 
     * 时间片段的管理类
     * 
     * @author 谢云峰
     * @version [V1.00, 2017-6-6]
     * @see [相关类/方法]
     * @since V1.00
     */
    class TLInfo
    {
        // 时间片段左边的位置
        private long left;
        
        // 时间片段右边的位置
        private long rigth;
        
        // 时间片段对应的播放文件的index
        private int index;
        
        public int getIndex()
        {
            return index;
        }
        
        public void setIndex(int index)
        {
            this.index = index;
        }
        
        public long getLeft()
        {
            return left;
        }
        
        public void setLeft(long left)
        {
            this.left = left;
        }
        
        public long getRigth()
        {
            return rigth;
        }
        
        public void setRigth(long rigth)
        {
            this.rigth = rigth;
        }
    }
    
    public void setOnScrollListener(OnPlaySeekBarListener onPlaySeekBarListener)
    {
        this.onPlaySeekBarListener = onPlaySeekBarListener;
    }
    
    public interface OnPlaySeekBarListener
    {
        void onScrolled(long nowTime);
        
        void onScrolling();
    }
    
    @SuppressLint("SimpleDateFormat")
    public String longToString(long time)
    {
        return new SimpleDateFormat("HH:mm").format(new Date(time));
    }
}
