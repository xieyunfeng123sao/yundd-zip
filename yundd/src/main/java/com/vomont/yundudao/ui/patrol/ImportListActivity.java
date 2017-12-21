package com.vomont.yundudao.ui.patrol;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.ui.patrol.adapter.ImportAdapter;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoHelpter;
import com.vomont.yundudao.utils.GlideCacheUtil;
import com.vomont.yundudao.utils.ProgressDialog;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class ImportListActivity extends Activity
{
    
    private List<VideoInfo> mlist = new ArrayList<VideoInfo>();
    
    private VideoHelpter helpter;
    
    private GridView local_video;
    
    private ImportAdapter adapter;
    
    private ImageView go_back;
    
    private TextView top_name;
    
    private Dialog dialog;
    
    private String xiangce = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        helpter = new VideoHelpter(this);
        local_video = (GridView)findViewById(R.id.local_video);
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        top_name.setText("本地视频");
        adapter = new ImportAdapter(this);
        local_video.setAdapter(adapter);
        dialog = ProgressDialog.createLoadingDialog(this, "");
        dialog.show();
        local_video.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ImportListActivity.this, VideoImportActivity.class);
                intent.putExtra("videoPath", mlist.get(position).getPath());
                startActivity(intent);
                finish();
            }
        });
        go_back.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        new Thread(new Runnable()
        {
            public void run()
            {
                getLoadMedia();
                getVideoBySysCamare(xiangce);
                handler.sendEmptyMessage(100);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideCacheUtil.getInstance().clearImageAllCache(Appcation.context);
        System.gc();
    }

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            dialog.dismiss();
            adapter.setData(mlist);
            adapter.notifyDataSetChanged();
        };
    };
    
    public void getLoadMedia()
    {
        Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        try
        {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
//                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)); // id
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
//                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM)); // 专辑
//                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST)); // 艺术家
//                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 显示名称
//                String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)); // 路径
                long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)); // 时长
//                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)); // 大小
//                String resolution = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                VideoBean bean = helpter.selectByName(displayName);
                if ((bean == null) && path.substring(path.length() - 4, path.length()).equals(".mp4"))
                {
                    MediaMetadataRetriever retr = new MediaMetadataRetriever();
                    try
                    {
                        retr.setDataSource(path);
                    }
                    catch (Exception e)
                    {
                        continue;
                    }
                    String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
                    String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
                    retr.release();
                    if ((height != null || width != null) &&duration >= 1000)
                    {
                        VideoInfo info = new VideoInfo();
                        info.path = path;
                        info.duration = duration;
                        mlist.add(info);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            cursor.close();
        }
    }
    
    public void getVideoBySysCamare(String path)
    {
        File file = new File(path);
        File[] strName = file.listFiles();
        if (strName != null && strName.length != 0)
        {
            for (File name : strName)
            {
                if (name.isDirectory())
                {
                    getVideoBySysCamare(name.getAbsolutePath());
                }
                else
                {
                    if ((name.getAbsolutePath().substring(name.getAbsolutePath().length() - 4, name.getAbsolutePath().length())).equals(".mp4"))
                    {
                        MediaPlayer player = new MediaPlayer();
                        try
                        {
                            player.setDataSource(name.getAbsolutePath());
                            player.prepare();
                            int length = player.getDuration();
                            
                            MediaMetadataRetriever retr = new MediaMetadataRetriever();
                            try
                            {
                                retr.setDataSource(name.getAbsolutePath());
                            }
                            catch (Exception e)
                            {
                                return;
                            }
                            String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
                            String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
                            retr.release();
                            player.release();
                            if ((height != null || width != null) && length >= 1000)
                            {
                                VideoBean bean = helpter.selectByName(name.getName().replace(".mp4", ""));
                                if (bean == null)
                                {
                                    VideoInfo info = new VideoInfo();
                                    info.path = name.getAbsolutePath();
                                    info.duration = length;
                                    mlist.add(info);
                                }
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
                }
            }
        }
    }
}
