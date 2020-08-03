package com.len.utils;

import org.testng.Assert;

import com.len.bean.ResponseVo;

public class LoginUtil {

	public static String login(String domain, String environment) {

		// 登录地址
		String login_url = domain + "/dust/api/v1/admin/login";

		ResponseVo rv = HttpUtil.login(GetConfigUtil.getConfigPropertyByNameAndKey(environment, "username"),
				GetConfigUtil.getConfigPropertyByNameAndKey(environment, "password"),
				GetConfigUtil.getConfigPropertyByNameAndKey(environment, "userType"), login_url);
		Assert.assertEquals(rv.getJson().getString("message"), "success");

		return rv.getResponseStr();
	}
}
