package com.vomont.yundudao.presenter.reportform;

import com.vomont.yundudao.model.reportform.IReportformModel;
import com.vomont.yundudao.mvpview.reportform.IReportMoreView;

public class ReportMorePresenter
{
//    private IReportMoreView view;
    
    private IReportformModel model;
    
    public ReportMorePresenter(IReportMoreView view)
    {
//        this.view = view;
        model = new IReportformModel();
    }
    
    public String getFormMore(String userid, long starttime, long endtime, String typeid, String subfactoryid)
    {
        String result = model.getFormByType(userid, starttime, endtime, typeid, subfactoryid);
        return result;
    }
}
