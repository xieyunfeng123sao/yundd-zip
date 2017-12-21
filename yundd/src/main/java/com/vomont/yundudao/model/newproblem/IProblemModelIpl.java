package com.vomont.yundudao.model.newproblem;

import org.apache.http.Header;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.vomont.yundudao.common.Con_Action.HTTP_PAMRS;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.utils.HttpUtil;

public class IProblemModelIpl implements IProblemModel
{
    @Override
    public void getListProblem(String userid, int latestid, int creatorid, int ownerid, final OnHttpListener onHttpListener)
    {
        // HTTP_TYPE.type1
        RequestParams reParams = new RequestParams();
        reParams.add(HTTP_PAMRS.msgid, "360");
        reParams.add(HTTP_PAMRS.userid, userid);
        reParams.add("latestid", latestid + "");
        if (creatorid != 0)
            reParams.add("creatorid", creatorid + "");
        if (ownerid != 0)
            reParams.add("ownerid", ownerid + "");
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
}
