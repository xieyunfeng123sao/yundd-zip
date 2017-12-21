package com.vomont.yundudao.view;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * yangqiangyu on 1/24/16 00:22
 */
@SuppressLint({"DrawAllocation", "ClickableViewAccessibility", "SimpleDateFormat"})
public class PlaySeekBar extends View
{
    
    public int SEEK_STAE_24 = 0;// 一天
    
    public int SEEK_STAE_1 = 1;// 1小时
    
    public int SEEK_STAE_5 = 2;// 5分钟
    
    private int seek_statu = 0;
    
    // 屏幕宽度
    private int width;
    
    private int m = 6;
    
    Paint paint = new Paint();
    
    Paint paint1 = new Paint();
    
    Paint paint2 = new Paint();
    
    Paint paint3 = new Paint();
    
    Paint paint4 = new Paint();
    
    // 起始时间
    private long startTime;
    
    // 结束时间
    private long endTime;
    
    // 滑块
    private Bitmap mImage;
    
    private List<WMFileInfo> mlist;
    
    // 滑块的宽度
    private int mImage_width = 0;
    
    // 滑块的当前位置
    private int mImage_left = DensityUtil.dip2px(getContext(), 20);
    
    private int mImage_left_old;
    
    // 用于记录时间片的位置信息
    private List<TLInfo> tl_mlist;
    
    private boolean isFirst;
    
    private OnPlaySeekBarListener onPlaySeekBarListener;
    
    // 需要播放的片段
    private int needPlayNum;
    
    private List<String> list_time = new ArrayList<String>();
    
    private int OnefirstPlayFile;
    
    private int OnelastPlayFile;
    
    private boolean needPlay;
    
    private boolean isFirstPos;
    
    private long dayStart, dayEnd;
    
    private OnContrlTimeListener onContrlTimeListener;
    
