package com.vomont.yundudao.ui.createproblem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vomont.yundudao.R;
import com.vomont.yundudao.utils.GlideCacheUtil;
import com.vomont.yundudao.utils.addpic.LocalMedia;
import com.vomont.yundudao.view.ios.AlertDialog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageSettingActivity extends Activity implements OnClickListener
{
    
    private ViewPager pager_img_setting;
    
    private ImageView img_setting_goback;
    
    private TextView img_setting_index;
    
    private TextView img_setting_max;
    
    private LinearLayout edit_img;
    
    private LinearLayout delete_img;
    
    private Intent intent;
    
    private int nowPosition;
    
    private List<ImageView> img_list;
    
    private ViewPagerAdapter adapter;
    
    private List<LocalMedia> sendList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_setting);
        GlideCacheUtil.getInstance().clearImageAllCache(getApplicationContext());
        System.gc();
        initView();
        initListener();
        initData();
    }
    
    @SuppressWarnings("unchecked")
    private void initData()
    {
        img_list = new ArrayList<ImageView>();
        intent = getIntent();
        nowPosition = intent.getIntExtra("position", 0);
        sendList = (List<LocalMedia>)intent.getSerializableExtra("sendpic");
        img_setting_index.setText(nowPosition + 1 + "");
        img_setting_max.setText(sendList.size() + "");
        
        for (@SuppressWarnings("unused")
        LocalMedia localMedia : sendList)
        {
            ImageView view = new ImageView(this);
            view.setScaleType(ScaleType.FIT_CENTER);
            img_list.add(view);
        }
        adapter = new ViewPagerAdapter(img_list);
        pager_img_setting.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        pager_img_setting.setCurrentItem(nowPosition);
        pager_img_setting.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int arg0)
            {
                
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
                nowPosition = arg0;
                img_setting_index.setText(nowPosition + 1 + "");
                ImageLoader.getInstance().displayImage("file://" + sendList.get(arg0).getPath(), img_list.get(arg0));
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0)
            {
                
            }
        });
    }
    
    private void initListener()
    {
        img_setting_goback.setOnClickListener(this);
        edit_img.setOnClickListener(this);
        delete_img.setOnClickListener(this);
    }
    
    private void initView()
    {
        pager_img_setting = (ViewPager)findViewById(R.id.pager_img_setting);
        img_setting_goback = (ImageView)findViewById(R.id.img_setting_goback);
        img_setting_index = (TextView)findViewById(R.id.img_setting_index);
        img_setting_max = (TextView)findViewById(R.id.img_setting_max);
        edit_img = (LinearLayout)findViewById(R.id.edit_img);
        delete_img = (LinearLayout)findViewById(R.id.delete_img);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        // Glide.with(getApplicationContext()).load(new
        // File(sendList.get(nowPosition).getPath())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img_list.get(nowPosition));
        ImageLoader.getInstance().displayImage("file://" + sendList.get(nowPosition).getPath(), img_list.get(nowPosition));
    }
    
    @Override
    public void onClick(View arg0)
    {
        switch (arg0.getId())
        {
            case R.id.img_setting_goback:
                intent.putExtra("sendpaths", (Serializable)sendList);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.edit_img:
                intent = new Intent(ImageSettingActivity.this, ProblemImageChangeActivity.class);
                intent.putExtra("changeimg", sendList.get(nowPosition));
                startActivityForResult(intent, 0x11);
                break;
            case R.id.delete_img:
                new AlertDialog(this).builder().setMsg("确定删除").setNegativeButton("确定", new OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        if (sendList != null && sendList.size() != 0)
                        {
                            sendList.remove(nowPosition);
                            img_list.remove(nowPosition);
                            adapter.notifyDataSetChanged();
                            if ((nowPosition + 1) <= sendList.size())
                            {
                                pager_img_setting.setCurrentItem(nowPosition);
                            }
                            else
                            {
                                pager_img_setting.setCurrentItem(0);
                            }
                            if (sendList == null || sendList.size() == 0)
                            {
                                intent.putExtra("sendpaths", (Serializable)sendList);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            img_setting_max.setText(sendList.size() + "");
                        }
                    }
                }).setPositiveButton("取消", new OnClickListener()
                {
                    @Override
                    public void onClick(View arg0)
                    {
                        
                    }
                }).show();
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x11)
        {
            if (data != null)
            {
                LocalMedia localMedia = (LocalMedia)data.getSerializableExtra("changeimg");
                sendList.get(nowPosition).setPath(localMedia.getPath());
                
            }
        }
    }
    
    class ViewPagerAdapter extends PagerAdapter
    {
        
        List<ImageView> viewLists;
        
        public ViewPagerAdapter(List<ImageView> lists)
        {
            viewLists = lists;
        }
        
        @Override
        public Parcelable saveState()
        {
            return null;
        }
        
        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }
        
        @Override
        public int getCount()
        {
            return viewLists.size();
        }
        
        @Override
        public boolean isViewFromObject(View arg0, Object arg1)
        {
            return arg0 == arg1;
        }
        
        @Override
        public void destroyItem(View view, int position, Object object)
        {
            ((ViewPager)view).removeView((View)object);
        }
        
        @Override
        public Object instantiateItem(View view, int position)
        {
            ((ViewPager)view).addView(viewLists.get(position), 0);
            
            return viewLists.get(position);
        }
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            intent.putExtra("sendpaths", (Serializable)sendList);
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }
}
