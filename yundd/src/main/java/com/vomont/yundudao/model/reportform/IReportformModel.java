package com.vomont.yundudao.model.reportform;

import java.io.IOException;

import org.apache.http.Header;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.bean.FormBean;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.utils.HttpUtil;
import com.vomont.yundudao.utils.OkHttpClientManager;
import com.vomont.yundudao.utils.OkHttpClientManager.Param;

public class IReportformModel {

	public void getFormList(String userid, long starttime, long endtime,
			final OnHttpListener onHttpListener) {
		// HTTP_TYPE.type1
		RequestParams reParams = new RequestParams();
		reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type22);
		reParams.add(HTTP_PAMRS.userid, userid);
		reParams.add("starttime", starttime / 1000 + "");
		reParams.add("endtime", endtime / 1000 + "");
		HttpUtil.post(reParams, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseBody) {
				super.onSuccess(statusCode, headers, responseBody);
				Gson gson = new Gson();
				String name = responseBody;
				Log.e("insert", name);
				FormBean formBean = gson.fromJson(name, FormBean.class);
				onHttpListener.onSucess(formBean);
			}

			@Override
			public void onFailure(String responseBody, Throwable error) {
				super.onFailure(responseBody, error);
				onHttpListener.onFail();
			}
		});
	}

	public String getFormByType(String userid, long starttime, long endtime,
			String typeid, String subfactoryid) {
		Param param1 = new Param(HTTP_PAMRS.msgid, HTTP_TYPE.type23);
		Param param2 = new Param(HTTP_PAMRS.userid, userid);
		Param param3 = new Param("starttime", starttime / 1000 + "");
		Param param4 = new Param("endtime", endtime / 1000 + "");
		Param param5 = new Param("typeid", typeid + "");
		Param param6 = new Param("subfactoryid", subfactoryid + "");
		try {
			String result = OkHttpClientManager.postAsString(
					Appcation.BASE_URL, param1, param2, param3, param4, param5,
					param6);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		// RequestParams reParams = new RequestParams();
		// reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type23);
		// reParams.add(HTTP_PAMRS.userid, userid);
		// reParams.add("starttime", starttime / 1000 + "");
		// reParams.add("endtime", endtime / 1000 + "");
		// reParams.add("typeid", typeid);
		// reParams.add("subfactoryid", subfactoryid);
		// HttpUtil.post(reParams, new TextHttpResponseHandler()
		// {
		// @Override
		// public void onSuccess(int statusCode, Header[] headers, String
		// responseBody)
		// {
		// super.onSuccess(statusCode, headers, responseBody);
		// Gson gson = new Gson();
		// FormBean formBean = gson.fromJson(responseBody, FormBean.class);
		// onHttpListener.onSucess(formBean);
		// }
		//
		// @Override
		// public void onFailure(String responseBody, Throwable error)
		// {
		// super.onFailure(responseBody, error);
		// onHttpListener.onFail();
		// }
		// });
	}

}
