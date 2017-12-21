package com.vomont.yundudao.ui.createproblem;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vomont.fileloadsdk.UpLoadUtil;
import com.vomont.fileloadsdk.WMFileLoadSdk;
import com.vomont.fileloadsdk.WMFileLoadSdk.EventBigFileCallback;
import com.vomont.fileloadsdk.WMFileLoadSdk.EventCallback;
import com.vomont.yundudao.R;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.bean.DetalBean;
import com.vomont.yundudao.bean.DetalInfo;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.PicTimeInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.bean.TagInfo;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.mvpview.detal.IDetalView;
import com.vomont.yundudao.mvpview.problem.ICreateProView;
import com.vomont.yundudao.presenter.detal.DetalPresenter;
import com.vomont.yundudao.ui.createproblem.adapter.SendImgGridAdapter;
import com.vomont.yundudao.ui.createproblem.view.DatePopuWindow;
import com.vomont.yundudao.ui.createproblem.view.DatePopuWindow.SetClickBack;
import com.vomont.yundudao.ui.patrol.SelectSubActivity;
import com.vomont.yundudao.ui.pic.AddPicActivity;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.DataUtils;
import com.vomont.yundudao.utils.GlideCacheUtil;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.utils.addpic.LocalMedia;
import com.vomont.yundudao.utils.imagecrom.CompressHelper;
import com.vomont.yundudao.view.NoScrollGridView.NoScrollGridView;
import com.vomont.yundudao.view.ios.ActionSheetDialog;
import com.vomont.yundudao.view.ios.ActionSheetDialog.OnSheetItemClickListener;
import com.vomont.yundudao.view.ios.ActionSheetDialog.SheetItemColor;
import com.vomont.yundudao.view.ios.AlertDialog;

/**
 * 
 */
// * 十年生死两茫茫，写程序，到天亮。
// * 千行代码，Bug 何处藏。
// * 纵使上线又怎样，朝令改，夕断肠。
// * 领导每天新想法，天天改，日日忙。
// * 相顾无言，惟有泪千行。
// * 每晚灯火阑珊处，程序员，又加班，工作狂~
@SuppressLint({"SimpleDateFormat", "HandlerLeak"})
public class CreateProblemActivity extends Activity implements OnClickListener, ICreateProView, IDetalView
{
    // , IFactoryView
    private ImageView go_back;
    
    private TextView top_name;
    
    private TextView jump;
    
    private LinearLayout create_img_getimg;
    
    private EditText creat_problem_comment;
    
    private LinearLayout create_problem_date;
    
    private TextView create_problem_date_text;
    
    private LinearLayout create_problem_factoryname;
    
    private TextView create_problem_factoryname_text;
    
    private LinearLayout create_problem_copyman;
    
    private TextView create_problem_copyman_text;
    
    private LinearLayout create_problem_tag;
    
    private TextView create_problem_tag_text;
    
    private LinearLayout create_problem_dealman;
    
    private TextView create_problem_dealman_text;
    
    private ActionSheetDialog actionSheetDialog;
    
    private Intent intent;
    
    private int REQUEST_CODE_CONTENT_IMAGE = 11;
    
    private int REQUEST_CODE_PHONE_IMAGE = 22;
    
    private int REQUEST_CODE_CENTER_IMG = 33;
    
    private DatePopuWindow datePopuWindow;
    
    private PicTimeInfo picTimeInfo;
    
    private int ACTION_SELECTTAG = 0xffff1;
    
    private int ACTION_DEALMAN = 0xffff2;
    
    private int ACTION_COPYMAN = 0xffff3;
    
    private int ACTION_SELECTDEV = 0xffff4;
    
    private int ACTION_IMAGE_SETTING = 0xffff6;
    
    private String imgPath;
    
    private TagInfo tagInfo;
    
    private DetalInfo detalInfo;
    
    private SubFactory subFactory;
    
    private String longTime;
    
    private String copymanid;
    
    private int relatedsubfactoryid;
    
    private Dialog dialog;
    
    private ShareUtil shareUtil;
    
    private List<FactoryInfo> mlist;
    
    private NoScrollGridView send_problem_img;
    
    private SendImgGridAdapter adapter;
    
    private List<LocalMedia> send_mlist;
    
    private String name;
    
