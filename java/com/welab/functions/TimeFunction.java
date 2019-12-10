package com.welab.functions;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.welab.utils.StringUtil;

public class TimeFunction implements Function {

	@Override
	public String execute(String[] args) {

		long time =  System.currentTimeMillis();

		if (args.length > 0 && StringUtil.isNotEmpty(args[0])) {

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, Integer.valueOf(args[0]));
			Date date = new Date(calendar.get(Calendar.YEAR)-1900,calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
			return String.format("%s", date.getTime());

		}
		return String.format("%s",time);
	}

	@Override
	public String getReferenceKey() {
		return "time";
	}
	
}
