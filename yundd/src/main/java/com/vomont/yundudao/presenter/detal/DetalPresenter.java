package com.vomont.yundudao.presenter.detal;

import com.vomont.yundudao.bean.DetalBean;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.model.detal.DetalModel;
import com.vomont.yundudao.model.detal.DetalModelImpl;
import com.vomont.yundudao.mvpview.detal.IDetalView;

public class DetalPresenter {

	private DetalModel detalModel;
	private IDetalView iDetalView;

	public DetalPresenter(IDetalView iDetalView) {
		this.iDetalView = iDetalView;
		detalModel = new DetalModelImpl();
	}

	public void getDetal(String useid) {
		detalModel.getDetalPeople(useid, new OnHttpListener() {

			@Override
			public void onSucess(Object object) {
				iDetalView.getDetalman((DetalBean) object);
			}

			@Override
			public void onFail() {
				iDetalView.getFailed();
			}
		});
	}
}
