package com.vomont.yundudao.view;

import com.vomont.yundudao.common.Con_Action;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity implements Con_Action
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        // {
        // setTranslucentStatus(true);
        // SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // tintManager.setStatusBarTintEnabled(true);
        // tintManager.setStatusBarTintResource(oncreateColor());// ֪ͨ��������ɫ
        // }
        // setContentView(R.layout.activity_main);
    }
    
    @Override
    public void startActivity(Intent intent)
    {
        super.startActivity(intent);
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    
    @SuppressLint("NewApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options)
    {
        super.startActivityForResult(intent, requestCode, options);
//        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
    // public int oncreateColor()
    // {
    // return R.color.main_color;
    // }
    //
    // @TargetApi(19)
    // private void setTranslucentStatus(boolean on)
    // {
    // Window win = getWindow();
    // WindowManager.LayoutParams winParams = win.getAttributes();
    // final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    // if (on)
    // {
    // winParams.flags |= bits;
    // }
    // else
    // {
    // winParams.flags &= ~bits;
    // }
    // win.setAttributes(winParams);
    // }
    
}
