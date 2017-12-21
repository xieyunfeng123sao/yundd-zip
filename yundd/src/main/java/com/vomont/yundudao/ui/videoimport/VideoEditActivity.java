package com.vomont.yundudao.ui.videoimport;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.mabeijianxi.smallvideorecord2.LocalMediaCompress;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.LocalMediaConfig;
import com.mabeijianxi.smallvideorecord2.model.OnlyCompressOverBean;
import com.vomont.yundudao.R;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.video.TrimVideoUtils;
import com.vomont.yundudao.utils.video.TrimVideoUtils.TrimFileCallBack;
import com.vomont.yundudao.utils.video.VideoUtil;
import com.vomont.yundudao.view.MyHorizontalScrollView;
import com.vomont.yundudao.view.MyHorizontalScrollView.ScrollType;
import com.vomont.yundudao.view.MyHorizontalScrollView.ScrollViewListener;

@SuppressLint("SimpleDateFormat")
public class VideoEditActivity extends Activity implements OnClickListener
{
    private TextView video_edit_next;
    
    private ImageView video_edit_goback;
    
    private VideoView videoView;
    
    private TextView video_length;
    
    private LinearLayout edit_img_video;
    
    private ImageView edit_left_line;
    
    private ImageView edit_right_line;
    
    private View video_line;
    
    private TextView left_time;
    
    private TextView right_time;
    
    private MyHorizontalScrollView img_scroll;
    
    private String videoPath;
    
    // 屏幕的宽度
    private int winWith;
    
    // 左滑块的宽度
    private int left_with, left_hegiht;
    
    private int left_time_marin_left;
    
    // 右滑块的宽度
    private int right_with;
    
    // 左边时间的宽度
    private int left_time_with;
    
    // 视频刻度的长度
    private int length_time_line;
    
    private int videoLength;
    
    private int old_time;
    
    private boolean conControl;
    
    private boolean isNeed;
    
    private Dialog dialog;
    
    private Context context;
    
    private boolean goFromNext = false;
    
    private boolean isFirst = true;
    
    String name;
    
    // 生成的视频的长度
    private String newName;
    
    // 图片的总长度
    private int ll_winWith;
    
    // 图片的数量
    private int num = 0;
    
    // 单个图片的长度
    private int item_width;
    
    private int nowRightTime = VideoManager.maxTime;
    
    private int nowLeftTime;
    
    private RelativeLayout left_trans;
    
    private RelativeLayout right_trans;
    
