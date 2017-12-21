package com.vomont.yundudao.model.problem;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.bean.ProblemDetailBean;
import com.vomont.yundudao.bean.ProblemListBean;
import com.vomont.yundudao.bean.ProblemInfo;
import com.vomont.yundudao.bean.ProblemTypeBean;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.common.Con_Action.HTTP_TYPE;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.utils.HttpUtil;

public class ICreateModelIpl implements ICreateModel
{
    
    @Override
    public void sendProblem(String userid, ProblemInfo problemInfo, final OnHttpListener onHttpListener)
    {
        // HTTP_TYPE.type1
        RequestParams reParams = new RequestParams();
        reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type11);
        reParams.add(HTTP_PAMRS.userid, userid);
        HttpUtil.post(reParams, new TextHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody)
            {
                super.onSuccess(statusCode, headers, responseBody);
                onHttpListener.onSucess(responseBody);
            }
            
            @Override
            public void onFailure(String responseBody, Throwable error)
            {
                super.onFailure(responseBody, error);
                onHttpListener.onFail();
            }
        });
    }
    
    @Override
    public void getProbleType(String userid, final OnHttpListener onHttpListener)
    {
        // HTTP_TYPE.type1
        RequestParams reParams = new RequestParams();
        reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type15);
        reParams.add(HTTP_PAMRS.userid, userid);
        HttpUtil.post(reParams, new TextHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody)
            {
                super.onSuccess(statusCode, headers, responseBody);
                
                Gson gson = new Gson();
                ProblemTypeBean problemTypeBean = gson.fromJson(responseBody, ProblemTypeBean.class);
                onHttpListener.onSucess(problemTypeBean);
            }
            
            @Override
            public void onFailure(String responseBody, Throwable error)
            {
                super.onFailure(responseBody, error);
                onHttpListener.onFail();
            }
        });
    }
    
    @Override
    public void getProblemList(String userid, String pageidx, final OnHttpListener onHttpListener)
    {
        
        RequestParams reParams = new RequestParams();
        reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type16);
        reParams.add(HTTP_PAMRS.userid, userid);
        reParams.add(HTTP_PAMRS.pageidx, pageidx);
        HttpUtil.post(reParams, new TextHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody)
            {
                super.onSuccess(statusCode, headers, responseBody);
                Gson gson = new Gson();
                ProblemListBean bean = gson.fromJson(responseBody, ProblemListBean.class);
                onHttpListener.onSucess(bean);
            }
            
            @Override
            public void onFailure(String responseBody, Throwable error)
            {
                super.onFailure(responseBody, error);
                onHttpListener.onFail();
            }
        });
    }
    
    @Override
    public void getProblemDetail(String userid, String problemid, final OnHttpListener onHttpListener)
    {
        RequestParams reParams = new RequestParams();
        reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type17);
        reParams.add(HTTP_PAMRS.userid, userid);
        reParams.add(HTTP_PAMRS.problemid, problemid);
        
        HttpUtil.post(reParams, new TextHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody)
            {
                super.onSuccess(statusCode, headers, responseBody);
                Log.e("insert", responseBody);
                Gson gson = new Gson();
                try
                {
                    ProblemDetailBean bean = gson.fromJson(responseBody, ProblemDetailBean.class);
                    onHttpListener.onSucess(bean);
                }
                catch (Exception e)
                {
                    onHttpListener.onFail();
                }
                
            }
            
            @Override
            public void onFailure(String responseBody, Throwable error)
            {
                super.onFailure(responseBody, error);
                onHttpListener.onFail();
            }
        });
    }
    
    @Override
    public void sendProblemDetail(String userid, String problemtypeid, String relateddeviceid, String donetime, String ownerid, String ccuserids, String problemdesp, String imagecontent,
        String imagetype, String sourcetype, String relatedsubfactoryid, final OnHttpListener onHttpListener)
    {
        
        RequestParams reParams = new RequestParams();
        reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type18);
        reParams.add(HTTP_PAMRS.userid, userid);
        reParams.add(HTTP_PAMRS.problemtypeid, problemtypeid);
        reParams.add(HTTP_PAMRS.relateddeviceid, relateddeviceid);
        reParams.add(HTTP_PAMRS.donetime, donetime);
        reParams.add(HTTP_PAMRS.ownerid, ownerid);
        reParams.add(HTTP_PAMRS.ccuserids, ccuserids);
        reParams.add(HTTP_PAMRS.problemdesp, problemdesp);
        reParams.add(HTTP_PAMRS.imagecontents, imagecontent);
        reParams.add(HTTP_PAMRS.imagetype, imagetype);
        reParams.add("sourcetype", sourcetype);
        reParams.add("relatedsubfactoryid", relatedsubfactoryid);
        
        HttpUtil.post(reParams, new TextHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody)
            {
                super.onSuccess(statusCode, headers, responseBody);
                JSONObject object;
                try
                {
                    object = new JSONObject(responseBody);
                    int result = object.getInt("result");
                    if (result == 0)
                    {
                        onHttpListener.onSucess(result);
                    }
                    else
                    {
                        onHttpListener.onFail();
                    }
                }
                catch (JSONException e1)
                {
                    e1.printStackTrace();
                }
            }
            
            @Override
            public void onFailure(String responseBody, Throwable error)
            {
                super.onFailure(responseBody, error);
                onHttpListener.onFail();
            }
        });
        
    }
    
    @Override
    public void problemHandle(String userid, String problemid, String actiontype, String actionresult, String actiondesp, final OnHttpListener onHttpListener)
    {
        RequestParams reParams = new RequestParams();
        reParams.add(HTTP_PAMRS.msgid, HTTP_TYPE.type19);
        reParams.add(HTTP_PAMRS.userid, userid);
        reParams.add(HTTP_PAMRS.problemid, problemid);
        reParams.add(HTTP_PAMRS.actiontype, actiontype);
        
        reParams.add(HTTP_PAMRS.actionresult, actionresult);
        // if (!actiontype.equals("2"))
        // {
        // reParams.add(HTTP_PAMRS.actiondesp, "无");
        // }
        // else
        // {
        reParams.add(HTTP_PAMRS.actiondesp, actiondesp);
        // }
        HttpUtil.post(reParams, new TextHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody)
            {
                super.onSuccess(statusCode, headers, responseBody);
                try
                {
                    String name = responseBody;
                    JSONObject obj = new JSONObject(name);
                    int result = obj.getInt("result");
                    onHttpListener.onSucess(result);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                
            }
            
            @Override
            public void onFailure(String responseBody, Throwable error)
            {
                super.onFailure(responseBody, error);
                onHttpListener.onFail();
            }
        });
    }
    
}
