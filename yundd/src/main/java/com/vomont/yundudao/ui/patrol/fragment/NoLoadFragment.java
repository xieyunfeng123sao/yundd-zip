package com.vomont.yundudao.ui.patrol.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.vomont.yundudao.R;
import com.vomont.yundudao.ui.patrol.VedioPlayActivity;
import com.vomont.yundudao.ui.patrol.adapter.NoLoadAdapter;
import com.vomont.yundudao.upload.VideoBean;
import com.vomont.yundudao.upload.VideoHelpter;
import com.vomont.yundudao.utils.ShareUtil;
import com.vomont.yundudao.utils.SortClass;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NoLoadFragment extends Fragment
{
    
    private VideoHelpter helper;
    
    private View view;
    
    private ListView list_noload;
    
    private List<VideoBean> mlist;
    
    private NoLoadAdapter adapter;
    
    private ShareUtil shareUtil;
    
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_noload, container, false);
        list_noload = (ListView)view.findViewById(R.id.list_noload);
        helper = new VideoHelpter(getActivity());
        shareUtil=new ShareUtil(getActivity());
        list_noload.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getActivity(), VedioPlayActivity.class);
                intent.putExtra("name", mlist.get(position).getName());
                intent.putExtra("subid", mlist.get(position).getSubfatoryid());
                intent.putExtra("subname", mlist.get(position).getSubname());
                intent.putExtra("looker", mlist.get(position).getLooker());
                intent.putExtra("lookername", mlist.get(position).getLookername());
                intent.putExtra("desp", mlist.get(position).getDesp());
                intent.putExtra("path", mlist.get(position).getVideoPath());
                startActivity(intent);
            }
        });
        list_noload.postDelayed(new Runnable()
        {
            public void run()
            {
                showView();
            }
        }, 100);
        return view;
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    
    @Override
    public void onStop()
    {
        super.onStop();
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
        mlist = new ArrayList<VideoBean>();
        if (helper.selectNoPack() != null)
        {
            List<VideoBean> allMlist=new ArrayList<VideoBean>();
            allMlist.addAll(helper.selectAll());
            for (int i = 0; i < allMlist.size(); i++)
            {
                if (shareUtil.getShare().getAccountid() == allMlist.get(i).getCreateId())
                {
                if ((allMlist.get(i).getVideoid() == 0)&&(allMlist.get(i).getPos()==0)&&(allMlist.get(i).getLoadstate()!=1)&&(allMlist.get(i).getLoadstate()!=3))
                {
                    mlist.add(allMlist.get(i));
                }
                }
            }
        }
        SortClass sort = new SortClass();
        Collections.sort(mlist, sort);
        if (getActivity() != null)
        {
            adapter = new NoLoadAdapter(getActivity(), mlist);
            list_noload.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        
    }
    
    public int getNoLoadNum()
    {
        return mlist.size();
    }
}
