package com.vomont.yundudao.ui.verification;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.bean.RegisterBean;
import com.vomont.yundudao.mvpview.register.IRegiseterView;
import com.vomont.yundudao.presenter.register.RegisterPresenter;
import com.vomont.yundudao.utils.CashActivty;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.view.BaseActivity;

public class VerificationNumActivity extends BaseActivity implements OnClickListener,IRegiseterView
{
    private ImageView go_back;
    
    private Button num_next;
    
    private EditText login_code_num;
    
    @SuppressWarnings("unused")
    private Context context;
    
    private TextView top_name;
    
    private RegisterPresenter registerPresenter;
    
    private Dialog dialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registernum);
        
        if (!CashActivty.activityList.contains(VerificationNumActivity.this))
        {
            CashActivty.addActivity(VerificationNumActivity.this);
        }
        
        go_back = (ImageView)findViewById(R.id.go_back);
        num_next = (Button)findViewById(R.id.num_next);
        top_name = (TextView)findViewById(R.id.top_name);
        login_code_num = (EditText)findViewById(R.id.login_code_num);
        registerPresenter=new RegisterPresenter(this);
        context = this;
        Appcation.getInstance().addActivity(this);
        initListener();
        top_name.setText("验证码登录");
        login_code_num.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.length() < 11)
                {
                    num_next.setEnabled(false);
                    num_next.setBackgroundResource(R.drawable.text_register_pressed);
                }
                if (s.length() == 11)
                {
                    num_next.setEnabled(true);
                    num_next.setBackgroundResource(R.drawable.login_button);
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
        
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
        num_next.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.go_back:
                finish();
                break;
            case R.id.num_next:
            	registerPresenter.getVerCode(login_code_num.getText().toString());
//                RequestParams reParams = new RequestParams();
//                reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type1);
//                reParams.add(HTTP_PAMRS.tel, login_code_num.getText().toString());
//                HttpUtil.post(reParams, new TextHttpResponseHandler()
//                {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, String responseBody)
//                    {
//                        super.onSuccess(statusCode, headers, responseBody);
//                        
//                        Gson gson = new Gson();
//                        registerBean = gson.fromJson(responseBody, RegisterBean.class);
//                        Intent intent = new Intent();
//                        intent.putExtra("num", login_code_num.getText().toString());
//                        
//                        if (null != registerBean && registerBean.getResult() == 0)
//                        {
//                            intent.putExtra("code", registerBean.getVerifCode());
//                            intent.putExtra("hasRegister", registerBean.getHasRegister());
//                        }
//                        intent.setAction(VERIFICATION_ACTION);
//                        startActivity(intent);
//                        
//                    }
//                    
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error)
//                    {
//                        super.onFailure(statusCode, headers, responseBody, error);
//                        Intent intent = new Intent();
//                        intent.putExtra("num", login_code_num.getText().toString());
//                        
//                        if (null != registerBean && registerBean.getResult() == 0)
//                        {
//                            intent.putExtra("code", registerBean.getVerifCode());
//                            intent.putExtra("hasRegister", registerBean.getHasRegister());
//                        }
//                        intent.setAction(VERIFICATION_ACTION);
//                        startActivity(intent);
//                    }
//                });
                break;
            default:
                break;
        }
    }

	@Override
	public void showProgess() {
		dialog=ProgressDialog.createLoadingDialog(this, "加载中...");
		dialog.show();
	}

	@Override
	public void hideProgress() {
		dialog.dismiss();
	}

	@Override
	public void getVerCodeSucess(RegisterBean registerBean) {
		
      Intent intent = new Intent();
      intent.putExtra("num", login_code_num.getText().toString());
      
      if (null != registerBean && registerBean.getResult() == 0)
      {
          intent.putExtra("code", registerBean.getVerifCode());
          intent.putExtra("hasRegister", registerBean.getHasRegister());
      }
      intent.setAction(VERIFICATION_ACTION);
      startActivity(intent);
	}

	@Override
	public void getVerCodeFail() {
      Intent intent = new Intent();
      intent.putExtra("num", login_code_num.getText().toString());
      intent.setAction(VERIFICATION_ACTION);
      startActivity(intent);
		
	}

	@Override
	public void reigsterSucess(RegisterBean registerBean) {
		
	}

	@Override
	public void registerFail() {
		
	}
}
