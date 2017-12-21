package com.vomont.yundudao.mvpview.register;

import com.vomont.yundudao.bean.RegisterBean;

public interface IRegiseterView {
		void showProgess();
		
		void hideProgress();
		
		void getVerCodeSucess(RegisterBean registerBean);
		void getVerCodeFail();
		
		void reigsterSucess(RegisterBean registerBean);
		
		void registerFail();

}
