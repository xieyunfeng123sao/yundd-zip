package com.vomont.yundudao.ui.patrol;

import java.io.IOException;

import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.videoimport.VideoEditActivity;
import com.vomont.yundudao.ui.videoimport.VideoFaceActivity;
import com.vomont.yundudao.upload.VideoManager;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class VideoImportActivity extends Activity implements OnClickListener
{
    private ImageView go_back;
    
    private TextView top_name;
    
    private VideoView videoedit_video;
    
    private LinearLayout videoedit_ll;
    
    private TextView videoedit_edit;
    
    private TextView videoedit_ok;
    
    private ImageView videoedit_play;
    
    private String path;
    
    private String videoName;
    
    private String videoPath="";
    
    private  int length;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoedit);
        initView();
        initListener();
        initData();
    }
    
    private void initView()
    {
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        videoedit_video = (VideoView)findViewById(R.id.videoedit_video);
        videoedit_ll = (LinearLayout)findViewById(R.id.videoedit_ll);
        videoedit_edit = (TextView)findViewById(R.id.videoedit_edit);
        videoedit_ok = (TextView)findViewById(R.id.videoedit_ok);
        videoedit_play = (ImageView)findViewById(R.id.videoedit_play);
        videoedit_play.setImageResource(R.drawable.vedio_start);
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
        videoedit_edit.setOnClickListener(this);
        videoedit_ok.setOnClickListener(this);
        videoedit_play.setOnClickListener(this);
    }
    
    private void initData()
    {
        path = getIntent().getStringExtra("videoPath");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoedit_video.setLayoutParams(layoutParams);
        videoedit_video.setVideoPath(path);
      
        String[] str=path.split("/");
        videoName=str[str.length-1].replace(".mp4", "");
        for(int i=0;i<(str.length-1);i++)
        {
            if(i!=0)
            {
                videoPath=videoPath+"/"+str[i];
            }
           
        }
        
        top_name.setText("视频编辑");
        videoedit_video.seekTo(100);
        MediaPlayer player = new MediaPlayer();
        try
        {
            player.setDataSource(path);
            player.prepare();
            length = player.getDuration();
            player.release();
            if(VideoManager.maxTime>=length)
            {
                videoedit_ll.setVisibility(View.GONE);
                videoedit_edit.setVisibility(View.VISIBLE);
                videoedit_ok.setText("完成");
            }
            else
            {
                videoedit_edit.setVisibility(View.GONE);
                videoedit_ll.setVisibility(View.VISIBLE);
                videoedit_ok.setText("编辑");
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
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.go_back:
                finish();
                break;
            case R.id.videoedit_edit:
                Intent intentV = new Intent(VideoImportActivity.this, VideoEditActivity.class);
                intentV.putExtra("videoPath", path);
                startActivity(intentV);
                finish();
                break;
            case R.id.videoedit_ok:
                if(VideoManager.maxTime>=length)
                {
                    Intent intent = new Intent(VideoImportActivity.this, VideoFaceActivity.class);
                    intent.putExtra("path", videoPath);
                    intent.putExtra("name", videoName);
                    intent.putExtra("length", length);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intentVideo = new Intent(VideoImportActivity.this, VideoEditActivity.class);
                    intentVideo.putExtra("videoPath", path);
                    startActivity(intentVideo);
                    finish();
                }
                break;
            case R.id.videoedit_play:
                videoedit_video.start();
                videoedit_play.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    
}
