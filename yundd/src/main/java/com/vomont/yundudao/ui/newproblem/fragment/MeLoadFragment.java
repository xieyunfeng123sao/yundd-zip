package com.vomont.yundudao.ui.newproblem.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.ActionInfo;
import com.vomont.yundudao.bean.NewProblemBean;
import com.vomont.yundudao.bean.NewProblemCall;
import com.vomont.yundudao.mvpview.newproblem.INewProblemView;
import com.vomont.yundudao.presenter.newproblem.NewProlemPresenter;
import com.vomont.yundudao.ui.createproblem.ProblemHandleActivity;
import com.vomont.yundudao.ui.newproblem.adapter.SendToMeAdapter;
import com.vomont.yundudao.ui.newproblem.adapter.SendToMeAdapter.OnActionCallBack;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.view.ios.ActionSheetDialog;
import com.vomont.yundudao.view.ios.ActionSheetDialog.OnSheetItemClickListener;
import com.vomont.yundudao.view.ios.ActionSheetDialog.SheetItemColor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class MeLoadFragment extends Fragment implements INewProblemView
{
    private PullToRefreshListView problem_tome_list;
    
    private SendToMeAdapter adapter;
    
    private List<NewProblemBean> mlist;
    
    private int page = 1;
    
    private NewProlemPresenter presenter;
    
    private ShareUtil shareUtil;
    
    private int clickPosition;
    
    private ActionSheetDialog actionSheetDialog;
    
    private int action_result;
    
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_problem_tome, container, false);
        problem_tome_list = (PullToRefreshListView)view.findViewById(R.id.problem_tome_list);
        problem_tome_list.setMode(Mode.BOTH);
        problem_tome_list.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
        problem_tome_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
        problem_tome_list.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
        mlist = new ArrayList<NewProblemBean>();
        shareUtil = new ShareUtil(getActivity());
        presenter = new NewProlemPresenter(this);
        adapter = new SendToMeAdapter(getActivity());
        adapter.setData(mlist);
        problem_tome_list.setAdapter(adapter);
        problem_tome_list.postDelayed(new Runnable()
        {
            public void run()
            {
                problem_tome_list.setRefreshing();
            }
        }, 400);
        problem_tome_list.setOnRefreshListener(new OnRefreshListener2<ListView>()
        {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                page = 1;
                presenter.getProblemList(shareUtil.getShare().getUserid() + "", 0, shareUtil.getShare().getAccountid(), 0);
            }
            
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
            {
                page++;
                presenter.getProblemList(shareUtil.getShare().getUserid() + "", mlist.get(mlist.size()-1).getProblemid(), shareUtil.getShare().getAccountid(), 0);
            }
        });
        
        adapter.setOnActionListener(new OnActionCallBack()
        {
            @Override
            public void OnClick(final int position, int state, final int problemid)
            {
                actionSheetDialog = new ActionSheetDialog(getActivity()).builder();
                if (state == 3)
                {
                    actionSheetDialog.addSheetItem("已整改", SheetItemColor.Blue, new OnSheetItemClickListener()
                    {
                        @Override
                        public void onClick(int which)
                        {
                            action_result = 2;
                            startIntentActvity(3, 2, problemid, position, "已整改");
                        }
                    });
                    
                    actionSheetDialog.addSheetItem("未整改", SheetItemColor.Blue, new OnSheetItemClickListener()
                    {
                        @Override
                        public void onClick(int which)
                        {
                            action_result = 3;
                            startIntentActvity(3, 3, problemid, position, "未整改");
                        }
                    });
                }
                else if (state == 4)
                {
                    actionSheetDialog.addSheetItem("整改通过", SheetItemColor.Blue, new OnSheetItemClickListener()
                    {
                        @Override
                        public void onClick(int which)
                        {
                            action_result = 4;
                            startIntentActvity(4, 4, problemid, position, "整改通过");
                        }
                    });
                    
                    actionSheetDialog.addSheetItem("未通过", SheetItemColor.Blue, new OnSheetItemClickListener()
                    {
                        @Override
                        public void onClick(int which)
                        {
                            action_result = 5;
                            startIntentActvity(4, 5, problemid, position, "未通过");
                        }
                    });
                }
                actionSheetDialog.addSheetItem("评   论", SheetItemColor.Blue, new OnSheetItemClickListener()
                {
                    @Override
                    public void onClick(int which)
                    {
                        action_result = 0;
                        startIntentActvity(2, 0, problemid, position, "评   论");
                    }
                });
                actionSheetDialog.setCancelable(true);
                actionSheetDialog.setCanceledOnTouchOutside(true);
                actionSheetDialog.show();
            }
        });
        return view;
    }
    
    private void startIntentActvity(int action, int result, int problemid, int position, String title)
    {
        Intent intent = new Intent(getActivity(), ProblemHandleActivity.class);
        intent.putExtra("action", action);
        intent.putExtra("result", result);
        intent.putExtra("problemid", problemid);
        intent.putExtra("title", title);
        startActivityForResult(intent, 20);
        clickPosition = position;
    }
    
    @Override
    public void onStart()
    {
        super.onStart();
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 20:
                if (data != null)
                {
                    ActionInfo info = new ActionInfo();
                    int accountid = data.getIntExtra("accountid", 0);
                    String accountname = data.getStringExtra("accountname");
                    String desp = "";
                    desp = data.getStringExtra("desp");
                    info.setActiondesp(desp);
                    if (action_result != 0)
                    {
                        mlist.get(clickPosition).setProblemstatus(action_result);
                    }
                    info.setActionresult(action_result);
                    info.setActionaccountname(accountname);
                    info.setActionaccountid(accountid);
                    info.setActiontime(new Date().getTime() / 1000);
                    if (mlist.get(clickPosition).getActions() == null)
                    {
                        List<ActionInfo> list = new ArrayList<ActionInfo>();
                        list.add(info);
                        mlist.get(clickPosition).setActions(list);
                    }
                    else
                    {
                        mlist.get(clickPosition).getActions().add(info);
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    public void getSucess(String json)
    {
        Gson gson = new Gson();
        NewProblemCall call = gson.fromJson(json, NewProblemCall.class);
        if (call != null && call.getResult() == 0)
        {
            if (call.getProblems() != null && call.getProblems().size() != 0)
            {
                if (page == 1)
                {
                    mlist.clear();
                }
                mlist.addAll(call.getProblems());
            }
            adapter.notifyDataSetChanged();
        }
        problem_tome_list.onRefreshComplete();
    }
    
    @Override
    public void getError()
    {
        problem_tome_list.onRefreshComplete();
        if (getActivity() != null && !getActivity().isDestroyed())
            Toast.makeText(getActivity(), "问题获取失败，请检测网络!", Toast.LENGTH_SHORT).show();
    }
    
}
