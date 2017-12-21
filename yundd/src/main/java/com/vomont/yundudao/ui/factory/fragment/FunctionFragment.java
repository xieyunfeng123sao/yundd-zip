package com.vomont.yundudao.ui.factory.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import com.vomont.yundudao.R;
import com.vomont.yundudao.utils.Playutil;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("InflateParams")
public class FunctionFragment extends Fragment implements OnClickListener {
	public static FunctionFragment fragment;
	// 图片显示的ui 抓拍
	private LinearLayout phone_img, vedio_phone;
	// 封装的播放相关的逻辑
	private Playutil playutil;
	// 图片的路径
	private List<String> mlist_name;

	private View view;

	private int factoryid, subfactoryid,deviceid;

	public static FunctionFragment getInstence() {
		if (null == fragment)
			fragment = new FunctionFragment();
		return fragment;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_basic_function, null);
		phone_img = (LinearLayout) view.findViewById(R.id.phone_img);
		vedio_phone = (LinearLayout) view.findViewById(R.id.vedio_phone);

		vedio_phone.setOnClickListener(this);
		return view;
	}

	public void getPlay(Playutil playutil, int factoryid, int subfactoryid,int deviceid) {
		this.playutil = playutil;
		this.factoryid = factoryid;
		this.subfactoryid = subfactoryid;
		this.deviceid=deviceid;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vedio_phone:
			 playutil.saveSnapshot(factoryid,subfactoryid,deviceid);
			addPhoneToView();
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
//		view.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//
//				addPhoneToView();
//			}
//		}, 300);
	}

	public void addPhoneToView() {
		if (playutil.getImageName() != null) {
			mlist_name = playutil.getImageName();
		} else {
			mlist_name = new ArrayList<String>();
		}
		List<ImageView> list_img = new ArrayList<ImageView>();
		if(mlist_name.size()!=0)
		{
			for (int i = 0; i < 4; i++) {
				ImageView imageView = new ImageView(getActivity());
				LayoutParams layoutParams = new LayoutParams(
						(phone_img.getWidth() - 50) / 4, phone_img.getWidth() / 4);
				if (i != 3) {
					layoutParams.setMargins(10, 0, 0, 0);
				} else {
					layoutParams.setMargins(10, 0, 0, 10);
				}
				imageView.setLayoutParams(layoutParams);

				list_img.add(imageView);
				phone_img.addView(imageView);
			}
			if (mlist_name.size() > 0) {
				for (int i = 0; i < mlist_name.size(); i++) {
					list_img.get(0).setImageBitmap(
							getLoacalBitmap(playutil.getPath() + File.separator
									+ mlist_name.get(mlist_name.size() - 1)));
					if ((mlist_name.size() - 2) >= 0) {

						list_img.get(1).setImageBitmap(
								getLoacalBitmap(playutil.getPath() + File.separator
										+ mlist_name.get(mlist_name.size() - 2)));
					}
					if ((mlist_name.size() - 3) >= 0) {
						list_img.get(2).setImageBitmap(
								getLoacalBitmap(playutil.getPath() + File.separator
										+ mlist_name.get(mlist_name.size() - 3)));
					}
					if ((mlist_name.size() - 4) >= 0) {
						list_img.get(3).setImageBitmap(
								getLoacalBitmap(playutil.getPath() + File.separator
										+ mlist_name.get(mlist_name.size() - 4)));
					}
				}
			}
		}
	

	}

	/**
	 * 加载本地图片
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