    private Handler mHandler = new Handler()
    {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 10:
                    // 显示8张图图片
                    String bitPath = (String)msg.obj;
                    ImageView img = new ImageView(VideoEditActivity.this);
                    img.setScaleType(ScaleType.FIT_XY);
                    LayoutParams params = null;
                    if (msg.arg1 == 1)
                    {
                        params = new LayoutParams(ll_winWith - item_width * (num - 1), LayoutParams.MATCH_PARENT);
                    }
                    else
                    {
                        params = new LayoutParams(item_width, LayoutParams.MATCH_PARENT);
                    }
                    img.setLayoutParams(params);
                    if (!VideoEditActivity.this.isDestroyed())
                    {
                        Glide.with(context).load(new File(bitPath)).into(img);
                    }
                    edit_img_video.addView(img);
                    break;
                case 20:
                    int currentPosition = videoView.getCurrentPosition();
                    if (videoLength == 0)
                    {
                        videoLength = videoView.getDuration();
                    }
                    if (old_time >= currentPosition && !isNeed)
                    {
                        currentPosition = videoLength;
                        conControl = true;
                    }
                    android.widget.RelativeLayout.LayoutParams param = new android.widget.RelativeLayout.LayoutParams(video_line.getWidth(), video_line.getHeight());
                    
                    if ((currentPosition * length_time_line / videoLength + left_with) < edit_right_line.getRight())
                    {
                        param.setMargins(currentPosition * length_time_line / videoLength + left_with, 0, 0, 0);
                    }
                    else
                    {
                        param.setMargins(edit_right_line.getLeft(), 0, 0, 0);
                    }
                    video_line.setLayoutParams(param);
                    old_time = currentPosition;
                    isNeed = false;
                    if (length_time_line != 0 && currentPosition >= (edit_right_line.getLeft() - edit_left_line.getWidth()) * videoLength / length_time_line)
                    {
                        // 如果大于右边界 就暂停
                        videoView.pause();
                    }
                    break;
                case 30:
                    // Toast.makeText(context, "暂不支持该视频的导入!", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    break;
                case 40:
                    File file = new File(VideoManager.yasuo + "/" + newName + ".mp4");
                    if (file.exists() && file.length() > 500 * 1024 * 1024)
                    {
                        Toast.makeText(context, "视频的大小超过了限制，最多500M!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        return;
                        // 选择本地视频压缩
//                        LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
//                        final LocalMediaConfig config = buidler
//                                .setVideoPath( VideoManager.path+"/"+newName+".mp4")
//                                .captureThumbnailsTime(1)
//                                .doH264Compress(new AutoVBRMode())
//                                .setFramerate(15)
//                                .setScale(1.0f)
//                                .build();
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                LocalMediaCompress localMediaCompress=          new LocalMediaCompress(config);
//                                OnlyCompressOverBean onlyCompressOverBean = localMediaCompress.startCompress();
//                                if(onlyCompressOverBean.isSucceed()) {
//                                    String path = onlyCompressOverBean.getVideoPath();
//                                    Message message = new Message();
//                                    message.what = 50;
//                                    message.obj = path;
//                                    mHandler.sendMessage(message);
//                                }
//                                else
//                                {
//                                    mHandler.sendEmptyMessage(60);
//                                }
//                            }
//                        }).start();
                    }
//                    else
//                    {
                        Intent intent = new Intent(VideoEditActivity.this, VideoFaceActivity.class);
                        intent.putExtra("path", VideoManager.yasuo);
                        intent.putExtra("name", newName);
                        intent.putExtra("length", videoLength);
                        startActivityForResult(intent, 0);
                        dialog.dismiss();
//                    }
                    break;
                case 50:
                    String  path=msg.obj.toString();
                    String[] listItem= path.split("/");
                    Intent tent = new Intent(VideoEditActivity.this, VideoFaceActivity.class);
                    tent.putExtra("path", path.replace(listItem[listItem.length-1],""));
                    tent.putExtra("name", listItem[listItem.length-1].replace(".mp4",""));
                    tent.putExtra("length", videoLength);
                    startActivityForResult(tent, 0);
                    dialog.dismiss();
                    break;
                case 60:
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);
        context = this;
        initView();
        initListener();
        initData();
        onTouchView();
        initVideo();
    }
    
    private void initView()
    {
        video_edit_next = (TextView)findViewById(R.id.video_edit_next);
        video_edit_goback = (ImageView)findViewById(R.id.video_edit_goback);
        videoView = (VideoView)findViewById(R.id.videoView);
        video_length = (TextView)findViewById(R.id.video_length);
        edit_img_video = (LinearLayout)findViewById(R.id.edit_img_video);
        edit_left_line = (ImageView)findViewById(R.id.edit_left_line);
        edit_right_line = (ImageView)findViewById(R.id.edit_right_line);
        video_line = findViewById(R.id.video_line);
        left_time = (TextView)findViewById(R.id.left_time);
        right_time = (TextView)findViewById(R.id.right_time);
        img_scroll = (MyHorizontalScrollView)findViewById(R.id.img_scroll);
        left_trans = (RelativeLayout)findViewById(R.id.left_trans);
        right_trans = (RelativeLayout)findViewById(R.id.right_trans);
        img_scroll.setHandler(new Handler());
        img_scroll.setOnScrollStateChangedListener(new ScrollViewListener()
        {
            @Override
            public void onScrollChanged(MyHorizontalScrollView.ScrollType scrollType)
            {
                // MyHorizontalScrollView滑动监听 滑动过程中改变左右两边的时间值
                if (scrollType == ScrollType.IDLE)
                {
                    updataRightText();
                    updataLeftText();
                }
                else if (scrollType == ScrollType.FLING)
                {
                    updataRightText();
                    updataLeftText();
                }
            }
        });
    }
    
    private void initListener()
    {
        video_edit_next.setOnClickListener(this);
        video_edit_goback.setOnClickListener(this);
    }
    
