package com.vomont.yundudao.ui.pic;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentProvider;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.createproblem.ProblemImageChangeActivity;
import com.vomont.yundudao.ui.pic.adapter.AddPicAdapter;
import com.vomont.yundudao.ui.pic.adapter.AddPicAdapter.OnCheckImageListener;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.GlideCacheUtil;
import com.vomont.yundudao.utils.addpic.LocalMedia;
import com.vomont.yundudao.utils.addpic.LocalMediaFolder;
import com.vomont.yundudao.utils.addpic.LocalMediaLoader;
import com.vomont.yundudao.utils.addpic.LocalMediaLoader.LocalMediaLoadListener;

public class AddPicActivity extends FragmentActivity implements OnClickListener
{
    private ImageView picture_left_back;
    
    private TextView picture_tv_title;
    
    private TextView picture_tv_right;
    
    private GridView pic_gridview;
    
    private TextView id_preview;
    
    private TextView id_edit;
    
    private TextView tv_ok;
    
    private int showType;
    
    private AddPicAdapter adapter;
    
    private List<LocalMedia> localMedias;
    
    private List<LocalMedia> neddMedias;
    
    private int maxNum;
    
    private List<LocalMediaFolder> mFolders;
    
    private final static int INTENT_TO_PIC_LIST = 0;
    
    private final static int INTENT_TO_PREVIEW_PIC = 1;
    
    private final static int INTENT_TO_CHANGE_PIC = 2;
    
