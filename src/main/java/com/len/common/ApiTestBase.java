package com.len.common;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.len.bean.RequestVo;
import com.len.bean.ResponseVo;
import com.len.bean.UseCase;
import com.len.utils.AssertUtil;
import com.len.utils.FunctionUtil;
import com.len.utils.GetConfigUtil;
import com.len.utils.HttpUtil;
import com.len.utils.LoginUtil;
import com.len.utils.StringUtil;

public class ApiTestBase {

	static String domain = "";
	String x_user_token = "";

	public static HashMap<String, String> headers = new HashMap<>();

	/**
	 * 替换符，如果数据中包含“${}”则会被替换成公共参数中存储的数据
	 */
	protected Pattern replaceParamPattern = Pattern.compile("\\$\\{(.*?)\\}");
	private final static String argsSpit = "(?<!\\\\),";

	/**
	 * 截取自定义方法正则表达式：__xxx(ooo)\\u4E00-\\u9FA5支持中文
	 */
	protected Pattern funPattern = Pattern.compile("__(\\w*?)\\((([^)]*,?)*)\\)");

	/**
	 * 公共参数数据池（全局可用）
	 */
	private static Map<String, String> saveDatas = new HashMap<String, String>();

	public static RequestVo requestvo = new RequestVo();
	public static ResponseVo responsevo;

	@Parameters({ "environment" })
	@BeforeSuite()
	public void init(String environment) {
		domain = GetConfigUtil.getConfigPropertyByNameAndKey(environment, "domain");

		// String loginResponse = LoginUtil.login(domain, environment);
		// saveResult(loginResponse,"merchantId = $.data.merchantId;token =
		// $.data.token;");

		headers.clear();
		headers.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		// headers.put("Accept", "application/x-www-form-urlencoded");
		// headers.put("x-user-token", saveDatas.get("token"));
		// headers.put("x-merchant-id", saveDatas.get("merchantId"));
		// headers.put("x-user-mobile",
		// GetConfigUtil.getConfigPropertyByNameAndKey(environment, "username"));

		// saveDatas.put("secretKey",
		// GetConfigUtil.getConfigPropertyByNameAndKey(environment, "secretKey"));
		// saveDatas.put("accessKey",
		// GetConfigUtil.getConfigPropertyByNameAndKey(environment, "accessKey"));
		// saveDatas.put("timestamp", ""+System.currentTimeMillis()/1000);
	}

