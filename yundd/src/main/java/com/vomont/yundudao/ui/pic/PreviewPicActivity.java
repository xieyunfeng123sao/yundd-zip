package com.vomont.yundudao.ui.pic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.createproblem.ProblemImageChangeActivity;
import com.vomont.yundudao.ui.pic.adapter.PreViewAdapater;
import com.vomont.yundudao.utils.GlideCacheUtil;
import com.vomont.yundudao.utils.addpic.LocalMedia;
import com.vomont.yundudao.utils.addpic.LocalMediaFolder;

public class PreviewPicActivity extends Activity implements OnClickListener
{
    private ImageView previewpic_left_back;
    
    private TextView preview_now_num;
    
    private TextView preview_all_num;
    
    private LinearLayout ll_preview;
    
    private TextView preview_check;
    
    private ViewPager preview_pager;
    
    private TextView preview_edit;
    
    private TextView preview_ok;
    
    private LinearLayout preview_ll_ok;
    
    private List<LocalMediaFolder> mFolders;
    
    private List<LocalMedia> mMedias;
    
    private List<LocalMedia> sendMedias;
    
    private PreViewAdapater adapater;
    
    private List<ImageView> mlist;
    
    private int nowPosition;
    
    private int maxNum;
    
    public final int INTENT_TO_CHANG_IMG = 0;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previewpic);
        GlideCacheUtil.getInstance().clearImageAllCache(getApplicationContext());
        initView();
        initListener();
        initData();
    }
    
    private void initListener()
    {
        previewpic_left_back.setOnClickListener(this);
        ll_preview.setOnClickListener(this);
        preview_edit.setOnClickListener(this);
        preview_ll_ok.setOnClickListener(this);
    }
    
    @SuppressWarnings("unchecked")
    private void initData()
    {
        mMedias = (List<LocalMedia>)getIntent().getSerializableExtra("LocalMedia");
        sendMedias = (List<LocalMedia>)getIntent().getSerializableExtra("sendLocalMedia");
        mFolders = (List<LocalMediaFolder>)getIntent().getSerializableExtra("LocalMediaFolder");
        maxNum = getIntent().getIntExtra("maxNum", 0);
        nowPosition = getIntent().getIntExtra("position", 0);
        mlist = new ArrayList<ImageView>();
        for (int i = 0; i < mMedias.size(); i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ScaleType.FIT_CENTER);
            mlist.add(imageView);
        }
        ImageLoader.getInstance().displayImage("file://"+mMedias.get(nowPosition).getPath(),  mlist.get(nowPosition));
