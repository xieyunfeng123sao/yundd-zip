package com.vomont.yundudao.ui.createproblem;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.ProblemDetailInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.ui.createproblem.adapter.ImageShowAdapter;
import com.vomont.yundudao.ui.createproblem.adapter.ImageShowAdapter.OnCallBack;
import com.vomont.yundudao.ui.createproblem.adapter.ProblemActionAdapter;
import com.vomont.yundudao.upload.VideoManager;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.DataUtils;
import com.vomont.yundudao.utils.EmptyUtil;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.NoScrollGridView.NoScrollGridView;

/**
 * 每个人心中都有一杆秤，每一杆秤的标准各不相同，所以结果也就不懂；虽然会有点缺斤少两，但只要不过分，咱们还是愉快的
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SimpleDateFormat")
public class ProblemDetailActivity extends Activity implements OnClickListener
{
    private TextView top_name;
    
    private ImageView go_back;
    
    private NoScrollGridView problem_img;
    
    private TextView missdate_txt;
    
    private TextView problem_details_factoryname;
    
    private TextView problem_detairule;
    
    private TextView problem_dealman;
    
    private TextView problem_describeman;
    
    private TextView problem_copyman;
    
    private ListView problem_speak_list;
    
    private ShareUtil shareUtil;
    
    private ProblemDetailInfo problemDetailInfo;
    
    private Button problem_detail_speak;
    
    private List<FactoryInfo> factory_mlist;
    
    // 跳转的状态
    // 2评论 3 整改 4复查
    
    private int action_state = 0;
    
    private List<ImagePath> mlistPath;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_details);
        initView();
        initData();
        initListener();
    }
    
    /**
     * 初始化view
     */
    private void initView()
    {
        top_name = (TextView)findViewById(R.id.top_name);
        go_back = (ImageView)findViewById(R.id.go_back);
        problem_img = (NoScrollGridView)findViewById(R.id.problem_img);
        missdate_txt = (TextView)findViewById(R.id.missdate_txt);
        problem_details_factoryname = (TextView)findViewById(R.id.problem_details_factoryname);
        problem_detairule = (TextView)findViewById(R.id.problem_detairule);
        problem_dealman = (TextView)findViewById(R.id.problem_dealman);
        problem_describeman = (TextView)findViewById(R.id.problem_describeman);
        problem_copyman = (TextView)findViewById(R.id.problem_copyman);
        problem_speak_list = (ListView)findViewById(R.id.problem_speak_list);
        problem_detail_speak = (Button)findViewById(R.id.problem_detail_speak);
    }
    
    /**
     * 初始化数据
     */
    @SuppressWarnings("unchecked")
    private void initData()
    {
        problem_speak_list.setFocusable(false);
        Intent intent = getIntent();
        problemDetailInfo = (ProblemDetailInfo)intent.getSerializableExtra("problemDetailInfo");
        factory_mlist = (List<FactoryInfo>)intent.getSerializableExtra("factorylist");
        top_name.setText(R.string.problemdetail);
        shareUtil = new ShareUtil(this);
        showData();
        judgeAction();
    }
    
    /**
     * 判断可操作的动作
     */
    private void judgeAction()
    {
        if (problemDetailInfo != null)
        {
            if (shareUtil.getShare().getAccountid() == problemDetailInfo.getCreatorid())
            {
                // 创建人
                if (problemDetailInfo.getProblemstatus() == 2 || problemDetailInfo.getProblemstatus() == 3)
                {
                    // 复查 跳转到复查界面
                    action_state = 4;
                    problem_detail_speak.setBackgroundResource(R.color.fucha);
                    problem_detail_speak.setText("复查");
                    problem_detail_speak.setEnabled(true);
                }
                else if (problemDetailInfo.getProblemstatus() == 4)
                {
                    // 整改通过 没有跳转的需求
                    problem_detail_speak.setBackgroundResource(R.color.actionsheet_gray);
                    problem_detail_speak.setText("整改通过");
                    problem_detail_speak.setEnabled(false);
                }
                else
                {
                    // 整改状态 创建人只能评论
                    action_state = 2;
                    problem_detail_speak.setBackgroundResource(R.color.pinglun);
                    problem_detail_speak.setText("评论");
                    problem_detail_speak.setEnabled(true);
                }
            }
            else if (shareUtil.getShare().getAccountid() == problemDetailInfo.getOwnerid())
            {
                // 处理人
                if (problemDetailInfo.getProblemstatus() == 1 || problemDetailInfo.getProblemstatus() == 5)
                {
                    // 整改 跳转到整改界面
                    action_state = 3;
                    problem_detail_speak.setBackgroundResource(R.color.zhenggai);
                    problem_detail_speak.setText("整改");
                }
                else if (problemDetailInfo.getProblemstatus() == 4)
                {
                    // 整改通过 无需操作
                    problem_detail_speak.setBackgroundResource(R.color.actionsheet_gray);
                    problem_detail_speak.setText("整改通过");
                    problem_detail_speak.setEnabled(false);
                }
                else
                {
                    // 带复查的阶段 可以评论
                    action_state = 2;
                    problem_detail_speak.setBackgroundResource(R.color.pinglun);
                    problem_detail_speak.setText("评论");
                    problem_detail_speak.setEnabled(true);
                }
            }
            else
            {
                // 抄送人
                if (problemDetailInfo.getProblemstatus() == 4)
                {
                    // 整改完成 无需操作
                    problem_detail_speak.setBackgroundResource(R.color.actionsheet_gray);
                    problem_detail_speak.setText("整改通过");
                    problem_detail_speak.setEnabled(false);
                }
                else
                {
                    // 抄送人只能评论
                    action_state = 2;
                    problem_detail_speak.setBackgroundResource(R.color.pinglun);
                    problem_detail_speak.setText("评论");
                    problem_detail_speak.setEnabled(true);
                }
            }
        }
    }
    
    /**
     * 初始化监听
     */
    private void initListener()
    {
        go_back.setOnClickListener(this);
        problem_detail_speak.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.go_back:
                finish();
                break;
            case R.id.problem_detail_speak:
                // 挑转到操作也页面
                Intent intent = new Intent(this, ProblemHandleActivity.class);
                intent.putExtra("action", action_state);
                intent.putExtra("problemid", problemDetailInfo.getProblemid());
                intent.putExtra("mlistPath", (Serializable)problemDetailInfo.getImgeNames());
                startActivityForResult(intent, 10);
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
        {
            problemDetailInfo = (ProblemDetailInfo)data.getSerializableExtra("problemDetailInfo");
            showData();
            judgeAction();
        }
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        File file = new File(VideoManager.detail_img_cash);
        File[] files = file.listFiles();
        if (files != null && files.length != 0)
        {
            for (int i = 0; i < files.length; i++)
            {
                files[i].delete();
            }
            file.delete();
        }
    }
    
    private void showData()
    {
        if (problemDetailInfo != null)
        {
            ImageShowAdapter imageShowAdapter = new ImageShowAdapter(ProblemDetailActivity.this, problemDetailInfo.getImgeNames());
            problem_img.setAdapter(imageShowAdapter);
            imageShowAdapter.notifyDataSetChanged();
            ACache aCache = ACache.get(ProblemDetailActivity.this);
            aCache.put("imagecontents", (Serializable)problemDetailInfo.getImagecontents());
            mlistPath = new ArrayList<ImagePath>();
            imageShowAdapter.setOnCallBackListener(new OnCallBack()
            {
                @Override
                public void getPath(String path, int position)
                {
                    ImagePath path2 = new ImagePath();
                    path2.path = path;
                    path2.position = position;
                    mlistPath.add(path2);
                }
            });
            problem_img.setOnItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(ProblemDetailActivity.this, LookDetailImageActivity.class);
                    intent.putExtra("position", position);
                    List<String> mlist = new ArrayList<String>();
                    for (int i = 0; i < mlistPath.size(); i++)
                    {
                        for (int j = 0; j < mlistPath.size(); j++)
                        {
                            if (i == mlistPath.get(j).position)
                            {
                                if (!mlist.contains(mlistPath.get(j).path))
                                {
                                    mlist.add(mlistPath.get(j).path);
                                }
                            }
                        }
                    }
                    Log.e("ceshi", problemDetailInfo.getImgeNames().get(position));
                    intent.putExtra("mlistPath", (Serializable)problemDetailInfo.getImgeNames());
                    startActivity(intent);
                }
            });
            
            // 日期
            long time = new Date().getTime() / 1000 - 24 * 60 * 60;
            int old = (int)(time - problemDetailInfo.getDonetime());
            if (old <= 0)
            {
                missdate_txt.setTextColor(getResources().getColor(R.color.zhenggai));
                missdate_txt.setText(DataUtils.longToString(problemDetailInfo.getDonetime() * 1000, "yyyy年MM月dd日") + "(未过期)");
            }
            else
            {
                missdate_txt.setText(DataUtils.longToString(problemDetailInfo.getDonetime() * 1000, "yyyy年MM月dd日") + "(已过期)");
                missdate_txt.setTextColor(getResources().getColor(R.color.actionsheet_red));
            }
            // 监控点
            if (factory_mlist != null && factory_mlist.size() != 0)
            {
                for (int i = 0; i < factory_mlist.size(); i++)
                {
                    if (factory_mlist.get(i).getMlist() != null && factory_mlist.get(i).getMlist().size() != 0)
                    {
                        for (int j = 0; j < factory_mlist.get(i).getMlist().size(); j++)
                        {
                            if (factory_mlist.get(i).getMlist().get(j).getMlist_device() != null && factory_mlist.get(i).getMlist().get(j).getMlist_device().size() != 0)
                            {
                                for (int m = 0; m < factory_mlist.get(i).getMlist().get(j).getMlist_device().size(); m++)
                                {
                                    if (factory_mlist.get(i).getMlist().get(j).getMlist_device().get(m).getDeviceid() == problemDetailInfo.getRelateddeviceid())
                                    {
                                        problem_details_factoryname.setText(factory_mlist.get(i).getMlist().get(j).getMlist_device().get(m).getDevicename() + "(定点监控)");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (TextUtils.isEmpty(problem_details_factoryname.getText().toString()))
            {
                FactoryBean factoryBean = (FactoryBean)aCache.getAsObject("factoryBean");
                if (factoryBean != null && factoryBean.getSubfactorys() != null && factoryBean.getSubfactorys().size() != 0)
                {
                    for (SubFactory subFactory : factoryBean.getSubfactorys())
                    {
                        if (subFactory.getSubfactoryid() == problemDetailInfo.getRelatedsubfactoryid())
                        {
                            problem_details_factoryname.setText(subFactory.getSubfactoryname() + "(现场巡视)");
                        }
                    }
                }
            }
            
            // 标签
            problem_detairule.setText(problemDetailInfo.getProblemtypename());
            // 处理人
            if (!TextUtils.isEmpty(problemDetailInfo.getOwnername()))
            {
                problem_dealman.setText(problemDetailInfo.getOwnername());
            }
            // 抄送人
            if (!EmptyUtil.listIsEmpty(problemDetailInfo.getCcusers()))
            {
                String copyname = "";
                for (int j = 0; j < problemDetailInfo.getCcusers().size(); j++)
                {
                    if (j == 0)
                    {
                        copyname = problemDetailInfo.getCcusers().get(j).getAccountname();
                    }
                    else
                    {
                        copyname = copyname + "," + problemDetailInfo.getCcusers().get(j).getAccountname();
                    }
                }
                problem_copyman.setText(copyname);
            }
            
            // 描述
            if (!TextUtils.isEmpty(problemDetailInfo.getProblemdesp()))
            {
                try
                {
                    if (!URLDecoder.decode(problemDetailInfo.getProblemdesp(), "utf-8").contains("null"))
                    {
                        problem_describeman.setText(URLDecoder.decode(problemDetailInfo.getProblemdesp(), "utf-8"));
                    }
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
            }
            ProblemActionAdapter adapter = new ProblemActionAdapter(this, problemDetailInfo.getActions());
            problem_speak_list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(problem_speak_list);
        }
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
    
    class ImagePath
    {
        String path;
        
        int position;
    }
    
}
