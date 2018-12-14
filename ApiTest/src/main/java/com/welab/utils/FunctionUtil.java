package com.welab.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.welab.functions.Function;

public class FunctionUtil{

	private static final Map<String, Class<? extends Function>> functionsMap = new HashMap<>();
	static {
		//bodyfile 特殊处理
		functionsMap.put("bodyfile", null);
		List<Class<?>> clazzes = ClassFinder.getAllAssignedClass(Function.class);
		clazzes.forEach((clazz) -> {
			try {
				// function
				Function tempFunc = (Function) clazz.newInstance();
				String referenceKey = tempFunc.getReferenceKey();
				if (referenceKey.length() > 0) { // ignore self
					functionsMap.put(referenceKey, tempFunc.getClass());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				//TODO 
			}
		});
	}
	
	public static boolean isFunction(String functionName){
		return functionsMap.containsKey(functionName);
	}
	
	public static String getValue(String functionName,String[] args){
		try {
			String value = functionsMap.get(functionName).newInstance().execute(args);
			return value.replace("\\,", ",");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

}
