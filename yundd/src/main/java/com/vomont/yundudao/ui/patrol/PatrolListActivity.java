package com.vomont.yundudao.ui.patrol;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.PatrolDetailItem;
import com.vomont.yundudao.bean.PatrolListItem;
import com.vomont.yundudao.mvpview.patrol.IPatrolListView;
import com.vomont.yundudao.presenter.patrol.PatrolListPresenter;
import com.vomont.yundudao.ui.patrol.adapter.PatrolListAdapter;
import com.vomont.yundudao.ui.patrol.adapter.PatrolListAdapter.OnItemClick;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.utils.UriUtil;

public class PatrolListActivity extends Activity implements OnClickListener, IPatrolListView
{
    private ImageView patrol_list_back;
    
    private ImageView patrol_list_search;
    
    private ImageView patrol_list_upload;
    
    private Intent intent;
    
    private Context context;
    
    private PullToRefreshListView pull_patrol;
    
    private Dialog dialog;
    
    private ShareUtil util;
    
    private PatrolListPresenter presenter;
    
    private List<PatrolListItem> mlist;
    
    private PatrolListAdapter adapter;
    
    private FactoryBean factoryBean;
    
    private int page = 1;
    
    // private int pullShowPosition;
    
    private LinearLayout get_vedio;
    
    private ImageView empty_patrol_list;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_list);
        initView();
        initData();
        initListener();
    }
    
    private void initListener()
    {
        patrol_list_back.setOnClickListener(this);
        patrol_list_search.setOnClickListener(this);
        patrol_list_upload.setOnClickListener(this);
        get_vedio.setOnClickListener(this);
    }
    
    private void initView()
    {
        patrol_list_back = (ImageView)findViewById(R.id.patrol_list_back);
        patrol_list_search = (ImageView)findViewById(R.id.patrol_list_search);
        patrol_list_upload = (ImageView)findViewById(R.id.patrol_list_upload);
        pull_patrol = (PullToRefreshListView)findViewById(R.id.pull_patrol);
        get_vedio = (LinearLayout)findViewById(R.id.get_vedio);
        empty_patrol_list = (ImageView)findViewById(R.id.empty_patrol_list);
        context = this;
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        deleteFile();
    }
    
    private void deleteFile()
    {
        File file = new File(VideoManager.detail_img_cash);
        File[] files = file.listFiles();
        if (files != null && files.length != 0)
        {
            for (int i = 0; i < files.length; i++)
            {
                files[i].delete();
            }
            file.delete();
        }
    }
    
    @Override
    protected void onRestart()
    {
        super.onRestart();
        ACache aCache = ACache.get(this);
        String name = aCache.getAsString("videosend");
        if (name != null && name.equals("1"))
        {
            pull_patrol.setRefreshing();
            aCache.put("videosend", "0");
        }
        if (dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
    }
    
    private void initData()
    {
        util = new ShareUtil(context);
        presenter = new PatrolListPresenter(this);
        presenter.getPatrolList(util.getShare().getUser_id(), "", 1);
        mlist = new ArrayList<PatrolListItem>();
        factoryBean = (FactoryBean)getIntent().getSerializableExtra("factoryBean");
        pull_patrol.setMode(Mode.BOTH);
        pull_patrol.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
        pull_patrol.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        pull_patrol.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
        
        pull_patrol.setOnRefreshListener(new OnRefreshListener2<ListView>()
        {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                deleteFile();
                page = 1;
                presenter.getPatrolList(util.getShare().getUser_id(), "", 0);
            }
            
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                page++;
                presenter.getPatrolList(util.getShare().getUser_id(), "", mlist.get(mlist.size()-1).getVideoid());
            }
        });
        pull_patrol.postDelayed(new Runnable()
        {
            public void run()
            {
                pull_patrol.setRefreshing();
            }
        }, 200);
        
        adapter = new PatrolListAdapter(context, this.mlist, factoryBean);
        pull_patrol.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnItemAdapterClick(new OnItemClick()
        {
            @Override
            public void onClick(int position)
            {
                
                Intent intent = new Intent(context, PatrolDetailActivity.class);
                intent.putExtra("patrolitem", mlist.get(position));
                intent.putExtra("factoryBean", factoryBean);
                context.startActivity(intent);
//                presenter.getPatrolDetail(util.getShare().getUser_id(), mlist.get(position).getVideoid());
//                dialog = ProgressDialog.createLoadingDialog(context, "");
//                dialog.show();
            }
        });
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.patrol_list_back:
                finish();
                break;
            case R.id.patrol_list_search:
                break;
            case R.id.patrol_list_upload:
                intent = new Intent(context, PatrolLoadVedioActivity.class);
                startActivity(intent);
                break;
