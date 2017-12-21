package com.vomont.yundudao.model.problem;

import com.vomont.yundudao.bean.ProblemInfo;
import com.vomont.yundudao.model.OnHttpListener;

public interface ICreateModel {
	void  sendProblem(String userid,ProblemInfo problemInfo,OnHttpListener onHttpListener);
	
	void  getProbleType(String userid,OnHttpListener onHttpListener);
	
	void getProblemList(String userid,String pageidx,OnHttpListener onHttpListener);
	
	void  getProblemDetail(String userid,String problemid,OnHttpListener onHttpListener);

	void  sendProblemDetail(String userid,String problemtypeid,String relateddeviceid,String donetime,String ownerid,String ccuserids,String problemdesp,String imagecontent,String imagetype,String sourcetype,String relatedsubfactoryid,OnHttpListener onHttpListener);
	
	void  problemHandle(String userid,String problemid,String actiontype,String actionresult,String actiondesp,OnHttpListener onHttpListener);
}
