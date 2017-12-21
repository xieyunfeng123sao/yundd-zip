package com.vomont.yundudao.presenter.factory;


import com.vomont.yundudao.bean.FactoryBean;
import com.vomont.yundudao.model.factory.FactoryModel;
import com.vomont.yundudao.model.factory.FactoryModelImpl;
import com.vomont.yundudao.model.factory.OnFactoryListener;
import com.vomont.yundudao.mvpview.fatory.IFactoryView;

public class FactoryPresenter {

	private FactoryModel factoryModel;

	private IFactoryView iFactoryView;
	

	public FactoryPresenter(IFactoryView iFactoryView) {
		this.iFactoryView = iFactoryView;
		factoryModel = new FactoryModelImpl();
	}

	public void getFactory(String useid) {
		factoryModel.getFactory(useid, new OnFactoryListener() {
			@Override
			public void OnSucecss(FactoryBean factoryBean) {
				iFactoryView.getFactory(factoryBean);
			}

			@Override
			public void OnFail() {
				iFactoryView.getFactory(null);
			}
		});
	}
}