    public PlaySeekBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView();
    }
    
    public PlaySeekBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView();
    }
    
    public PlaySeekBar(Context context)
    {
        super(context);
        initView();
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
     * 
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
    
    // 视图的绘制
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
        if (seek_statu != SEEK_STAE_5)
        {
            list_time.removeAll(list_time);
            list_time.add(longToString(startTime));
            for (int i = 0; i < 5; i++)
            {
                list_time.add(longToString(startTime + (endTime - startTime) / m * (i + 1)));
            }
            if(longToString(endTime).equals("00:00"))
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
        
        for (int i = 0; i < list_time.size(); i++)
        {
            canvas.drawText(list_time.get(i), DensityUtil.dip2px(getContext(), 8) + i * (width - DensityUtil.dip2px(getContext(), 20) * 2) / m, DensityUtil.dip2px(getContext(), 40), paint2);
        }
    }
    
    // 初始化开始和结束时间
    public void setDayTime(long startTime, long endTime)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        invalidate();
    }
    
    public void setTimeFile(List<WMFileInfo> mlist)
    {
        this.mlist = mlist;
        isFirstPos = true;
        isFirst = false;
        mImage_left = DensityUtil.dip2px(getContext(), 20);
    }
    
    public void upDataLine(int i, int pos)
    {
        mImage_left =
            (int)(DensityUtil.dip2px(getContext(), 20) + ((mlist.get(i).getEndTime() - mlist.get(i).getStartTime()) * pos / 10000 + mlist.get(i).getStartTime() - startTime)
                * (width - DensityUtil.dip2px(getContext(), 20) * 2) / (endTime - startTime));
        jupeLeft();
        invalidate();
    }
    
    private void jupeLeft()
    {
        if (mImage_left >= (width - DensityUtil.dip2px(getContext(), 20)))
        {
            if (seek_statu == SEEK_STAE_1)
            {
                if ((endTime + 30 * 60 * 1000) <= dayEnd)
                {
                    endTime = endTime + 30 * 60 * 1000;
                    startTime = endTime - 60 * 60 * 1000;
                }
                else
                {
                    endTime = dayEnd;
                    startTime = endTime - 60 * 60 * 1000;
                }
//                mImage_left = width / 2;
            }
            else if (seek_statu == SEEK_STAE_5)
            {
                if ((endTime + 2.5 * 60 * 1000) <= dayEnd)
                {
                    endTime = (long)(endTime + 2.5 * 60 * 1000);
                    startTime = (long)(endTime - 5 * 60 * 1000);
                }
                else
                {
                    endTime = dayEnd;
                    startTime = (long)(endTime - 5 * 60 * 1000);
                }
//                mImage_left = width / 2;
            }
        }
        else if (mImage_left <= DensityUtil.dip2px(getContext(), 20))
        {
            if (seek_statu == SEEK_STAE_1)
            {
                if ((startTime - 30 * 60 * 1000) >= dayStart)
                {
                    startTime = startTime - 30 * 60 * 1000;
                }
                else
                {
                    startTime = dayStart;
                }
                endTime = startTime + 60 * 60 * 1000;
            }
            else if (seek_statu == SEEK_STAE_5)
            {
                if ((startTime - 2.5 * 60 * 1000) >= dayStart)
                {
                    startTime = (long)(startTime - 2.5 * 60 * 1000);
                }
                else
                {
                    startTime = dayStart;
                }
                endTime = startTime + 5 * 60 * 1000;
//                mImage_left = width / 2;
            }
            
        }
    }
    
    public void OneDayTime(long dayStart, long dayEnd)
    {
        this.dayEnd = dayEnd;
        this.dayStart = dayStart;
    }
    
    public void upDataTime(int type, int i, int pos, long starttime, long endtime)
    {
        this.startTime = starttime;
        this.endTime = endtime;
        seek_statu = type;
        if (type == SEEK_STAE_24)
        {
            m = 6;
        }
        else if (type == SEEK_STAE_1)
        {
            m = 6;
        }
        else
        {
            m = 5;
        }
        if(mlist!=null&&mlist.size()!=0)
        {
            mImage_left =
                (int)(DensityUtil.dip2px(getContext(), 20) + ((mlist.get(i).getEndTime() - mlist.get(i).getStartTime()) * pos / 10000 + mlist.get(i).getStartTime() - startTime)
                    * (width - DensityUtil.dip2px(getContext(), 20) * 2) / (endTime - startTime)); 
        }
        else
        {
            mImage_left=DensityUtil.dip2px(getContext(), 20);
        }
       
        invalidate();
    }
    
    /**
     * 画时间轴
     * 
     * @param canvas
     */
    private void drawTime(Canvas canvas)
    {
        tl_mlist = new ArrayList<PlaySeekBar.TLInfo>();
        if (mlist != null && mlist.size() != 0)
        {
            for (int i = 0; i < mlist.size(); i++)
            {
                if (mlist.get(i).getStartTime() <= startTime && mlist.get(i).getEndTime() >= startTime)
                {
                    OnefirstPlayFile = i;
                    break;
                }
                else if (mlist.get(i).getStartTime() >= startTime && mlist.get(i).getStartTime() <= endTime)
                {
                    OnelastPlayFile = i;
                    break;
                }
            }
            for (int i = 0; i < mlist.size(); i++)
            { 
                if (mlist.get(i).getStartTime() < endTime && mlist.get(i).getEndTime() > startTime)
                {
                    OnelastPlayFile = i;
                }
            }
            for (int i = OnefirstPlayFile; i < (OnelastPlayFile + 1); i++)
            {
                TLInfo info = new TLInfo();
                if (mlist.get(i).getStartTime() <= startTime)
                {
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
                    
                    info.rigth = (mlist.get(i).getEndTime() - startTime) * (width - DensityUtil.dip2px(getContext(), 20) * 2) / (endTime - startTime) + DensityUtil.dip2px(getContext(), 20);
                }
                tl_mlist.add(info);
                RectF re1 = new RectF(info.getLeft(), (float)DensityUtil.dip2px(getContext(), 11), info.getRigth(), (float)DensityUtil.dip2px(getContext(), 14));
                canvas.drawRoundRect(re1, 0, 0, paint3);
            }
        }
    }
    
    private void drawHasPlayTime(Canvas canvas)
    {
        needPlayNum=0;
        int left;
        if (!isFirst)
        {
            mImage_width = mImage.getScaledWidth(canvas);
            // 滑块
            left = DensityUtil.dip2px(getContext(), 20) - mImage_width / 2;
            isFirst = true;
        }
        else
        {
            left = mImage_left - mImage_width / 2;
        }
        if (isFirstPos)
        {
            if (tl_mlist != null && tl_mlist.size() != 0)
            {
                mImage_left = (int)tl_mlist.get(0).getLeft();
            }
            play();
            isFirstPos = false;
        }
        canvas.drawBitmap(mImage, left, 0, paint);
        if (tl_mlist != null && tl_mlist.size() != 0)
        {
            for (int i = 0; i < tl_mlist.size(); i++)
            {
                if (tl_mlist.get(i).getLeft() <= (mImage_left) && tl_mlist.get(i).getRigth() >= (mImage_left))
                {
                    needPlayNum = i + 1;
                }
            }
            for (int i = 0; i < needPlayNum; i++)
            {
                if (i == (needPlayNum - 1))
                {
                    Log.e("insert", "needPlayNum="+needPlayNum);
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
    
    private void play()
    {
        if (tl_mlist != null && tl_mlist.size() != 0)
        {
            for (int i = 0; i < tl_mlist.size(); i++)
            {
                if (mImage_left >= tl_mlist.get(i).getLeft() && mImage_left <= tl_mlist.get(i).getRigth())
                {
                    long nowtime = startTime + (mImage_left - DensityUtil.dip2px(getContext(), 20)) * (endTime - startTime) / (width - DensityUtil.dip2px(getContext(), 20) * 2);
                    int pos =
                        (int)((nowtime - mlist.get(i + OnefirstPlayFile).getStartTime()) * 10000 / (mlist.get(i + OnefirstPlayFile).getEndTime() - mlist.get(i + OnefirstPlayFile).getStartTime()));
                    onPlaySeekBarListener.onScrolled(i + OnefirstPlayFile, pos);
                    needPlay = true;
                    mImage_left_old = mImage_left;
                    break;
                }
                else
                {
                    needPlay = false;
                    onPlaySeekBarListener.onStop();
                }
            }
            if (!needPlay)
            {
                mImage_left = mImage_left_old;
                invalidate();
            }
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                moveLogic(event);
                onContrlTimeListener.stop();
                break;
            case MotionEvent.ACTION_MOVE:
                moveLogic(event);
                break;
            case MotionEvent.ACTION_UP:
                moveLogic(event);
                jupeLeft();
                play();
                onContrlTimeListener.start();
                break;
            case MotionEvent.ACTION_CANCEL:
                jupeLeft();
                play();
                onContrlTimeListener.start();
                break;
            default:
                break;
        }
        return true;
    }
    
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
    
    public String longToString(long time)
    {
        return new SimpleDateFormat("HH:mm").format(new Date(time));
    }
    
    public String longToStringS(long time)
    {
        return new SimpleDateFormat("mm:ss").format(new Date(time));
    }
    
    class TLInfo
    {
        private long left;
        
        private long rigth;
        
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
    
    public void controlTimeTask(OnContrlTimeListener onContrlTimeListener)
    {
        this.onContrlTimeListener = onContrlTimeListener;
    }
    
    public interface OnContrlTimeListener
    {
        void start();
        
        void stop();
    }
    
    public void setOnScrollListener(OnPlaySeekBarListener onPlaySeekBarListener)
    {
        this.onPlaySeekBarListener = onPlaySeekBarListener;
    }
    
    public interface OnPlaySeekBarListener
    {
        // i 播放第几个片段
        // pos 播放的pos
        void onScrolled(int i, int pos);
        
        void onStop();
    }
}