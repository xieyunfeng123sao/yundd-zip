package com.vomont.yundudao.ui;

import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.bean.RegisterBean;
import com.vomont.yundudao.mvpview.register.IRegiseterView;
import com.vomont.yundudao.presenter.register.RegisterPresenter;
import com.vomont.yundudao.utils.CashActivty;
import com.vomont.yundudao.utils.MobileUtils;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.SearchWatherUtil;
import com.vomont.yundudao.view.BaseActivity;
import com.vomont.yundudao.view.checkswitch.CheckSwitchButton;

@SuppressLint("HandlerLeak")
public class RegisterActivity extends BaseActivity implements OnClickListener,
		IRegiseterView {
    
	private Context context;
	// 返回
	private ImageView go_back;
	// top
	private TextView top_name;
	// 手机号，验证码，密码，第二次密码
	private EditText register_num, register_code, register_password,
			register_again_password;
	// 获取验证码
	private TextView register_code_get;
	// 结束
	private Button register_finish;
	// 错误手机，错误验证码，错误密码
	private TextView register_error_num, register_error_code,
			register_error_againpsd;
	// 密码是否可见
	private CheckSwitchButton register_look_psd;

	private RegisterBean registerBean;

	private Handler handler;

	private Timer timer;

	// 读秒时间
	private int i = 60;

	private RegisterPresenter registerPresenter;

	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);
		context = this;
		if (!CashActivty.activityList.contains(RegisterActivity.this)) {
			CashActivty.addActivity(RegisterActivity.this);
		}
		Appcation.getInstance().addActivity(this);
		initView();
		initData();
		initListener();
		initInputListener();
		initHandler();
	}

	private void initView() {
		go_back = (ImageView) findViewById(R.id.go_back);
		top_name = (TextView) findViewById(R.id.top_name);
		register_num = (EditText) findViewById(R.id.register_num);
		register_code = (EditText) findViewById(R.id.register_code);
		register_password = (EditText) findViewById(R.id.register_password);
		register_again_password = (EditText) findViewById(R.id.register_again_password);
		register_code_get = (TextView) findViewById(R.id.register_code_get);
		register_finish = (Button) findViewById(R.id.register_finish);
		register_error_num = (TextView) findViewById(R.id.register_error_num);
		register_error_code = (TextView) findViewById(R.id.register_error_code);
		register_error_againpsd = (TextView) findViewById(R.id.register_error_againpsd);
		register_look_psd = (CheckSwitchButton) findViewById(R.id.register_look_psd);

		register_password.addTextChangedListener(new SearchWatherUtil(
				register_password));
		register_again_password.addTextChangedListener(new SearchWatherUtil(
				register_again_password));
	}

	/**
     * 
     */
	private void initInputListener() {
		register_num.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (start < 10) {
					register_error_num.setText(R.string.input_surenum);
				}
				if (start == 10) {
					register_error_num.setText("");
				}
				isSure();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		register_code.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (null != registerBean && registerBean.getResult() == 0) {
					if (!s.toString().equals(registerBean.getVerifCode())) {
						register_error_code.setText(R.string.error_code);
					} else {
						register_error_code.setText("");
					}
				} else {
					register_error_code.setText("");
				}
				isSure();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		register_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				isSure();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		register_again_password.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!s.toString()
						.equals(register_password.getText().toString())) {
					register_error_againpsd.setText(R.string.error_two_psd);
				} else {
					register_error_againpsd.setText("");
				}
				isSure();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		register_look_psd.setChecked(false);
		register_look_psd
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							register_password
									.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
							register_again_password
									.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
						} else {
							register_password
									.setInputType(InputType.TYPE_CLASS_TEXT
											| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							register_again_password
									.setInputType(InputType.TYPE_CLASS_TEXT
											| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						}
					}
				});
	}

	private void initListener() {
		register_code_get.setOnClickListener(this);
		register_finish.setOnClickListener(this);
		go_back.setOnClickListener(this);
	}

	private void initData() {
		top_name.setText(R.string.top_register);
		registerPresenter = new RegisterPresenter(this);
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go_back:
			finish();
			break;
		case R.id.register_code_get:
			if (!TextUtils.isEmpty(register_num.getText().toString())) {
				if (MobileUtils.isMobile(register_num.getText().toString())) {
					register_code_get
							.setBackgroundResource(R.drawable.text_register_pressed);
					timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}, 1000, 1000);
					register_code_get.setText("60s");
					register_code_get.setEnabled(false);
					registerPresenter.getVerCode(register_num.getText()
							.toString());
				} else {
					Toast.makeText(context, R.string.input_surenum,
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(context, R.string.num_empty, Toast.LENGTH_LONG)
						.show();
			}
			break;

		case R.id.register_finish:
			registerPresenter.register(register_num.getText().toString(),
					register_code.getText().toString(), register_password
							.getText().toString());

			break;
		default:
			break;
		}
	}

	private void initHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
				case 1:
					i--;
					register_code_get.setText(i + "s");
					if (i == 0) {
						register_code_get
								.setBackgroundResource(R.drawable.text_register);
						register_code_get.setText(R.string.get_msg);
						register_code_get.setEnabled(true);
						timer.cancel();
						timer.purge();
						timer = null;
						i = 10;
					}

					break;

				default:
					break;
				}
			}
		};
	}

	private void isSure() {
		if (null != registerBean) {
			if (register_num.getText().toString().length() == 11) {
				if (registerBean.getResult() == 0
						&& registerBean.getHasRegister() == 0
						&& registerBean.getVerifCode().equals(
								register_code.getText().toString())) {
					if (register_password.getText().toString().length() >= 6
							&& register_again_password
									.getText()
									.toString()
									.equals(register_password.getText()
											.toString())) {
						register_finish
								.setBackgroundResource(R.drawable.login_button);
						register_finish.setEnabled(true);
					} else {
						register_finish
								.setBackgroundResource(R.drawable.text_register_pressed);
						register_finish.setEnabled(false);
					}
				} else {
					register_finish
							.setBackgroundResource(R.drawable.text_register_pressed);
					register_finish.setEnabled(false);
				}
			} else {
				register_finish
						.setBackgroundResource(R.drawable.text_register_pressed);
				register_finish.setEnabled(false);
			}
		} else {
			register_finish
					.setBackgroundResource(R.drawable.text_register_pressed);
			register_finish.setEnabled(false);
		}
	}

	@Override
	public void showProgess() {
		dialog = ProgressDialog.createLoadingDialog(this, "加载中...");
		dialog.show();
	}

	@Override
	public void hideProgress() {
		dialog.dismiss();
	}

	@Override
	public void getVerCodeSucess(RegisterBean registerBean) {
		this.registerBean = registerBean;
		if (registerBean.getResult() != 0) {
			Toast.makeText(context, R.string.erroe_code_get, Toast.LENGTH_LONG)
					.show();
		}

		if (registerBean.getHasRegister() == 1) {
			register_error_num.setText(R.string.num_hasregister);
		}
	}

	@Override
	public void getVerCodeFail() {
		Toast.makeText(context, R.string.erroe_code_get, Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public void reigsterSucess(RegisterBean registerBean) {
		if (registerBean.getResult() == 0) {
			Toast.makeText(context, "注册成功", Toast.LENGTH_LONG).show();
			finish();
		} else {
			Toast.makeText(context, R.string.error_register, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void registerFail() {

	}

}
