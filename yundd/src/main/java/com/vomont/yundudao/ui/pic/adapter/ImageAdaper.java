package com.vomont.yundudao.ui.pic.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageAdaper extends PagerAdapter {

	private List<ImageView> mlist;

	public ImageAdaper(List<ImageView> mlist) {
		this.mlist = mlist;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mlist.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(mlist.get(position));
		return mlist.get(position);
	}

}
