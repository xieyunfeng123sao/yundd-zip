package com.vomont.yundudao.model.verfication;

import org.apache.http.Header;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.bean.RegisterBean;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.utils.HttpUtil;

public class IVerFicationModeImpl implements IVerFicationMode {

	@Override
	public void CodeLogin(String num, String code,
			final OnVerFicationListener onVerFicationListener) {
        RequestParams reParams = new RequestParams();
        reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type4);
        reParams.add(HTTP_PAMRS.tel, num);
        reParams.add(HTTP_PAMRS.verifCode, code);
        HttpUtil.post(reParams, new TextHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody)
            {
                super.onSuccess(statusCode, headers, responseBody);
                Gson gson = new Gson();
                RegisterBean   registerBean = gson.fromJson(responseBody, RegisterBean.class);
                onVerFicationListener.OnSucess(registerBean);
            }
            
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error)
            {
                super.onFailure(statusCode, headers, responseBody, error);
                onVerFicationListener.OnFail();
            }
            
        });
	}

}
