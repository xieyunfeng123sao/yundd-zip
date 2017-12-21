package com.vomont.yundudao.ui.patrol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DetalBean;
import com.vomont.yundudao.bean.DetalInfo;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.PatrolListItem;
import com.vomont.yundudao.bean.PicTimeBean;
import com.vomont.yundudao.bean.PicTimeInfo;
import com.vomont.yundudao.bean.ProblemDetailBean;
import com.vomont.yundudao.bean.ProblemDetailInfo;
import com.vomont.yundudao.bean.ProblemListBean;
import com.vomont.yundudao.bean.ProblemListlInfo;
import com.vomont.yundudao.bean.ProblemTypeBean;
import com.vomont.yundudao.bean.ProblemTypeInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.mvpview.detal.IDetalView;
import com.vomont.yundudao.mvpview.problem.ICenterProblemView;
import com.vomont.yundudao.presenter.detal.DetalPresenter;
import com.vomont.yundudao.presenter.problem.ICenterProPresenter;
import com.vomont.yundudao.ui.createproblem.CenterProblemActivity;
import com.vomont.yundudao.ui.createproblem.CreateProblemActivity;
import com.vomont.yundudao.ui.createproblem.ProblemDetailActivity;
import com.vomont.yundudao.ui.factory.adapter.VedioProblemAdapter;
import com.vomont.yundudao.ui.pic.AllPicActivity;
import com.vomont.yundudao.ui.pic.PicCenterActivity;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.CommonUtil;
import com.vomont.yundudao.utils.ImageUtils;
import com.vomont.yundudao.utils.Playutil;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.video.CommonVideoView;
import com.vomont.yundudao.view.video.CommonVideoView.OnScreenListener;

@SuppressLint({"SimpleDateFormat", "HandlerLeak"})
public class PatrolDetailActivity extends Activity implements OnClickListener, ICenterProblemView, IDetalView
{
    private RelativeLayout top_relat;
    
    private LinearLayout vedio_project;
    
    private ImageView patrol_detail_back;
    
    private CommonVideoView commonVideoView;
    
    private TextView patrol_detail_name;
    
    private ImageView patrol_detail_zhuapai;
    
    private ImageView patrol_to_pic;
    
    private ImageView patrol_detail_shanpai;
    
    private TextView patrol_detail_subfatory;
    
    private TextView patrol_detail_uptime;
    
    private TextView patrol_detail_desp;
    
    private PatrolListItem item;
    
    private FactoryBean factoryBean;
    
    private RelativeLayout patrol_re_video;
    
    private LinearLayout patrol_img;
    
    private TextView patrol_empty_img;
    
    private int width, hegith, patrol_re_video_height;
    
    private int factoryId, subFactoryId;
    
    // 判断是否是横屏
    private boolean isLandscape = false;
    
    private Intent intent;
    
    private Playutil playutil;
    
    private List<FactoryInfo> mlist;
    
    private List<String> mlist_name;
    
    private ImageView patrol_problem;
    
    private ShareUtil shareUtil;
    
    private ICenterProPresenter iCenterProPresenter;
    
    private DetalPresenter detalPresenter;
    
    private Dialog dialog;
    
    private List<ProblemTypeInfo> prblem_type_mlist;
    
    private List<ProblemListlInfo> problem_list;
    
    private List<DetalInfo> man_List;
    
    private ProblemDetailInfo problemDetailInfo;
    
    private TextView patrol_empty_problem;
    
    private ListView patrol_problem_list;
    
    private int intentPostion;
    
    private int m;
    
    private List<String> nameList;
    
