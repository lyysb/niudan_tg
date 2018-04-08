package com.niudantg.util;

import java.util.Comparator;

import ktx.pojo.domain.ExtensionStaff;

public class Comparator_Extension implements Comparator {

	public int compare(Object arg0, Object arg1) {
		ExtensionStaff score0 = (ExtensionStaff) arg0;
		ExtensionStaff score1 = (ExtensionStaff) arg1;

		// 首先比较年龄，如果年龄相同，则比较名字

		return score0.Status - score1.Status;

	}
}