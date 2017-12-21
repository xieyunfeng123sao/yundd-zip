package com.vomont.yundudao.ui.patrol;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.mabeijianxi.smallvideorecord2.LocalMediaCompress;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.LocalMediaConfig;
import com.mabeijianxi.smallvideorecord2.model.OnlyCompressOverBean;
import com.vomont.fileloadsdk.WMFileLoadSdk;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DetalInfo;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoHelpter;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.upload.VideoUpService;
import com.vomont.yundudao.utils.BitmapUtil;
import com.vomont.yundudao.utils.CommonUtil;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.video.CommonVideoView;
import com.vomont.yundudao.view.video.CommonVideoView.OnScreenListener;

@SuppressLint({"ClickableViewAccessibility", "SimpleDateFormat"})
public class VedioPlayActivity extends Activity implements OnClickListener {

    private RelativeLayout top_vedio;

    private ImageView vedio_back;

    private LinearLayout ll_attribute;

    private CommonVideoView play_video;

    private TextView recorde_time;

    private LinearLayout ll_man;

    private TextView look_man;

    private LinearLayout ll_addr;

    private TextView vedio_address;

    private EditText input_vedio_conn;

    private LinearLayout ll_vedio_action;

    private RelativeLayout re_video;

    private RelativeLayout vedio_preservation;

    private RelativeLayout vedio_upload;

    private String videoname;

    private Intent intent;

    private int width, patrol_re_video_height, height;

    private int ACTION_TO_LOOK = 0xff0001;

    private int ACTION_TO_SUB = 0xff0002;

    private List<DetalInfo> detal_mlist;

    private FactoryInfo factoryInfo;

    private SubFactory subFactory;

    private VideoHelpter videoHelpter;

    private String str_lookerid = "";

    // private VideoUpService videoUpService;

    private int subFatoryid;

    private String subname;

    private String lookername;

    private String desp;

    private VideoBean upVideoBean;

    private boolean isLandscape;

    private Dialog dialog;

    private boolean isFinish = true;

    private String videoPath;

    Date date = null;

    private int canClick;

//    private UserInfo userInfo;

    private ShareUtil shareUtil;

