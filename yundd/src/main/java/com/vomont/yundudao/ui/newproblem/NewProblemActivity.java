package com.vomont.yundudao.ui.newproblem;

import java.util.ArrayList;

import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.createproblem.CreateProblemActivity;
import com.vomont.yundudao.ui.factory.adapter.MyFragmentPagerAdapter;
import com.vomont.yundudao.ui.newproblem.fragment.MeLoadFragment;
import com.vomont.yundudao.ui.newproblem.fragment.SelectProblemFragment;
import com.vomont.yundudao.ui.newproblem.fragment.SendToMeFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class NewProblemActivity extends FragmentActivity implements OnClickListener
{
    
    private int currIndex;// 当前页卡编号
    
    private ArrayList<Fragment> fragmentList;
    
    private ViewPager problem_viewpager;
    
    private TextView problem_cursor;
    
    private TextView problem_send_me;
    
    private TextView problem_me_create;
    
    private TextView problem_select;
    
    private ImageView go_back;
    
    private TextView top_name;
    
    private SendToMeFragment sendToMeFragment;
    
    private MeLoadFragment meLoadFragment;
    
    private SelectProblemFragment selectProblemFragment;
    
    private ImageView problem_list_add;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_problem);
        initView();
        initData();
        InitViewPager();
        InitTextBar();
        initListener();
    }
    
    private void initData()
    {
        top_name.setText("问题");
    }
    
    private void initListener()
    {
        problem_send_me.setOnClickListener(this);
        problem_me_create.setOnClickListener(this);
        problem_select.setOnClickListener(this);
        go_back.setOnClickListener(this);
        problem_list_add.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.problem_send_me:
                problem_viewpager.setCurrentItem(0);
                break;
            case R.id.problem_me_create:
                problem_viewpager.setCurrentItem(1);
                break;
            case R.id.problem_select:
                problem_viewpager.setCurrentItem(2);
                break;
            case R.id.go_back:
                finish();
                break;
            case R.id.problem_list_add:
                Intent intent = new Intent(NewProblemActivity.this, CreateProblemActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    
    private void initView()
    {
        go_back = (ImageView)findViewById(R.id.go_back);
        problem_viewpager = (ViewPager)findViewById(R.id.problem_viewpager);
        problem_cursor = (TextView)findViewById(R.id.problem_cursor);
        problem_send_me = (TextView)findViewById(R.id.problem_send_me);
        problem_me_create = (TextView)findViewById(R.id.problem_me_create);
        problem_select = (TextView)findViewById(R.id.problem_select);
        top_name = (TextView)findViewById(R.id.top_name);
        problem_list_add = (ImageView)findViewById(R.id.problem_list_add);
    }
    
    /*
     * 初始化ViewPager
     */
    @SuppressWarnings("deprecation")
    public void InitViewPager()
    {
        fragmentList = new ArrayList<Fragment>();
        sendToMeFragment = new SendToMeFragment();
        meLoadFragment = new MeLoadFragment();
        selectProblemFragment = new SelectProblemFragment();
        fragmentList.add(sendToMeFragment);
        fragmentList.add(meLoadFragment);
        fragmentList.add(selectProblemFragment);
        // 给ViewPager设置适配器
        problem_viewpager.setOffscreenPageLimit(2);
        problem_viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        problem_viewpager.setCurrentItem(2);// 设置当前显示标签页为第一页
        problem_send_me.setTextColor(getResources().getColor(R.color.text_color));
        problem_me_create.setTextColor(getResources().getColor(R.color.text_color));
        problem_select.setTextColor(getResources().getColor(R.color.main_color));
        problem_viewpager.setOnPageChangeListener(new LoadOnPageChangeListener());// 页面变化时的监听器
    }
    
    /*
     * 初始化图片的位移像素
     */
    public void InitTextBar()
    {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        // 得到显示屏宽度
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        // 1/2屏幕宽度
        int tabLineLength = metrics.widthPixels / 3;
        LayoutParams lp = (LayoutParams)problem_cursor.getLayoutParams();
        lp.width = tabLineLength;
        lp.leftMargin = (int)(2 * tabLineLength);
        problem_cursor.setLayoutParams(lp);
    }
    
    public class LoadOnPageChangeListener implements OnPageChangeListener
    {
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
            // 取得该控件的实例
            LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams)problem_cursor.getLayoutParams();
            if (currIndex == arg0)
            {
                ll.leftMargin = (int)(currIndex * problem_cursor.getWidth() + arg1 * problem_cursor.getWidth());
            }
            else if (currIndex > arg0)
            {
                ll.leftMargin = (int)(currIndex * problem_cursor.getWidth() - (1 - arg1) * problem_cursor.getWidth());
            }
            problem_cursor.setLayoutParams(ll);
        }
        
        @Override
        public void onPageScrollStateChanged(int arg0)
        {
            
        }
        
        @Override
        public void onPageSelected(int arg0)
        {
            currIndex = arg0;
            if (currIndex == 0)
            {
                problem_send_me.setTextColor(getResources().getColor(R.color.main_color));
                problem_me_create.setTextColor(getResources().getColor(R.color.text_color));
                problem_select.setTextColor(getResources().getColor(R.color.text_color));
            }
            else if (currIndex == 1)
            {
                problem_send_me.setTextColor(getResources().getColor(R.color.text_color));
                problem_me_create.setTextColor(getResources().getColor(R.color.main_color));
                problem_select.setTextColor(getResources().getColor(R.color.text_color));
            }
            else
            {
                problem_send_me.setTextColor(getResources().getColor(R.color.text_color));
                problem_me_create.setTextColor(getResources().getColor(R.color.text_color));
                problem_select.setTextColor(getResources().getColor(R.color.main_color));
            }
        }
    }
}
