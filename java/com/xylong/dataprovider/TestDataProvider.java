package com.welab.dataprovider;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import com.welab.utils.ExcelUtils;

public class TestDataProvider {

	@DataProvider(name = "getDataForExcel")
	public static Object[][] getDataForExcel(Method m) throws Exception {
		Object[][] testObjArray = ExcelUtils.getTableArray("casefiles//testWe-check.xlsx",
				m.getDeclaringClass().getSimpleName(), m.getName());
		return (testObjArray);
	}
	
	@DataProvider(name = "getFrontDataForExcel")
	public static Object[][] getFrontDataForExcel(Method m) throws Exception {
		Object[][] testObjArray = ExcelUtils.getTableArray("casefiles//testWe-check-front.xlsx",
				m.getDeclaringClass().getSimpleName(), m.getName());
		return (testObjArray);
	}
	
	@DataProvider(name = "getToolsDataForExcel")
	public static Object[][] getToolsDataForExcel(Method m) throws Exception {
		Object[][] testObjArray = ExcelUtils.getTableArray("casefiles//testToolsData.xlsx",
				m.getDeclaringClass().getSimpleName(), m.getName());
		return (testObjArray);
	}

}
