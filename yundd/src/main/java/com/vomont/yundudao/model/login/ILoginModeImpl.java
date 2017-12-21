package com.vomont.yundudao.model.login;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vomont.yundudao.application.Appcation;
import com.vomont.yundudao.bean.UserInfo;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.utils.MD5Util;

public class ILoginModeImpl implements ILoginModel
{
    @Override
    public void login(String name, String password, final OnLoginListener onLoginListener)
    {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        final RequestBody requestBody;
        requestBody = new FormEncodingBuilder().add("msgid", HTTP_TYPE.type3)// 259
            .add(HTTP_PAMRS.tel, name)
            .add(HTTP_PAMRS.pswd, MD5Util.getMd5(password))
            .build();
        try
        {
            Request request = new Request.Builder().url(Appcation.BASE_URL).post(requestBody).build();
            okHttpClient.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onResponse(Response arg0)
                    throws IOException
                {
                    Gson gson = new Gson();
                    String log = arg0.body().string();
                    Log.e("insert", log);
                    try
                    {
                        UserInfo userInfo = gson.fromJson(log, UserInfo.class);
                        onLoginListener.OnSucess(userInfo);
                    }
                    catch (Exception e)
                    {
                        onLoginListener.OnFail();
                    }
                }
                @Override
                public void onFailure(Request arg0, IOException arg1)
                {
                    onLoginListener.OnFail();
                }
            });
        }
        catch (Exception e)
        {
            onLoginListener.OnFail();
        }
    }
}
