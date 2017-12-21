package com.vomont.yundudao.presenter.problem;

import com.vomont.yundudao.bean.ProblemDetailBean;
import com.vomont.yundudao.bean.ProblemListBean;
import com.vomont.yundudao.bean.ProblemTypeBean;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.model.problem.ICreateModel;
import com.vomont.yundudao.model.problem.ICreateModelIpl;
import com.vomont.yundudao.mvpview.problem.ICenterProblemView;
import com.vomont.yundudao.mvpview.problem.ICreateProView;

public class ICenterProPresenter {

	private ICenterProblemView iCenterProblemView;

	private ICreateModel iCreateModel;

	private ICreateProView iCreateProView;


	public ICenterProPresenter(ICenterProblemView iCenterProblemView) {
		this.iCenterProblemView = iCenterProblemView;
		iCreateModel = new ICreateModelIpl();
	}

	public ICenterProPresenter(ICreateProView iCreateProView) {
		this.iCreateProView = iCreateProView;
		iCreateModel = new ICreateModelIpl();
	}

	public void getProblemType(String userid) {
		iCreateModel.getProbleType(userid, new OnHttpListener() {

			@Override
			public void onSucess(Object object) {
				iCenterProblemView.getType((ProblemTypeBean) object);
			}

			@Override
			public void onFail() {
				iCenterProblemView.getType(null);
			}
		});
	}

	public void getProblemDetailList(String userid,String pageidx) {
		iCreateModel.getProblemList(userid, pageidx,new OnHttpListener() {

			@Override
			public void onSucess(Object object) {
				iCenterProblemView.getList((ProblemListBean) object);
			}

			@Override
			public void onFail() {
				iCenterProblemView.getList(null);
			}
		});
	}

	public void getProblemDetail(String userid, String problemid) {
		iCreateModel.getProblemDetail(userid, problemid, new OnHttpListener() {

			@Override
			public void onSucess(Object object) {
				iCenterProblemView.getProbleDtail((ProblemDetailBean) object);
			}

			@Override
			public void onFail() {
				iCenterProblemView.getProbleDtail(null);
			}
		});
	}

	public void sendProblemDetail(String userid, String problemtypeid,
			String relateddeviceid, String donetime, String ownerid,
			String ccuserids, String problemdesp, String imagecontent,
			String imagetype,String sourcetype,String relatedsubfactoryid) {

		iCreateModel.sendProblemDetail(userid, problemtypeid, relateddeviceid,
				donetime, ownerid, ccuserids, problemdesp, imagecontent,
				imagetype,sourcetype,relatedsubfactoryid, new OnHttpListener() {

					@Override
					public void onSucess(Object object) {
						iCreateProView.getResultBySendInfo(0);
					}
					@Override
					public void onFail() {
						iCreateProView.getResultBySendInfo(1);
					}
				});

	}

}
