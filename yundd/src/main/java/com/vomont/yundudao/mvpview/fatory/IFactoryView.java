package com.vomont.yundudao.mvpview.fatory;

import com.vomont.yundudao.bean.FactoryBean;

public interface IFactoryView {
	void getFactory(FactoryBean factoryBean);
	
	void onFail();
}
