package com.vomont.yundudao.mvpview.problem;

import com.vomont.yundudao.bean.ProblemDetailBean;
import com.vomont.yundudao.bean.ProblemListBean;
import com.vomont.yundudao.bean.ProblemTypeBean;

public interface ICenterProblemView {
	
	void  getList(ProblemListBean bean);
	 void  getType(ProblemTypeBean result);
	 void getProbleDtail(ProblemDetailBean problemBean) ;
}
