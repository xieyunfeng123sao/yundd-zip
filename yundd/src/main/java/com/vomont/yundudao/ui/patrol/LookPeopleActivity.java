package com.vomont.yundudao.ui.patrol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.DetalBean;
import com.vomont.yundudao.bean.DetalInfo;
import com.vomont.yundudao.mvpview.detal.IDetalView;
import com.vomont.yundudao.presenter.detal.DetalPresenter;
import com.vomont.yundudao.ui.patrol.adapter.CanLookAdapter;
import com.vomont.yundudao.ui.patrol.adapter.CanLookAdapter.OnCheckClickListener;
import com.vomont.yundudao.utils.ShareUtil;

public class LookPeopleActivity extends Activity implements OnClickListener,
		IDetalView {

	private ImageView look_people_back;
	private TextView look_people_sure;
	private CheckBox all_people_canlook;
	private CheckBox same_people_canlook;
	private ListView list_can_look;
	private List<DetalInfo> mlist;
	private DetalPresenter detalPresenter;

	private ShareUtil shareUtil;

	private CanLookAdapter adapter;

	private List<DetalInfo> list_position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look_people);
		initView();
		initData();
		initListener();
	}

	private void initListener() {
		look_people_back.setOnClickListener(this);
		look_people_sure.setOnClickListener(this);
		all_people_canlook
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							list_can_look.setVisibility(View.GONE);
							same_people_canlook.setChecked(false);
						} else {
							list_can_look.setVisibility(View.VISIBLE);
							same_people_canlook.setChecked(true);
						}

					}
				});
		same_people_canlook
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							list_can_look.setVisibility(View.VISIBLE);
							all_people_canlook.setChecked(false);
						} else {
							list_can_look.setVisibility(View.GONE);
							all_people_canlook.setChecked(true);
						}
					}
				});
	}

	private void initData() {
		list_position = new ArrayList<DetalInfo>();
		shareUtil = new ShareUtil(this);
		detalPresenter = new DetalPresenter(this);
		detalPresenter.getDetal(shareUtil.getShare().getUser_id() + "");
		mlist = new ArrayList<DetalInfo>();
		list_can_look.setVisibility(View.GONE);
		all_people_canlook.setChecked(true);
	}

	private void initView() {
		look_people_back = (ImageView) findViewById(R.id.look_people_back);
		look_people_sure = (TextView) findViewById(R.id.look_people_sure);
		all_people_canlook = (CheckBox) findViewById(R.id.all_people_canlook);
		same_people_canlook = (CheckBox) findViewById(R.id.same_people_canlook);
		list_can_look = (ListView) findViewById(R.id.list_can_look);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.look_people_back:
			finish();
			break;
		case R.id.look_people_sure:
			Intent intent = getIntent();
			if (!all_people_canlook.isChecked()) {
				if (list_position.size()!=0&&list_position.size() < mlist.size()) {
					intent.putExtra("canlook", (Serializable) list_position);
					setResult(RESULT_OK, intent);
					finish();
				}
				else if(list_position.size() == mlist.size())
				{
					intent.putExtra("allcanlook", 0);
					setResult(RESULT_OK, intent);
					finish();
				}
				else
				{
					
				}
			}
			else
			{
				intent.putExtra("allcanlook", 0);
				setResult(RESULT_OK, intent);
				finish();
			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public void getDetalman(DetalBean detalBean) {
		if (detalBean != null && detalBean.getAccounts() != null) {
			mlist.addAll(detalBean.getAccounts());
			for (int i = 0; i < mlist.size(); i++) {
				if (shareUtil.getShare().getAccountid() == mlist.get(i).getId()) {
					mlist.remove(i);
				}
			}
			adapter = new CanLookAdapter(this, mlist);
			list_can_look.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			adapter.setCheckListener(new OnCheckClickListener() {

				@Override
				public void noChecked(int position) {
					if (list_position.contains(mlist.get(position))) {
						list_position.remove(mlist.get(position));
					}
				}

				@Override
				public void hasChcked(int position) {
					list_position.add(mlist.get(position));
				}
			});
		}
	}

	@Override
	public void getFailed() {
	}

}