    private void initData()
    {
        Intent intent = getIntent();
        videoPath = intent.getStringExtra("videoPath");
        videoView.setVideoPath(videoPath);
        videoView.start();
        videoView.setOnPreparedListener(new OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                // 开始播放后才能获取到正确的总时间长度
                videoLength = videoView.getDuration();
                video_length.setText(Time2String(videoLength));
                if (videoLength / 1000 > VideoManager.maxTime / 1000)
                {
                    // 设置MyHorizontalScrollView的长度
                    ll_winWith = winWith * (videoLength) / VideoManager.maxTime;
                }
                else
                {
                    // 设置MyHorizontalScrollView的长度
                    ll_winWith = winWith;
                }
                img_scroll.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        // 异步加载图片
                        asyncloadImage();
                        // 此处是初始化两边的时间
                        updataRightText();
                        updataLeftText();
                    }
                }, 200);
            }
        });
        
        // 更新时间轴
        upDataTime();
    }
    
    private void upDataTime()
    {
        new Thread(new Runnable()
        {
            public void run()
            {
                while (!conControl)
                {
                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(20);
                }
            }
        }).start();
    }
    
    /**
     * 
     * 更新左边的时间
     * 在MH发生滑动或者左边时间轴移动时能够实时更新准确的时间
     * 
     * @see [类、类#方法、类#成员]
     */
    public void updataLeftText()
    {
        // 当视频时间大于1分钟的时候
        if (videoLength / 1000 > VideoManager.maxTime / 1000)
        {
            int canMoveLength = ll_winWith - winWith;
            int addTime = (videoLength - VideoManager.maxTime) * img_scroll.getScrollX() / canMoveLength;
            nowLeftTime = addTime + VideoManager.maxTime * edit_left_line.getLeft() / length_time_line;
        }
        else
        {
            nowLeftTime = videoLength * edit_left_line.getLeft() / length_time_line;
        }
        if (nowLeftTime <= 0)
        {
            nowLeftTime = 0;
        }
        
        left_time.setText(Time2String(nowLeftTime));
    }
    
    /**
     * 
     * 更新右边的时间
     * 在MH发生滑动或者右边时间轴移动时能够实时更新准确的时间
     * 
     * @see [类、类#方法、类#成员]
     */
    public void updataRightText()
    {
        int removeTime = winWith - edit_right_line.getWidth() - edit_right_line.getLeft();
        if (videoLength / 1000 > VideoManager.maxTime / 1000)
        {
            int canMoveLength = ll_winWith - winWith;
            int addview = (videoLength - VideoManager.maxTime) * img_scroll.getScrollX() / canMoveLength;
            if (removeTime <= 2)
            {
                removeTime = 0;
            }
            nowRightTime = VideoManager.maxTime + addview - VideoManager.maxTime * removeTime / length_time_line;
        }
        else
        {
            nowRightTime = videoLength - videoLength * removeTime / length_time_line;
        }
        right_time.setText(Time2String(nowRightTime));
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        winWith = getWindowManager().getDefaultDisplay().getWidth();
        left_with = edit_left_line.getWidth();
        left_hegiht = edit_left_line.getHeight();
        right_with = edit_right_line.getWidth();
        left_time_with = left_time.getWidth();
        length_time_line = winWith - left_with - right_with;
        if (isFirst)
        {
            initRightView();
            isFirst = false;
        }
        
    }
    
    /**
     * 初始化右滑块
     * <一句话功能简述>
     * <功能详细描述>
     * 
     * @see [类、类#方法、类#成员]
     */
    private void initRightView()
    {
        // 让右滑块显示在最右边
        RelativeLayout.LayoutParams pa = (RelativeLayout.LayoutParams)edit_right_line.getLayoutParams();
        pa.leftMargin = winWith - edit_right_line.getWidth();
        edit_right_line.setLayoutParams(pa);
        
        RelativeLayout.LayoutParams txt_pa = (RelativeLayout.LayoutParams)right_time.getLayoutParams();
        txt_pa.leftMargin = winWith - right_time.getWidth();
        right_time.setLayoutParams(txt_pa);
        
        RelativeLayout.LayoutParams r_pa = (RelativeLayout.LayoutParams)right_trans.getLayoutParams();
        r_pa.leftMargin = winWith;
        right_trans.setLayoutParams(r_pa);
    }
    
    private void initVideo()
    {
        TrimVideoUtils trimVideoUtils = TrimVideoUtils.getInstance();
        // 设置回调
        trimVideoUtils.setTrimCallBack(new TrimFileCallBack()
        {
            @Override
            // 裁剪失败回调
            public void trimError(int eType)
            {
                switch (eType)
                {
                    case TrimVideoUtils.FILE_NOT_EXISTS: // 文件不存在
                        System.out.println("视频文件不存在");
                        mHandler.sendEmptyMessage(30);
                        break;
                    case TrimVideoUtils.TRIM_STOP: // 手动停止裁剪
                        System.out.println("停止裁剪");
                        mHandler.sendEmptyMessage(30);
                        break;
                    case TrimVideoUtils.TRIM_FAIL: // 裁剪失败
                        System.out.println("裁剪失败");
                        mHandler.sendEmptyMessage(30);
                        break;
                    default:
                        break;
                }
            }
            
            @Override
            // 裁剪成功回调
            public void trimCallback(boolean isNew, int startS, int endS, int vTotal, File file, File trimFile)
            {
                /**
                 * 裁剪回调
                 * 
                 * @param isNew 是否新剪辑
                 * @param starts 开始时间(秒)
                 * @param ends 结束时间(秒)
                 * @param vTime 视频长度
                 * @param file 需要裁剪的文件路径
                 * @param trimFile 裁剪后保存的文件路径
                 */
                // ===========
                System.out.println("isNew : " + isNew);
                System.out.println("startS : " + startS);
                System.out.println("endS : " + endS);
                System.out.println("vTotal : " + vTotal);
                System.out.println("file : " + file.getAbsolutePath());
                System.out.println("trimFile : " + trimFile.getAbsolutePath());
                mHandler.sendEmptyMessage(40);
                
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.video_edit_next:
                int start_time = nowLeftTime;
                int end_time = nowRightTime;
                if ((end_time - start_time) / 1000 < 1)
                {
                    Toast.makeText(VideoEditActivity.this, "播放视频不能少于1秒!", Toast.LENGTH_LONG).show();
                    return;
                }
                else if ((end_time - start_time) / 1000 > 5 * 60)
                {
                    Toast.makeText(VideoEditActivity.this, "播放视频不能超过5分钟!", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    dialog = ProgressDialog.createLoadingDialog(VideoEditActivity.this, "");
                    dialog.show();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    newName = format.format(new Date());
                    File file=new File(VideoManager.yasuo);
                    if(!file.exists())
                    {
                        file.mkdirs();
                    }
                    VideoUtil.cutVideo(videoPath, VideoManager.yasuo + "/" + newName + ".mp4", start_time / 1000, end_time / 1000);
                }
                break;
            case R.id.video_edit_goback:
                finish();
                break;
            default:
                break;
        }
    }
    
    /**
     * 左右滑块滑动的逻辑
     */
    private void onTouchView()
    {
        edit_left_line.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        conControl = true;
                        android.widget.RelativeLayout.LayoutParams param = new android.widget.RelativeLayout.LayoutParams(video_line.getWidth(), video_line.getHeight());
                        param.setMargins(edit_left_line.getLeft() + edit_left_line.getWidth(), 0, 0, 0);
                        video_line.setLayoutParams(param);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        android.widget.RelativeLayout.LayoutParams param1 = new android.widget.RelativeLayout.LayoutParams(video_line.getWidth(), video_line.getHeight());
                        param1.setMargins(edit_left_line.getLeft() + edit_left_line.getWidth(), 0, 0, 0);
                        video_line.setLayoutParams(param1);
                        if (((int)event.getRawX() + left_with) < edit_right_line.getLeft())
                        {
                            moveLeftViewWithFinger(edit_left_line, event.getRawX());
                            updataLeftText();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        conControl = false;
                        restartPlay();
                        upDataTime();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        
        edit_right_line.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        conControl = true;
                        android.widget.RelativeLayout.LayoutParams param = new android.widget.RelativeLayout.LayoutParams(video_line.getWidth(), video_line.getHeight());
                        param.setMargins(edit_left_line.getLeft() + edit_left_line.getWidth(), 0, 0, 0);
                        video_line.setLayoutParams(param);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if ((int)event.getRawX() > (edit_left_line.getLeft() + edit_left_line.getWidth() + edit_right_line.getWidth()))
                        {
                            moveRightViewWithFinger(edit_right_line, event.getRawX());
                            updataRightText();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        conControl = false;
                        restartPlay();
                        upDataTime();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        if (goFromNext)
        {
            conControl = false;
            restartPlay();
            upDataTime();
            goFromNext = false;
        }
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        goFromNext = true;
        conControl = true;
    }
    
    /**
     * 设置View的布局属性，使得view随着手指移动 注意：view所在的布局必须使用RelativeLayout 而且不得设置居中等样式
     * 
     * @param view
     * @param rawX
     * @param rawY
     */
    private void moveLeftViewWithFinger(View view, float rawX)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        RelativeLayout.LayoutParams left_params;
        if ((int)rawX + left_with <= edit_right_line.getLeft())
        {
            params.leftMargin = (int)rawX;
            view.setLayoutParams(params);
            
            if (rawX >= left_time_with && ((int)rawX) < right_time.getLeft())
            {
                RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)left_time.getLayoutParams();
                param.leftMargin = (int)rawX - left_time_with;
                left_time.setLayoutParams(param);
            }
            
        }
        left_params = new RelativeLayout.LayoutParams(view.getLeft(), RelativeLayout.LayoutParams.MATCH_PARENT);
        left_trans.setLayoutParams(left_params);
    }
    
    /**
     * 设置View的布局属性，使得view随着手指移动 注意：view所在的布局必须使用RelativeLayout 而且不得设置居中等样式
     * 
     * @param view
     * @param rawX
     * @param rawY
     */
    private void moveRightViewWithFinger(View view, float rawX)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view.getLayoutParams();
        params.leftMargin = (int)rawX - edit_right_line.getWidth();
        view.setLayoutParams(params);
        
        if ((int)rawX > (left_time.getWidth() + left_time.getLeft()) && (int)rawX <= (winWith - right_time.getWidth()))
        {
            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)right_time.getLayoutParams();
            param.leftMargin = (int)rawX;
            right_time.setLayoutParams(param);
        }
        
        RelativeLayout.LayoutParams r_params = new RelativeLayout.LayoutParams((winWith - view.getLeft() - view.getWidth()), RelativeLayout.LayoutParams.MATCH_PARENT);
        r_params.leftMargin = view.getLeft() + view.getWidth();
        right_trans.setLayoutParams(r_params);
    }
    
    private void restartPlay()
    {
        isNeed = true;
        videoView.seekTo(edit_left_line.getLeft() * videoLength / length_time_line);
        videoView.start();
    }
    
    /**
     * 获取图片显示在view中
     */
    private void asyncloadImage()
    {
        // 子线程，开启子线程去下载或者去缓存目录找图片，并且返回图片在缓存目录的地址
        final MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(videoPath);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        name = format.format(new Date());
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (videoLength > VideoManager.maxTime)
                    {
                        if (ll_winWith * 8 % winWith != 0)
                        {
                            num = ll_winWith * 8 / winWith + 1;
                        }
                        else
                        {
                            num = ll_winWith * 8 / winWith;
                        }
                    }
                    else
                    {
                        num = 8;
                    }
                    item_width = winWith / 8;
                    for (int i = 0; i < num; i++)
                    {
                        int time = i * videoLength / num;
                        Bitmap bitmap = mmr.getFrameAtTime(time * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                        BitmapUtil.savePhotoByte(bitmap, VideoManager.detail_img_cash, name + i + ".jpg");
                        bitmap.recycle();
                        Message message = new Message();
                        message.what = 10;
                        message.obj = VideoManager.detail_img_cash + "/" + name + i + ".jpg";
                        if (i == (num - 1))
                        {
                            message.arg1 = 1;
                        }
                        mHandler.sendMessage(message);
                    }
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (SecurityException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalStateException e)
                {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
        {
            int result = data.getIntExtra("finish", -1);
            if (result == 0)
            {
                finish();
            }
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        File file = new File(VideoManager.detail_img_cash);
        if (file.exists())
        {
            File[] files = file.listFiles();
            for (File file2 : files)
            {
                file2.delete();
            }
        }
    }
    
    public String Time2String(int time)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");// 初始化Formatter的转换格式。
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String hms = formatter.format(time);
        return hms;
        
    }
    
}
