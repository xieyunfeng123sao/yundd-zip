package com.vomont.yundudao.ui.patrol.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.PatrolDetailItem;
import com.vomont.yundudao.bean.PatrolListItem;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.bean.TypeVideo;
import com.vomont.yundudao.mvpview.patrol.IPatrolListView;
import com.vomont.yundudao.ui.patrol.PatrolDetailActivity;
import com.vomont.yundudao.ui.patrol.VedioPlayActivity;
import com.vomont.yundudao.ui.patrol.adapter.AllVideoAdapter;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoHelpter;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.utils.SortClass;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AllLoadFragment extends Fragment implements IPatrolListView
{
    
    private View view;
    
    private ListView list_allload_video;
    
    private VideoHelpter helper;
    
    private static AllLoadFragment fragment;
    
    private List<VideoBean> unUpmlist;
    
    private List<VideoBean> upmlist;
    
    private List<TypeVideo> mlist;
    
    private AllVideoAdapter adapter;
    
    private int noUpSize = 0;
    
    private FactoryBean factoryBean;
    
    private List<FactoryInfo> factoryBeans;
    
//    private ShareUtil util;
//    
//    private PatrolListPresenter presenter;
    
    private int onePosition, twoPosition;
    
    private Dialog dialog;
    
    private ShareUtil shareUtil;
    
    public static AllLoadFragment getInstence()
    {
        if (null == fragment)
            fragment = new AllLoadFragment();
        return fragment;
    }
    
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_allload, container, false);
        list_allload_video = (ListView)view.findViewById(R.id.list_allload_video);
        helper = new VideoHelpter(getActivity());
