package com.vomont.yundudao.view.expandView;

import java.util.ArrayList;
import java.util.List;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DeviceInfo;
import com.vomont.yundudao.bean.SubFactory;
import com.vomont.yundudao.bean.FactoryInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class ViewBottom extends LinearLayout {

	private Context mContext;

	private int displayWidth, displayHeight;

	private OneAdapter adapter;

	private TwoAdapter twoAadpter;

	private ThreeAdapter threeAdapter;

	private ListView one_list, two_list, three_list;

	private List<FactoryInfo> mlist;

	private List<SubFactory> twolist;

	private List<DeviceInfo> threelist;

	private OnItemClicListener onItemClicListener;

	private int OnePostion, TwoPostion, ThreePostion;

	public ViewBottom(Context context, List<FactoryInfo> mlist) {
		super(context);

		this.mlist = mlist;
		this.mContext = context;
		init();

		// initHandler();
	}

	public ViewBottom(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	/**
	 * 
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	private void init() {
		displayWidth = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay().getWidth();
		displayHeight = ((Activity) mContext).getWindowManager()
				.getDefaultDisplay().getHeight();

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.linearlayout_bottom, this);

		one_list = (ListView) findViewById(R.id.one_list);
		two_list = (ListView) findViewById(R.id.two_list);
		three_list = (ListView) findViewById(R.id.three_list);

		LayoutParams one_layput = new LayoutParams(displayWidth / 3,
				displayHeight / 3);
		three_list.setLayoutParams(one_layput);
		two_list.setLayoutParams(one_layput);
		one_list.setLayoutParams(one_layput);
		one_list.setOnItemClickListener(myOneListviewListener);
		two_list.setOnItemClickListener(myTwoListviewListener);
		three_list.setOnItemClickListener(myThreeListviewListener);

		initData();
	}

	private void initData() {
		FactoryInfo subfactoryInfo = new FactoryInfo();
		subfactoryInfo.setFactoryname("全部");
		mlist.add(0, subfactoryInfo);
		adapter = new OneAdapter(mlist, mContext);
		one_list.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	/**
	 * @param oneList
	 *            一级页面的数据
	 */
	public void setOneList(List<FactoryInfo> mlistdata) {

	}

	/**
	 * 获取二级菜单的数据
	 * 
	 * @param twoListdata
	 */

	public void setTwoList(List<SubFactory> twoListdata) {
		SubFactory subFactory = new SubFactory();
		subFactory.setSubfactoryname("全部");
		twoListdata.add(0, subFactory);
		twoAadpter = new TwoAdapter(twoListdata, mContext);
		two_list.setAdapter(twoAadpter);
	}

	/**
	 * 获取三级菜单的 数据
	 * 
	 * @param threeListData
	 */
	public void setThreeList(List<DeviceInfo> threeListData) {
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setDevicename("全部");
		threeListData.add(0, deviceInfo);
		threeAdapter = new ThreeAdapter(threeListData, mContext);
		three_list.setAdapter(threeAdapter);
	}

	private OnItemClickListener myOneListviewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				final int position, long id) {
			adapter.setPosition(position);
			adapter.notifyDataSetChanged();

			if (position == 0) {
				two_list.setVisibility(View.GONE);
				onItemClicListener.lastItem();
			} else {
				twolist = new ArrayList<SubFactory>();
				if (mlist.get(position).getMlist() != null) {
					twolist.addAll(mlist.get(position).getMlist());
				}
				SubFactory subFactory = new SubFactory();
				subFactory.setSubfactoryname("全部");
				twolist.add(0, subFactory);
				twoAadpter = new TwoAdapter(twolist, mContext);
				two_list.setAdapter(twoAadpter);
				twoAadpter.notifyDataSetChanged();
				two_list.setVisibility(View.VISIBLE);
			}
			three_list.setVisibility(View.GONE);
			OnePostion = position;
			TwoPostion=0;
			ThreePostion=0;
			onItemClicListener.getItem(OnePostion, TwoPostion, ThreePostion);
		}
	};

	private OnItemClickListener myTwoListviewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			twoAadpter.setPosition(position);
			twoAadpter.notifyDataSetChanged();
			if (position == 0) {
				three_list.setVisibility(View.GONE);
				two_list.setVisibility(View.GONE);
				onItemClicListener.lastItem();
			} else {
				threelist = new ArrayList<DeviceInfo>();
				if (twolist.get(position).getMlist_device() != null) {
					threelist.addAll(twolist.get(position).getMlist_device());
				}
				DeviceInfo deviceInfo = new DeviceInfo();
				deviceInfo.setDevicename("全部");
				threelist.add(0, deviceInfo);
				threeAdapter = new ThreeAdapter(threelist, mContext);
				three_list.setAdapter(threeAdapter);
				threeAdapter.notifyDataSetChanged();
				threeAdapter.setPosition(0);
				threeAdapter.notifyDataSetChanged();
				two_list.setVisibility(View.VISIBLE);
				three_list.setVisibility(View.VISIBLE);
			}
			TwoPostion = position;
			onItemClicListener.getItem(OnePostion, TwoPostion, ThreePostion);
		}
	};

	private OnItemClickListener myThreeListviewListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			threeAdapter.setPosition(position);
			threeAdapter.notifyDataSetChanged();
			two_list.setVisibility(View.GONE);
			three_list.setVisibility(View.GONE);
			ThreePostion = position;
			onItemClicListener.getItem(OnePostion, TwoPostion, ThreePostion);
			onItemClicListener.lastItem();
		}
	};

	public interface OnItemClicListener {
		void getItem(int onePostion, int twoPostion, int threePostion);
		void lastItem();
	}

	public void setOnitemClickListener(OnItemClicListener onItemClicListener) {
		this.onItemClicListener = onItemClicListener;
	}

}