    private String name;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpic);
        GlideCacheUtil.getInstance().clearImageAllCache(getApplicationContext());
        initView();
        initData();
        initListener();
    }
    
    private void initListener()
    {
        picture_left_back.setOnClickListener(this);
        picture_tv_right.setOnClickListener(this);
        id_preview.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        id_edit.setOnClickListener(this);
    }
    
    private void initData()
    {
        localMedias = new ArrayList<LocalMedia>();
        neddMedias = new ArrayList<LocalMedia>();
        mFolders = new ArrayList<LocalMediaFolder>();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        showType = intent.getIntExtra("showType", 0);
        // 显示第几项的图片列表
        // showType = intent.getIntExtra("showType", 0);
        
        // 最多可以选择图片的数量
        maxNum = intent.getIntExtra("maxNum", 9);
        adapter = new AddPicAdapter(this);
        adapter.setList(localMedias);
        adapter.setMaxCheck(maxNum);
        pic_gridview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        // 获取本地图片的回调方法
        new LocalMediaLoader(this, 0, false).loadAllImage(new LocalMediaLoadListener()
        {
            @Override
            public void loadComplete(List<LocalMediaFolder> folders)
            {
                if (name != null)
                {
                    File file = new File(VideoManager.base + "/" + name);
                    if (file.exists())
                    {
                        File[] files = file.listFiles();
                        LocalMediaFolder localMediaFolder = new LocalMediaFolder();
                        List<LocalMedia> mlist_locaList = new ArrayList<LocalMedia>();
                        for (File filename : files)
                        {
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(filename.getPath());
                            localMedia.setIsChecked(false);
                            mlist_locaList.add(localMedia);
                        }
                        Collections.reverse(mlist_locaList);
                        localMediaFolder.setImages(mlist_locaList);
                        localMediaFolder.setName(name);
                        if (files != null && files.length != 0 && files[0].exists())
                        {
                            localMediaFolder.setFirstImagePath(files[0].getPath());
                        }
                        mFolders.add(localMediaFolder);
                        localMedias.addAll(mlist_locaList);
                    }
                    picture_left_back.setVisibility(View.GONE);
                    picture_tv_title.setText(name);
                }
                else
                {
                    mFolders.addAll(folders);
                    picture_tv_title.setText(folders.get(showType).getName());
                    localMedias.addAll(folders.get(showType).getImages());
                }
                adapter.setList(localMedias);
                adapter.notifyDataSetChanged();
                
            }
        });
        // 回调需要上传的图片的信息
        adapter.setOnCheckImageListener(new OnCheckImageListener()
        {
            @Override
            public void getLocalMedias(List<LocalMedia> mlist)
            {
                neddMedias.clear();
                neddMedias.addAll(mlist);
                pullButtomView(mlist);
            }
        });
        
        // 跳转到图片预览界面
        pic_gridview.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(AddPicActivity.this, PreviewPicActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("LocalMediaFolder", (Serializable)mFolders);
                intent.putExtra("LocalMedia", (Serializable)localMedias);
                intent.putExtra("sendLocalMedia", (Serializable)neddMedias);
                intent.putExtra("maxNum", maxNum);
                startActivityForResult(intent, INTENT_TO_PREVIEW_PIC);
            }
        });
    }
    
    /**
     * 最底部几个按键的显示样式
     * 
     * @param mlist
     */
    public void pullButtomView(List<LocalMedia> mlist)
    {
        if (mlist.size() != 0)
        {
            tv_ok.setText("确定" + "(" + mlist.size() + ")");
            tv_ok.setTextColor(getResources().getColor(R.color.main_color));
            id_preview.setTextColor(getResources().getColor(R.color.main_color));
            if (mlist.size() == 1)
            {
                id_edit.setTextColor(getResources().getColor(R.color.main_color));
            }
            else
            {
                id_edit.setTextColor(getResources().getColor(R.color.text_color));
            }
        }
        else
        {
            tv_ok.setText("确定");
            tv_ok.setTextColor(getResources().getColor(R.color.text_color));
            id_preview.setTextColor(getResources().getColor(R.color.text_color));
            id_edit.setTextColor(getResources().getColor(R.color.text_color));
        }
    }
    
    private void initView()
    {
        picture_left_back = (ImageView)findViewById(R.id.picture_left_back);
        picture_tv_title = (TextView)findViewById(R.id.picture_tv_title);
        picture_tv_right = (TextView)findViewById(R.id.picture_tv_right);
        pic_gridview = (GridView)findViewById(R.id.pic_gridview);
        id_preview = (TextView)findViewById(R.id.id_preview);
        tv_ok = (TextView)findViewById(R.id.tv_ok);
        id_edit = (TextView)findViewById(R.id.id_edit);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
    }
    
    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
        // 点击返回键 跳转文件夹列表界面
            case R.id.picture_left_back:
                intent = new Intent(AddPicActivity.this, PicListActivity.class);
                intent.putExtra("LocalMediaFolder", (Serializable)mFolders);
                startActivityForResult(intent, INTENT_TO_PIC_LIST);
                break;
            // 点击取消按钮
            case R.id.picture_tv_right:
                finish();
                break;
            // 点击预览按钮
            case R.id.id_preview:
                if (neddMedias.size() > 0)
                {
                    intent = new Intent(AddPicActivity.this, PreviewPicActivity.class);
                    intent.putExtra("LocalMediaFolder", (Serializable)mFolders);
                    intent.putExtra("LocalMedia", (Serializable)neddMedias);
                    intent.putExtra("sendLocalMedia", (Serializable)neddMedias);
                    intent.putExtra("maxNum", maxNum);
                    startActivityForResult(intent, INTENT_TO_PREVIEW_PIC);
                }
                break;
            // 点击确定按钮
            case R.id.tv_ok:
                if (neddMedias.size() > 0)
                {
                    intent = getIntent();
                    intent.putExtra("sendimg", (Serializable)neddMedias);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            // 点击编辑按钮
            case R.id.id_edit:
                if (neddMedias.size() == 1)
                {
                    intent = new Intent(AddPicActivity.this, ProblemImageChangeActivity.class);
                    intent.putExtra("position", showType);
                    intent.putExtra("LocalMediaFolder", (Serializable)mFolders);
                    intent.putExtra("changeimg", neddMedias.get(0));
                    startActivityForResult(intent, INTENT_TO_CHANGE_PIC);
                }
                break;
            default:
                break;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2)
    {
        super.onActivityResult(arg0, arg1, arg2);
        switch (arg0)
        {
        // 图片文件夹列表返回
            case INTENT_TO_PIC_LIST:
                if (arg2 != null)
                {
                    showType = arg2.getIntExtra("showType", 0);
                    picture_tv_title.setText(mFolders.get(showType).getName());
                    localMedias.clear();
                    localMedias.addAll(mFolders.get(showType).getImages());
                    adapter.setList(localMedias);
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    finish();
                }
                break;
            // 图片预览界面返回
            case INTENT_TO_PREVIEW_PIC:
                if (arg2 != null)
                {
                    if (arg2.getSerializableExtra("sendimg") != null)
                    {
                        Intent intent = getIntent();
                        intent.putExtra("sendimg", (Serializable)(List<LocalMedia>)arg2.getSerializableExtra("sendimg"));
                        setResult(RESULT_OK, intent);
                        finish();
                        
                    }
                    else
                    {
                        mFolders.clear();
                        mFolders.addAll((List<LocalMediaFolder>)arg2.getSerializableExtra("LocalMediaFolder"));
                        localMedias.clear();
                        localMedias.addAll(mFolders.get(showType).getImages());
                        neddMedias.clear();
                        neddMedias.addAll((List<LocalMedia>)arg2.getSerializableExtra("sendLocalMedia"));
                        adapter.setMaxCheck(maxNum);
                        adapter.setList(localMedias);
                        adapter.notifyDataSetChanged();
                        pullButtomView(neddMedias);
                    }
                }
                break;
            case INTENT_TO_CHANGE_PIC:
                if (arg2 != null)
                {
                    mFolders.clear();
                    mFolders.addAll((List<LocalMediaFolder>)arg2.getSerializableExtra("LocalMediaFolder"));
                    localMedias.clear();
                    localMedias.addAll(mFolders.get(showType).getImages());
                    neddMedias.clear();
                    neddMedias.add((LocalMedia)arg2.getSerializableExtra("changeimg"));
                    adapter.setMaxCheck(maxNum);
                    adapter.setList(localMedias);
                    pic_gridview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    pullButtomView(neddMedias);
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        GlideCacheUtil.getInstance().clearImageAllCache(this);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        GlideCacheUtil.getInstance().clearImageAllCache(this);
    }
}
