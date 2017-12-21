package com.vomont.yundudao.model.patrol;

import org.apache.http.Header;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.bean.PatrolDetail;
import com.vomont.yundudao.bean.PatrolList;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.utils.HttpUtil;

public class PatrolModelImpl implements IPatrolModel {

	@Override
	public void getPatrolList(int userid, String searchdescription,
			int latestid, final OnHttpListener onHttpListener) {
		// HTTP_TYPE.type1
		RequestParams reParams = new RequestParams();
		reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type20);
		reParams.add(HTTP_PAMRS.userid, userid + "");
		reParams.add(HTTP_PAMRS.searchdescription, searchdescription);
		reParams.add("latestid", latestid + "");
		HttpUtil.post(reParams, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseBody) {
				super.onSuccess(statusCode, headers, responseBody);
				Gson gson = new Gson();
//				Log.e("callback",responseBody);
				PatrolList patrolList = gson.fromJson(responseBody,
						PatrolList.class);
				if (patrolList.getResult() == 0) {
					onHttpListener.onSucess(patrolList);
				} else {
					onHttpListener.onFail();
				}
			}

			@Override
			public void onFailure(String responseBody, Throwable error) {
				super.onFailure(responseBody, error);
				onHttpListener.onFail();
			}
		});
	}

	@Override
	public void getPatrolDetail(int userid, int videoid,
			final OnHttpListener onHttpListener) {
		// HTTP_TYPE.type1
		RequestParams reParams = new RequestParams();
		reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type21);
		reParams.add(HTTP_PAMRS.userid, userid + "");
		reParams.add(HTTP_PAMRS.videoid, videoid+"");
		HttpUtil.post(reParams, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseBody) {
				super.onSuccess(statusCode, headers, responseBody);
				Gson gson = new Gson();
				Log.e("callback", responseBody);
				PatrolDetail detail = gson.fromJson(responseBody,
						PatrolDetail.class);
				if(detail.getResult()==0)
				{
					onHttpListener.onSucess(detail.getVideo());
				}
				else
				{
					onHttpListener.onFail();
				}
			}

			@Override
			public void onFailure(String responseBody, Throwable error) {
				super.onFailure(responseBody, error);
				onHttpListener.onFail();
			}
		});
	}

}
