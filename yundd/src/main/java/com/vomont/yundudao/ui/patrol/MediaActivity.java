package com.vomont.yundudao.ui.patrol;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.GlideCacheUtil;
import com.vomont.yundudao.utils.MediaRecorderUtil;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.utils.UriUtil;

@SuppressLint({"SimpleDateFormat", "HandlerLeak"})
public class MediaActivity extends Activity implements OnClickListener
{
    
    private RelativeLayout mediarecorde;
    
    private MediaRecorderUtil util;
    
    private ImageView change_camera;
    
    private ImageView media_go_back;
    
//    private ImageView camera_phone_img;
//    
//    private ImageView video_import_img;
//    
//    private ImageView video_upload_img;
    
    private ImageView flashlight_button;
    
    private LinearLayout camera_phone;
    
    private LinearLayout camera_videotape;
    
    private LinearLayout video_import;
    
    private LinearLayout video_upload;
    
    private ImageView camera_videotape_img;
    
    private TextView camera_videotape_txt;
    
    private TextView showtime;
    
    private boolean isOpenFlashLight;
    
    private boolean isRecordering;
    
    private String path;
    
    private boolean isTiming;
    
    private String name;
    
    private int second = -2;
    
    private ShareUtil shareUtil;
    
    private boolean isChange = false;
    