    private ZipService zipService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_vedio_action);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        shareUtil = new ShareUtil(this);
        // 绑定服务在主界面 后面的界面调用 解绑service后 不会销毁该服务
        Intent intent = new Intent(this, ZipService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        initView();
        initData();
        getViewwidth();
        initLienter();
    }

    @SuppressLint("ResourceAsColor")
    private void initData() {
        Intent intent = getIntent();
        videoname = intent.getStringExtra("name");
        str_lookerid = intent.getStringExtra("looker");
        subFatoryid = intent.getIntExtra("subid", -1);
        subname = intent.getStringExtra("subname");
        lookername = intent.getStringExtra("lookername");
        desp = intent.getStringExtra("desp");
        videoPath = intent.getStringExtra("path");
        canClick = intent.getIntExtra("canclick", 0);
        File file = new File(VideoManager.video_face_img + "/" + videoname + ".jpg");
        if (!file.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = BitmapUtil.getVideoThumbnail(videoPath + "/" + videoname + ".mp4");
                    BitmapUtil.savePhotoByte(bitmap, VideoManager.video_face_img, videoname + ".jpg");
                    if (bitmap != null)
                        bitmap.recycle();
                }
            }).start();
        }
        if (videoPath == null) {
            videoPath = VideoManager.path;
        }
        if (!TextUtils.isEmpty(lookername)) {
            look_man.setText(lookername);
        }
        if (!TextUtils.isEmpty(subname)) {
            vedio_address.setText(subname);
        }
        if (!TextUtils.isEmpty(desp)) {
            input_vedio_conn.setText(desp);
        }
        try {
            recorde_time.setText(nameForDate(videoname));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(recorde_time.getText().toString());
        } catch (ParseException e) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = new Date();
            recorde_time.setText(formatter.format(new Date()));
            e.printStackTrace();
        }
        play_video.setVisibility(View.GONE);

        videoHelpter = new VideoHelpter(this);
        if (canClick == 1) {
            ll_man.setEnabled(false);
            ll_addr.setEnabled(false);
            vedio_preservation.setEnabled(false);
            vedio_upload.setEnabled(false);
            vedio_preservation.setBackgroundColor(getResources().getColor(R.color.biantai_gray));
            vedio_upload.setBackgroundColor(getResources().getColor(R.color.biantai_gray));
            input_vedio_conn.setEnabled(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        play_video.startPath(videoPath + "/" + videoname + ".mp4");
    }

    private void initLienter() {
        vedio_back.setOnClickListener(this);
        ll_man.setOnClickListener(this);
        ll_addr.setOnClickListener(this);
        vedio_preservation.setOnClickListener(this);
        vedio_upload.setOnClickListener(this);
        play_video.setOnScreenListener(new OnScreenListener() {
            @Override
            public void startScreen() {
                top_vedio.setVisibility(View.GONE);
                ll_attribute.setVisibility(View.GONE);
                ll_vedio_action.setVisibility(View.GONE);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
                re_video.setLayoutParams(params);
                CommonUtil.setFullScreen(VedioPlayActivity.this);
                isFinish = false;
            }

            @Override
            public void NoScreen() {
                top_vedio.setVisibility(View.VISIBLE);
                ll_attribute.setVisibility(View.VISIBLE);
                ll_vedio_action.setVisibility(View.VISIBLE);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, patrol_re_video_height);
                re_video.setLayoutParams(params);
                CommonUtil.cancelFullScreen(VedioPlayActivity.this);
                isFinish = true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        play_video.setVisibility(View.VISIBLE);
    }

    private void initView() {
        top_vedio = (RelativeLayout) findViewById(R.id.top_vedio);
        vedio_back = (ImageView) findViewById(R.id.vedio_back);
        play_video = (CommonVideoView) findViewById(R.id.play_video);
        ll_attribute = (LinearLayout) findViewById(R.id.ll_attribute);
        recorde_time = (TextView) findViewById(R.id.recorde_time);
        ll_man = (LinearLayout) findViewById(R.id.ll_man);
        look_man = (TextView) findViewById(R.id.look_man);
        ll_addr = (LinearLayout) findViewById(R.id.ll_addr);
        vedio_address = (TextView) findViewById(R.id.vedio_address);
        input_vedio_conn = (EditText) findViewById(R.id.input_vedio_conn);
        ll_vedio_action = (LinearLayout) findViewById(R.id.ll_vedio_action);
        vedio_preservation = (RelativeLayout) findViewById(R.id.vedio_preservation);
        vedio_upload = (RelativeLayout) findViewById(R.id.vedio_upload);
        re_video = (RelativeLayout) findViewById(R.id.re_video);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_man:
                intent = new Intent(VedioPlayActivity.this, LookPeopleActivity.class);
                startActivityForResult(intent, ACTION_TO_LOOK);
                break;
            case R.id.ll_addr:
                intent = new Intent(VedioPlayActivity.this, SelectSubActivity.class);
                startActivityForResult(intent, ACTION_TO_SUB);
                break;
            case R.id.vedio_preservation:
                // if (judgeInput() == 1)
                // {
                saveVideo(videoPath + "/" + videoname + ".mp4");
                intent = new Intent(VedioPlayActivity.this, PatrolLoadVedioActivity.class);
                startActivity(intent);
                finish();
                // }
                break;
            case R.id.vedio_upload:
                if (judgeInput() == 1) {
                    dialog = ProgressDialog.createLoadingDialog(this, "");
                    dialog.show();
                    saveVideo(videoPath + "/" + videoname + ".mp4");
                    if (upVideoBean.getFileId() != 0) {
                        // 如果fileId不为0 说明已经在上传 就继续上传 先暂停在上传 防止冲突
                        WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(upVideoBean.getFileId(), true);
                        // 繼續上傳
                        WMFileLoadSdk.getInstance().WM_VFile_PauseUpload(upVideoBean.getFileId(), false);
                        // 上传状态改为1 表示正在上传
                        upVideoBean.setLoadstate(1);
                        videoHelpter.updateBean(videoname, upVideoBean);
                    } else {
                            if(!videoPath.equals(VideoManager.path))
                            {
                                //如果不是app拍摄的视频 都要压缩上传
                                zipService.zipVideo(videoPath,videoname,upVideoBean);
                            }
                            else
                            {
                                // 第一次上传
                                int fileId = WMFileLoadSdk.getInstance().WM_VFile_UploadBigFile(videoPath + "/" + videoname + ".mp4");
                                if (fileId == 0) {
                                    Toast.makeText(VedioPlayActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
                                } else {
                                    upVideoBean.setFileId(fileId);
                                    // 上传状态改为1 表示正在上传
                                    upVideoBean.setLoadstate(1);
                                    // 表示状态是上传 而不是保存的数据
                                    upVideoBean.setIsSave(1);
                                    videoHelpter.updateBean(videoname, upVideoBean);
                                }
                            }

                    }
                    intent = new Intent(VedioPlayActivity.this, PatrolLoadVedioActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.vedio_back:
                finish();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_TO_LOOK) {
            detal_mlist = new ArrayList<DetalInfo>();
            if (data != null) {
                detal_mlist = (List<DetalInfo>) data.getSerializableExtra("canlook");
                if (detal_mlist != null && detal_mlist.size() != 0) {
                    String canlook = "";
                    for (int i = 0; i < detal_mlist.size(); i++) {
                        canlook = canlook + detal_mlist.get(i).getName() + " ";
                        str_lookerid = str_lookerid + detal_mlist.get(i).getId() + ",";
                    }
                    look_man.setText(canlook);
                } else {
                    look_man.setText("全部人员");
                }
            }
        } else if (requestCode == ACTION_TO_SUB) {
            if (data != null) {
                factoryInfo = (FactoryInfo) data.getSerializableExtra("fatory");
                subFactory = (SubFactory) data.getSerializableExtra("subfatory");
                vedio_address.setText(factoryInfo.getFactoryname() + "/" + subFactory.getSubfactoryname());
                subFatoryid = subFactory.getSubfactoryid();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unbindService(conn);
        if (dialog != null) {
            dialog.dismiss();
        }
        unbindService(conn);
    }

    private int judgeInput() {
        if (TextUtils.isEmpty(look_man.getText().toString()) || look_man.getText().toString().equals("请选择查看人")) {
            Toast.makeText(this, "查看人不能为空！", Toast.LENGTH_LONG).show();
            return 0;
        }

        if (TextUtils.isEmpty(vedio_address.getText().toString()) || vedio_address.getText().toString().equals("请选择巡视点")) {
            Toast.makeText(this, "巡视点不能为空！", Toast.LENGTH_LONG).show();
            return 0;
        }
        if (TextUtils.isEmpty(input_vedio_conn.getText().toString())) {
            Toast.makeText(this, "描述不能为空！", Toast.LENGTH_LONG).show();
            return 0;
        }
        return 1;
    }

    @SuppressWarnings("static-access")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 切换为竖屏
        isLandscape = !isLandscape;
        if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
            top_vedio.setVisibility(View.VISIBLE);
            ll_attribute.setVisibility(View.VISIBLE);
            ll_vedio_action.setVisibility(View.VISIBLE);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, patrol_re_video_height);
            re_video.setLayoutParams(params);
            CommonUtil.cancelFullScreen(VedioPlayActivity.this);
        }
        // 切换为横屏
        else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            CommonUtil.setFullScreen(VedioPlayActivity.this);
            top_vedio.setVisibility(View.GONE);
            ll_attribute.setVisibility(View.GONE);
            ll_vedio_action.setVisibility(View.GONE);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, width);
            re_video.setLayoutParams(params);
        }
    }

    private String nameForDate(String str)
            throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = format.parse(str);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    @SuppressWarnings("deprecation")
    private void getViewwidth() {
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        int view_width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int view_height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        re_video.measure(view_width, view_height);

        patrol_re_video_height = re_video.getMeasuredHeight();

    }

    public void saveVideo(String filePath) {
        long time;
        if (date == null) {
            time = 0;
        } else {
            time = date.getTime();
        }

        VideoBean bean = videoHelpter.selectByName(videoname);

        if (bean == null) {
            VideoBean videoBean = new VideoBean();
            videoBean.setName(videoname);
            videoBean.setSubname(vedio_address.getText().toString());
            videoBean.setSubfatoryid(subFatoryid);
            videoBean.setLooker(str_lookerid);
            videoBean.setLookername(look_man.getText().toString());
            videoBean.setCreattime(time);
            videoBean.setDesp(input_vedio_conn.getText().toString());
            videoBean.setCreateId(shareUtil.getShare().getAccountid());
            // videoBean.setIsPack(0);
            videoBean.setVideoPath(videoPath);
            File file = new File(videoPath + "/" + videoname + ".mp4");
            int mBufferSize = 1024 * 300;
            int forSize = (int) ((file.length() % mBufferSize == 0) ? file.length() / mBufferSize : (file.length() / mBufferSize + 1));

            if (forSize != 0) {
                // 根据文件名查找是否存在这个视频
                // 查找数据库里时候有该视频 没有就添加 有就更新数据
                videoHelpter.addData(videoBean);
                upVideoBean = videoBean;
            }
        } else {
            if (bean.getIsSave() == 1) {
                if (judgeInput() == 0) {
                    return;
                }
            }
            bean.setSubname(vedio_address.getText().toString());
            bean.setSubfatoryid(subFatoryid);
            bean.setLooker(str_lookerid);
            bean.setLookername(look_man.getText().toString());
            bean.setDesp(input_vedio_conn.getText().toString());
            // videoBean.setIsPack(0);
            upVideoBean = bean;
            videoHelpter.updateBean(videoname, bean);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 横竖屏切换
            if (isLandscape) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                if (!isFinish) {
                    top_vedio.setVisibility(View.VISIBLE);
                    ll_attribute.setVisibility(View.VISIBLE);
                    ll_vedio_action.setVisibility(View.VISIBLE);
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, patrol_re_video_height);
                    re_video.setLayoutParams(params);
                    CommonUtil.cancelFullScreen(VedioPlayActivity.this);
                    isFinish = true;
                } else {
                    finish();
                }
            }
        }
        return true;
    }

    private ServiceConnection conn = new ServiceConnection()
    {
        /** 获取服务对象时的操作 */
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            // 返回一个MsgService对象
            zipService = ((ZipService.ZipBinder) service).getService();
        }

        /** 无法获取到服务对象时的操作 */
        public void onServiceDisconnected(ComponentName name)
        {
            // 、 myService = null;
        }
    };

}
