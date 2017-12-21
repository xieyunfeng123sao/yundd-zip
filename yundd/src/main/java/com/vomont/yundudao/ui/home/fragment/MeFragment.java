package com.vomont.yundudao.ui.home.fragment;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.MainActivity;
import com.vomont.yundudao.R;
import com.vomont.yundudao.base64.BASE64Encoder;
import com.vomont.yundudao.bean.DetalBean;
import com.vomont.yundudao.bean.DetalInfo;
import com.vomont.yundudao.bean.UserInfo;
import com.vomont.yundudao.common.Con_Action;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.mvpview.detal.IDetalView;
import com.vomont.yundudao.presenter.detal.DetalPresenter;
import com.vomont.yundudao.ui.personalcenter.VideoSettingActivity;
import com.vomont.yundudao.ui.setting.AppProblemActivity;
import com.vomont.yundudao.ui.setting.FeedbackActivity;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.HttpUtil;
import com.vomont.yundudao.utils.ImageUtils;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.circleImageView.CircleImageView;
import com.vomont.yundudao.view.ios.ActionSheetDialog;
import com.vomont.yundudao.view.ios.ActionSheetDialog.OnSheetItemClickListener;
import com.vomont.yundudao.view.ios.ActionSheetDialog.SheetItemColor;
import com.vomont.yundudao.view.ios.AlertDialog;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({"InflateParams", "SdCardPath", "SimpleDateFormat"})
public class MeFragment extends Fragment implements OnClickListener, IDetalView
{
    
    private CircleImageView user_iamge;
    
    private Button exit;
    
    private ImageView go_back;
    
    private TextView top_name;
    
    private TextView user_phone;
    
    private TextView user_name;
    
    private Intent intent;
    
    private LinearLayout me_problem, me_view, me_change_psd, me_setting, me_about;
    
    private String image_data;
    
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
    
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    
    private ShareUtil shareUtil;
    
    private TextView version;
    
    private LinearLayout me_video_setting;
    
    private TextView video_av;
    
