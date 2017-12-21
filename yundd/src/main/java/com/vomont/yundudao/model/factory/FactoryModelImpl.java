package com.vomont.yundudao.model.factory;

import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.utils.HttpUtil;

public class FactoryModelImpl implements FactoryModel {

	@Override
	public void getFactory(String userid,
			final OnFactoryListener onFactoryListener) {
		RequestParams reParams = new RequestParams();
		reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type11);
		reParams.add(HTTP_PAMRS.userid, userid);
		HttpUtil.post(reParams, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseBody) {
				super.onSuccess(statusCode, headers, responseBody);
				Gson gson = new Gson();
				FactoryBean factoryBean = gson.fromJson(responseBody, FactoryBean.class);
				onFactoryListener.OnSucecss(factoryBean);
			}
			@Override
			public void onFailure(String responseBody, Throwable error) {
				super.onFailure(responseBody, error);
				onFactoryListener.OnFail();
			}
		});
	}

}
