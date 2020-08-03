package com.len.utils;

public class CompareUtil {

	public static String getInRange(int actual, int min, int max) {
		if (actual >= min && actual <= max) {
			System.out.println("实际结果：" + actual + "在区间：" + min + "-" + max + "内");
			return "true";
		} else {
			System.err.println("实际结果：" + actual + "不在区间" + min + "-" + max + "内");
			return "false";
		}
	}
}