    Dialog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaplay);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog = ProgressDialog.createLoadingDialog(MediaActivity.this, "");
        initView();
        initData();
        initListener();
        shareUtil = new ShareUtil(this);
    }
    
    private void initData()
    {
        util = new MediaRecorderUtil();
        
        path = VideoManager.path;
        File file = new File(path);
        if (!file.exists())
        {
            file.mkdirs();
        }
    }
    
    private void initListener()
    {
        change_camera.setOnClickListener(this);
        media_go_back.setOnClickListener(this);
        flashlight_button.setOnClickListener(this);
        camera_phone.setOnClickListener(this);
        camera_videotape.setOnClickListener(this);
        video_import.setOnClickListener(this);
        video_upload.setOnClickListener(this);
    }
    
    private void initView()
    {
        mediarecorde = (RelativeLayout)findViewById(R.id.mediarecorde);
        change_camera = (ImageView)findViewById(R.id.change_camera);
        media_go_back = (ImageView)findViewById(R.id.media_go_back);
        flashlight_button = (ImageView)findViewById(R.id.flashlight_button);
        camera_phone = (LinearLayout)findViewById(R.id.camera_phone);
        camera_videotape = (LinearLayout)findViewById(R.id.camera_videotape);
        video_import = (LinearLayout)findViewById(R.id.video_import);
        video_upload = (LinearLayout)findViewById(R.id.video_upload);
        camera_videotape_img = (ImageView)findViewById(R.id.camera_videotape_img);
        camera_videotape_txt = (TextView)findViewById(R.id.camera_videotape_txt);
        showtime = (TextView)findViewById(R.id.show_time);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK && data != null)
            {
                Uri uri = data.getData();
                
                MediaMetadataRetriever retr = new MediaMetadataRetriever();
                try
                {
                    retr.setDataSource(UriUtil.getPath(MediaActivity.this, uri));
                }
                catch (Exception e)
                {
                    Toast.makeText(MediaActivity.this, "该视频为无效文件！", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
                String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
                if (height != null || width != null)
                {
                    Intent intent = new Intent(MediaActivity.this, VideoImportActivity.class);
                    intent.putExtra("videoPath", UriUtil.getPath(MediaActivity.this, uri));
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MediaActivity.this, "该视频为无效文件！", Toast.LENGTH_SHORT).show();
                }
            }
        }
        
    }
    
    @Override
    public void onClick(View arg0)
    {
        switch (arg0.getId())
        {
            case R.id.media_go_back:
                // 返回
                finish();
                break;
            case R.id.change_camera:
                // 切换摄像头
                if (!isRecordering)
                {
                    util.changeCamara();
                    isChange = !isChange;
                    if (isChange)
                    {
                        flashlight_button.setVisibility(View.GONE);
                    }
                    else
                    {
                        flashlight_button.setVisibility(View.VISIBLE);
                    }
                    flashlight_button.setImageResource(R.drawable.button_off);
                    isOpenFlashLight = false;
                }
                break;
            case R.id.flashlight_button:
                // 打开闪光灯
                if (isOpenFlashLight)
                {
                    util.openMode(false);
                    flashlight_button.setImageResource(R.drawable.button_off);
                }
                else
                {
                    util.openMode(true);
                    flashlight_button.setImageResource(R.drawable.button_on);
                }
                isOpenFlashLight = !isOpenFlashLight;
                break;
            case R.id.camera_phone:
                // 拍照
                if (!isRecordering)
                {
                    util.TakePicture(new PictureCallback()
                    {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera)
                        {
                            Bitmap bit = BitmapUtil.Bytes2Bimap(data);
                            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                            String name = format.format(new Date());
                            name = name + "-0-0-0.jpg";
                            Bitmap bitmap=null;
                            if(isChange)
                            {
                              bitmap = BitmapUtil.rotateBitmapByDegree(bit, -90);
                            }
                            else
                            {
                              bitmap = BitmapUtil.rotateBitmapByDegree(bit, 90);
                            }
                        
                            BitmapUtil.savePhotoByte(bitmap, VideoManager.yundd_phone + shareUtil.getShare().getNum(), name);
                            if (bit != null)
                                bit.recycle();
                            if (bitmap != null)
                                bitmap.recycle();
                         
                            camera.startPreview();
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    Toast.makeText(MediaActivity.this, "拍照成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                else
                {
                    Toast.makeText(MediaActivity.this, "正在录制视频，无法截图", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.camera_videotape:
                // 录像
                if (isRecordering)
                {
                    stopVideo(1);
                }
                else
                {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                    name = format.format(new Date());
                      int  result=   util.startRecord(path, name);
                    if(result==0)
                    {
                        //录像失败
                        return;
                    }
                    isTiming = true;
                    new Thread(new Runnable()
                    {
                        public void run()
                        {
                            try
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        camera_videotape.setEnabled(false);
                                    }
                                });
                                while (isTiming)
                                {
                                    second++;
                                    Thread.sleep(1000);
                                    handler.sendEmptyMessage(10);
                                }
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    media_go_back.setVisibility(View.GONE);
                    change_camera.setVisibility(View.GONE);
                    flashlight_button.setVisibility(View.GONE);
                    camera_videotape_img.setImageResource(R.drawable.video_tape_stop);
                    camera_videotape_txt.setText("正在录制");
                }
                isRecordering = !isRecordering;
                break;
            case R.id.video_import:
                // 导入本地视频
                if (!isRecordering)
                {
                    Intent intent = new Intent(MediaActivity.this, ImportListActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MediaActivity.this, "正在录制视频，无法导入视频", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.video_upload:
                // 上传列表
                if (!isRecordering)
                {
                    Intent it_video = new Intent(MediaActivity.this, PatrolLoadVedioActivity.class);
                    startActivity(it_video);
                }
                else
                {
                    Toast.makeText(MediaActivity.this, "正在录制视频", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    
    private void stopVideo(final int m)
    {
        isTiming = false;
        second=-2;
        showtime.setText("00:00");
        media_go_back.setVisibility(View.VISIBLE);
        change_camera.setVisibility(View.VISIBLE);
        flashlight_button.setVisibility(View.VISIBLE);
        camera_videotape_img.setImageResource(R.drawable.video_re);
        camera_videotape_txt.setText("录像");
        new Thread(new Runnable()
        {
            public void run()
            {
                util.stopRecord();
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if (m == 1 || m == 2)
                        {
                            Intent intent = new Intent(MediaActivity.this, VedioPlayActivity.class);
                            intent.putExtra("path", path);
                            intent.putExtra("name", name);
                            startActivity(intent);
                            finish();
                            if (m == 2)
                            {
                                Toast.makeText(MediaActivity.this, "最多录制5分钟", Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(m==3)
                        {
                            finish();
                        }
                    }
                });
            }
        }).start();
        
    }
    
    Handler handler = new Handler()
    {
        @SuppressLint("ShowToast")
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 10:
                    camera_videotape.setEnabled(true);
                    showtime.setText(secToTime(second));
                    if (second == 300)
                    {
                        new Thread(new Runnable()
                        {
                            public void run()
                            {
                                try
                                {
                                    Thread.sleep(300);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        dialog.show();
                                        stopVideo(2);
                                    }
                                });
                            }
                        }).start();
                    }
                    break;
                default:
                    break;
            }
            
        };
    };
    
    @Override
    protected void onStop()
    {
        super.onStop();
        isRecordering=false;
        stopVideo(0);
        util.deleteVideo();
        dialog.dismiss();
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        mediarecorde.postDelayed(new Runnable()
        {
            public void run()
            {
                util.create(mediarecorde, MediaActivity.this, MediaActivity.this);
            }
        }, 300);
        
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        GlideCacheUtil.getInstance().clearImageAllCache(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            stopVideo(3);
            return  true;
        }
        return false;
    }


    
    // a integer to xx:xx:xx
    public String secToTime(int time)
    {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else
        {
            minute = time / 60;
            if (minute < 60)
            {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            }
            else
            {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }
    
    public static String unitFormat(int i)
    {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
    
}
