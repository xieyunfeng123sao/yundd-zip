package com.vomont.yundudao.ui.createproblem;

import java.io.File;
import java.io.Serializable;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DetalBean;
import com.vomont.yundudao.bean.DetalInfo;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.ProblemDetailBean;
import com.vomont.yundudao.bean.ProblemDetailInfo;
import com.vomont.yundudao.bean.ProblemListBean;
import com.vomont.yundudao.bean.ProblemListlInfo;
import com.vomont.yundudao.bean.ProblemTypeBean;
import com.vomont.yundudao.bean.ProblemTypeInfo;
import com.vomont.yundudao.mvpview.detal.IDetalView;
import com.vomont.yundudao.mvpview.problem.ICenterProblemView;
import com.vomont.yundudao.presenter.detal.DetalPresenter;
import com.vomont.yundudao.presenter.problem.ICenterProPresenter;
import com.vomont.yundudao.ui.createproblem.adapter.ProblemListAdapter;
import com.vomont.yundudao.ui.createproblem.adapter.ProblemStatusAdapter;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.ImageUtils;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.ShareUtil;

@SuppressLint({"InflateParams", "HandlerLeak"})
public class CenterProblemActivity extends Activity implements ICenterProblemView, IDetalView, OnClickListener
{
    private PullToRefreshListView problem_list;
    
    private ImageView problem_list_goback;
    
    private TextView problem_list_top_name;
    
    private ShareUtil shareUtil;
    
    private ICenterProPresenter iCenterProPresenter;
    
    private List<ProblemTypeInfo> mlist;
    
    private List<ProblemListlInfo> mlist_detail;
    
    private ListView problem_type_list;
    
    private Dialog dialog;
    
    private ProblemListAdapter adapter;
    
    private ProblemStatusAdapter status_adapter;
    
    private List<FactoryInfo> factory_mlist;
    
    private List<String> problem_status;
    
    private List<ProblemListlInfo> list_info;
    
    private int page = 1;
    
    private DetalPresenter detalPresenter;
    
    private int intentPostion;
    
    private List<DetalInfo> man_List;
    
    private ProblemDetailInfo problemDetailInfo;
    
    private int nowShowPosition;
    
    private ImageView problem_list_add;
    
    private ImageView show_view;
    
    private LinearLayout problem_list_top_ll;
    
    private ImageView empty_problem_list;
    
    private int m = 0;
    
