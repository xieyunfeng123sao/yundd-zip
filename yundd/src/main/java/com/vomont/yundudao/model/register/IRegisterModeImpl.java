package com.vomont.yundudao.model.register;

import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.bean.RegisterBean;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.utils.HttpUtil;
import com.vomont.yundudao.utils.MD5Util;

public class IRegisterModeImpl implements IRegisterMode {

	@Override
	public void getVerCode(String num,
			final OnGetVerCodeListener onGetVerCodeListener) {
		// HTTP_TYPE.type1
		RequestParams reParams = new RequestParams();
		reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type1);
		reParams.add(HTTP_PAMRS.tel, num);
		HttpUtil.post(reParams, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseBody) {
				super.onSuccess(statusCode, headers, responseBody);

				Gson gson = new Gson();
				RegisterBean registerBean = gson.fromJson(responseBody,
						RegisterBean.class);
				onGetVerCodeListener.OnSucessGetVerCode(registerBean);
			}

			@Override
			public void onFailure(String responseBody, Throwable error) {
				super.onFailure(responseBody, error);
				onGetVerCodeListener.OnFailGetVerCode();
			}
		});
	}

	@Override
	public void register(String num, String verCode, String password,
			final OnRegisterListener onRegisterListener) {
		RequestParams reParams = new RequestParams();
		reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type2);
		reParams.add(HTTP_PAMRS.tel, num);
		reParams.add(HTTP_PAMRS.verifCode, verCode);
		reParams.add(HTTP_PAMRS.pswd, MD5Util.getMd5(password));
		HttpUtil.post(reParams, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseBody) {
				super.onSuccess(statusCode, headers, responseBody);
				Gson gson = new Gson();
				RegisterBean reBean = gson.fromJson(responseBody,
						RegisterBean.class);

				onRegisterListener.getSucessRegister(reBean);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseBody, Throwable error) {
				onRegisterListener.getFailRegister();
			}
		});

	}

}