    private boolean isFinish = true;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initView();
        getViewwidth();
        initListener();
        initData();
    }
    
    @SuppressWarnings("deprecation")
    private void getViewwidth()
    {
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        hegith = wm.getDefaultDisplay().getHeight();
        int view_width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int view_height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        patrol_re_video.measure(view_width, view_height);
        patrol_re_video_height = patrol_re_video.getMeasuredHeight();
    }
    
    private void initData()
    {
        shareUtil = new ShareUtil(this);
        nameList = new ArrayList<String>();
        iCenterProPresenter = new ICenterProPresenter(this);
        playutil = new Playutil(this);
        item = (PatrolListItem)getIntent().getSerializableExtra("patrolitem");
        factoryBean = (FactoryBean)getIntent().getSerializableExtra("factoryBean");
        addFactoryInfo(factoryBean);
        showSubName();
        if (item.getUploadername() != null)
        {
            patrol_detail_name.setText(item.getUploadername());
        }
        patrol_detail_uptime.setText(new SimpleDateFormat("yyyy-MM-dd").format(item.getUploadtime() * 1000));
        if (item.getVideodesp() != null)
        {
            try
            {
                patrol_detail_desp.setText(URLDecoder.decode(item.getVideodesp(), "utf-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
        }
//        getProblemList();
        commonVideoView.start(item.getVideoplayurl());
        
        commonVideoView.setOnScreenListener(new OnScreenListener()
        {
            @Override
            public void startScreen()
            {
                CommonUtil.setFullScreen(PatrolDetailActivity.this);
                top_relat.setVisibility(View.GONE);
                vedio_project.setVisibility(View.GONE);
                LayoutParams params = new LayoutParams(width, hegith);
                patrol_re_video.setLayoutParams(params);
                isFinish = false;
            }
            
            @Override
            public void NoScreen()
            {
                CommonUtil.cancelFullScreen(PatrolDetailActivity.this);
                top_relat.setVisibility(View.VISIBLE);
                vedio_project.setVisibility(View.VISIBLE);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, patrol_re_video_height);
                patrol_re_video.setLayoutParams(params);
                isFinish = true;
            }
        });
        
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        addPhoneToView();
        
    }
    
    private void initListener()
    {
        patrol_problem.setOnClickListener(this);
        patrol_to_pic.setOnClickListener(this);
        patrol_detail_back.setOnClickListener(this);
        patrol_detail_zhuapai.setOnClickListener(this);
        patrol_detail_shanpai.setOnClickListener(this);
        
        patrol_problem_list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                detalPresenter = new DetalPresenter(PatrolDetailActivity.this);
                detalPresenter.getDetal(shareUtil.getShare().getUser_id() + "");
                dialog = ProgressDialog.createLoadingDialog(PatrolDetailActivity.this, "");
                dialog.show();
                intentPostion = position;
            }
        });
    }
    
    private void initView()
    {
        patrol_problem_list = (ListView)findViewById(R.id.patrol_problem_list);
        patrol_empty_problem = (TextView)findViewById(R.id.patrol_empty_problem);
        patrol_re_video = (RelativeLayout)findViewById(R.id.patrol_re_video);
        top_relat = (RelativeLayout)findViewById(R.id.top_relat);
        vedio_project = (LinearLayout)findViewById(R.id.vedio_project);
        patrol_detail_back = (ImageView)findViewById(R.id.patrol_detail_back);
        patrol_detail_name = (TextView)findViewById(R.id.patrol_detail_name);
        patrol_detail_zhuapai = (ImageView)findViewById(R.id.patrol_detail_zhuapai);
        patrol_detail_shanpai = (ImageView)findViewById(R.id.patrol_detail_shanpai);
        patrol_detail_subfatory = (TextView)findViewById(R.id.patrol_detail_subfatory);
        patrol_detail_uptime = (TextView)findViewById(R.id.patrol_detail_uptime);
        patrol_detail_desp = (TextView)findViewById(R.id.patrol_detail_desp);
        patrol_to_pic = (ImageView)findViewById(R.id.patrol_to_pic);
        patrol_img = (LinearLayout)findViewById(R.id.patrol_img);
        patrol_empty_img = (TextView)findViewById(R.id.patrol_empty_img);
        patrol_problem = (ImageView)findViewById(R.id.patrol_problem);
        commonVideoView = (CommonVideoView)findViewById(R.id.commonVideoView);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.patrol_detail_back:
                finish();
                break;
            case R.id.patrol_detail_zhuapai:
                dialog = ProgressDialog.createLoadingDialog(PatrolDetailActivity.this, "");
                dialog.show();
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                        String date = format.format(new Date());
                        final String name = date + "-" + factoryId + "-" + subFactoryId + "-" + 0;
                        final String path = VideoManager.yundd_phone + shareUtil.getShare().getNum();
                        final Bitmap bitmap = commonVideoView.getPicFromVideo();
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if (bitmap != null)
                                {
                                    commonVideoView.saveBitmap(path, name, bitmap);
                                    bitmap.recycle();
                                    Toast.makeText(PatrolDetailActivity.this, "抓图成功！", Toast.LENGTH_SHORT).show();
                                    addPhoneToView();
                                }
                                else
                                {
                                    Toast.makeText(PatrolDetailActivity.this, "抓图失败！", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                }).start();
                
                break;
            case R.id.patrol_to_pic:
                // 所有图片
                
                intent = new Intent(PatrolDetailActivity.this, AllPicActivity.class);
                intent.putExtra("factoryBean", (Serializable)mlist);
                startActivity(intent);
                break;
            case R.id.patrol_problem:
                Intent intent = new Intent(this, CenterProblemActivity.class);
                intent.putExtra("factoryBean", (Serializable)mlist);
                startActivity(intent);
                break;
            case R.id.patrol_detail_shanpai:
                dialog = ProgressDialog.createLoadingDialog(PatrolDetailActivity.this, "");
                dialog.show();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHHmmss");
                final String date1 = format1.format(new Date());
                final String name1 = date1 + "-" + factoryId + "-" + subFactoryId + "-" + 0;
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        final Bitmap bitmap1 = commonVideoView.getPicFromVideo();
                        runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if (bitmap1 != null)
                                {
                                    commonVideoView.saveBitmap(VideoManager.yundd_phone + shareUtil.getShare().getNum(), name1, bitmap1);
                                    bitmap1.recycle();
                                    Toast.makeText(PatrolDetailActivity.this, "抓图成功！", Toast.LENGTH_LONG).show();
                                    addPhoneToView();
                                    String string = new String(name1);
                                    String[] info = string.split("-");
                                    PicTimeInfo picTimeInfo = new PicTimeInfo();
                                    picTimeInfo.setDeviceid(Integer.parseInt(info[3]));
                                    picTimeInfo.setSubfactoryid(Integer.parseInt(info[2]));
                                    picTimeInfo.setFactoryid(Integer.parseInt(info[1]));
                                    picTimeInfo.setPath(VideoManager.yundd_phone + shareUtil.getShare().getNum());
                                    picTimeInfo.setTime(date1);
                                    picTimeInfo.setName(name1 + ".jpg");
                                    Intent intent1 = new Intent(PatrolDetailActivity.this, CreateProblemActivity.class);
                                    intent1.putExtra("picinfo", picTimeInfo);
                                    startActivity(intent1);
                                }
                                else
                                {
                                    Toast.makeText(PatrolDetailActivity.this, "抓图失败！", Toast.LENGTH_LONG).show();
                                }
                                dialog.dismiss();
                            }
                        });
                    };
                }).start();
                
                break;
            default:
                break;
        }
    }
    
    private void showSubName()
    {
        String subName = "";
        String name = "";
        subFactoryId = item.getOwnersubfactid();
        
        if (factoryBean != null && factoryBean.getSubfactorys() != null && factoryBean.getSubfactorys().size() != 0)
        {
            for (int i = 0; i < factoryBean.getSubfactorys().size(); i++)
            {
                if (factoryBean.getSubfactorys().get(i).getSubfactoryid() == item.getOwnersubfactid())
                {
                    subName = factoryBean.getSubfactorys().get(i).getSubfactoryname();
                    if (factoryBean.getFactorys() != null && factoryBean.getFactorys().size() != 0)
                    {
                        for (int j = 0; j < factoryBean.getFactorys().size(); j++)
                        {
                            if (factoryBean.getSubfactorys().get(i).getOwnerfactoryid() == factoryBean.getFactorys().get(j).getFactoryid())
                            {
                                factoryId = factoryBean.getSubfactorys().get(i).getOwnerfactoryid();
                                name = factoryBean.getFactorys().get(j).getFactoryname();
                            }
                        }
                    }
                }
            }
        }
        
        if (subName.equals(""))
        {
            patrol_detail_subfatory.setText(name);
        }
        else
        {
            if (name.equals(""))
            {
                patrol_detail_subfatory.setText(subName);
            }
            else
            {
                patrol_detail_subfatory.setText(name + "/" + subName);
            }
        }
    }
    
