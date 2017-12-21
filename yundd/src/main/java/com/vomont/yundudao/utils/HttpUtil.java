package com.vomont.yundudao.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.application.Appcation;

public class HttpUtil
{
    // 165 132
    private static AsyncHttpClient client = new AsyncHttpClient();
    
    static
    {
        client.setTimeout(3000); // 设置链接超时，如果不设置，默认为10s
    }
    
    public static void get(final RequestParams params, final AsyncHttpResponseHandler responseHandler)
    {
        // Appcation.getInstance().getURL();
        // Appcation.getInstance().setListener(new OnUrlListener() {
        // @Override
        // public void onSucess(String url) {
        // client.get("", params, responseHandler);
        // }
        // });
    }
    
    public static void post(final RequestParams params, final TextHttpResponseHandler responseHandler)
    {
        client.setTimeout(3000);
        client.post(Appcation.BASE_URL, params, responseHandler);
    }
    
}
