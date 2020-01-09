package com.welab.functions;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.welab.utils.StringUtil;

public class DateFormatFunction implements Function {

	@Override
	public String execute(String[] args) {
		//获取当前系统的时间
		Date today=new Date();
		//格式化日期
		SimpleDateFormat dateformat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (args.length > 0 && StringUtil.isNotEmpty(args[0])) {
			dateformat= new SimpleDateFormat(args[0]);
			return dateformat.format(today);
		}
		return dateformat.format(today);
	}

	@Override
	public String getReferenceKey() {
		return "dateFormat";
	}
	
}