//    private void getProblemList()
//    {
//        iCenterProPresenter.getProblemType(shareUtil.getShare().getUser_id() + "");
//    }
//    
    @SuppressWarnings("static-access")
    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // 切换为竖屏
        isLandscape = !isLandscape;
        if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_PORTRAIT)
        {
            CommonUtil.cancelFullScreen(PatrolDetailActivity.this);
            top_relat.setVisibility(View.VISIBLE);
            vedio_project.setVisibility(View.VISIBLE);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, patrol_re_video_height);
            patrol_re_video.setLayoutParams(params);
            isFinish = true;
            
        }
        // 切换为横屏
        else if (newConfig.orientation == this.getResources().getConfiguration().ORIENTATION_LANDSCAPE)
        {
            CommonUtil.setFullScreen(PatrolDetailActivity.this);
            top_relat.setVisibility(View.GONE);
            vedio_project.setVisibility(View.GONE);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, width);
            patrol_re_video.setLayoutParams(params);
            isFinish = false;
        }
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
                    if (mlist != null && mlist.size() != 0)
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
                }
                if (factoryBean.getDevices() != null)
                {
                    if (mlist != null && mlist.size() != 0)
                    {
                        for (int i = 0; i < mlist.size(); i++)
                        {
                            if (mlist.get(i).getMlist() != null && mlist.get(i).getMlist().size() != 0)
                            {
                                
                                for (int j = 0; j < mlist.get(i).getMlist().size(); j++)
                                {
                                    List<DeviceInfo> dev_list = new ArrayList<DeviceInfo>();
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
        }
    }
    
    /**
     * 显示图片列表
     */
    public void addPhoneToView()
    {
        if (playutil.getImageName() != null && playutil.getImageName().size() != 0)
        {
            mlist_name = playutil.getImageName();
            Collections.reverse(mlist_name);
            patrol_empty_img.setVisibility(View.GONE);
        }
        else
        {
            patrol_empty_img.setVisibility(View.GONE);
            mlist_name = new ArrayList<String>();
            return;
        }
        patrol_img.removeAllViews();
        List<ImageView> list_img = new ArrayList<ImageView>();
        if (mlist_name.size() != 0)
        {
            int view_width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int view_height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            patrol_img.measure(view_width, view_height);
            for (int i = 0; i < 4; i++)
            {
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((width - 60) / 4, width / 4);
                if (i != 3)
                {
                    layoutParams.setMargins(15, 0, 0, 0);
                }
                else
                {
                    layoutParams.setMargins(10, 0, 0, 15);
                }
                imageView.setScaleType(ScaleType.FIT_XY);
                imageView.setLayoutParams(layoutParams);
                list_img.add(imageView);
                patrol_img.addView(imageView);
            }
            patrol_img.getChildAt(0).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    backPicInfo(0);
                }
            });
            patrol_img.getChildAt(1).setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    backPicInfo(1);
                }
            });
            patrol_img.getChildAt(2).setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    backPicInfo(2);
                }
            });
            patrol_img.getChildAt(3).setOnClickListener(new OnClickListener()
            {
                
                @Override
                public void onClick(View v)
                {
                    backPicInfo(3);
                }
            });
            if (mlist_name.size() > 0)
            {
                for (int i = 0; i < mlist_name.size(); i++)
                {
                    Glide.with(PatrolDetailActivity.this).load(new File(playutil.getPath() + File.separator + mlist_name.get(mlist_name.size() - 1))).into(list_img.get(0));
                    // LoadLocalImageUtil.getInstance().displayFromSDCard(playutil.getPath() + File.separator +
                    // mlist_name.get(mlist_name.size() - 1), list_img.get(0));
                    if ((mlist_name.size() - 2) >= 0)
                    {
                        Glide.with(PatrolDetailActivity.this).load(new File(playutil.getPath() + File.separator + mlist_name.get(mlist_name.size() - 2))).into(list_img.get(1));
                        // LoadLocalImageUtil.getInstance().displayFromSDCard(playutil.getPath() + File.separator +
                        // mlist_name.get(mlist_name.size() - 2), list_img.get(1));
                    }
                    if ((mlist_name.size() - 3) >= 0)
                    {
                        Glide.with(PatrolDetailActivity.this).load(new File(playutil.getPath() + File.separator + mlist_name.get(mlist_name.size() - 3))).into(list_img.get(2));
                        // LoadLocalImageUtil.getInstance().displayFromSDCard(playutil.getPath() + File.separator +
                        // mlist_name.get(mlist_name.size() - 3), list_img.get(2));
                    }
                    if ((mlist_name.size() - 4) >= 0)
                    {
                        Glide.with(PatrolDetailActivity.this).load(new File(playutil.getPath() + File.separator + mlist_name.get(mlist_name.size() - 4))).into(list_img.get(3));
                        // LoadLocalImageUtil.getInstance().displayFromSDCard(playutil.getPath() + File.separator +
                        // mlist_name.get(mlist_name.size() - 4), list_img.get(3));
                    }
                }
            }
        }
        
    }
    
    private void backPicInfo(int index)
    {
        // if (canClick)
        if (playutil.getImageName().size() >= (index + 1))
        {
            String name = playutil.getImageName().get(index);
            String newname = name.substring(0, name.length() - 4);
            String string = new String(newname);
            String[] info = string.split("-");
            PicTimeInfo picTimeInfo = new PicTimeInfo();
            picTimeInfo.setDeviceid(Integer.parseInt(info[3]));
            picTimeInfo.setSubfactoryid(Integer.parseInt(info[2]));
            picTimeInfo.setFactoryid(Integer.parseInt(info[1]));
            picTimeInfo.setPath(playutil.getPath());
            picTimeInfo.setTime(info[0]);
            picTimeInfo.setName(name);
            PicTimeBean timeBean = new PicTimeBean();
            List<PicTimeInfo> info_mlist = new ArrayList<PicTimeInfo>();
            info_mlist.add(picTimeInfo);
            timeBean.setPaths(info_mlist);
            Intent intent = new Intent(this, PicCenterActivity.class);
            intent.putExtra("picinfo", timeBean);
            intent.putExtra("postion", 0);
            startActivity(intent);
        }
    }
    
    /**
     * 加载本地图片
     * 
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url)
    {
        try
        {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void getDetalman(DetalBean detalBean)
    {
        man_List = new ArrayList<DetalInfo>();
        if (detalBean != null && detalBean.getAccounts() != null)
        {
            man_List.addAll(detalBean.getAccounts());
        }
        else
        {
            Toast.makeText(this, "详情获取失败！", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
        iCenterProPresenter.getProblemDetail(shareUtil.getShare().getUser_id() + "", problem_list.get(intentPostion).getProblemid() + "");
    }
    
    @Override
    public void getFailed()
    {
        Toast.makeText(this, "详情获取失败！", Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }
    
    @Override
    public void getList(ProblemListBean bean)
    {
        problem_list = new ArrayList<ProblemListlInfo>();
        if (bean != null && bean.getProblems() != null)
        {
            patrol_empty_problem.setVisibility(View.GONE);
            patrol_problem_list.setVisibility(View.VISIBLE);
            problem_list.addAll(bean.getProblems());
            VedioProblemAdapter adapter = new VedioProblemAdapter(this, problem_list, prblem_type_mlist);
            patrol_problem_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(patrol_problem_list);
        }
        else
        {
            patrol_problem_list.setVisibility(View.GONE);
            patrol_empty_problem.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void getType(ProblemTypeBean result)
    {
        prblem_type_mlist = new ArrayList<ProblemTypeInfo>();
        if (result != null && result.getProblemtypes() != null)
        {
            prblem_type_mlist.addAll(result.getProblemtypes());
        }
        iCenterProPresenter.getProblemDetailList(shareUtil.getShare().getUser_id() + "", "1");
    }
    
    @Override
    public void getProbleDtail(ProblemDetailBean problemBean)
    {
        if (problemBean != null && problemBean.getResult() == 0)
        {
            problemDetailInfo = problemBean.getProblem();
            for (int i = 0; i < problemDetailInfo.getImagecontents().size(); i++)
            {
                asyncloadImage(problemDetailInfo.getImagecontents().get(i).getImagecontent(), i);
            }
        
        }
        dialog.dismiss();
    }
    
    public void setListViewHeightBasedOnChildren(ListView listView)
    {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++)
        {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (isFinish)
            {
                finish();
            }
            else
            {
                if (isLandscape)
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                else
                {
                    CommonUtil.cancelFullScreen(PatrolDetailActivity.this);
                    top_relat.setVisibility(View.VISIBLE);
                    vedio_project.setVisibility(View.VISIBLE);
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, patrol_re_video_height);
                    patrol_re_video.setLayoutParams(params);
                }
                isFinish = true;
            }
        }
        return true;
    }
    
    private synchronized void asyncloadImage(final String imagecontent, final int position)
    {
        final Handler mHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                if (msg.what == 1)
                {
                    m++;
                    String name = (String)msg.obj;
                    nameList.add(name);
                    if (m == problemDetailInfo.getImagecontents().size())
                    {
                        Intent intent = new Intent(PatrolDetailActivity.this, ProblemDetailActivity.class);
                        problemDetailInfo.setImagecontents(null);
                        problemDetailInfo.setImgeNames(nameList);
                        intent.putExtra("problemDetailInfo", (Serializable)problemDetailInfo);
                        intent.putExtra("delman", (Serializable)man_List);
                        intent.putExtra("factorylist", (Serializable)mlist);
                        startActivity(intent);
                        m = 0;
                        nameList = new ArrayList<String>();
                        dialog.dismiss();
                    }
                }
            }
        };
        // 子线程，开启子线程去下载或者去缓存目录找图片，并且返回图片在缓存目录的地址
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String mPath = VideoManager.detail_img_cash + "/" + name + position + ".jpg";
                    File file = new File(mPath);
                    // 这个URI是图片下载到本地后的缓存目录中的URI
                    if (imagecontent != null && !file.exists())
                    {
                        ImageUtils.byte2File(Base64.decode(URLDecoder.decode(imagecontent, "utf-8"), Base64.DEFAULT), VideoManager.detail_img_cash, name + position + ".jpg");
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = name + position;
                        mHandler.sendMessage(msg);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
    
}
