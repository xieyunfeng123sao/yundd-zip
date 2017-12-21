package com.vomont.yundudao.presenter.newproblem;

import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.model.newproblem.IProblemModel;
import com.vomont.yundudao.model.newproblem.IProblemModelIpl;
import com.vomont.yundudao.mvpview.newproblem.INewProblemView;

public class NewProlemPresenter
{
    private IProblemModel model;
    
    private INewProblemView view;
    
    public NewProlemPresenter(INewProblemView view)
    {
        this.view = view;
        model = new IProblemModelIpl();
    }
    
    public void getProblemList(String userid,int latestid, int creatorid, int ownerid)
    {
        model.getListProblem(userid, latestid , creatorid, ownerid, new OnHttpListener()
        {
            @Override
            public void onSucess(Object object)
            {
                view.getSucess(object.toString());
            }
            
            @Override
            public void onFail()
            {
                view.getError();
            }
        });
    }
    
}
