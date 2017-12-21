package com.vomont.yundudao.ui.patrol;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.vomont.fileloadsdk.WMFileLoadSdk;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.TypeVideo;
import com.vomont.yundudao.ui.patrol.adapter.DeleteVideoAdapter;
import com.vomont.yundudao.ui.patrol.adapter.DeleteVideoAdapter.OnCheckBoxChangeListenr;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoHelpter;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.utils.SortClass;
import com.vomont.yundudao.view.ios.AlertDialog;

public class DeleteVideoActivity extends Activity implements OnClickListener
{
    private ListView delete_video_list;
    
    private ImageView delete_video_back;
    
    private ImageView delete_img;
    
    private List<DeletePosition> mlistPositions;
    
    private VideoHelpter helper;
    
    private ArrayList<TypeVideo> mlist;
    
    private int removeI;
    
    private DeleteVideoAdapter adapter;
    
    private ShareUtil shareUtil;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletevideo);
        shareUtil = new ShareUtil(this);
        initView();
        initListener();
        delete_video_list.postDelayed(new Runnable()
        {
            public void run()
            {
                showView();
            }
        }, 100);
    }
    
    private void initListener()
    {
        delete_video_back.setOnClickListener(this);
        delete_img.setOnClickListener(this);
    }
    
    private void initView()
    {
        delete_video_list = (ListView)findViewById(R.id.delete_video_list);
        delete_video_back = (ImageView)findViewById(R.id.delete_video_back);
        delete_img = (ImageView)findViewById(R.id.delete_img);
        mlistPositions = new ArrayList<DeleteVideoActivity.DeletePosition>();
        helper = new VideoHelpter(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.delete_video_back:
                finish();
                break;
            case R.id.delete_img:
                
                if (mlistPositions == null || mlistPositions.size() == 0)
                {
                    Toast.makeText(DeleteVideoActivity.this, "请选择视频!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    new AlertDialog(this).builder().setMsg("是否删除视频").setNegativeButton("确定", new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (mlistPositions != null && mlistPositions.size() != 0)
                            {
                                for (int i = 0; i < mlistPositions.size(); i++)
                                {
                                    VideoBean bean = mlist.get(mlistPositions.get(i).position).getMlist().get(mlistPositions.get(i).childPosition);
                                    File file = new File(bean.getVideoPath() + "/" + bean.getName() + ".mp4");
                                    if (file.exists())
                                    {
                                        file.delete();
                                    }
                                    helper.deleteVideo(bean.getName());
                                    WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(bean.getFileId(), true);
                                    WMFileLoadSdk.getInstance().WM_VFile_StopUpload(bean.getFileId());
                                }
                                delete_video_list.postDelayed(new Runnable()
                                {
                                    public void run()
                                    {
                                        showView();
                                    }
                                }, 100);
                                mlistPositions.removeAll(mlistPositions);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }).setPositiveButton("取消", new OnClickListener()
                    {
                        
                        @Override
                        public void onClick(View v)
                        {
                        }
                    }).show();
                }
                break;
            default:
                break;
        }
    }
    
    @SuppressWarnings("unchecked")
    public void showView()
    {
        mlist = new ArrayList<TypeVideo>();
        ArrayList<VideoBean> unUpmlist = new ArrayList<VideoBean>();
        ArrayList<VideoBean> upmlist = new ArrayList<VideoBean>();
        List<VideoBean> mMlist = new ArrayList<VideoBean>();
        if (helper.selectAll() != null)
            mMlist.addAll(helper.selectAll());
        
        if (mMlist != null && mMlist.size() != 0)
            
            for (int i = 0; i < mMlist.size(); i++)
            {
                if (shareUtil.getShare().getAccountid() == mMlist.get(i).getCreateId())
                {
                    if (mMlist.get(i).getVideoid() == 0)
                    {
                        unUpmlist.add(mMlist.get(i));
                    }
                    else
                    {
                        upmlist.add(mMlist.get(i));
                    }
                }
            }
        if (unUpmlist != null && unUpmlist.size() != 0)
        {
            TypeVideo novideo = new TypeVideo();
            novideo.setType("未上传");
            SortClass sort = new SortClass();
            Collections.sort(unUpmlist, sort);
            novideo.setMlist(unUpmlist);
            mlist.add(novideo);
        }
        if (upmlist != null && upmlist.size() != 0)
        {
            TypeVideo video = new TypeVideo();
            video.setType("已上传");
            SortClass sort = new SortClass();
            Collections.sort(upmlist, sort);
            video.setMlist(upmlist);
            mlist.add(video);
        }
        
        adapter = new DeleteVideoAdapter(this, mlist);
        delete_video_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnCheckBoxListener(new OnCheckBoxChangeListenr()
        {
            @Override
            public void delete(int postion, int childPostion)
            {
                DeletePosition deletePosition = new DeletePosition();
                deletePosition.childPosition = childPostion;
                deletePosition.position = postion;
                if (contains(deletePosition))
                {
                    mlistPositions.remove(removeI);
                }
            }
            
            @Override
            public void add(int postion, int childPostion)
            {
                DeletePosition deletePosition = new DeletePosition();
                deletePosition.childPosition = childPostion;
                deletePosition.position = postion;
                if (!contains(deletePosition))
                {
                    mlistPositions.add(deletePosition);
                }
            }
        });
    }
    
    private boolean contains(DeletePosition deletePosition)
    {
        boolean result = false;
        if (mlistPositions != null && mlistPositions.size() != 0)
        {
            for (int i = 0; i < mlistPositions.size(); i++)
            {
                if (mlistPositions.get(i).childPosition == deletePosition.childPosition && mlistPositions.get(i).position == deletePosition.position)
                {
                    result = true;
                    removeI = i;
                }
            }
        }
        return result;
    }
    
    @Override
    protected void onPause()
    {
        super.onPause();
    }
    
    class DeletePosition
    {
        private int position;
        
        private int childPosition;
    }
}
