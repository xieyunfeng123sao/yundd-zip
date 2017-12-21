package com.vomont.yundudao.common;

public interface Problem_Action {

	public interface WMProblemStatus {
		public static final int WMProblemStatus_Invalid = 0;
		public static final int WMProblemStatus_Pending = 1; // 不合格 (待整改)
		public static final int WMProblemStatus_FinishAdjust = 2; // 已整改 （待复查）
		public static final int WMProblemStatus_NoAdjust = 3; // 无需整改 （待复查）
		public static final int WMProblemStatus_Ok = 4; // 整改通过
		public static final int WMProblemStatus_NotOk = 5; // 整改未通过（待整改）
	}

	public interface WMProblemAction {
		public static final int WMProblemAction_Invalide = 0;
		public static final int WMProblemAction_Creat = 1; // 创建问题
		public static final int WMProblemAction_Discuss = 2; // 评论
		public static final int WMProblemAction_Resolve = 3; // 整改
		public static final int WMProblemAction_Recheck = 4; // 复查
	}
}
