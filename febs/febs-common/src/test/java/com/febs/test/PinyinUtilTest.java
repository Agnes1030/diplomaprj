package com.febs.test;

import com.febs.common.utils.PinyinUtil;

public class PinyinUtilTest {
	public void testQuanPin() {
		String res = PinyinUtil.getFullPinyin("产品中心");
		System.out.println(res);
	}

	public void testAAQuanPin() {
		String res = PinyinUtil.getFullPinyin("产品中心,XL,1.2*1.5");
		System.out.println(res);
	}
}