    private DetalPresenter detalPresenter;
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_me, null);
        user_iamge = (CircleImageView)view.findViewById(R.id.user_image_top);
        exit = (Button)view.findViewById(R.id.exit);
        go_back = (ImageView)view.findViewById(R.id.go_back);
        top_name = (TextView)view.findViewById(R.id.top_name);
        top_name.setText(R.string.radio_me);
        user_phone = (TextView)view.findViewById(R.id.user_phone);
        me_problem = (LinearLayout)view.findViewById(R.id.me_problem);
        me_view = (LinearLayout)view.findViewById(R.id.me_view);
        me_change_psd = (LinearLayout)view.findViewById(R.id.me_change_psd);
        me_setting = (LinearLayout)view.findViewById(R.id.me_setting);
        me_about = (LinearLayout)view.findViewById(R.id.me_about);
        version = (TextView)view.findViewById(R.id.version);
        me_video_setting = (LinearLayout)view.findViewById(R.id.me_video_setting);
        video_av = (TextView)view.findViewById(R.id.video_av);
        user_name=(TextView)view.findViewById(R.id.user_name);
        shareUtil = new ShareUtil(getActivity());
        File file = new File(VideoManager.toppath + "/" + "topimg" + shareUtil.getShare().getAccountid() + ".jpg");
        if (file.exists())
        {
            Glide.with(getActivity()).load(new File(VideoManager.toppath + "/" + "topimg" + shareUtil.getShare().getAccountid() + ".jpg")).into(user_iamge);
        }
        try
        {
            version.setText(getAppVersion());
        }
        catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        user_phone.setText(shareUtil.getShare().getNum());
        initListener();
        detalPresenter = new DetalPresenter(this);
        detalPresenter.getDetal(shareUtil.getShare().getUser_id() + "");
        return view;
    }
    
    /** 获取单个App版本号 **/
    public String getAppVersion()
        throws NameNotFoundException
    {
        PackageManager pManager = getActivity().getPackageManager();
        PackageInfo packageInfo = pManager.getPackageInfo(getActivity().getPackageName(), 0);
        String appVersion = packageInfo.versionName;
        return appVersion;
    }
    
    private void initListener()
    {
        user_iamge.setOnClickListener(this);
        exit.setOnClickListener(this);
        me_problem.setOnClickListener(this);
        go_back.setVisibility(View.GONE);
        me_problem.setOnClickListener(this);
        me_view.setOnClickListener(this);
        me_change_psd.setOnClickListener(this);
        me_setting.setOnClickListener(this);
        me_about.setOnClickListener(this);
        me_video_setting.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.user_image_top:
                new ActionSheetDialog(getActivity()).builder().setCancelable(true).setCanceledOnTouchOutside(true).addSheetItem("拍照", SheetItemColor.Blue, new OnSheetItemClickListener()
                {
                    @Override
                    public void onClick(int which)
                    {
                        // 调用系统的拍照功能
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定调用相机拍照后照片的储存路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
                    }
                }).addSheetItem("从相册中选取", SheetItemColor.Blue, new OnSheetItemClickListener()
                {
                    @Override
                    public void onClick(int which)
                    {
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    }
                }).show();
                break;
            case R.id.exit:
                AlertDialog alertDialog = new AlertDialog(getActivity());
                alertDialog.builder();
                alertDialog.setMsg("是否退出");
                alertDialog.setNegativeButton("确定", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        UserInfo user = shareUtil.getShare();
                        shareUtil.setShare(user.getNum(), "", -1, user.getVeyeuserid(), user.getVeyekey(), user.getAccountid(), null, 0, null, 0,0);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                        ACache aCache = ACache.get(getActivity());
                        aCache.put("factoryBean", "");
                        aCache.put("problemDetailInfo", "");
                    }
                });
                
                alertDialog.setPositiveButton("取消", new OnClickListener()
                {
                    
                    @Override
                    public void onClick(View v)
                    {
                        
                    }
                });
                alertDialog.show();
                break;
            
            case R.id.me_problem:
                intent = new Intent(getActivity(), AppProblemActivity.class);
                startActivity(intent);
                break;
            
            case R.id.me_view:
                intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                break;
            
            case R.id.me_change_psd:
                intent = new Intent();
                intent.setAction(Con_Action.CHANGE_PSD_ACTION);
                startActivity(intent);
                break;
            case R.id.me_setting:
                intent = new Intent();
                intent.setAction(Con_Action.SETTING_ACTION);
                startActivity(intent);
                break;
            case R.id.me_about:
                intent = new Intent();
                intent.setAction(Con_Action.ABOUT_ACTION);
                startActivity(intent);
                break;
            case R.id.me_video_setting:
                intent = new Intent(getActivity(), VideoSettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        ACache aCache = ACache.get(getActivity());
        String av = aCache.getAsString("videosetting");
        if (!TextUtils.isEmpty(av))
        {
            if (av.equals("0"))
            {
                video_av.setText("标清");
            }
            else if (av.equals("1"))
            {
                video_av.setText("高清");
            }
            else
            {
                video_av.setText("全高清");
            }
        }
        else
        {
            video_av.setText("标清");
        }
        
    }
    
    @SuppressWarnings("static-access")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != getActivity().RESULT_OK)
            return;
        switch (requestCode)
        {
            case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
                
                startPhotoZoom(Uri.fromFile(tempFile), 150);
                break;
            case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
                // 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
                if (data != null)
                    startPhotoZoom(data.getData(), 150);
                break;
            
            case PHOTO_REQUEST_CUT:// 返回的结果
                if (data != null)
                    setPicToView(data);
                break;
        }
    }
    
    // 将进行剪裁后的图片显示到UI界面上
    @SuppressWarnings("unused")
    private void setPicToView(Intent picdata)
    {
        Bundle bundle = picdata.getExtras();
        if (bundle != null)
        {
            Bitmap bit = bundle.getParcelable("data");
            final Bitmap photo = BitmapUtil.imageZoom(bit);
            byte[] top = ImageUtils.bitmap2Bytes(photo, CompressFormat.JPEG);
            BASE64Encoder encoder = new BASE64Encoder();
            image_data = encoder.encode(top);
            final Dialog dialog = com.vomont.yundudao.utils.ProgressDialog.createLoadingDialog(getActivity(), "");
            dialog.show();
            ShareUtil shareUtil = new ShareUtil(getActivity());
            RequestParams reParams = new RequestParams();
            reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type8);
            reParams.add(HTTP_PAMRS.userid, shareUtil.getShare().getUser_id() + "");
            reParams.add(HTTP_PAMRS.icondata, image_data);
            HttpUtil.post(reParams, new TextHttpResponseHandler()
            {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseBody)
                {
                    super.onSuccess(statusCode, headers, responseBody);
                    JSONObject object;
                    try
                    {
                        object = new JSONObject(responseBody);
                        if (object.getInt("result") == 0)
                        {
                            user_iamge.setImageBitmap(photo);
                        }
                        else
                        {
                            Toast.makeText(getActivity(), R.string.error_image, Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    
                    dialog.dismiss();
                }
                
                @Override
                public void onProgress(int bytesWritten, int totalSize)
                {
                    super.onProgress(bytesWritten, totalSize);
                    int count = (int)((bytesWritten * 1.0 / totalSize) * 100);
                    // 上传进度显示
                    dialog.dismiss();
                }
                
                @Override
                public void onFailure(String responseBody, Throwable error)
                {
                    super.onFailure(responseBody, error);
                    Toast.makeText(getActivity(), R.string.error_image, Toast.LENGTH_LONG).show();
                    user_iamge.setImageResource(R.drawable.log);
                    dialog.dismiss();
                }
            });
        }
        else
        {
            Toast.makeText(getActivity(), R.string.error_getimage, Toast.LENGTH_LONG).show();
        }
        // image_data = Base64Image.GetImageStr(fileName);
        
    }
    
    private void startPhotoZoom(Uri uri, int size)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    
    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    
    /**
     * 获取网落图片资源
     * 
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url)
    {
        URL myFileURL;
        Bitmap bitmap = null;
        try
        {
            myFileURL = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection)myFileURL.openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            // 连接设置获得数据流
            conn.setDoInput(true);
            // 不使用缓存
            conn.setUseCaches(false);
            // 这句可有可无，没有影响
            // conn.connect();
            // 得到数据流
            InputStream is = conn.getInputStream();
            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            // 关闭数据流
            is.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void getDetalman(DetalBean detalBean)
    {
        if (detalBean != null && detalBean.getAccounts() != null)
        {
            for(DetalInfo info:detalBean.getAccounts())
            {
                if(info.getId()==shareUtil.getShare().getAccountid())
                {
                    user_name.setText(info.getName());
                }
            }
        }
        
    }

    @Override
    public void getFailed()
    {
        
    }
}
