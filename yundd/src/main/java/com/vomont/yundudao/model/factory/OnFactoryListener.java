package com.vomont.yundudao.model.factory;

import com.vomont.yundudao.bean.FactoryBean;

public interface OnFactoryListener {
	void OnSucecss(FactoryBean factoryBean);

	void OnFail();
}
