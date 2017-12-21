package com.vomont.yundudao.model.tag;

import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.bean.TagBean;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.utils.HttpUtil;

public class TagModelImpl implements TagModel {

	@Override
	public void getTag(String userid, final OnHttpListener onHttpListener) {
		RequestParams reParams = new RequestParams();
		reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type15);
		reParams.add(HTTP_PAMRS.userid, userid);
		HttpUtil.post(reParams, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseBody) {
				super.onSuccess(statusCode, headers, responseBody);
				Gson gson = new Gson();
				String name=responseBody;
				if(name!=null)
				{
					TagBean tagBean = gson.fromJson(name, TagBean.class);
					onHttpListener.onSucess(tagBean);
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
