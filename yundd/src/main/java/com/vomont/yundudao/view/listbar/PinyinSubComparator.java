package com.vomont.yundudao.view.listbar;

import java.util.Comparator;

import com.vomont.yundudao.bean.SubFactory;

/**
 * 
 * @author 
 *
 */
public class PinyinSubComparator implements Comparator<SubFactory> {

	public int compare(SubFactory o1, SubFactory o2) {
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
