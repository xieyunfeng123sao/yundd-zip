package com.vomont.yundudao.ui.patrol.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.PatrolListItem;

@SuppressLint("SimpleDateFormat")
public class PatrolListAdapter extends BaseAdapter {

	private List<PatrolListItem> mlist;

	private Context context;

	private FactoryBean factoryBean;

	private OnItemClick onItemClick;

	public PatrolListAdapter(Context context, List<PatrolListItem> mlist,
			FactoryBean factoryBean) {
		this.mlist = mlist;
		this.context = context;
		this.factoryBean = factoryBean;
	}

	@Override
	public int getCount() {
		return null != mlist ? mlist.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		PatrolListHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_patrol_list, null);
			holder = new PatrolListHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.list_send_people);
			holder.subfatory = (TextView) convertView
					.findViewById(R.id.list_patrol_subfatory);
			holder.createtime = (TextView) convertView
					.findViewById(R.id.list_patrol_time);
			holder.videodesp = (TextView) convertView
					.findViewById(R.id.list_videodesp);
			holder.videoLength = (TextView) convertView
					.findViewById(R.id.list_patrol_vediosize);
			holder.img = (ImageView) convertView
					.findViewById(R.id.list_patrol_img);
			holder.list_start_play = (ImageView) convertView
					.findViewById(R.id.list_start_play);
			convertView.setTag(holder);
		} else {
			holder = (PatrolListHolder) convertView.getTag();
		}

		holder.list_start_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onItemClick != null) {
					onItemClick.onClick(position);
				}
			}
		});

		holder.name.setText(mlist.get(position).getUploadername());
		holder.createtime.setText(new SimpleDateFormat("yyyy-MM-dd")
				.format(mlist.get(position).getUploadtime() * 1000));
		holder.videoLength.setText(intForTime(mlist.get(position)
				.getVideolength()));
		String subName = "";
		String name = "";

		if (factoryBean != null && factoryBean.getSubfactorys() != null
				&& factoryBean.getSubfactorys().size() != 0) {
			for (int i = 0; i < factoryBean.getSubfactorys().size(); i++) {
				if (factoryBean.getSubfactorys().get(i).getSubfactoryid() == mlist
						.get(position).getOwnersubfactid()) {
					subName = factoryBean.getSubfactorys().get(i)
							.getSubfactoryname();
					if (factoryBean.getFactorys() != null
							&& factoryBean.getFactorys().size() != 0) {
						for (int j = 0; j < factoryBean.getFactorys().size(); j++) {
							if (factoryBean.getSubfactorys().get(i)
									.getOwnerfactoryid() == factoryBean
									.getFactorys().get(j).getFactoryid()) {
								name = factoryBean.getFactorys().get(j)
										.getFactoryname();
							}
						}
					}
				}
			}
		}

		if (subName.equals("")) {
			holder.subfatory.setText(name);
		} else {
			if (name.equals("")) {
				holder.subfatory.setText(subName);
			} else {
				holder.subfatory.setText(name + "/" + subName);
			}
		}

//		try {
//			ImageUtils.byte2File(Base64.decode(URLDecoder.decode(
//					mlist.get(position).getImagecontent(), "utf-8"),
//					Base64.DEFAULT), VideoManager.detail_img_cash,mlist.get(position).getUploadtime()
//					 + ".jpg");
//			09-19 11:26:17.521: E/callback(9210):          "imageurl" : "http://192.168.0.187:56/6/6-e20566fac018bfe9.jpg",

		Glide.with(context).load(mlist.get(position).getImageurl()).into(holder.img);
//			Glide.with(context).load(new File(VideoManager.detail_img_cash + "/" + mlist.get(position).getUploadtime()
//                + ".jpg")).into(holder.img);
//			LoadLocalImageUtil.getInstance().displayFromSDCard(
//					VideoManager.detail_img_cash + "/" + mlist.get(position).getUploadtime()
//							+ ".jpg", holder.img);
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		} 

		if (mlist.get(position).getVideodesp() != null) {
			try {
				holder.videodesp.setText(URLDecoder.decode(mlist.get(position)
						.getVideodesp(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return convertView;
	}

	public void setOnItemAdapterClick(OnItemClick onItemClick) {
		this.onItemClick = onItemClick;
	}

	public interface OnItemClick {
		void onClick(int position);
	}

	class PatrolListHolder {
		TextView name;

		TextView subfatory;

		TextView createtime;

		ImageView img;

		ImageView list_start_play;

		TextView videodesp;

		TextView videoLength;
	}

	/**
	 * 秒转分秒
	 * 
	 * @param time
	 * @return
	 */
	private String intForTime(int time) {
		if (time < 60) {
			if (time < 10) {
				return "00" + ":0" + time;
			}
			return "00" + ":" + time;
		} else {
			String mm;
			String ss;
			int m = time / 60;
			int s = time % 60;
			if (m < 10) {
				mm = "0" + m;
			} else {
				mm = m + "";
			}
			if (s < 10) {
				ss = "0" + s;
			} else {
				ss = s + "";
			}
			return mm + ":" + ss;
		}
	}

}
