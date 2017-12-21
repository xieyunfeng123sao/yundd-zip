package com.vomont.yundudao.ui.home.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.Message;
import com.vomont.yundudao.bean.MsgBean;
import com.vomont.yundudao.common.Con_Action;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.ui.message.MsgListActivity;
import com.vomont.yundudao.utils.HttpUtil;
import com.vomont.yundudao.utils.MySqliteHelp;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.badgeView.BadgeView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint({"InflateParams", "SimpleDateFormat"})
public class HomeFragment extends Fragment implements OnClickListener
{
    
    private View view;
    
    public static HomeFragment homeFragment;
    
    // 返回键
    private ImageView go_back;
    
    // 标题
    private TextView top_name;
    
    @SuppressWarnings("unused")
    // 问题中心 系统消息 显示图标
    private ImageView core_num, sys_num;
    
    // 系统消息
    private LinearLayout sys_msg;
    
    private LinearLayout home_problem;
    
    private LinearLayout home_baojing;
    
    private Handler mHandler;
    
    // 消息
    private MsgBean msgBean;
    
    private List<Message> mlist;
    
    private List<Message> mlist_sql;
    
    private BadgeView badge;
    
    private MySqliteHelp sHelp;
    
    // 系统消息的时间 系统消息的标题
    private TextView sys_new_time, sys_new_title;
    
    //
    // public static HomeFragment getInstence() {
    // if (homeFragment == null)
    // homeFragment = new HomeFragment();
    // return homeFragment;
    // }
    
    public HomeFragment()
    {
    }
    
    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_home, null);
        initView();
        initListener();
        initData();
        return view;
    }
    
    private void initListener()
    {
        home_problem.setOnClickListener(this);
        home_baojing.setOnClickListener(this);
    }
    
    private void initView()
    {
        go_back = (ImageView)view.findViewById(R.id.go_back);
        top_name = (TextView)view.findViewById(R.id.top_name);
        core_num = (ImageView)view.findViewById(R.id.core_num);
        sys_msg = (LinearLayout)view.findViewById(R.id.sys_msg);
        sys_num = (ImageView)view.findViewById(R.id.sys_num);
        sys_new_time = (TextView)view.findViewById(R.id.sys_new_time);
        sys_new_title = (TextView)view.findViewById(R.id.sys_new_title);
        home_problem = (LinearLayout)view.findViewById(R.id.home_problem);
        home_baojing = (LinearLayout)view.findViewById(R.id.home_baojing);
        badge = new BadgeView(getActivity(), sys_num);
        
    }
    
    private void initData()
    {
        mlist = new ArrayList<Message>();
        mlist_sql = new ArrayList<Message>();
        go_back.setVisibility(View.GONE);
        top_name.setText(R.string.radio_msg);
        sHelp = new MySqliteHelp(getActivity());
        sHelp.getWritableDatabase();
        
        // 获取数据库里的信息 显示最新一条数据在界面
        sHelp.getInfo(mlist_sql);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (mlist_sql.size() != 0)
        {
            String time = sDateFormat.format(new Date(mlist_sql.get(mlist_sql.size() - 1).getMsgpubtime() * 1000 + 0));
            sys_new_time.setText(time);
            sys_new_title.setText(mlist_sql.get(mlist_sql.size() - 1).getMsgtitle());
        }
        
        // 跳转消息列表界面
        sys_msg.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                Intent intent = new Intent();
                intent.setAction(Con_Action.MSG_ACTION);
                startActivity(intent);
                mlist.removeAll(mlist);
                badge.hide();
            }
        });
        // 读取未读信息
        sHelp.onSelectNoRead(mlist);
        getNoReadNum();
    }
    
    public interface GetSysMessage
    {
        void onEnd();
    }
    
    public void initHandler(final GetSysMessage getSysMessage)
    {
        // 从服务器获取最新的消息
        mHandler = new Handler();
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                RequestParams reParams = new RequestParams();
                reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type10);
                final ShareUtil shareUtil = new ShareUtil(getActivity());
                reParams.add(HTTP_PAMRS.msgversion, shareUtil.getVersion() + "");
                reParams.add(HTTP_PAMRS.userid, shareUtil.getShare().getUser_id() + "");
                HttpUtil.post(reParams, new TextHttpResponseHandler()
                {
                    
                    @Override
                    public void onFailure(String responseBody, Throwable error)
                    {
                        super.onFailure(responseBody, error);
                        getSysMessage.onEnd();
                    }
                    
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseBody)
                    {
                        super.onSuccess(statusCode, headers, responseBody);
                        Gson gson = new Gson();
                        Log.e("callback", responseBody);
                        try
                        {
                            msgBean = gson.fromJson(responseBody, MsgBean.class);
                        }
                        catch (Exception e)
                        {
                            
                        }
                        
                        // 获取数据成功
                        if (msgBean != null)
                            if (msgBean.getResult() == 0)
                            {
                                // 版本有更新 说明数据有更新
                                if (shareUtil.getVersion() != msgBean.getMsgversion())
                                {
                                    for (int i = 0; i < msgBean.getMessage().size(); i++)
                                    {
                                        // 获取的数据保存到数据库
                                        sHelp.onInsert(msgBean.getMessage().get(i));
                                        mlist.add(msgBean.getMessage().get(i));
                                    }
                                    shareUtil.setVersion(msgBean.getMsgversion());
                                    // 更新桌面数据 已最新的数据展示
                                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String time = sDateFormat.format(new Date(mlist.get(mlist.size() - 1).getMsgpubtime() * 1000 + 0));
                                    sys_new_time.setText(time);
                                    sys_new_title.setText(mlist.get(mlist.size() - 1).getMsgtitle());
                                    getNoReadNum();
                                }
                            }
                    }
                });
                getSysMessage.onEnd();
            }
        });
    }
    
    /**
     * 显示未读信息的数量
     */
    private void getNoReadNum()
    {
        badge.setTextSize(10);
        if (mlist.size() > 99)
        {
            badge.setText("99+");
        }
        else
        {
            badge.setText(mlist.size() + "");
        }
        if (mlist.size() > 0)
        {
            badge.show();
        }
        else
        {
            badge.hide();
        }
    }
    
    @Override
    public void onClick(View arg0)
    {
        Intent intent;
        switch (arg0.getId())
        {
            case R.id.home_problem:
                intent = new Intent(getActivity(), MsgListActivity.class);
                intent.putExtra("name", "问题消息");
                startActivity(intent);
                break;
            case R.id.home_baojing:
                intent = new Intent(getActivity(), MsgListActivity.class);
                intent.putExtra("name", "报警消息");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    
}