//        presenter = new PatrolListPresenter(this);
        shareUtil=new ShareUtil(getActivity());
        ACache aCache = ACache.get(getActivity());
        factoryBean = (FactoryBean)aCache.getAsObject("factoryBean");
        addFactoryInfo(factoryBean);
        list_allload_video.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (noUpSize != 0)
                {
                    // 有未上传的视频
                    if (position <= (noUpSize))
                    {
                        // 点击的是未上传的视频
                        Intent intent = new Intent(getActivity(), VedioPlayActivity.class);
                        intent.putExtra("name", mlist.get(0).getMlist().get(position - 1).getName());
                        intent.putExtra("subid", mlist.get(0).getMlist().get(position - 1).getSubfatoryid());
                        intent.putExtra("subname", mlist.get(0).getMlist().get(position - 1).getSubname());
                        intent.putExtra("looker", mlist.get(0).getMlist().get(position - 1).getLooker());
                        intent.putExtra("lookername", mlist.get(0).getMlist().get(position - 1).getLookername());
                        intent.putExtra("desp", mlist.get(0).getMlist().get(position - 1).getDesp());
                        intent.putExtra("path", mlist.get(0).getMlist().get(position - 1).getVideoPath());
                        startActivity(intent);
                    }
                    else
                    {
                        
                        onePosition = 1;
                        twoPosition = position - 2 - noUpSize;
                        
                        Intent intent = new Intent(getActivity(), VedioPlayActivity.class);
                        intent.putExtra("canclick", 1);
                        intent.putExtra("name", mlist.get(onePosition).getMlist().get(twoPosition).getName());
                        intent.putExtra("subid", mlist.get(onePosition).getMlist().get(twoPosition).getSubfatoryid());
                        intent.putExtra("subname", mlist.get(onePosition).getMlist().get(twoPosition).getSubname());
                        intent.putExtra("looker", mlist.get(onePosition).getMlist().get(twoPosition).getLooker());
                        intent.putExtra("lookername", mlist.get(onePosition).getMlist().get(twoPosition).getLookername());
                        intent.putExtra("desp", mlist.get(onePosition).getMlist().get(twoPosition).getDesp());
                        intent.putExtra("path", mlist.get(onePosition).getMlist().get(twoPosition).getVideoPath());
                        startActivity(intent);
                     
                    }
                }
                else
                {
                    onePosition = 0;
                    twoPosition = position - 1;
                    Intent intent = new Intent(getActivity(), VedioPlayActivity.class);
                    intent.putExtra("canclick", 1);
                    intent.putExtra("name", mlist.get(onePosition).getMlist().get(twoPosition).getName());
                    intent.putExtra("subid", mlist.get(onePosition).getMlist().get(twoPosition).getSubfatoryid());
                    intent.putExtra("subname", mlist.get(onePosition).getMlist().get(twoPosition).getSubname());
                    intent.putExtra("looker", mlist.get(onePosition).getMlist().get(twoPosition).getLooker());
                    intent.putExtra("lookername", mlist.get(onePosition).getMlist().get(twoPosition).getLookername());
                    intent.putExtra("desp", mlist.get(onePosition).getMlist().get(twoPosition).getDesp());
                    intent.putExtra("path", mlist.get(onePosition).getMlist().get(twoPosition).getVideoPath());
                    startActivity(intent);
                }
                
            }
        });
        list_allload_video.postDelayed(new Runnable()
        {
            public void run()
            {
                showView();
            }
        }, 100);
        
        return view;
    }
    
    @Override
    public void onResume()
    {
        showView();
        super.onResume();
    }
    
    @SuppressWarnings("unchecked")
    public void showView()
    {
        mlist = new ArrayList<TypeVideo>();
        List<VideoBean> mMlist = new ArrayList<VideoBean>();
        unUpmlist = new ArrayList<VideoBean>();
        upmlist = new ArrayList<VideoBean>();
        if (helper.selectAll() != null)
            mMlist.addAll(helper.selectAll());
        if (mMlist != null && mMlist.size() != 0)
        {
            for (int i = 0; i < mMlist.size(); i++)
            {
                if (shareUtil.getShare().getAccountid() == mMlist.get(i).getCreateId())
                {
                if (mMlist.get(i).getVideoid() == 0&&(mMlist.get(i).getPos()==0)&&(mMlist.get(i).getLoadstate()!=1)&&(mMlist.get(i).getLoadstate()!=3))
                {
                    unUpmlist.add(mMlist.get(i));
                }
                else if(mMlist.get(i).getLoadstate()==2)
                {
                    upmlist.add(mMlist.get(i));
                }
                }
            }
        }
        
        noUpSize = 0;
        if (unUpmlist != null && unUpmlist.size() != 0)
        {
            noUpSize = unUpmlist.size();
            TypeVideo novideo = new TypeVideo();
            novideo.setType("未上传");
            SortClass sort = new SortClass();
            Collections.sort(unUpmlist, sort);
            novideo.setMlist(unUpmlist);
            mlist.add(novideo);
        }
        if (upmlist != null && upmlist.size() != 0)
        {
            TypeVideo video = new TypeVideo();
            video.setType("已上传");
            SortClass sort = new SortClass();
            Collections.sort(upmlist, sort);
            video.setMlist(upmlist);
            mlist.add(video);
        }
        
        if (getActivity() != null)
        {
            adapter = new AllVideoAdapter(getActivity(), mlist);
            list_allload_video.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        
    }
    
    private void addFactoryInfo(FactoryBean factoryBean)
    {
        if (factoryBean != null)
        {
            factoryBeans = new ArrayList<FactoryInfo>();
            if (factoryBean.getFactorys() != null)
            {
                // 18551768363
                factoryBeans.addAll(factoryBean.getFactorys());
                if (factoryBean.getSubfactorys() != null)
                {
                    if (factoryBeans != null && factoryBeans.size() != 0)
                    {
                        for (int i = 0; i < factoryBeans.size(); i++)
                        {
                            List<SubFactory> sub_list = new ArrayList<SubFactory>();
                            for (int j = 0; j < factoryBean.getSubfactorys().size(); j++)
                            {
                                if (factoryBeans.get(i).getFactoryid() == factoryBean.getSubfactorys().get(j).getOwnerfactoryid())
                                {
                                    sub_list.add(factoryBean.getSubfactorys().get(j));
                                }
                            }
                            factoryBeans.get(i).setMlist(sub_list);
                        }
                    }
                }
                if (factoryBean.getDevices() != null)
                {
                    if (factoryBeans != null && factoryBeans.size() != 0)
                    {
                        for (int i = 0; i < factoryBeans.size(); i++)
                        {
                            if (factoryBeans.get(i).getMlist() != null && factoryBeans.get(i).getMlist().size() != 0)
                            {
                                
                                for (int j = 0; j < factoryBeans.get(i).getMlist().size(); j++)
                                {
                                    List<DeviceInfo> dev_list = new ArrayList<DeviceInfo>();
                                    for (int k = 0; k < factoryBean.getDevices().size(); k++)
                                    {
                                        if (factoryBeans.get(i).getMlist().get(j).getSubfactoryid() == factoryBean.getDevices().get(k).getSubfactoryid())
                                        {
                                            dev_list.add(factoryBean.getDevices().get(k));
                                        }
                                    }
                                    factoryBeans.get(i).getMlist().get(j).setMlist_device(dev_list);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void getListData(List<PatrolListItem> mlist)
    {
        
    }
    
    @Override
    public void onFail()
    {
        
    }
    
    @Override
    public void getSucess(PatrolDetailItem item)
    {
        if (item != null)
        {
            Intent intent = new Intent(getActivity(), PatrolDetailActivity.class);
            intent.putExtra("patrolitem", item);
            intent.putExtra("factoryBean", factoryBean);
            startActivity(intent);
        }
        dialog.dismiss();
    }
    
    @Override
    public void getFail()
    {
        dialog.dismiss();
    }
    
}