    private List<String> nameList;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_list);
        initView();
        initData();
        initListener();
    }
    
    /**
     * 监听
     */
    private void initListener()
    {
        problem_list_goback.setOnClickListener(this);
        problem_list_add.setOnClickListener(this);
        problem_list_top_ll.setOnClickListener(this);
        problem_list.setOnRefreshListener(new OnRefreshListener2<ListView>()
        {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                page = 1;
                iCenterProPresenter.getProblemDetailList(shareUtil.getShare().getUser_id() + "", page + "");
            }
            
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                page++;
                iCenterProPresenter.getProblemDetailList(shareUtil.getShare().getUser_id() + "", page + "");
            }
        });
        problem_list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                dialog = ProgressDialog.createLoadingDialog(CenterProblemActivity.this, "加载中");
                dialog.show();
                detalPresenter = new DetalPresenter(CenterProblemActivity.this);
                detalPresenter.getDetal(shareUtil.getShare().getUser_id() + "");
                intentPostion = position;
            }
        });
    }
    
    /**
     * 初始化view
     */
    private void initView()
    {
        problem_list = (PullToRefreshListView)findViewById(R.id.problem_list);
        problem_list_goback = (ImageView)findViewById(R.id.problem_list_goback);
        problem_list_top_name = (TextView)findViewById(R.id.problem_list_top_name);
        problem_list_add = (ImageView)findViewById(R.id.problem_list_add);
        problem_list_top_ll = (LinearLayout)findViewById(R.id.problem_list_top_ll);
        show_view = (ImageView)findViewById(R.id.show_view);
        empty_problem_list = (ImageView)findViewById(R.id.empty_problem_list);
    }
    
    @SuppressWarnings("unchecked")
    private void initData()
    {
        problem_list.setMode(Mode.BOTH);
        problem_list.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
        problem_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        problem_list.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
        problem_list_top_name.setText("全部");
        shareUtil = new ShareUtil(this);
        iCenterProPresenter = new ICenterProPresenter(this);
        mlist = new ArrayList<ProblemTypeInfo>();
        mlist_detail = new ArrayList<ProblemListlInfo>();
        list_info = new ArrayList<ProblemListlInfo>();
        factory_mlist = (List<FactoryInfo>)getIntent().getSerializableExtra("factoryBean");
        problem_status = new ArrayList<String>();
        problem_status.add("全部");
        problem_status.add("待整改");
        problem_status.add("待复查");
        problem_status.add("整改合格 ");
        nameList = new ArrayList<String>();
        problem_list.postDelayed(new Runnable()
        {
            public void run()
            {
                problem_list.setRefreshing();
            }
        }, 200);
        adapter = new ProblemListAdapter(CenterProblemActivity.this, list_info, mlist, factory_mlist);
        problem_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void getType(ProblemTypeBean result)
    {
        
        if (result != null && result.getProblemtypes() != null)
        {
            mlist.addAll(result.getProblemtypes());
        }
        iCenterProPresenter.getProblemDetailList(shareUtil.getShare().getUser_id() + "", "1");
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
    }
    
    @Override
    protected void onRestart()
    {
        super.onRestart();
        ACache aCache = ACache.get(this);
        String name = aCache.getAsString("problemsend");
        if (name != null && name.equals("1"))
        {
            problem_list.postDelayed(new Runnable()
            {
                public void run()
                {
                    problem_list.setRefreshing();
                }
            }, 200);
            aCache.put("problemsend", "0");
        }
    }
    
    @Override
    public void getList(ProblemListBean bean)
    {
        if (bean != null && bean.getProblems() != null)
        {
            if (page == 1)
            {
                if (mlist_detail.size() >= 20)
                {
                    // 老数据和新数据进行比对 如果有更新就添加 没有就算了
                    // 目前只考虑到20条数据，接口的设计 只支持这种情况
                    
                    int addSize = -1;
                    if (bean.getProblems() != null && bean.getProblems().size() != 0)
                    {
                        for (int i = 0; i < bean.getProblems().size(); i++)
                        {
                            for (int j = 0; j < mlist_detail.size(); j++)
                            {
                                if (bean.getProblems().get(i).getProblemid() == mlist_detail.get(j).getProblemid())
                                {
                                    mlist_detail.get(j).setProblemstatus(bean.getProblems().get(i).getProblemstatus());
                                    addSize = i;
                                    break;
                                }
                            }
                            if (addSize == i)
                                break;
                        }
                    }
                    if (addSize != -1 && addSize != 0)
                    {
                        for (int i = addSize - 1; i >= 0; i--)
                        {
                            mlist_detail.add(0, bean.getProblems().get(i));
                            list_info.add(0, bean.getProblems().get(i));
                        }
                    }
                }
                else
                {
                    
                    if (mlist_detail.size() != 0)
                    {
                        int addSize = -1;
                        if (bean.getProblems() != null && bean.getProblems().size() != 0)
                        {
                            for (int i = 0; i < bean.getProblems().size(); i++)
                            {
                                for (int j = 0; j < mlist_detail.size(); j++)
                                {
                                    if (bean.getProblems().get(i).getProblemid() == mlist_detail.get(j).getProblemid())
                                    {
                                        mlist_detail.get(j).setProblemstatus(bean.getProblems().get(i).getProblemstatus());
                                        addSize = i;
                                        break;
                                    }
                                }
                                if (addSize == i)
                                    break;
                            }
                        }
                        if (addSize != -1 && addSize != 0)
                        {
                            for (int i = addSize - 1; i >= 0; i--)
                            {
                                mlist_detail.add(0, bean.getProblems().get(i));
                                list_info.add(0, bean.getProblems().get(i));
                            }
                        }
                    }
                    else
                    {
                        mlist_detail.addAll(bean.getProblems());
                        list_info.addAll(mlist_detail);
                    }
                    
                }
                
            }
            else
            {
//                pullShowPistion = mlist_detail.size() - 1;
                for (int i = 0; i < bean.getProblems().size(); i++)
                {
                    mlist_detail.add(bean.getProblems().get(i));
                }
                list_info.clear();
                for (int i = 0; i < mlist_detail.size(); i++)
                {
                    list_info.add(mlist_detail.get(i));
                }
            }
            list_info.clear();
            showType();
        }
        problem_list.onRefreshComplete();
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.problem_list_goback:
                finish();
                break;
            case R.id.problem_list_add:
                Intent intent = new Intent(CenterProblemActivity.this, CreateProblemActivity.class);
                startActivity(intent);
                break;
            case R.id.problem_list_top_ll:
                showPoPuWindow();
                show_view.setPivotX(show_view.getWidth() / 2);
                show_view.setPivotY(show_view.getHeight() / 2);// 支点在图片中心
                show_view.setRotation(180);
                break;
            default:
                break;
        }
    }
    
    private void showPoPuWindow()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_problemtype_popu, null);
        problem_type_list = (ListView)view.findViewById(R.id.problem_type_list);
        
        status_adapter = new ProblemStatusAdapter(this, problem_status);
        problem_type_list.setAdapter(status_adapter);
        status_adapter.notifyDataSetChanged();
        
        final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        
        popupWindow.setTouchable(true);
        
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionsheet_top_normal));// 设置背景
        popupWindow.setOutsideTouchable(true);
        
        popupWindow.setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                show_view.setPivotX(show_view.getWidth() / 2);
                show_view.setPivotY(show_view.getHeight() / 2);// 支点在图片中心
                show_view.setRotation(0);
            }
        });
        popupWindow.showAsDropDown(problem_list_top_name);
        
        problem_type_list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                nowShowPosition = position;
                list_info.clear();
                showType();
                popupWindow.dismiss();
            }
        });
    }
    
    public void showType()
    {
        for (int i = 0; i < mlist_detail.size(); i++)
        {
            if (nowShowPosition == 0)
            {
                list_info.add(mlist_detail.get(i));
                problem_list_top_name.setText("全部");
            }
            else if (nowShowPosition == 1)
            {
                if (mlist_detail.get(i).getProblemstatus() == 1)
                {
                    list_info.add(mlist_detail.get(i));
                }
                problem_list_top_name.setText("待整改");
            }
            else if (nowShowPosition == 2)
            {
                problem_list_top_name.setText("待复查");
                if (mlist_detail.get(i).getProblemstatus() == 2 || mlist_detail.get(i).getProblemstatus() == 3 || mlist_detail.get(i).getProblemstatus() == 5)
                {
                    list_info.add(mlist_detail.get(i));
                }
            }
            else
            {
                problem_list_top_name.setText("整改合格");
                if (mlist_detail.get(i).getProblemstatus() == 4)
                {
                    list_info.add(mlist_detail.get(i));
                }
            }
        }
        if (list_info == null || list_info.size() == 0)
        {
            empty_problem_list.setVisibility(View.VISIBLE);
        }
        else
        {
            empty_problem_list.setVisibility(View.GONE);
            
        }
        adapter.notifyDataSetChanged();
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
        iCenterProPresenter.getProblemDetail(shareUtil.getShare().getUser_id() + "", list_info.get(intentPostion - 1).getProblemid() + "");
    }
    
    @Override
    public void getFailed()
    {
        Toast.makeText(this, "详情获取失败！", Toast.LENGTH_LONG).show();
        dialog.dismiss();
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
        else
        {
            Toast.makeText(this, "详情获取失败！", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
        
    }
    
    @SuppressLint("SimpleDateFormat") 
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
                        Intent intent = new Intent(CenterProblemActivity.this, ProblemDetailActivity.class);
                        problemDetailInfo.setImagecontents(null);
                        problemDetailInfo.setImgeNames(nameList);
                        intent.putExtra("problemDetailInfo", (Serializable)problemDetailInfo);
                        intent.putExtra("delman", (Serializable)man_List);
                        intent.putExtra("factorylist", (Serializable)factory_mlist);
                        startActivity(intent);
                        m = 0;
                        nameList = new ArrayList<String>();
                        dialog.dismiss();
                    }
                }
            }
        };
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String name = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                    String mPath = VideoManager.detail_img_cash + "/" + name +position + ".jpg";
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
