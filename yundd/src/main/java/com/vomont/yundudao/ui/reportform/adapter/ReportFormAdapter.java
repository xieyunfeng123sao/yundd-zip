package com.vomont.yundudao.ui.reportform.adapter;

import java.util.List;

import com.vomont.fileloadsdk.ACache;
import com.vomont.yundudao.R;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.bean.FactoryInfo;
import com.vomont.yundudao.bean.FormFactory;
import com.vomont.yundudao.bean.SubFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReportFormAdapter extends BaseAdapter {

	private Context context;

	private List<FormFactory> mlist;

	public ReportFormAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<FormFactory> mlist) {
		this.mlist = mlist;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_reportform, null);
			holder = new Holder();
			holder.form_item_factoryname = (TextView) convertView
					.findViewById(R.id.form_item_factoryname);
			holder.form_item_no_change_num = (TextView) convertView
					.findViewById(R.id.form_item_no_change_num);
			holder.form_item_no_look_num = (TextView) convertView
					.findViewById(R.id.form_item_no_look_num);
			holder.form_item_changed_num = (TextView) convertView
					.findViewById(R.id.form_item_changed_num);
			holder.form_item_result_num = (TextView) convertView
					.findViewById(R.id.form_item_result_num);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		ACache aCache = ACache.get(context);
		FactoryBean factoryBean = (FactoryBean) aCache
				.getAsObject("factoryBean");
		if (factoryBean!=null&&factoryBean.getSubfactorys() != null) {
			SubFactory sub = null;
			for (SubFactory subFactory : factoryBean.getSubfactorys()) {
				if (subFactory.getSubfactoryid() == mlist.get(position)
						.getSubfactoryid()) {
					sub = subFactory;
				}
			}
			if (sub != null) {
				holder.form_item_factoryname.setText(sub.getSubfactoryname()
						+ "");
				if (factoryBean.getFactorys() != null) {
					FactoryInfo fac = null;
					for (FactoryInfo factoryInfo : factoryBean.getFactorys()) {
						if (factoryInfo.getFactoryid() == sub
								.getOwnerfactoryid()) {
							fac = factoryInfo;
						}
					}
					if (fac != null) {
						holder.form_item_factoryname.setText(fac.getFactoryname()+" / "+sub.getSubfactoryname()
								+ "");
					} else {
						holder.form_item_factoryname.setText(sub.getSubfactoryname());
					}
				} else {
					holder.form_item_factoryname.setText(sub.getSubfactoryname());
				}

			} else {
				holder.form_item_factoryname.setText("");
			}

		} else {
			holder.form_item_factoryname.setText("");
		}
		// holder.form_item_factoryname.setText(mlist.get(position).getSubfactoryid()
		// + "");
		holder.form_item_no_change_num.setText(mlist.get(position)
				.getStatus_1() + "");
		holder.form_item_no_look_num.setText(mlist.get(position).getStatus_2()
				+ "");
		holder.form_item_changed_num.setText(mlist.get(position).getStatus_3()
				+ "");
		if (mlist.get(position).getStatus_3() != 0) {
			int m = mlist.get(position).getStatus_3() * 100 /(mlist.get(position).getStatus_1()
                + mlist.get(position).getStatus_2() + mlist.get(position)
                .getStatus_3()) ;
			holder.form_item_result_num.setText(m + "%");
		} else {
			holder.form_item_result_num.setText("0%");
		}

		return convertView;
	}

	class Holder {
		TextView form_item_factoryname;

		TextView form_item_no_change_num;

		TextView form_item_no_look_num;

		TextView form_item_changed_num;

		TextView form_item_result_num;
	}

}
