package com.welab.dataprovider;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import com.welab.utils.ExcelUtils;

public class TestDataProvider {

	@DataProvider(name = "getDataForExcel")
	public static Object[][] getDataForExcel(Method m) throws Exception {
		Object[][] testObjArray = ExcelUtils.getTableArray("casefiles//testDemo.xlsx",
				m.getDeclaringClass().getSimpleName(), m.getName());
		return (testObjArray);
	}

}