    private FactoryBean factoryBean;
    
    // private int deviceid;
    
    private DetalPresenter detalPresenter;
    
    private List<DetalInfo> detail_list;
    
    private Map<Integer, String> hasSend;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_problem);
        GlideCacheUtil.getInstance().clearImageAllCache(getApplicationContext());
        System.gc();
        initView();
        initData();
        initListener();
    }
    
    private void initListener()
    {
        go_back.setOnClickListener(this);
        jump.setOnClickListener(this);
        create_img_getimg.setOnClickListener(this);
        create_problem_date.setOnClickListener(this);
        create_problem_factoryname.setOnClickListener(this);
        create_problem_tag.setOnClickListener(this);
        create_problem_dealman.setOnClickListener(this);
        create_problem_copyman.setOnClickListener(this);
        detalPresenter = new DetalPresenter(this);
        detalPresenter.getDetal(shareUtil.getShare().getUser_id() + "");
        send_problem_img.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (position == send_mlist.size())
                {
                    addImage();
                }
                else
                {
                    intent = new Intent(CreateProblemActivity.this, ImageSettingActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("sendpic", (Serializable)send_mlist);
                    startActivityForResult(intent, ACTION_IMAGE_SETTING);
                }
            }
        });
    }
    
    @SuppressLint("UseSparseArrays")
    private void initData()
    {
        top_name.setText("创建问题");
        jump.setText("发送");
        intent = getIntent();
        jump.setVisibility(View.VISIBLE);
        send_mlist = new ArrayList<LocalMedia>();
        picTimeInfo = (PicTimeInfo)intent.getSerializableExtra("picinfo");
        adapter = new SendImgGridAdapter(this, send_mlist);
        send_problem_img.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ACache aCache = ACache.get(this);
        factoryBean = (FactoryBean)aCache.getAsObject("factoryBean");
        if (picTimeInfo != null)
        {
            imgPath = picTimeInfo.getPath() + "/" + picTimeInfo.getName();
            relatedsubfactoryid = picTimeInfo.getSubfactoryid();
            LocalMedia localMedia = new LocalMedia();
            localMedia.setPath(imgPath);
            send_mlist.add(localMedia);
        }
        SimpleDateFormat sFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String nowdate = sFormat.format(new Date());
        create_problem_date_text.setText(nowdate);
        longTime = DataUtils.stringToLong(nowdate, "yyyy年MM月dd日") / 1000 + "";
        if (factoryBean != null && picTimeInfo != null)
        {
            if (factoryBean.getSubfactorys() != null && factoryBean.getSubfactorys().size() != 0)
            {
                for (int i = 0; i < factoryBean.getSubfactorys().size(); i++)
                {
                    if (factoryBean.getSubfactorys().get(i).getSubfactoryid() == picTimeInfo.getSubfactoryid())
                    {
                        subFactory = factoryBean.getSubfactorys().get(i);
                        create_problem_factoryname_text.setText(factoryBean.getSubfactorys().get(i).getSubfactoryname());
                        create_problem_factoryname_text.setTextColor(getResources().getColor(R.color.main_color));
                    }
                }
            }
            else if (factoryBean.getSubfactorys() != null && factoryBean.getSubfactorys().size() != 0)
            {
                for (int i = 0; i < factoryBean.getSubfactorys().size(); i++)
                {
                    if (factoryBean.getSubfactorys().get(i).getSubfactoryid() == picTimeInfo.getSubfactoryid())
                    {
                        subFactory = factoryBean.getSubfactorys().get(i);
                        create_problem_factoryname_text.setText(factoryBean.getSubfactorys().get(i).getSubfactoryname());
                        create_problem_factoryname_text.setTextColor(getResources().getColor(R.color.main_color));
                    }
                }
            }
        }
        addFactoryInfo(factoryBean);
        hasSend = new HashMap<Integer, String>();
        
        WMFileLoadSdk.getInstance().setEventBigFileCallback(new EventBigFileCallback()
        {
            @Override
            public void OnSucess(int arg0, String arg1)
            {
                hasSend.put(arg0, arg1);
                if (hasSend.size() != 0 && (hasSend.size() == send_mlist.size()))
                {
                    Map<Integer, String> map = sortMapByKey(hasSend);
                    String paths = "";
                    for (int i = 0; i < map.size(); i++)
                    {
                        paths = paths + map.get(i) + ",";
                    }
                    String input = "";
                    if (!TextUtils.isEmpty(creat_problem_comment.getText().toString()))
                    {
                        input = creat_problem_comment.getText().toString();
                    }
                    simplePostClick(input, paths);
                }
                
            }
            
            @Override
            public void OnProgress(int arg0, int arg1)
            {
                
            }
            
            @Override
            public void OnFail(int arg0, int arg1)
            {
                hasSend.put(arg0, "");
                if (hasSend.size() != 0 && (hasSend.size() == send_mlist.size()))
                {
                    Toast.makeText(CreateProblemActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        WMFileLoadSdk.getInstance().setEventCallback(new EventCallback()
        {
            @Override
            public void OnSucess(int arg0, String arg1)
            {
                hasSend.put(arg0, arg1);
                if (hasSend.size() != 0 && (hasSend.size() == send_mlist.size()))
                {
                    Map<Integer, String> map = sortMapByKey(hasSend);
                    String paths = "";
                    
                    for (Integer key : map.keySet())
                    {
                        String value = map.get(key);
                        paths = paths + value + ",";
                    }
                 
                    String input = "";
                    if (!TextUtils.isEmpty(creat_problem_comment.getText().toString()))
                    {
                        input = creat_problem_comment.getText().toString();
                    }
                    simplePostClick(input, paths);
                }
                
            }
            
            @Override
            public void OnFail(int arg0, int arg1)
            {
                hasSend.put(arg0, "");
                if (hasSend.size() != 0 && (hasSend.size() == send_mlist.size()))
                {
                    Toast.makeText(CreateProblemActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }
    
    /**
     * 其他來源的所有的图片统一整理后显示
     * 
     * @param path
     */
    private void savaNewBit(final String path, int i)
    {

        String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + i;
        new CompressHelper.Builder(CreateProblemActivity.this).setMaxWidth(480) // 默认最大宽度为720
            .setMaxHeight(640)
            // 默认最大高度为960
            .setQuality(70)
            // 默认压缩质量为80
            .setCompressFormat(CompressFormat.JPEG)
            // 设置默认压缩为jpg格式
            .setFileName(name)
            // 设置你的文件名
            .setDestinationDirectoryPath(VideoManager.sendimg)
            .build()
            .compressToFile(new File(path));
        send_mlist.get(i).setPath(VideoManager.sendimg + "/" + name + ".jpg");
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
    }
    
    private void initView()
    {
        go_back = (ImageView)findViewById(R.id.go_back);
        top_name = (TextView)findViewById(R.id.top_name);
        jump = (TextView)findViewById(R.id.jump);
        create_img_getimg = (LinearLayout)findViewById(R.id.create_img_getimg);
        send_problem_img = (NoScrollGridView)findViewById(R.id.send_problem_img);
        create_problem_date = (LinearLayout)findViewById(R.id.create_problem_date);
        create_problem_date_text = (TextView)findViewById(R.id.create_problem_date_text);
        create_problem_factoryname = (LinearLayout)findViewById(R.id.create_problem_factoryname);
        create_problem_factoryname_text = (TextView)findViewById(R.id.create_problem_factoryname_text);
        create_problem_tag = (LinearLayout)findViewById(R.id.create_problem_tag);
        create_problem_tag_text = (TextView)findViewById(R.id.create_problem_tag_text);
        create_problem_dealman = (LinearLayout)findViewById(R.id.create_problem_dealman);
        create_problem_dealman_text = (TextView)findViewById(R.id.create_problem_dealman_text);
        create_problem_copyman = (LinearLayout)findViewById(R.id.create_problem_copyman);
        create_problem_copyman_text = (TextView)findViewById(R.id.create_problem_copyman_text);
        creat_problem_comment = (EditText)findViewById(R.id.creat_problem_comment);
        shareUtil = new ShareUtil(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.go_back:
                if (send_mlist != null && send_mlist.size() != 0)
                {
                    AlertDialog alertDialog = new AlertDialog(this);
                    alertDialog.builder();
                    alertDialog.setMsg("是否退出");
                    alertDialog.setNegativeButton("确定", new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            deleteimg();
                            finish();
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
                }
                else
                {
                    finish();
                }
                break;
            case R.id.create_img_getimg:
                addImage();
                break;
            case R.id.create_problem_date:
                datePopuWindow = new DatePopuWindow(this);
                datePopuWindow.showAtLocation(this.findViewById(R.id.scroll), Gravity.BOTTOM, 0, 0);
                datePopuWindow.setOnClickBack(new SetClickBack()
                {
                    @Override
                    public void getDateString(String date)
                    {
                        String time = "";
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        try
                        {
                            time = new SimpleDateFormat("yyyy年MM月dd日").format(sdf.parse(date));
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }
                        create_problem_date_text.setText(time);
                        
                        longTime = DataUtils.stringToLong(date, "yyyy/MM/dd") / 1000 + "";
                    }
                });
                break;
            case R.id.create_problem_factoryname:
                intent = new Intent(CreateProblemActivity.this, SelectSubActivity.class);
                startActivityForResult(intent, ACTION_SELECTDEV);
                break;
            case R.id.create_problem_tag:
                intent = new Intent(CreateProblemActivity.this, SelectTagActivity.class);
                startActivityForResult(intent, ACTION_SELECTTAG);
                break;
            case R.id.create_problem_dealman:
                intent = new Intent(CreateProblemActivity.this, SelectDealActivity.class);
                startActivityForResult(intent, ACTION_DEALMAN);
                break;
            case R.id.create_problem_copyman:
                intent = new Intent(CreateProblemActivity.this, SeclectCopyActivity.class);
                startActivityForResult(intent, ACTION_COPYMAN);
                break;
            case R.id.jump:
                if (tagInfo == null)
                {
                    Toast.makeText(this, "问题类型不能为空!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (subFactory == null)
                {
                    Toast.makeText(this, "监控点不能为空!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (longTime == null)
                {
                    Toast.makeText(this, "截止时间不能为空!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (detalInfo == null)
                {
                    Toast.makeText(this, "处理人不能为空!", Toast.LENGTH_LONG).show();
                    return;
                }
               
                if (TextUtils.isEmpty(creat_problem_comment.getText().toString()))
                {
                    Toast.makeText(this, "问题描述不能为空!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (send_mlist != null && send_mlist.size() != 0)
                {
                    dialog = ProgressDialog.createLoadingDialog(this, "问题上传中");
                    dialog.show();
                    new Thread(new Runnable()
                    {
                        public void run()
                        {
                            for (int i = 0; i < send_mlist.size(); i++)
                            {
                                savaNewBit(send_mlist.get(i).getPath(), i);
                            }
                            for (int i = 0; i < send_mlist.size(); i++)
                            {
                                if (new File(send_mlist.get(i).getPath()).length() <= UpLoadUtil.fragmentSize)
                                {
                                    WMFileLoadSdk.getInstance().WM_VFile_UploadFile(send_mlist.get(i).getPath());
                                }
                                else
                                {
                                    WMFileLoadSdk.getInstance().WM_VFile_UploadBigFile(send_mlist.get(i).getPath());
                                }
                            }
                      
                        }
                    }).start();
                }
                else
                {
                    Toast.makeText(CreateProblemActivity.this, "请添加图片", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
    
    private void addImage()
    {
        if (send_mlist.size() < 9)
        {
            actionSheetDialog = new ActionSheetDialog(CreateProblemActivity.this);
            actionSheetDialog.builder().addSheetItem("拍照", SheetItemColor.Blue, new OnSheetItemClickListener()
            {
                @Override
                public void onClick(int which)
                {
                    if (send_mlist.size() < 9)
                    {
                        getImageFromPhone();
                    }
                    else
                    {
                        Toast.makeText(CreateProblemActivity.this, "最多上传9张图片", Toast.LENGTH_LONG).show();
                    }
                }
            }).addSheetItem("从相册中选取", SheetItemColor.Blue, new OnSheetItemClickListener()
            {
                @Override
                public void onClick(int which)
                {
                    Intent intent = new Intent(CreateProblemActivity.this, AddPicActivity.class);
                    intent.putExtra("maxNum", (9 - send_mlist.size()));
                    startActivityForResult(intent, REQUEST_CODE_CONTENT_IMAGE);
                }
            }).addSheetItem("从图片中心导入", SheetItemColor.Blue, new OnSheetItemClickListener()
            {
                @Override
                public void onClick(int which)
                {
                    Intent intent = new Intent(CreateProblemActivity.this, AddPicActivity.class);
                    intent.putExtra("name", "yundd-photo" + shareUtil.getShare().getNum());
                    intent.putExtra("maxNum", (9 - send_mlist.size()));
                    startActivityForResult(intent, REQUEST_CODE_CENTER_IMG);
                }
            }).show();
        }
        else
        {
            Toast.makeText(CreateProblemActivity.this, "最多上传9张图片", Toast.LENGTH_LONG).show();
        }
    }
    
    // 简单的带参数和Header的post请求
    public void simplePostClick(String inputtext, String imag)
    {
        ACache aCache = ACache.get(this);
        aCache.put("problemsend", "1");
        // 暂时这里用的okhttp 前面的 asynchttp的都需要改了 有问题的
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        final Map<String, String> params = new HashMap<String, String>();
        params.put(HTTP_PAMRS.msgid, HTTP_TYPE.type18);
        params.put(HTTP_PAMRS.userid, shareUtil.getShare().getUser_id() + "");
        params.put("problemtypeid", tagInfo.getTypeid() + "");
        params.put("relatedsubfactoryid", relatedsubfactoryid + "");
        params.put("donetime", longTime + "");
        params.put("ownerid", detalInfo.getId() + "");
        params.put("ccuserids", "9,");
        try
        {
            params.put("problemdesp", URLEncoder.encode(inputtext, "utf-8"));
            params.put("imagepathlist", imag);
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        params.put("vfilesvrid", shareUtil.getShare().getVfilesvr().getVfilesvrid() + "");
        StringBuffer sb = new StringBuffer();
        // 设置表单参数
        for (String key : params.keySet())
        {
            sb.append(key + "=" + params.get(key) + "&");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sb.toString());
        Request request = new Request.Builder().url(Appcation.BASE_URL).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onResponse(Response arg0)
                throws IOException
            {
                try
                {
                    String name = arg0.body().string();
                    JSONObject object = new JSONObject(name);
                    if (object.getInt("result") == 0)
                    {
                        handler.sendEmptyMessage(1);
                    }
                    else
                    {
                        handler.sendEmptyMessage(0);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                }
            }
            
            @Override
            public void onFailure(Request arg0, IOException arg1)
            {
                handler.sendEmptyMessage(0);
            }
        });
    }
    
    Handler handler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    Toast.makeText(CreateProblemActivity.this, "问题上传失败！", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    break;
                case 1:
                    Toast.makeText(CreateProblemActivity.this, "问题上传成功！", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    finish();
                    break;
                case 10:
                    String input = "";
                    if (!TextUtils.isEmpty(creat_problem_comment.getText().toString()))
                    {
                        input = creat_problem_comment.getText().toString();
                    }
                    String send = (String)msg.obj;
                    simplePostClick(input, send);
                    break;
                default:
                    break;
            }
        };
    };
    
    /**
     * 获取本地图片
     */
    protected void getImageFromAlbum()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");// 相片类型
        startActivityForResult(intent, REQUEST_CODE_CONTENT_IMAGE);
    }
    
    /**
     * 获取拍照图片
     */
    protected void getImageFromPhone()
    {
        // 拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
        // 有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 获取文件
        name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "-0-0-0";
        File file1 = new File(VideoManager.yundd_phone + shareUtil.getShare().getNum());
        if (!file1.exists())
        {
            file1.mkdirs();
        }
        File file = createFileIfNeed(VideoManager.yundd_phone + shareUtil.getShare().getNum() + "/" + name + ".jpg");
        // 拍照后原图回存入此路径下
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_PHONE_IMAGE);
    }
    
    public int returnName()
    {
        int name = 0;
        if (send_mlist != null && send_mlist.size() != 0)
        {
            name = send_mlist.size();
        }
        return name;
    }
    
    private void deleteimg()
    {
        File file = new File(VideoManager.sendimg);
        if (file.isDirectory())
        { // 否则如果它是一个目录
            File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
            for (int i = 0; i < files.length; i++)
            { // 遍历目录下所有的文件
                files[i].delete();
            }
            file.delete();
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTENT_IMAGE)
        {
            // 从相册获取的图片
            if (data != null)
            {
                send_mlist.addAll((List<LocalMedia>)data.getSerializableExtra("sendimg"));
                adapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == REQUEST_CODE_PHONE_IMAGE)
        {
            if (resultCode == RESULT_OK)
            {
                LocalMedia localMedia = new LocalMedia();
                localMedia.setPath(VideoManager.yundd_phone + shareUtil.getShare().getNum() + "/" + name + ".jpg");
                send_mlist.add(localMedia);
                adapter.notifyDataSetChanged();
            }
            else
            {
                File file = new File(VideoManager.yundd_phone + shareUtil.getShare().getNum() + "/" + name + ".jpg");
                file.delete();
            }
        }
        else if (requestCode == REQUEST_CODE_CENTER_IMG)
        {
            // 图片中心获取的图片
            if (data != null)
            {
                send_mlist.addAll((List<LocalMedia>)data.getSerializableExtra("sendimg"));
                adapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == ACTION_SELECTTAG)
        {
            if (data != null)
            {
                tagInfo = (TagInfo)data.getSerializableExtra("tag");
                create_problem_tag_text.setText(tagInfo.getTypename());
                create_problem_tag_text.setTextColor(getResources().getColor(R.color.main_color));
            }
        }
        else if (requestCode == ACTION_DEALMAN)
        {
            if (data != null)
            {
                detalInfo = (DetalInfo)data.getSerializableExtra("dealman");
                create_problem_dealman_text.setText("处理人：" + detalInfo.getName());
                create_problem_dealman_text.setTextColor(getResources().getColor(R.color.main_color));
            }
        }
        else if (requestCode == ACTION_COPYMAN)
        {
            if (data != null)
            {
                List<DetalInfo> back_mlist = (List<DetalInfo>)data.getSerializableExtra("copyman");
                String name = "";
                copymanid = "";
                if (back_mlist != null && back_mlist.size() != 0)
                    for (int i = 0; i < back_mlist.size(); i++)
                    {
                        if (i == 0)
                        {
                            copymanid = back_mlist.get(i).getId() + "," + copymanid;
                            name = name + back_mlist.get(i).getName();
                        }
                        else
                        {
                            copymanid = back_mlist.get(i).getId() + "," + copymanid;
                            name = name + "," + back_mlist.get(i).getName();
                        }
                    }
                if (copymanid.contains("null"))
                {
                    copymanid = copymanid.substring(4, copymanid.length());
                }
                create_problem_copyman_text.setText("抄送人：" + name);
                create_problem_copyman_text.setTextColor(getResources().getColor(R.color.main_color));
            }
        }
        else if (requestCode == ACTION_SELECTDEV)
        {
            if (data != null)
            {
                subFactory = (SubFactory)data.getSerializableExtra("subfatory");
                relatedsubfactoryid = subFactory.getSubfactoryid();
                create_problem_factoryname_text.setText(subFactory.getSubfactoryname());
                create_problem_factoryname_text.setTextColor(getResources().getColor(R.color.main_color));
            
            }
        }
        else if (requestCode == ACTION_IMAGE_SETTING)
        {
            if (data != null && data.getSerializableExtra("sendpaths") != null)
            {
                send_mlist.clear();
                List<LocalMedia> sendlist = (List<LocalMedia>)data.getSerializableExtra("sendpaths");
                send_mlist.addAll(sendlist);
                adapter.notifyDataSetChanged();
            }
        }
    }
    
    // 在sd卡中创建一保存图片（原图和缩略图共用的）文件夹
    private File createFileIfNeed(String file_Name)
    {
        File fileJA = new File(VideoManager.sendimg);
        if (!fileJA.exists())
        {
            fileJA.mkdirs();
        }
        File fileNew = new File(file_Name);
        if (!fileNew.exists())
        {
            try
            {
                fileNew.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return fileNew;
    }
    
    @Override
    public void getResultBySendInfo(int i)
    {
        if (i == 0)
        {
            Toast.makeText(this, "问题上传成功！", Toast.LENGTH_SHORT).show();
            deleteimg();
            finish();
        }
        else
        {
            Toast.makeText(this, "问题上传失败！", Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        deleteimg();
        GlideCacheUtil.getInstance().clearImageAllCache(getApplicationContext());
        GlideCacheUtil.getInstance().clearImageAllCache(this);
    }
    
    private void addFactoryInfo(FactoryBean factoryBean)
    {
        if (factoryBean != null)
        {
            mlist = new ArrayList<FactoryInfo>();
            if (factoryBean.getFactorys() != null)
            {
                mlist.addAll(factoryBean.getFactorys());
                if (factoryBean.getSubfactorys() != null)
                {
                    for (int i = 0; i < mlist.size(); i++)
                    {
                        List<SubFactory> sub_list = new ArrayList<SubFactory>();
                        for (int j = 0; j < factoryBean.getSubfactorys().size(); j++)
                        {
                            if (mlist.get(i).getFactoryid() == factoryBean.getSubfactorys().get(j).getOwnerfactoryid())
                            {
                                sub_list.add(factoryBean.getSubfactorys().get(j));
                            }
                        }
                        mlist.get(i).setMlist(sub_list);
                    }
                }
                if (factoryBean.getDevices() != null)
                {
                    for (int i = 0; i < mlist.size(); i++)
                    {
                        if (mlist.get(i).getMlist() != null && mlist.get(i).getMlist().size() != 0)
                            
                            for (int j = 0; j < mlist.get(i).getMlist().size(); j++)
                            {
                                List<DeviceInfo> dev_list = new ArrayList<DeviceInfo>();
                                if (factoryBean.getDevices() != null && factoryBean.getDevices().size() != 0)
                                    for (int k = 0; k < factoryBean.getDevices().size(); k++)
                                    {
                                        if (mlist.get(i).getMlist().get(j).getSubfactoryid() == factoryBean.getDevices().get(k).getSubfactoryid())
                                        {
                                            dev_list.add(factoryBean.getDevices().get(k));
                                        }
                                    }
                                mlist.get(i).getMlist().get(j).setMlist_device(dev_list);
                            }
                    }
                }
            }
        }
    }
    
    @Override
    public void getDetalman(DetalBean detalBean)
    {
        detail_list = new ArrayList<DetalInfo>();
        if (detalBean != null && detalBean.getAccounts() != null)
        {
            detail_list.addAll(detalBean.getAccounts());
            for (int i = 0; i < detail_list.size(); i++)
            {
                if (shareUtil.getShare().getAccountid() == detail_list.get(i).getId())
                {
                    detail_list.remove(i);
                }
            }
            if (factoryBean != null && factoryBean.getSubfactorys() != null && factoryBean.getSubfactorys().size() != 0)
            {
                for (SubFactory subFactory : factoryBean.getSubfactorys())
                {
                    if (subFactory.getSubfactoryid() == relatedsubfactoryid)
                    {
                        if (shareUtil.getShare().getAccountid() != subFactory.getDirectoraccountid())
                        {
                            for (int i = 0; i < detail_list.size(); i++)
                            {
                                if (subFactory.getDirectoraccountid() == detail_list.get(i).getId())
                                {
                                    detalInfo = detail_list.get(i);
                                    create_problem_dealman_text.setText("处理人：" + detail_list.get(i).getName());
                                    create_problem_dealman_text.setTextColor(getResources().getColor(R.color.main_color));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
                if(dialog!=null&&dialog.isShowing())
                {
                    dialog.dismiss();
                    finish();
                    return true;
                }

            if (send_mlist != null && send_mlist.size() != 0)
            {
                AlertDialog alertDialog = new AlertDialog(this);
                alertDialog.builder();
                alertDialog.setMsg("是否退出");
                alertDialog.setNegativeButton("确定", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        deleteimg();
                        finish();
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
            }
            else
            {
                finish();
            }
        }
        return false;
    }
    
    @Override
    public void getFailed()
    {
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        GlideCacheUtil.getInstance().clearImageAllCache(this);
        GlideCacheUtil.getInstance().getCacheSize(this);
    }
    
    /**
     * 使用 Map按key进行排序
     * 
     * @param map
     * @return
     */
    public static Map<Integer, String> sortMapByKey(Map<Integer, String> map)
    {
        if (map == null || map.isEmpty())
        {
            return null;
        }
        MapKeyComparator comparator = new MapKeyComparator();
        Map<Integer, String> sortMap = new TreeMap<Integer, String>(comparator);
        
        sortMap.putAll(map);
        
        return sortMap;
    }
    
}
