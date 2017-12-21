package com.vomont.yundudao.view.listbar;

import java.util.Comparator;

import com.vomont.yundudao.bean.DetalInfo;

/**
 * 
 * @author 
 *
 */
public class PinyinDetalComparator implements Comparator<DetalInfo> {

	public int compare(DetalInfo o1, DetalInfo o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
