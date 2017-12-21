package com.vomont.yundudao.ui.verification;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.GridPasswordView.OnPasswordChangedListener;
import com.jungly.gridpasswordview.PasswordType;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.bean.RegisterBean;
import com.vomont.yundudao.utils.CashActivty;
import com.vomont.yundudao.utils.HttpUtil;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.BaseActivity;

public class VerificationActivity extends BaseActivity implements OnClickListener
{
    
    private Context context;
    
    private String code, num;
    
    private int hasRegister;
    
    private TextView verif_login_num, top_name, again_get_code;
    
    private Button verif_login_next;
    
    private ImageView go_back;
    
    private RegisterBean registerBean;
    
    private GridPasswordView ve_code;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Appcation.getInstance().addActivity(this);
        if (!CashActivty.activityList.contains(VerificationActivity.this))
        {
            CashActivty.addActivity(VerificationActivity.this);
        }
        initView();
        initData();
        initListener();
        codeStyle();
    }
    
    private void initView()
    {
        verif_login_num = (TextView)findViewById(R.id.verif_login_num);
        ve_code = (GridPasswordView)findViewById(R.id.ve_code);
        ve_code.setPasswordVisibility(true);
        ve_code.setPasswordType(PasswordType.NUMBER);
        verif_login_next = (Button)findViewById(R.id.verif_login_next);
        top_name = (TextView)findViewById(R.id.top_name);
        go_back = (ImageView)findViewById(R.id.go_back);
        again_get_code = (TextView)findViewById(R.id.again_get_code);
    }
    
    private void initData()
    {
        top_name.setText(R.string.top_verification);
        code = getIntent().getStringExtra("code");
        hasRegister = getIntent().getIntExtra("hasRegister", -1);
        num = getIntent().getStringExtra("num");
        verif_login_num.setText(num);
        context = this;
    }
    
    private void codeStyle()
    {
        ve_code.setOnPasswordChangedListener(new OnPasswordChangedListener()
        {
            @Override
            public void onMaxLength(String psw)
            {
                
            }
            
            @Override
            public void onChanged(String psw)
            {
                isSureNext();
            }
        });
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
        verif_login_next.setOnClickListener(this);
        again_get_code.setOnClickListener(this);
    }
    
    private void isSureNext()
    {
        if (ve_code.getPassWord().length() == 4)
        {
            verif_login_next.setBackgroundResource(R.drawable.login_button);
            verif_login_next.setEnabled(true);
        }
        else
        {
            verif_login_next.setBackgroundResource(R.drawable.text_register_pressed);
            verif_login_next.setEnabled(false);
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
            case R.id.verif_login_next:
                if (null != code)
                {
                    String intput_code = ve_code.getPassWord();
                    if (code.equals(intput_code))
                    {
                        RequestParams reParams = new RequestParams();
                        reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type4);
                        reParams.add(HTTP_PAMRS.tel, num);
                        reParams.add(HTTP_PAMRS.verifCode, intput_code);
                        HttpUtil.post(reParams, new TextHttpResponseHandler()
                        {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseBody)
                            {
                                super.onSuccess(statusCode, headers, responseBody);
                                ShareUtil shareUtil = new ShareUtil(context);
                                Gson gson = new Gson();
                                registerBean = gson.fromJson(responseBody, RegisterBean.class);
                                if (registerBean.getResult() == 0)
                                {
                                    shareUtil.setShare(num,
                                        "",
                                        registerBean.getUserid(),
                                        registerBean.getVeyeuserid(),
                                        registerBean.getVeyekey(),
                                        registerBean.getAccountid(),
                                        registerBean.getVfilesvr().getVfilesvrip(),
                                        registerBean.getVfilesvr().getVfilesvrport(),
                                        registerBean.getVeyesvrip(),
                                        registerBean.getVeyesvrport(),
                                        registerBean.getVfilesvr().getVfilesvrid());
                                    if (hasRegister == 0)
                                    {
                                        Intent intent = new Intent();
                                        intent.setAction(HOME_ACTION);
                                        startActivity(intent);
                                    }
                                    if (hasRegister == 1)
                                    {
                                        Intent intent = new Intent();
                                        intent.putExtra("num", num);
                                        intent.putExtra("code", code);
                                        intent.putExtra("userid", registerBean.getUserid());
                                        intent.setAction(NEWPSD_ACTION);
                                        startActivity(intent);
                                    }
                                }
                                else
                                {
                                    Toast.makeText(context, R.string.input_nice_code, Toast.LENGTH_LONG).show();
                                }
                            }
                            
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error)
                            {
                                super.onFailure(statusCode, headers, responseBody, error);
                                Toast.makeText(context, R.string.input_nice_code, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(context, R.string.input_nice_code, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(context, R.string.input_nice_code, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.again_get_code:
                RequestParams reParams = new RequestParams();
                reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type1);
                reParams.add(HTTP_PAMRS.tel, num);
                HttpUtil.post(reParams, new TextHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseBody)
                    {
                        super.onSuccess(statusCode, headers, responseBody);
                        Gson gson = new Gson();
                        registerBean = gson.fromJson(responseBody, RegisterBean.class);
                        if (registerBean.getResult() == 0)
                        {
                            ShareUtil shareUtil = new ShareUtil(context);
                            shareUtil.setShare(num, "", registerBean.getUserid(), registerBean.getVeyeuserid(), registerBean.getVeyekey(), registerBean.getAccountid(), registerBean.getVfilesvr()
                                .getVfilesvrip(), registerBean.getVfilesvr().getVfilesvrport(), registerBean.getVeyesvrip(), registerBean.getVeyesvrport(),registerBean.getVfilesvr().getVfilesvrid());
                            code = registerBean.getVerifCode();
                            hasRegister = registerBean.getHasRegister();
                        }
                        else if (registerBean.getResult() == 3)
                        {
                            Toast.makeText(context, "60秒内只能获取一次验证码！", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(context, R.string.erroe_code_get, Toast.LENGTH_LONG).show();
                        }
                    }
                    
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error)
                    {
                        super.onFailure(statusCode, headers, responseBody, error);
                        Toast.makeText(context, R.string.erroe_code_get, Toast.LENGTH_LONG).show();
                    }
                });
                break;
            default:
                break;
        }
    }
}
