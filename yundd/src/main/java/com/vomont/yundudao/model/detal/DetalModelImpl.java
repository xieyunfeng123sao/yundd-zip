package com.vomont.yundudao.model.detal;

import org.apache.http.Header;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.bean.DetalBean;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.utils.HttpUtil;

public class DetalModelImpl implements DetalModel {

	@Override
	public void getDetalPeople(String userid,
			final OnHttpListener onHttpListener) {
		RequestParams reParams = new RequestParams();
		reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type14);
		reParams.add(HTTP_PAMRS.userid, userid);
		HttpUtil.post(reParams, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseBody) {
				super.onSuccess(statusCode, headers, responseBody);
				Gson gson = new Gson();
				Log.e("insert", responseBody);
				DetalBean detalBean = gson.fromJson(responseBody,
						DetalBean.class);
				onHttpListener.onSucess(detalBean);
			}

			@Override
			public void onFailure(String responseBody, Throwable error) {
				super.onFailure(responseBody, error);
				onHttpListener.onFail();
			}
		});
	}

}
