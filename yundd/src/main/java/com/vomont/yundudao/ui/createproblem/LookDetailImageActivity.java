package com.vomont.yundudao.ui.createproblem;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.NewProblemImg;

public class LookDetailImageActivity extends Activity
{
    
    private TextView img_show_num;
    
    private TextView img_show_maxnum;
    
    private ViewPager detail_img_pager;
    
    private Intent intent;
    
    private int nowPosition;
    
    private List<ImageView> img_list;
    
    private RelativeLayout ll_layout;
    
    private DetailImageAdapter adapter;
    
    private List<NewProblemImg> mlistPath;
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_img);
        img_show_num = (TextView)findViewById(R.id.img_show_num);
        img_show_maxnum = (TextView)findViewById(R.id.img_show_maxnum);
        detail_img_pager = (ViewPager)findViewById(R.id.detail_img_pager);
        ll_layout = (RelativeLayout)findViewById(R.id.ll_layout);
        intent = getIntent();
        mlistPath = (List<NewProblemImg>)intent.getSerializableExtra("mlistPath");
        nowPosition = intent.getIntExtra("position", 0);
        img_show_num.setText(nowPosition + 1 + "");
        img_show_maxnum.setText(mlistPath.size() + "");
        img_list = new ArrayList<ImageView>();
        for (int i = 0; i < mlistPath.size(); i++)
        {
            ImageView view = new ImageView(this);
            view.setScaleType(ScaleType.FIT_CENTER);
            img_list.add(view);
            view.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
            
        }
        adapter = new DetailImageAdapter(img_list);
        detail_img_pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        detail_img_pager.setCurrentItem(nowPosition);
        detail_img_pager.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int arg0)
            {
                nowPosition = arg0;
                img_show_num.setText(nowPosition + 1 + "");
                Glide.with(getApplicationContext()).load(mlistPath.get(nowPosition).getImageurl()).error(R.drawable.qcenter_factorypic).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_list.get(nowPosition));
                // showImg();
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
        
        ll_layout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        Glide.with(getApplicationContext()).load(mlistPath.get(nowPosition).getImageurl()).error(R.drawable.qcenter_factorypic).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(img_list.get(nowPosition));
    }
    
    class DetailImageAdapter extends PagerAdapter
    {
        
        List<ImageView> viewLists;
        
        public DetailImageAdapter(List<ImageView> lists)
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
    
    public Bitmap stringtoBitmap(String string)
    {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            
            // 解决加载图片 内存溢出的问题
            // Options 只保存图片尺寸大小，不保存图片到内存
            BitmapFactory.Options opts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，其值表明缩放的倍数，SDK中建议其值是2的指数值,值越大会导致图片不清晰
            opts.inSampleSize = 2;
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length, opts);
            // 回收
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return bitmap;
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        System.gc();
    }
}
