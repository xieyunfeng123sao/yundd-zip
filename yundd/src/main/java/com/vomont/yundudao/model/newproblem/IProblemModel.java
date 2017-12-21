package com.vomont.yundudao.model.newproblem;

import com.vomont.yundudao.model.OnHttpListener;

public interface IProblemModel
{
    void getListProblem(String userid,int pageidx, int creatorid, int ownerid,OnHttpListener onHttpListener);
}
