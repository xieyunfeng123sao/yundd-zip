package com.vomont.yundudao.view.expandView;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryInfo;

public class ExpandTabView extends LinearLayout {

	private PopupWindow mPopupWindow;
	private Button selector_item;

	private ViewBottom viewBottom;

	private boolean isShow;

	// private AllSelectorListener expandViewItemListener;

	private List<FactoryInfo> mList;

	public ExpandTabView(Context context) {
		super(context);
		init(context);
	}

	public ExpandTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mList = new ArrayList<FactoryInfo>();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.expandview, this);
		selector_item = (Button) findViewById(R.id.selector_item);

//		viewBottom = new ViewBottom(context);
		mPopupWindow = new PopupWindow(viewBottom, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		selector_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isShow) {
					if (mList != null) {
						viewBottom.setVisibility(View.VISIBLE);
						mPopupWindow.showAsDropDown(v);
					}

				} else {
					mPopupWindow.dismiss();
					viewBottom.setVisibility(View.GONE);
				}
				isShow = !isShow;
			}
		});
	}

	/**
	 * @param mlist
	 * @param twoMlist
	 */
	public void setOneViewList(List<FactoryInfo> mlist) {
		mList.clear();
		mList.addAll(mlist);
		viewBottom.setOneList(mlist);
	}

}
