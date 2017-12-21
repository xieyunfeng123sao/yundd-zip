package com.vomont.yundudao.ui.pic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.PicTimeBean;
import com.vomont.yundudao.ui.createproblem.CreateProblemActivity;
import com.vomont.yundudao.ui.pic.adapter.ImageAdaper;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.GlideCacheUtil;
import com.vomont.yundudao.view.BaseActivity;

public class PicCenterActivity extends BaseActivity implements OnClickListener
{
    
    private RelativeLayout share_pic;
    
    private ImageView picview_go_back;
    
    private TextView picview_top_name;
    
    private TextView picview_name;
    
    private ViewPager picview_img;
    
    private RelativeLayout problem_send;
    
    private Intent intent;
    
    private int postion;
    
    private PicTimeBean picTimeBean;
    
    private List<ImageView> img_list;
    
    private ImageAdaper adaper;
    
    private int nowPosition;
    
    private FactoryBean bean;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imgview);
        GlideCacheUtil.getInstance().clearImageAllCache(getApplicationContext());
        initView();
        initData();
        initListener();
    }
    
    private void initData()
    {
        picview_top_name.setText("图片详情");
        img_list = new ArrayList<ImageView>();
        Intent intent = getIntent();
        picTimeBean = (PicTimeBean)intent.getSerializableExtra("picinfo");
        ACache aCache = ACache.get(this);
        postion = intent.getIntExtra("postion", -1);
        bean = (FactoryBean)aCache.getAsObject("factoryBean");
        nowPosition = postion;
        getName();
        for (int i = 0; i < picTimeBean.getPaths().size(); i++)
        {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ScaleType.FIT_XY);
            img_list.add(imageView);
        }
        
        adaper = new ImageAdaper(img_list);
        picview_img.setAdapter(adaper);
        adaper.notifyDataSetChanged();
        if (postion != -1)
        {
            picview_img.setCurrentItem(postion);
        }
        
        picview_img.setOnPageChangeListener(new OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                nowPosition = position;
                getName();
                Glide.with(getApplicationContext()).load(new File(picTimeBean.getPaths().get(position).getPath() + "/" + picTimeBean.getPaths().get(position).getName())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img_list.get(position));
            }
            
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                
            }
            
            @Override
            public void onPageScrollStateChanged(int state)
            {
                
            }
        });
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        Glide.with(getApplicationContext()).load(new File(picTimeBean.getPaths().get(nowPosition).getPath() + "/" + picTimeBean.getPaths().get(nowPosition).getName())).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(img_list.get(nowPosition));
    }
    
    public void getName()
    {
        String fatoryname = null;
        String subfatorynaem = null;
        String devicename = null;
        if (bean != null)
        {
            if (bean.getFactorys() != null && bean.getFactorys().size() != 0)
            {
                for (int i = 0; i < bean.getFactorys().size(); i++)
                {
                    if (bean.getFactorys().get(i).getFactoryid() == picTimeBean.getPaths().get(nowPosition).getFactoryid())
                    {
                        fatoryname = bean.getFactorys().get(i).getFactoryname();
                    }
                }
            }
            if (bean.getSubfactorys() != null && bean.getSubfactorys().size() != 0)
            {
                for (int i = 0; i < bean.getSubfactorys().size(); i++)
                {
                    if (bean.getSubfactorys().get(i).getSubfactoryid() == picTimeBean.getPaths().get(nowPosition).getSubfactoryid())
                    {
                        subfatorynaem = bean.getSubfactorys().get(i).getSubfactoryname();
                    }
                }
            }
            
            if (bean.getDevices() != null && bean.getDevices().size() != 0)
            {
                for (int i = 0; i < bean.getDevices().size(); i++)
                {
                    if (bean.getDevices().get(i).getDeviceid() == picTimeBean.getPaths().get(nowPosition).getDeviceid())
                    {
                        devicename = bean.getDevices().get(i).getDevicename();
                    }
                }
            }
        }
        String showname = "";
        if (fatoryname != null)
        {
            showname = showname + fatoryname + "/";
        }
        if (subfatorynaem != null)
        {
            showname = showname + subfatorynaem + "/";
        }
        if (devicename != null)
        {
            showname = showname + devicename;
        }
        picview_name.setText(showname);
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        GlideCacheUtil.getInstance().clearImageAllCache(getApplicationContext());
        GlideCacheUtil.getInstance().getCacheSize(getApplicationContext());
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        GlideCacheUtil.getInstance().clearImageAllCache(getApplicationContext());
        GlideCacheUtil.getInstance().getCacheSize(getApplicationContext());
    }
    
    private void initView()
    {
        share_pic = (RelativeLayout)findViewById(R.id.share_pic);
        picview_go_back = (ImageView)findViewById(R.id.picview_go_back);
        picview_top_name = (TextView)findViewById(R.id.picview_top_name);
        picview_name = (TextView)findViewById(R.id.picview_name);
        picview_img = (ViewPager)findViewById(R.id.picview_img);
        problem_send = (RelativeLayout)findViewById(R.id.problem_send);
    }
    
    private void initListener()
    {
        share_pic.setOnClickListener(this);
        picview_go_back.setOnClickListener(this);
        problem_send.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.share_pic:
                intent = new Intent(PicCenterActivity.this, SelectPicPopupWindow.class);
                intent.putExtra("path", picTimeBean.getPaths().get(nowPosition).getPath() + "/" + picTimeBean.getPaths().get(nowPosition).getName());
                startActivity(intent);
                break;
            case R.id.picview_go_back:
                finish();
                break;
            case R.id.problem_send:
                intent = new Intent(PicCenterActivity.this, CreateProblemActivity.class);
                intent.putExtra("picinfo", picTimeBean.getPaths().get(nowPosition));
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    
}
