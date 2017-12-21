package com.vomont.yundudao.presenter.reportform;

import com.vomont.yundudao.bean.FormBean;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.model.reportform.IReportformModel;
import com.vomont.yundudao.mvpview.reportform.IReportFormView;

public class ReportFormPresenter
{
    private IReportFormView view;
    
    private IReportformModel model;
    public ReportFormPresenter(IReportFormView view)
    {
        this.view=view;
        model=new IReportformModel();
    }
    
    
    public void getForm(String userid,long starttime,long endtime)
    {
        model.getFormList(userid, starttime, endtime, new OnHttpListener()
        {
            @Override
            public void onSucess(Object object)
            {
                   view.getSucess((FormBean)object); 
            }
            
            @Override
            public void onFail()
            {
                view.getError();
            }
        });
    }
}
