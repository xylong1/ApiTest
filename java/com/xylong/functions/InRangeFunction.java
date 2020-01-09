package com.welab.functions;

import com.welab.utils.CompareUtil;

public class InRangeFunction implements Function{

	@Override
	public String execute(String[] args) {
		
		int len = args.length;
		int actual = 0;
		int min = 0;
		int max = 0;
		if(len == 3) {
			if(args[0].matches("[0-9]+")) {//被判断的数值
				actual = Integer.parseInt(args[0]);
				//区间最小值
				min = Integer.parseInt(args[1]);
				//区间最大值
				max = Integer.parseInt(args[2]);
				return CompareUtil.getInRange(actual, min, max);
			}else {
				return args[0];
			}
		}else {
			return "比较实际结果在区间内时，实际结果/最小值/最大值参数有缺失";
		}
	}

	@Override
	public String getReferenceKey() {
		return "inRange";
	}
	
	
}
