package com.vomont.yundudao.ui.createproblem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.TagBean;
import com.vomont.yundudao.bean.TagInfo;
import com.vomont.yundudao.mvpview.tag.ITagView;
import com.vomont.yundudao.presenter.tag.TagPresenter;
import com.vomont.yundudao.ui.createproblem.adapter.TagAdapter;
import com.vomont.yundudao.ui.createproblem.adapter.TagItemAdapter;
import com.vomont.yundudao.utils.ACache;
import com.vomont.yundudao.utils.ProgressDialog;
import com.vomont.yundudao.utils.ShareUtil;

public class SelectTagActivity extends Activity implements OnClickListener,
		ITagView {

	private ImageView go_back;

	private TextView top_name;

	private TextView jump;

	private ListView tag_type;

	private ListView tag_detail;

	private TagAdapter tagAdapter;

	private TagPresenter tagPresenter;
	private ShareUtil shareUtil;

	private List<TagInfo> mlist;

	private TagItemAdapter tagItemAdapter;

	private List<TagInfo> now_detail_list;

	private Dialog dialog;
	
	private int selectedPosition=-1;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_tag);
		initView();
		initData();
		initListener();
	}

	private void initData() {
		shareUtil = new ShareUtil(this);
		tagPresenter = new TagPresenter(this);
		tagPresenter.getTag(shareUtil.getShare().getUser_id() + "");
		mlist = new ArrayList<TagInfo>();
		now_detail_list = new ArrayList<TagInfo>();
		jump.setVisibility(View.VISIBLE);
		jump.setText("确定");
		top_name.setText("选择标签");
		dialog = ProgressDialog.createLoadingDialog(this, "加载中");
		dialog.show();
	}

	private void initView() {
		go_back = (ImageView) findViewById(R.id.go_back);
		top_name = (TextView) findViewById(R.id.top_name);
		jump = (TextView) findViewById(R.id.jump);
		tag_type = (ListView) findViewById(R.id.tag_type);
		tag_detail = (ListView) findViewById(R.id.tag_detail);
	}

	private void initListener() {
		go_back.setOnClickListener(this);
		jump.setOnClickListener(this);
		tag_type.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    selectedPosition=-1;
				tagAdapter.setPosition(position);
				tagAdapter.notifyDataSetChanged();
				now_detail_list.clear();
				now_detail_list.addAll(mlist.get(position).getMlist());
				tagItemAdapter = new TagItemAdapter(SelectTagActivity.this,
						mlist.get(position).getMlist());
				tag_detail.setAdapter(tagItemAdapter);
				tagItemAdapter.notifyDataSetChanged();
			}
		});

		tag_detail.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			    tagItemAdapter.setPosition(position);
			    tagItemAdapter.notifyDataSetChanged();
			    selectedPosition=position;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go_back:
			finish();
			break;
		case R.id.jump:
		    if(selectedPosition!=-1)
		    {
		        Intent intent = getIntent();
		        intent.putExtra("tag", now_detail_list.get(selectedPosition));
		        setResult(RESULT_OK, intent);
		        finish(); 
		    }
		    else
		    {
		        Toast.makeText(SelectTagActivity.this, "请选择标签", Toast.LENGTH_LONG).show();
		    }
      
			break;
		default:
			break;
		}

	}

	@Override
	public void getTag(TagBean tagBean) {

		if (tagBean!=null&&tagBean.getProblemtypes()!= null) {
			for (int i = 0; i < tagBean.getProblemtypes().size(); i++) {
				if (tagBean.getProblemtypes().get(i).getOwnertypeid() == 0) {
					mlist.add(tagBean.getProblemtypes().get(i));
				}
			}
			for (int i = 0; i < mlist.size(); i++) {
				List<TagInfo> item = new ArrayList<TagInfo>();
				for (int j = 0; j < tagBean.getProblemtypes().size(); j++) {

					if (mlist.get(i).getTypeid() == tagBean.getProblemtypes()
							.get(j).getOwnertypeid()) {
						item.add(tagBean.getProblemtypes().get(j));
					}
				}
				mlist.get(i).setMlist(item);
			}
		     ACache aCache=ACache.get(this);
		        aCache.put("tag", (Serializable)mlist);
		        tagAdapter = new TagAdapter(this, mlist);
		        tag_type.setAdapter(tagAdapter);
		        tagAdapter.notifyDataSetChanged();
		        now_detail_list.addAll(mlist.get(0).getMlist());
		        tagItemAdapter = new TagItemAdapter(this, mlist.get(0).getMlist());
		        tag_detail.setAdapter(tagItemAdapter);
		        tagItemAdapter.notifyDataSetChanged();
		}
		 dialog.dismiss();
	}
}
