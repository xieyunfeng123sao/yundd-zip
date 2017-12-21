package com.vomont.yundudao.utils;

import java.util.List;

public class EmptyUtil {

	public static boolean listIsEmpty(List<?> mlist) {
		if (mlist != null && mlist.size() != 0) {
			return false;
		}
		return true;
	}

}