//            case R.id.get_vedio:
//                new ActionSheetDialog(PatrolListActivity.this).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem("拍摄", SheetItemColor.Blue, new OnSheetItemClickListener()
//                {
//                    @Override
//                    public void onClick(int which)
//                    {
//                        Intent intent = new Intent(context, MediaActivity.class);
//                        startActivity(intent);
//                    }
//                }).addSheetItem("从本地导入", SheetItemColor.Blue, new OnSheetItemClickListener()
//                {
//                    @Override
//                    public void onClick(int which)
//                    {
//                        Intent intent = new Intent();
//                        intent.setType("video/mp4"); // 选择视频 （mp4 3gp 是android支持的视频格式）
//                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        startActivityForResult(intent, 1);
//                    }
//                }).show();
//                break;
            default:
                break;
        }
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
                    retr.setDataSource(UriUtil.getPath(context, uri));
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "该视频为无效文件！", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                String height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
                String width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
                if (height != null || width != null)
                {
                    Intent intent = new Intent(context, VideoImportActivity.class);
                    intent.putExtra("videoPath", UriUtil.getPath(context, uri));
                    context.startActivity(intent);
                }
                else
                {
                    Toast.makeText(context, "该视频为无效文件！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    
    @Override
    public void getListData(List<PatrolListItem> list)
    {
        if (mlist != null)
        {
            // if (mlist != null && mlist.size() != 0)
            // {
            // pullShowPosition = mlist.size();
            // }
            if (page == 1)
            {
                if (mlist.size() >= 20)
                {
                    // 老数据和新数据进行比对 如果有更新就添加 没有就算了
                    // 目前只考虑到20条数据，接口的设计 只支持这种情况
                    
                    int addSize = -1;
                    if (list != null && list.size() != 0)
                    {
                        for (int i = 0; i < list.size(); i++)
                        {
                            for (int j = 0; j < mlist.size(); j++)
                            {
                                if (list.get(i).getVideoid() == mlist.get(j).getVideoid())
                                {
                                    addSize = i;
                                    break;
                                }
                            }
                            if (addSize == i)
                                break;
                        }
                    }
                    if (addSize != -1 && addSize != 0)
                    {
                        for (int i = addSize - 1; i >= 0; i--)
                        {
                            mlist.add(0, list.get(i));
                        }
                    }
                }
                else
                {
                    if (list != null && list.size() != 0)
                    {
                        if (mlist.size() != 0)
                        {
                            int addSize = -1;
                            for (int i = 0; i < list.size(); i++)
                            {
                                for (int j = 0; j < mlist.size(); j++)
                                {
                                    if (list.get(i).getVideoid() == mlist.get(j).getVideoid())
                                    {
                                        addSize = i;
                                        break;
                                    }
                                }
                                if (addSize == i)
                                    break;
                            }
                            
                            if (addSize != -1 && addSize != 0)
                            {
                                for (int i = addSize - 1; i >= 0; i--)
                                {
                                    mlist.add(0, list.get(i));
                                }
                            }
                        }
                        else
                        {
                            mlist.addAll(list);
                        }
                    }
                }
            }
            else
            {
                if (list != null)
                    for (int i = 0; i < list.size(); i++)
                    {
                        mlist.add(list.get(i));
                    }
            }
        }
        if (mlist == null || mlist.size() == 0)
        {
            empty_patrol_list.setVisibility(View.VISIBLE);
        }
        else
        {
            empty_patrol_list.setVisibility(View.GONE);
        }
        pull_patrol.onRefreshComplete();
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onFail()
    {
        pull_patrol.onRefreshComplete();
    }
    
    @Override
    public void getSucess(PatrolDetailItem item)
    {
        if (item != null)
        {
            Intent intent = new Intent(context, PatrolDetailActivity.class);
            intent.putExtra("patrolitem", item);
            intent.putExtra("factoryBean", factoryBean);
            context.startActivity(intent);
        }
        else
        {
            Toast.makeText(context, "获取详情失败！", Toast.LENGTH_LONG).show();
        }
        dialog.dismiss();
    }
    
    @Override
    public void getFail()
    {
        Toast.makeText(context, "获取详情失败！", Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }
}
