package com.vomont.yundudao.ui.verification;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.RegisterBean;
import com.vomont.yundudao.mvpview.fatory.IFactoryView;
import com.vomont.yundudao.presenter.factory.FactoryPresenter;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.CashActivty;
import com.vomont.yundudao.utils.HttpUtil;
import com.vomont.yundudao.utils.ImageUtils;
import com.vomont.yundudao.utils.MD5Util;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.SearchWatherUtil;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.BaseActivity;

public class NewPsdActivity extends BaseActivity implements OnClickListener,IFactoryView
{
    private ImageView go_back;
    
    private TextView jump, top_name, new_psd_num, new_psd_error_againpsd;
    
    private EditText new_psd_password, new_psd_again_password;
    
    private ToggleButton new_psd_look_psd;
    
    private Button new_psd_finish;
    
    private String num;
    
    private int userid;
    
    private Context context;
    
    private ShareUtil shareUtil;
    
    private Dialog dialog;
    
    private FactoryPresenter factoryPresenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verif_psd);
        context = this;
        Appcation.getInstance().addActivity(this);
        
        if (!CashActivty.activityList.contains(NewPsdActivity.this))
        {
            CashActivty.addActivity(NewPsdActivity.this);
        }
        factoryPresenter=new FactoryPresenter(this);
        initView();
        initData();
        initListener();
        showView();
    }
    
    private void initView()
    {
        go_back = (ImageView)findViewById(R.id.go_back);
        jump = (TextView)findViewById(R.id.jump);
        top_name = (TextView)findViewById(R.id.top_name);
        new_psd_num = (TextView)findViewById(R.id.new_psd_num);
        new_psd_error_againpsd = (TextView)findViewById(R.id.new_psd_error_againpsd);
        new_psd_password = (EditText)findViewById(R.id.new_psd_password);
        new_psd_again_password = (EditText)findViewById(R.id.new_psd_again_password);
        new_psd_look_psd = (ToggleButton)findViewById(R.id.new_psd_look_psd);
        new_psd_finish = (Button)findViewById(R.id.new_psd_finish);
        
        new_psd_password.addTextChangedListener(new SearchWatherUtil(new_psd_password));
        new_psd_again_password.addTextChangedListener(new SearchWatherUtil(new_psd_again_password));
    }
    
    private void initData()
    {
        num = getIntent().getStringExtra("num");
        userid = getIntent().getIntExtra("userid", -1);
        top_name.setText(R.string.top_new_psd);
        jump.setVisibility(View.VISIBLE);
        new_psd_num.setText(num);
        shareUtil = new ShareUtil(context);
    }
    
    private void showView()
    {
        new_psd_password.addTextChangedListener(new TextWatcher()
        {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                sureNext();
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                
            }
            
            @Override
            public void afterTextChanged(Editable s)
            {
                
            }
        });
        
        new_psd_again_password.addTextChangedListener(new TextWatcher()
        {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                sureNext();
                if (!new_psd_password.getText().toString().equals(new_psd_again_password.getText().toString()))
                {
                    new_psd_error_againpsd.setText(R.string.two_password_error);
                }
                else
                {
                    new_psd_error_againpsd.setText("");
                }
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                
            }
            
            @Override
            public void afterTextChanged(Editable s)
            {
                
            }
        });
        new_psd_look_psd.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    new_psd_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    new_psd_again_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    new_psd_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    new_psd_again_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        
    }
    
    private void sureNext()
    {
        if (new_psd_password.length() >= 6 && new_psd_password.getText().toString().equals(new_psd_again_password.getText().toString()))
        {
            new_psd_finish.setEnabled(true);
            new_psd_finish.setBackgroundResource(R.drawable.login_button);
        }
        else
        {
            new_psd_finish.setEnabled(false);
            new_psd_finish.setBackgroundResource(R.drawable.text_register_pressed);
        }
        
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
        jump.setOnClickListener(this);
        new_psd_finish.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.go_back:
                finish();
                break;
            case R.id.jump:
                shareUtil.setShare(num, null, userid, null, null,shareUtil.getShare().getAccountid(),null,0,null,0,0);
                factoryPresenter.getFactory(shareUtil.getShare().getUser_id() + "");
                dialog = ProgressDialog.createLoadingDialog(this, "登录中...");
                dialog.show();
//                Intent intent = new Intent();
//                intent.setAction(HOME_ACTION);
//                startActivity(intent);
                break;
            case R.id.new_psd_finish:
                RequestParams reParams = new RequestParams();
                reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type5);
                reParams.add(HTTP_PAMRS.userid, userid + "");
                reParams.add(HTTP_PAMRS.newpswd, MD5Util.getMd5(new_psd_password.getText().toString()));
                HttpUtil.post(reParams, new TextHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseBody)
                    {
                        super.onSuccess(statusCode, headers, responseBody);
                        Gson gson = new Gson();
                        RegisterBean registerBean = gson.fromJson(responseBody, RegisterBean.class);
                        if (registerBean.getResult() == 0)
                        {
                            shareUtil.setShare(num, new_psd_password.getText().toString(), userid, null, null,shareUtil.getShare().getAccountid(),null,0,null,0,0);
                            
//                            Intent intent = new Intent();
//                            intent.setAction(HOME_ACTION);
//                            startActivity(intent);
                            factoryPresenter.getFactory(shareUtil.getShare().getUser_id() + "");
                            dialog = ProgressDialog.createLoadingDialog(NewPsdActivity.this, "登录中...");
                            dialog.show();
                            
                        }
                        if (registerBean.getResult() == 1)
                        {
                            Toast.makeText(context, R.string.new_psd_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                    
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error)
                    {
                        super.onFailure(statusCode, headers, responseBody, error);
                        Toast.makeText(context, R.string.new_psd_fail, Toast.LENGTH_SHORT).show();
                    }
                });
                
                break;
            default:
                break;
        }
    }

	@Override
	public void getFactory(FactoryBean factoryBean) {
		  ACache aCache = ACache.get(NewPsdActivity.this);
	        aCache.put("factoryBean", factoryBean);
	        // 获取头像
	        RequestParams reParams = new RequestParams();
	        reParams.add(HTTP_PAMRS.msgid, "263");
	        reParams.add(HTTP_PAMRS.userid, shareUtil.getShare().getUser_id() + "");
	        HttpUtil.post(reParams, new TextHttpResponseHandler()
	        {
	            @Override
	            public void onSuccess(int statusCode, Header[] headers, String responseBody)
	            {
	                super.onSuccess(statusCode, headers, responseBody);
	                
	                try
	                {
	                    JSONObject object = new JSONObject(responseBody);
	                    
	                    if (object.getInt("result") == 0)
	                    {
	                        byte[] top = Base64.decode(URLDecoder.decode(object.getString("icondata"), "utf-8"), Base64.DEFAULT);
	                        Bitmap bitmap = ImageUtils.bytes2Bitmap(top);
	                        BitmapUtil.savePhotoByte(bitmap, VideoManager.toppath, "topimg" + shareUtil.getShare().getAccountid() + ".jpg");
	                    }
	                    Intent intent = new Intent();
	                    intent.setAction(HOME_ACTION);
	                    startActivity(intent);
	                    finish();
	                    dialog.dismiss();
	                }
	                catch (JSONException e)
	                {
	                    e.printStackTrace();
	                    dialog.dismiss();
	                }
	                catch (UnsupportedEncodingException e)
	                {
	                    dialog.dismiss();
	                    e.printStackTrace();
	                }
	                
	            }
	            
	            @Override
	            public void onFailure(String responseBody, Throwable error)
	            {
	                super.onFailure(responseBody, error);
	                dialog.dismiss();
	            }
	        });
		
	}

	@Override
	public void onFail() {
		 // 登录失败
        Toast.makeText(context, R.string.error_num, Toast.LENGTH_LONG).show();
        dialog.dismiss();
		
	}
}
