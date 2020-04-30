package com.test.demo;

import org.testng.annotations.Test;

import com.welab.bean.UseCase;
import com.welab.common.ApiTestBase;
import com.welab.dataprovider.TestDataProvider;

public class TestDemo  extends ApiTestBase{
	
	@Test(dataProvider = "getDataForExcel" , dataProviderClass = TestDataProvider.class,description="方法描述")
	public void testDemo(UseCase useCase){
		execuTestRaw(useCase);
	}
}
