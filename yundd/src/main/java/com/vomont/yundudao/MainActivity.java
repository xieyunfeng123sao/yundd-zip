package com.vomont.yundudao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mabeijianxi.smallvideorecord2.LocalMediaCompress;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.BaseMediaBitrateConfig;
import com.mabeijianxi.smallvideorecord2.model.LocalMediaConfig;
import com.mabeijianxi.smallvideorecord2.model.OnlyCompressOverBean;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.UserInfo;
import com.vomont.yundudao.mvpview.fatory.IFactoryView;
import com.vomont.yundudao.mvpview.login.ILoginView;
import com.vomont.yundudao.presenter.factory.FactoryPresenter;
import com.vomont.yundudao.presenter.login.LoginPresenter;
import com.vomont.yundudao.ui.ip.SetIpActivity;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.CashActivty;
import com.vomont.yundudao.utils.HttpUtil;
import com.vomont.yundudao.utils.ImageUtils;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.SearchWatherUtil;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.BaseActivity;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StrictMode.VmPolicy;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends BaseActivity implements OnClickListener, ILoginView, IFactoryView
{
    // 注册 验证码登录
    private TextView newuser_register, register_login;
    
    private Intent intent;
    
    private Context context;
    
    // 输入用户名 密码
    private EditText login_num, login_psd;
    
    // 登录
    private Button login_app;
    
    // 查看密码
    private ImageView login_psd_look, user_image;
    
    // 判断密码是否可见
    private boolean isLook = false;
    
    private ImageView ipsetting;
    
    private ShareUtil shareUtil;
    
    private LoginPresenter loginPresenter;
    
    private Dialog dialog;
    
    private FactoryPresenter factoryPresenter;
    
    private ACache aCache;
    
    private List<String> ips;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!CashActivty.activityList.contains(MainActivity.this))
        {
            CashActivty.addActivity(MainActivity.this);
        }
        aCache = ACache.get(this);
        initView();
        Appcation.getInstance().addActivity(this);
        initData();
        initListener();
    }
    private void initView()
    {
        newuser_register = (TextView)findViewById(R.id.newuser_register);
        register_login = (TextView)findViewById(R.id.register_login);
        login_num = (EditText)findViewById(R.id.login_num);
        login_psd = (EditText)findViewById(R.id.login_psd);
        login_app = (Button)findViewById(R.id.login_app);
        login_psd_look = (ImageView)findViewById(R.id.login_psd_look);
        user_image = (ImageView)findViewById(R.id.user_image);
        ipsetting = (ImageView)findViewById(R.id.ipsetting);
        context = this;
        shareUtil = new ShareUtil(context);
        loginPresenter = new LoginPresenter(this);
        factoryPresenter = new FactoryPresenter(this);
    }
    
    private void initListener()
    {
        newuser_register.setOnClickListener(this);
        register_login.setOnClickListener(this);
        login_app.setOnClickListener(this);
        login_psd_look.setOnClickListener(this);
        ipsetting.setOnClickListener(this);
    }
    
    private void initData()
    {
        // 判断是否自动登录
        if (!TextUtils.isEmpty(shareUtil.getShare().getNum()))
        {
            login_num.setText(shareUtil.getShare().getNum());
            if (!TextUtils.isEmpty(shareUtil.getShare().getPassword()))
            {
                login_psd.setText(shareUtil.getShare().getPassword());
                login();
            }
        }
        login_psd.addTextChangedListener(new SearchWatherUtil(login_psd));
        File file = new File(VideoManager.toppath + "/" + "topimg" + shareUtil.getShare().getAccountid() + ".jpg");
        if (file.exists())
        {
            Glide.with(this).load(new File(VideoManager.toppath + "/" + "topimg" + shareUtil.getShare().getAccountid() + ".jpg")).into(user_image);
        }
        
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.newuser_register:
                // 新用户注册
                intent = new Intent();
                intent.setAction(REGISTER_ACTION);
                startActivity(intent);
                break;
            case R.id.register_login:
                // 验证码登录
                intent = new Intent();
                intent.setAction(REGISTERNUM_ACTION);
                startActivity(intent);
                break;
            case R.id.login_psd_look:
                // 是否可以查看密码
                if (isLook)
                {
                    login_psd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    login_psd_look.setImageResource(R.drawable.eye_password_select);
                    isLook = false;
                }
                else
                {
                    login_psd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    login_psd_look.setImageResource(R.drawable.eye_password);
                    isLook = true;
                }
                break;
            case R.id.login_app:
                login();
                break;
            case R.id.ipsetting:
                intent = new Intent(MainActivity.this, SetIpActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    
    /**
     * 登录
     */
    private void login()
    {
        // 密码为空
        if (TextUtils.isEmpty(login_num.getText().toString()) || TextUtils.isEmpty(login_psd.getText().toString()))
        {
            Toast.makeText(context, R.string.error_num_psd, Toast.LENGTH_LONG).show();
        }
        else
        {
            loginPresenter.login(login_num.getText().toString(), login_psd.getText().toString());
        }
    }
    
    @Override
    public void hideProgress()
    {
        // dialog.dismiss();
    }
    
    @Override
    public void showProgress()
    {
        dialog = ProgressDialog.createLoadingDialog(this, "登录中...");
        dialog.show();
    }
    
    @Override
    public void loginSucess(UserInfo userInfo)
    {
        Message message = new Message();
        message.what = 1;
        message.obj = userInfo;
        handler.sendMessage(message);
    }
    
    @Override
    public void loginFail()
    {
        handler.sendEmptyMessage(0);
    }
    
    private Handler handler = new Handler()
    {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    Toast.makeText(context, R.string.error_login, Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    break;
                case 1:
                    UserInfo userInfo = (UserInfo)msg.obj;
                    if (userInfo != null && userInfo.getResult() == 0)
                    {
                        // 登录成功
                        shareUtil.setShare(login_num.getText().toString(),
                            login_psd.getText().toString(),
                            userInfo.getUser_id(),
                            userInfo.getVeyeuserid(),
                            userInfo.getVeyekey(),
                            userInfo.getAccountid(),
                            userInfo.getVfilesvr() != null ? userInfo.getVfilesvr().getVfilesvrip() : "",
                            userInfo.getVfilesvr() != null ? userInfo.getVfilesvr().getVfilesvrport() : 0,
                            userInfo.getVeyesvrip(),
                            userInfo.getVeyesvrport(),
                            userInfo.getVfilesvr() != null ? userInfo.getVfilesvr().getVfilesvrid() : 0);
                        factoryPresenter.getFactory(shareUtil.getShare().getUser_id() + "");
                        // 登录成功 查看本地是否缓存了历史的ip地址
                        if (aCache.getAsObject("ips") != null)
                        {
                            // 有历史的ip地址
                            ips = (List<String>)aCache.getAsObject("ips");
                            boolean hasIp = false;
                            for (String allip : ips)
                            {
                                // 
                                if (allip.equals(Appcation.BASE_URL.replace("http://", "")))
                                {
                                    ips.remove(allip);
                                    break;
                                }
                            }
                            ips.add(0,Appcation.BASE_URL.replace("http://", ""));
                            aCache.put("ips", (Serializable)ips);
                        }
                        else
                        {
                            // 没有历史ip地址列表 就创建一个
                            ips = new ArrayList<String>();
                            ips.add(Appcation.BASE_URL.replace("http://", ""));
                            aCache.put("ips", (Serializable)ips);
                        }
                        // 将登录成功的ip地址保存本地 下次启动后作为默认的ip地址
                        aCache.put("ip", Appcation.BASE_URL.replace("http://", ""));
                    }
                    else
                    {
                        // 登录失败
                        Toast.makeText(context, R.string.error_num, Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
            
        };
    };
    
    @Override
    public void getFactory(final FactoryBean factoryBean)
    {
        ACache aCache = ACache.get(MainActivity.this);
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
                        // Log.e("insert", object.getString("icondata")+"=========");
                        // byte[] top = Base64.decode(URLDecoder.decode(object.getString("icondata"), "utf-8"),
                        // Base64.DEFAULT);
                        // Bitmap bitmap = ImageUtils.bytes2Bitmap(top);
                        // BitmapUtil.savePhotoByte(bitmap, VideoManager.toppath, "topimg" +
                        // shareUtil.getShare().getAccountid() + ".jpg");
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
                // catch (UnsupportedEncodingException e)
                // {
                // dialog.dismiss();
                // e.printStackTrace();
                // }
            }
            
            @Override
            public void onFailure(String responseBody, Throwable error)
            {
                super.onFailure(responseBody, error);
                dialog.dismiss();
            }
        });
    }
    
    // byte数组到图片到硬盘上
    public void byte2image(byte[] data, String path)
    {
        if (data.length < 3 || path.equals(""))
            return;// 判断输入的byte是否为空
        try
        {
            FileOutputStream imageOutput = new FileOutputStream(new File(path));// 打开输入流
            imageOutput.write(data, 0, data.length);// 将byte写入硬盘
            imageOutput.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void onFail()
    {
        // 登录失败
        Toast.makeText(context, R.string.error_num, Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }
}
