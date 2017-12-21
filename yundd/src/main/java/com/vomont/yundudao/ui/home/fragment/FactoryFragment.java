package com.vomont.yundudao.ui.home.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.common.Con_Action;
import com.vomont.yundudao.mvpview.fatory.IFactoryView;
import com.vomont.yundudao.presenter.factory.FactoryPresenter;
import com.vomont.yundudao.ui.home.fragment.adapter.FactoryAdapter;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.ShareUtil;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class FactoryFragment extends Fragment implements IFactoryView {

	private ListView list_factory;

	private PullToRefreshScrollView scrollview;

	private TextView top_name;

	private ImageView go_back;

	private FactoryBean factoryBean;

	private List<FactoryInfo> mlist;

	private TextView factory_num, factory_num_about, monitoring_num,
			monitoring_num_about;

	private FactoryAdapter adapter;
	
	private FactoryPresenter factoryPresenter;
	
	private ShareUtil shareUtil;

	public FactoryFragment() {
//		this.factoryBean = factoryBean;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_factory, null);
		list_factory = (ListView) view.findViewById(R.id.list_factory);
		top_name = (TextView) view.findViewById(R.id.top_name);
		go_back = (ImageView) view.findViewById(R.id.go_back);
		scrollview = (PullToRefreshScrollView) view
				.findViewById(R.id.scrollview);
		factory_num = (TextView) view.findViewById(R.id.factory_num);
		factory_num_about = (TextView) view
				.findViewById(R.id.factory_num_about);
		monitoring_num = (TextView) view.findViewById(R.id.monitoring_num);
		monitoring_num_about = (TextView) view
				.findViewById(R.id.monitoring_num_about);
		list_factory.setFocusable(false);
		top_name.setText(R.string.radio_factory);
		go_back.setVisibility(View.GONE);
//		scrollview.setMode(Mode.PULL_FROM_START);
		scrollview.setMode(Mode.BOTH);
		scrollview.getLoadingLayoutProxy(false, true).setPullLabel(getString(R.string.pull_to_load));
		scrollview.getLoadingLayoutProxy(false, true).setRefreshingLabel(getString(R.string.loading));
		scrollview.getLoadingLayoutProxy(false, true).setReleaseLabel(getString(R.string.release_to_load));
		
		
		
		
		factoryPresenter=new FactoryPresenter(this);
		shareUtil=new ShareUtil(getActivity());
		scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				scrollview.onRefreshComplete();
			    factoryPresenter.getFactory(shareUtil.getShare().getUser_id()+"");
			}
		});

		list_factory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.setAction(Con_Action.FACTORY_ACTION);
				Bundle bundle = new Bundle();
				bundle.putSerializable("factory", mlist.get(arg2));
				intent.putExtra("factoryBean", (Serializable) mlist);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		ACache aCache=ACache.get(getActivity());
		factoryBean=(FactoryBean) aCache.getAsObject("factoryBean");
		if(factoryBean!=null)
		{
		    getFactoryInfo(factoryBean);
		}
		else
		{
		    scrollview.postDelayed(new Runnable()
	        {
	            public void run()
	            {
	                scrollview.setRefreshing();
	            }
	        }, 200);
		}
		return view;
	}

	public void getFactoryInfo(FactoryBean factoryBean) {
		if(factoryBean!=null&&factoryBean.getFactorys()!=null)
		{
			addFactoryInfo(factoryBean);
			initData(factoryBean);
		}
	}

	public void initData(FactoryBean factoryBean) {
		factory_num.setText(factoryBean.getFactorys().size() + "");
		int can_look = 0;
		int online = 0;
		int all_dev = 0;
		for (int i = 0; i < mlist.size(); i++) {
			if(mlist.get(i).getMlist()!=null&&mlist.get(i).getMlist().size()!=0)
			{
				for (int j = 0; j < mlist.get(i).getMlist().size(); j++) {
					if (mlist.get(i).getMlist().get(j).getMlist_device().size() != 0) {
						can_look++;
						for (int h = 0; h < mlist.get(i).getMlist().get(j)
								.getMlist_device().size(); h++) {
							all_dev++;
							if (mlist.get(i).getMlist().get(j).getMlist_device()
									.get(h).getOnline() == 1) {
								online++;
							}
						}
					}
				}
			}
		}
		if(factoryBean.getSubfactorys()!=null&&factoryBean.getSubfactorys().size()!=0)
		{
			factory_num_about.setText("可视" + can_look * 100
					/ factoryBean.getSubfactorys().size() + "%");
		}
		else
		{
			factory_num_about.setText("可视" +0 + "%");
		}
		if(all_dev==0)
		{
			monitoring_num_about.setText("在线" + 0 + "%");
		}
		else
		{
			monitoring_num_about.setText("在线" + online * 100 / all_dev + "%");
		}
		monitoring_num.setText(all_dev + "");
	}

	private void addFactoryInfo(FactoryBean factoryBean) {
		if (factoryBean != null) {
			mlist = new ArrayList<FactoryInfo>();
			if (factoryBean.getFactorys() != null) {
				// 18551768363
				mlist.addAll(factoryBean.getFactorys());
				if (factoryBean.getSubfactorys() != null) {
					if (mlist != null && mlist.size() != 0) {
						for (int i = 0; i < mlist.size(); i++) {
							List<SubFactory> sub_list = new ArrayList<SubFactory>();
							for (int j = 0; j < factoryBean.getSubfactorys()
									.size(); j++) {
								if (mlist.get(i).getFactoryid() == factoryBean
										.getSubfactorys().get(j)
										.getOwnerfactoryid()) {
									sub_list.add(factoryBean.getSubfactorys()
											.get(j));
								}
							}
							mlist.get(i).setMlist(sub_list);
						}
					}
				}
				if (factoryBean.getDevices() != null) {
					if (mlist != null && mlist.size() != 0) {
						for (int i = 0; i < mlist.size(); i++) {
							if (mlist.get(i).getMlist() != null
									&& mlist.get(i).getMlist().size() != 0) {

								for (int j = 0; j < mlist.get(i).getMlist()
										.size(); j++) {
									List<DeviceInfo> dev_list = new ArrayList<DeviceInfo>();
									for (int k = 0; k < factoryBean
											.getDevices().size(); k++) {
										if (mlist.get(i).getMlist().get(j)
												.getSubfactoryid() == factoryBean
												.getDevices().get(k)
												.getSubfactoryid()) {
											dev_list.add(factoryBean
													.getDevices().get(k));
										}
									}
									mlist.get(i).getMlist().get(j)
											.setMlist_device(dev_list);
								}
							}
						}
					}
				}
			}
			if(mlist!=null&&mlist.size()!=0)
			{
				adapter = new FactoryAdapter(mlist, getActivity());
				list_factory.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(list_factory);
				scrollview.onRefreshComplete();
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		scrollview.scrollTo(0, 0);
		super.onResume();
	}

	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

    @Override
    public void onFail()
    {
        scrollview.onRefreshComplete();
    }

    @Override
    public void getFactory(FactoryBean factoryBean)
    {
        ACache aCache = ACache.get(getActivity());
        aCache.put("factoryBean", factoryBean);
        getFactoryInfo(factoryBean);
        scrollview.onRefreshComplete();
    }

}