	// 执行每一条用例
	protected void execuTest(String param, String verify, String describe, String url, String method, String save) {
		System.out.println("--------------------------------------------------------");
		System.out.println("用例数据：" + "param:" + param + "  verify: " + verify + "  describe: " + describe + "  url: "
				+ url + "  method: " + method + "  save: " + save);

		param = getCommonParam(param);
		HashMap<String, Object> requesParams = new HashMap<>();
		if (!param.equals("null"))
			requesParams = JSON.parseObject(param, HashMap.class);
		requestvo.setRequestVo(headers, requesParams, domain + url);
		if (method.equals("Get")) {
			try {
				responsevo = HttpUtil.httpGet(requestvo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (method.equals("Post")) {
			responsevo = HttpUtil.httpPost(requestvo);
		}
		if (responsevo.getHttpCode() >= 400) {
			Assert.assertEquals(responsevo.getHttpCode(), "200", "响应代码有误");
		} else if (!verify.equals("null")) {
			verifyResult(responsevo.getJson().toString(), verify, describe);
		}
		if (!(save.equals("null") || save.isEmpty())) {
			saveResult(responsevo.getJson().toString(), save);
		}
	}

	protected void execuTest(UseCase useCase) {
		if (!useCase.getPreParam().equals("null")) {
			String preParam = buildParam(useCase.getPreParam());
			// 保存preParam参数到公共参数
			savePreParam(preParam);
		}
		if (!(useCase.getSleepTime().equals(null) || useCase.getSleepTime().equals("0")
				|| useCase.getSleepTime().equals("null"))) {
			if (useCase.getSleepTime().contains(".")) {// 读取表格的数值字符串时，默认多了一位小数，例如60，读取为60.0
				sleepTime(Integer.valueOf(useCase.getSleepTime().substring(0, useCase.getSleepTime().indexOf(".")))
						* 1000);
			} else {
				sleepTime(Integer.valueOf(useCase.getSleepTime()) * 1000);
			}
		}
		if (useCase.getRun().equals("Y")) {
			execuTest(useCase.getParam(), useCase.getVerify(), useCase.getDescribe(), useCase.getUrl(),
					useCase.getMethod(), useCase.getSave());
		}
	}

	// 执行每一条用例
	protected void execuTestRaw(String param, String verify, String describe, String url, String method, String save) {
		System.out.println("--------------------------------------------------------");
		System.out.println("用例数据：" + "param:" + param + "  verify: " + verify + "  describe: " + describe + "  url: "
				+ url + "  method: " + method + "  save: " + save);

		param = getCommonParam(param);
		HashMap<String, Object> requesParams = new HashMap<>();
		if (!param.equals("null"))
			requesParams = JSON.parseObject(param, HashMap.class);
		requestvo.setRequestVo(headers, requesParams, domain + url);
		if (method.equals("Get")) {
			try {
				responsevo = HttpUtil.httpGet(requestvo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (method.equals("Post")) {
			responsevo = HttpUtil.httpPostRaw(requestvo);
		}
		if (responsevo.getHttpCode() >= 400) {
			Assert.assertEquals(responsevo.getHttpCode(), "200", "响应代码有误");
		} else if (!verify.equals("null")) {
			verifyResult(responsevo.getJson().toString(), verify, describe);
		}
		if (!(save.equals("null") || save.isEmpty())) {
			saveResult(responsevo.getJson().toString(), save);
		}
	}

	protected void execuTestRaw(UseCase useCase) {
		if (!useCase.getPreParam().equals("null")) {
			String preParam = buildParam(useCase.getPreParam());
			// 保存preParam参数到公共参数
			savePreParam(preParam);
		}
		if (!(useCase.getSleepTime().equals(null) || useCase.getSleepTime().equals("0")
				|| useCase.getSleepTime().equals("null"))) {
			if (useCase.getSleepTime().contains(".")) {// 读取表格的数值字符串时，默认多了一位小数，例如60，读取为60.0
				sleepTime(Integer.valueOf(useCase.getSleepTime().substring(0, useCase.getSleepTime().indexOf(".")))
						* 1000);
			} else {
				sleepTime(Integer.valueOf(useCase.getSleepTime()) * 1000);
			}
		}
		if (useCase.getRun().equals("Y")) {
			execuTestRaw(useCase.getParam(), useCase.getVerify(), useCase.getDescribe(), useCase.getUrl(),
					useCase.getMethod(), useCase.getSave());
		}
	}

	/**
	 * 取公共参数 并替换参数
	 *
	 * @param param
	 * @return
	 */
	protected String getCommonParam(String param) {

		Matcher m = replaceParamPattern.matcher(param);// 取公共参数正则
		while (m.find()) {
			String replaceKey = m.group(1);
			String value;
			// 从公共参数池中获取值
			value = getSaveData(replaceKey);
			// 如果公共参数池中未能找到对应的值，该用例失败。
			Assert.assertNotNull(value, String.format("格式化参数失败，公共参数中找不到%s。", replaceKey));
			param = param.replace(m.group(), value);
		}
		return param;
	}

	/**
	 * 获取公共数据池中的数据
	 *
	 * @param key 公共数据的key
	 * @return 对应的value
	 */
	protected String getSaveData(String key) {
		if ("".equals(key) || !saveDatas.containsKey(key)) {
			return null;
		} else {
			return saveDatas.get(key);
		}
	}

	/**
	 * 提取json串中的值保存至公共池中
	 *
	 * @param json    将被提取的json串。
	 * @param allSave 所有将被保存的数据：xx=$.jsonpath.xx;oo=$.jsonpath.oo，将$.jsonpath.
	 *                xx提取出来的值存放至公共池的xx中，将$.jsonpath.oo提取出来的值存放至公共池的oo中
	 */
	protected void saveResult(String json, String allSave) {
		if (null == json || "".equals(json) || null == allSave || "".equals(allSave)) {
			return;
		}
		allSave = getCommonParam(allSave);
		String[] saves = allSave.split(";");
		String key, value;
		for (String save : saves) {
			Pattern pattern = Pattern.compile("([^;=]*)=([^;]*)");
			Matcher m = pattern.matcher(save.trim());
			while (m.find()) {
				key = getBuildValue(json, m.group(1));
				value = getBuildValue(json, m.group(2));
				System.out.println(String.format("存储公共参数   %s值为：%s", key, value));
				saveDatas.put(key, value);
			}
		}
	}

	/**
	 * 获取格式化后的值
	 *
	 * @param sourchJson
	 * @param key
	 * @return
	 */
	private String getBuildValue(String sourchJson, String key) {
		key = key.trim();
		Matcher funMatch = funPattern.matcher(key);
		if (key.startsWith("$.") || key.startsWith("$[")) {// jsonpath
			key = JSONPath.read(sourchJson, key).toString();
		} else if (funMatch.find()) {
			String args = funMatch.group(2);
			String[] argArr = args.split(argsSpit);
			for (int index = 0; index < argArr.length; index++) {
				String arg = argArr[index];
				if (arg.startsWith("$.") || arg.startsWith("$[")) {
					argArr[index] = JSONPath.read(sourchJson, arg).toString();
				}
			}
			String value = FunctionUtil.getValue(funMatch.group(1), argArr);
			key = StringUtil.replaceFirst(key, funMatch.group(), value);

		}
		return key.trim();
	}

	protected void verifyResult(String sourchData, String verifyStr, String describe) {
		if (StringUtil.isEmpty(verifyStr)) {
			return;
		}
		String allVerify = getCommonParam(verifyStr);
		// 通过';'分隔，通过jsonPath进行一一校验
		Pattern pattern = Pattern.compile("([^;]*) = ([^;]*)");
		Assert.assertTrue(pattern.matcher(allVerify.trim()).find(), "没有找到合法格式的校验内容，合法格式：xx1 = oo1;xx2 = oo2;xx3 = oo3");
		Matcher m = pattern.matcher(allVerify.trim());
		while (m.find()) {
			String actualValue = getBuildValue(sourchData, m.group(1));
			String exceptValue = getBuildValue(sourchData, m.group(2));
			Assert.assertEquals(actualValue, exceptValue, describe + "：验证预期结果失败。");
			System.out.println("实际结果:" + actualValue + "  预期结果: " + exceptValue + " 验证预期结果成功。 ");

		}
	}

	protected void verifyResult(String sourchData, String verifyStr, boolean contains) {
		if (StringUtil.isEmpty(verifyStr) || verifyStr.equals("null")) {
			return;
		}
		String allVerify = getCommonParam(verifyStr);
		if (contains) {
			// 验证结果包含
			System.out.println("sourchData: " + sourchData);
			AssertUtil.contains(sourchData, allVerify);
			System.out.println("验证包含: " + " allVerify: " + verifyStr + " 成功。 ");
		}
	}

	/**
	 * 组件预参数（处理__fucn()以及${xxxx}）
	 *
	 */
	protected String buildParam(String preParam) {
		// 处理${}
		preParam = getCommonParam(preParam);
		Matcher m = funPattern.matcher(preParam);
		while (m.find()) {
			String funcName = m.group(1);
			String args = m.group(2);
			String value;
			// bodyfile属于特殊情况，不进行匹配，在post请求的时候进行处理
			if (FunctionUtil.isFunction(funcName) && !funcName.equals("bodyfile")) {
				// 属于函数助手，调用那个函数助手获取。
				// value = FunctionUtil.getValue(funcName, args.split(","));
				value = FunctionUtil.getValue(funcName, args.split(argsSpit));
				// 解析对应的函数失败
				Assert.assertNotNull(value, String.format("解析函数失败：%s。", funcName));
				preParam = StringUtil.replaceFirst(preParam, m.group(), value);
			}
		}
		return preParam;
	}

	protected void savePreParam(String preParam) {
		// 通过';'分隔，将参数加入公共参数map中
		if (StringUtil.isEmpty(preParam) || preParam.contains("bodyfile")) {
			return;
		}
		String[] preParamArr = preParam.split(";");
		String key, value;
		for (String prepar : preParamArr) {
			if (StringUtil.isEmpty(prepar)) {
				continue;
			}
			key = prepar.split(" = ")[0];
			value = prepar.split(" = ")[1];
			System.out.println(String.format("存储%s参数，值为：%s。", key, value));
			saveDatas.put(key, value);
		}
	}

	public void sleepTime(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
