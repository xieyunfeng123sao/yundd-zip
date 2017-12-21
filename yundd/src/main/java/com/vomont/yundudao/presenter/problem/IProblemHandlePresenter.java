package com.vomont.yundudao.presenter.problem;

import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.model.problem.ICreateModel;
import com.vomont.yundudao.model.problem.ICreateModelIpl;
import com.vomont.yundudao.mvpview.problem.IPronblemHandleView;

public class IProblemHandlePresenter {

	private IPronblemHandleView view;

	private ICreateModel model;

	public IProblemHandlePresenter(IPronblemHandleView view) {
		this.view = view;
		model = new ICreateModelIpl();
	}

	public void problemAction(String userid, String problemid,
			String actiontype, String actionresult, String actiondesp) {
		model.problemHandle(userid, problemid, actiontype, actionresult,
				actiondesp, new OnHttpListener() {

					@Override
					public void onSucess(Object object) {
						view.handleResult((Integer) object);
					}

					@Override
					public void onFail() {
						view.handleResult(1);

					}
				});

	}

}
