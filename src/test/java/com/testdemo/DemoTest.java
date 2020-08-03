package com.testdemo;

import org.testng.annotations.Test;

import com.len.bean.UseCase;
import com.len.common.ApiTestBase;
import com.len.dataprovider.TestDataProvider;

public class DemoTest extends ApiTestBase {

	@Test(dataProvider = "getDataForExcel", dataProviderClass = TestDataProvider.class, description = "XXX描述")
	public void testDemo(UseCase useCase) {
		execuTestRaw(useCase);
	}
}