//        Glide.with(this).load(new File(mMedias.get(nowPosition).getPath())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into( mlist.get(nowPosition));
        preview_all_num.setText(mMedias.size() + "");
        preview_now_num.setText((nowPosition + 1) + "");
        showsendNum();
        if (mMedias.get(nowPosition).getIsChecked())
        {
            preview_check.setSelected(true);
        }
        else
        {
            preview_check.setSelected(false);
        }
        
        adapater = new PreViewAdapater(mlist);
        preview_pager.setAdapter(adapater);
        adapater.notifyDataSetChanged();
        preview_pager.setCurrentItem(nowPosition);
        preview_pager.setOnPageChangeListener(new OnPageChangeListener()
        {
            
            @Override
            public void onPageSelected(int arg0)
            {
                nowPosition = arg0;
                preview_now_num.setText((arg0 + 1) + "");
                if (mMedias.get(arg0).getIsChecked())
                {
                    preview_check.setSelected(true);
                }
                else
                {
                    preview_check.setSelected(false);
                }
                ImageLoader.getInstance().displayImage("file://"+mMedias.get(arg0).getPath(),  mlist.get(arg0));
//                Glide.with(PreviewPicActivity.this).load(new File(mMedias.get(arg0).getPath())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(mlist.get(arg0));
//                LoadLocalImageUtil.getInstance().displayFromSDCard(mMedias.get(arg0).getPath(), mlist.get(arg0));
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0)
            {
                
            }
        });
    }
    
    private void showsendNum()
    {
        if (sendMedias.size() == 0)
        {
            preview_ok.setText("确定");
        }
        else
        {
            preview_ok.setText("确定(" + sendMedias.size() + ")");
        }
    }
    
    private void initView()
    {
        previewpic_left_back = (ImageView)findViewById(R.id.previewpic_left_back);
        preview_now_num = (TextView)findViewById(R.id.preview_now_num);
        preview_all_num = (TextView)findViewById(R.id.preview_all_num);
        ll_preview = (LinearLayout)findViewById(R.id.ll_preview);
        preview_check = (TextView)findViewById(R.id.preview_check);
        preview_pager = (ViewPager)findViewById(R.id.preview_pager);
        preview_edit = (TextView)findViewById(R.id.preview_edit);
        preview_ok = (TextView)findViewById(R.id.preview_ok);
        preview_ll_ok = (LinearLayout)findViewById(R.id.preview_ll_ok);
    }
    
    @Override
    public void onClick(View v)
    {
        Intent intent;
        switch (v.getId())
        {
            case R.id.previewpic_left_back:
                intent = getIntent();
                intent.putExtra("sendLocalMedia", (Serializable)sendMedias);
                intent.putExtra("LocalMediaFolder", (Serializable)mFolders);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.ll_preview:
                if (mMedias.get(nowPosition).getIsChecked())
                {
                    preview_check.setSelected(false);
                    mMedias.get(nowPosition).setIsChecked(false);
                    for (int i = 0; i < mFolders.size(); i++)
                    {
                        for (int j = 0; j < mFolders.get(i).getImages().size(); j++)
                        {
                            if (mFolders.get(i).getImages().get(j).getPath().equals(mMedias.get(nowPosition).getPath()))
                            {
                                mFolders.get(i).getImages().get(j).setIsChecked(false);
                            }
                        }
                    }
                    for (int i = 0; i < sendMedias.size(); i++)
                    {
                        if (sendMedias.get(i).getPath().equals(mMedias.get(nowPosition).getPath()))
                        {
                            sendMedias.remove(i);
                        }
                    }
                }
                else
                {
                    if (sendMedias.size() < maxNum)
                    {
                        preview_check.setSelected(true);
                        mMedias.get(nowPosition).setIsChecked(true);
                        for (int i = 0; i < mFolders.size(); i++)
                        {
                            for (int j = 0; j < mFolders.get(i).getImages().size(); j++)
                            {
                                if (mFolders.get(i).getImages().get(j).getPath().equals(mMedias.get(nowPosition).getPath()))
                                {
                                    mFolders.get(i).getImages().get(j).setIsChecked(true);
                                }
                            }
                        }
                        sendMedias.add(mMedias.get(nowPosition));
                    }
                    else
                    {
                        Toast.makeText(PreviewPicActivity.this, "最多不超过" + maxNum + "张图片!", Toast.LENGTH_SHORT).show();
                    }
                }
                showsendNum();
                break;
            case R.id.preview_edit:
                intent = new Intent(PreviewPicActivity.this, ProblemImageChangeActivity.class);
                int position = 0;
                for (int i = 0; i < mFolders.size(); i++)
                {
                    for (int j = 0; j < mFolders.get(i).getImages().size(); j++)
                    {
                        if (mMedias.get(nowPosition).getPath().equals(mFolders.get(i).getImages().get(j).getPath()))
                        {
                            position = i;
                        }
                    }
                }
                intent.putExtra("position", position);
                intent.putExtra("LocalMediaFolder", (Serializable)mFolders);
                intent.putExtra("changeimg", mMedias.get(nowPosition));
                startActivityForResult(intent, INTENT_TO_CHANG_IMG);
                break;
            case R.id.preview_ll_ok:
                intent = getIntent();
                if (sendMedias.size() == 0)
                {
                    sendMedias.add(mMedias.get(nowPosition));
                    intent.putExtra("sendimg", (Serializable)sendMedias);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    intent.putExtra("sendimg", (Serializable)sendMedias);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent intent = getIntent();
            intent.putExtra("sendLocalMedia", (Serializable)sendMedias);
            intent.putExtra("LocalMediaFolder", (Serializable)mFolders);
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case INTENT_TO_CHANG_IMG:
                if (data != null)
                {
                    mFolders.clear();
                    mFolders.addAll((List<LocalMediaFolder>)data.getSerializableExtra("LocalMediaFolder"));
                    for (int i = 0; i < mFolders.size(); i++)
                    {
                        if (mFolders.get(i).getImages() != null && mFolders.get(i).getImages().size() != 0)
                        {
                            for (int j = 0; j < mFolders.get(i).getImages().size(); j++)
                            {
                                if (mFolders.get(i).getImages().get(j).getPath().equals(mMedias.get(nowPosition).getPath()))
                                {
                                    mFolders.get(i).getImages().get(j).setPath(((LocalMedia)data.getSerializableExtra("changeimg")).getPath());
                                }
                            }
                        }
                    }
                    
                    for (int i = 0; i < sendMedias.size(); i++)
                    {
                        if (sendMedias.get(i).getPath().equals(mMedias.get(nowPosition).getPath()))
                        {
                            sendMedias.get(i).setPath(((LocalMedia)data.getSerializableExtra("changeimg")).getPath());
                        }
                    }
                    mMedias.get(nowPosition).setPath(((LocalMedia)data.getSerializableExtra("changeimg")).getPath());
//                    LoadLocalImageUtil.getInstance().displayFromSDCard(mMedias.get(nowPosition).getPath(), mlist.get(nowPosition));
//                    Glide.with(PreviewPicActivity.this).load(new File(mMedias.get(nowPosition).getPath())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(mlist.get(nowPosition));
                    ImageLoader.getInstance().displayImage("file://"+mMedias.get(nowPosition).getPath(),  mlist.get(nowPosition));
                
                }
                break;
            
            default:
                break;
        }
        
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        GlideCacheUtil.getInstance().clearImageAllCache(this);
    }
    
}
