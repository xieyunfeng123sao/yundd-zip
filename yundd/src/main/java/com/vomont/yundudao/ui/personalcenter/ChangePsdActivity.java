package com.vomont.yundudao.ui.personalcenter;

import org.apache.http.Header;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.bean.RegisterBean;
import com.vomont.yundudao.utils.HttpUtil;
import com.vomont.yundudao.utils.MD5Util;
import com.vomont.yundudao.utils.SearchWatherUtil;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.BaseActivity;
import com.vomont.yundudao.view.checkswitch.CheckSwitchButton;

public class ChangePsdActivity extends BaseActivity implements OnClickListener
{
    
    private ImageView go_back;
    
    private TextView top_name, change_psd_error_againpsd;
    
    private EditText old_password, change_new_password, change_again_password;
    
    private CheckSwitchButton change_psd_look;
    
    private Button change_psd_finish;
    
    private Context context;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepsd);
        Appcation.getInstance().addActivity(this);
        initView();
        initData();
        initListener();
    }
    
    private void initView()
    {
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        old_password = (EditText)findViewById(R.id.old_password);
        change_new_password = (EditText)findViewById(R.id.change_new_password);
        change_again_password = (EditText)findViewById(R.id.change_again_password);
        change_psd_error_againpsd = (TextView)findViewById(R.id.change_psd_error_againpsd);
        change_psd_look = (CheckSwitchButton)findViewById(R.id.change_psd_look);
        change_psd_finish = (Button)findViewById(R.id.change_psd_finish);
        change_new_password.addTextChangedListener(new SearchWatherUtil(change_new_password));
        change_again_password.addTextChangedListener(new SearchWatherUtil(change_again_password));
        old_password.addTextChangedListener(new SearchWatherUtil(old_password));
    }
    
    private void initData()
    {
        context = this;
        top_name.setText(R.string.change_psd_top);
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
        change_psd_finish.setOnClickListener(this);
        
        change_new_password.addTextChangedListener(new TextWatcher()
        {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!change_again_password.getText().toString().equals(""))
                {
                    isSure();
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
        change_again_password.addTextChangedListener(new TextWatcher()
        {
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                isSure();
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
        change_psd_look.setChecked(false);
        change_psd_look.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    change_new_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    change_again_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    old_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else
                {
                    change_new_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    change_again_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    old_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    
                }
            }
        });
    }
    
    private boolean isSure()
    {
        if (!change_new_password.getText().toString().equals(change_again_password.getText().toString()))
        {
            change_psd_error_againpsd.setText(R.string.error_two_psd);
            return false;
        }
        else
        {
            change_psd_error_againpsd.setText("");
            return true;
        }
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.go_back:
                finish();
                break;
            case R.id.change_psd_finish:
                if (!TextUtils.isEmpty(old_password.getText().toString()))
                {
                    if (isSure())
                    {
                        if (change_new_password.getText().toString().length() >= 6)
                        {
                            RequestParams reParams = new RequestParams();
                            reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type7);
                            ShareUtil shareUtil = new ShareUtil(context);
                            reParams.add(HTTP_PAMRS.userid, shareUtil.getShare().getUser_id() + "");
                            reParams.add(HTTP_PAMRS.oldpswd, MD5Util.getMd5(old_password.getText().toString()));
                            reParams.add(HTTP_PAMRS.newpswd, MD5Util.getMd5(change_new_password.getText().toString()));
                            HttpUtil.post(reParams, new TextHttpResponseHandler()
                            {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseBody)
                                {
                                    super.onSuccess(statusCode, headers, responseBody);
                                    Gson gson = new Gson();
                                    RegisterBean registerBean = gson.fromJson(responseBody, RegisterBean.class);
                                    Log.d("insert", responseBody);
                                    if (registerBean.getResult() == 0)
                                    {
                                        finish();
                                        Toast.makeText(context, R.string.change_sucess, Toast.LENGTH_LONG).show();
                                    }
                                    else if (registerBean.getResult() == 11)
                                    {
                                        
                                        Toast.makeText(context, R.string.old_psd_fail, Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(context, R.string.change_fail, Toast.LENGTH_LONG).show();
                                    }
                                }
                                
                                @Override
                                public void onFailure(String responseBody, Throwable error)
                                {
                                    super.onFailure(responseBody, error);
                                    Toast.makeText(context, R.string.change_fail, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(context, R.string.password_type_error, Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(context, R.string.two_password_error, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(context, R.string.error_empty, Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
