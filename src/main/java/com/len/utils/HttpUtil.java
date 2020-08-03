package com.len.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.len.bean.RequestVo;
import com.len.bean.ResponseVo;

public class HttpUtil {

	static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public static ResponseVo login(String username, String password, String userType, String url) {
		RequestVo rv = new RequestVo();
		HashMap params = new HashMap();
		params.put("username", username);
		params.put("password", password);
		params.put("userType", userType);
		HashMap headers = new HashMap();
		headers.put("Content-Type", "application/json");
		rv.setRequestUrl(url);
		rv.setRquestHeaders(headers);
		rv.setRequestParams(params);
		return httpPostRaw(rv);
	}

	/**
	 * get request
	 *
	 * @param //RequestVo 通过hashmap赋值获得参数键值对，以及请求头信息
	 * @param //headers http请求头信息
	 * @return InterfaceReturnVo 接口调用返回结果数据对象，包括cookie信息及json串
	 * @throws Exception
	 */

	@SuppressWarnings({ "deprecation", "null" })
	public static ResponseVo httpGet(RequestVo requestvo) throws Exception {

		ResponseVo responsevo = new ResponseVo();
		List<Cookie> responsecookie = null;
		JSONObject json = null;
		String responseStr = null;
		String rUrl = requestvo.getRequestUrl() + "?";
		@SuppressWarnings({ "resource" })
		HttpClient client = new DefaultHttpClient();
		CookieStore cookieStore = new BasicCookieStore();
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		rUrl = setGetRequestParam(requestvo, rUrl);
		System.out.println("请求地址: " + rUrl);
		System.out.println("请求方式: GET");
		try {
			HttpGet httpget = new HttpGet(requestvo.getRequestUrl());
			if (!requestvo.getRequestHeaders().isEmpty()) {
				Set<?> entrySet = requestvo.getRequestHeaders().entrySet();
				for (Iterator<?> itor = entrySet.iterator(); itor.hasNext();) {
					Entry<?, ?> entry = (Entry<?, ?>) itor.next();
					httpget.addHeader(entry.getKey().toString(), entry.getValue().toString());
				}
			}

			long startTime = System.currentTimeMillis();
			HttpResponse httpResponse = client.execute(httpget, localContext);
			long endTime = System.currentTimeMillis();
			System.out.println("响应时间:" + (endTime - startTime) + "ms");
			responsecookie = cookieStore.getCookies();
			responsevo.setCookies(responsecookie);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			responsevo.setHttpCode(responseCode);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				org.apache.http.Header head = httpResponse.getFirstHeader("Content-Type");
				if (head.getValue().contains("application/json")) {
					json = JSON.parseObject(EntityUtils.toString(entity));
					System.err.println("响应参数:" + json);
					responsevo.setJson(json);
				} else {
					responseStr = EntityUtils.toString(entity);
					System.err.println("响应参数:" + responseStr.length());
					responsevo.setResponseStr(responseStr);
				}
			} else {
				System.err.println("http response is null");
			}
			httpget.abort();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return responsevo;
	}

	/**
	 * Post 请求 通过raw方式提交
	 */
	public static ResponseVo httpPostRaw(RequestVo requestvo) {
		ResponseVo responsevo = new ResponseVo();
		CloseableHttpClient httpClient = null;
		HttpPost httpPost = null;
		JSONObject json = new JSONObject();
		HttpContext localContext = new BasicHttpContext();
		try {
			httpClient = HttpClients.createDefault();

			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000)
					.build();
			httpPost = new HttpPost(requestvo.getRequestUrl());
			// 修改编码类型（如不需修改请注释）
			HashMap<String, String> header = requestvo.getRequestHeaders();
			// header.put("Content-Type", "application/json;charset=UTF-8");
			requestvo.setRquestHeaders(header);

			addAllHeaders(httpPost, header);
			httpPost.setConfig(requestConfig);
			setAllBody(httpPost, requestvo);
			long startTime = System.currentTimeMillis();
			CloseableHttpResponse response = httpClient.execute(httpPost, localContext);
			long endTime = System.currentTimeMillis();
			System.out.println("响应时间 :" + (endTime - startTime) + "ms");
			@SuppressWarnings("deprecation")
			CookieStore cookieStore = (CookieStore) localContext.getAttribute(ClientContext.COOKIE_STORE);
			responsevo.setCookies(cookieStore.getCookies());
			
			int responseCode = response.getStatusLine().getStatusCode();
			responsevo.setHttpCode(responseCode);

			if (responseCode == 200 || responseCode == 201) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					json = JSON.parseObject(EntityUtils.toString(entity));
					responsevo.setJson(json);
					responsevo.setResponseStr(json.toString());
					System.out.println("响应参数:" + json.toString());
				} else {
					System.out.println("响应为空");
				}
			} else {
				String responseStr = EntityUtils.toString(response.getEntity());
				System.out.println(responseStr);
				responsevo.setResponseStr(responseStr);
				System.out.println("http POST request return error, error code is " + responseCode);
			}
			httpPost.abort();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (httpPost != null) {
					httpPost.releaseConnection();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responsevo;
	}

	/**
	 * post request 通过表单方式提交
	 *
	 * @param requestvo
	 * @param           //cookiename 本次接口调用需用到的其他接口cookie名
	 * @return InterfaceReturnVo 接口调用返回结果数据对象，包括cookie信息及json串
	 */
	@SuppressWarnings("deprecation")
	public static ResponseVo httpPost(RequestVo requestvo) {

		ResponseVo responsevo = new ResponseVo();

		@SuppressWarnings({ "resource" })
		HttpClient client = new DefaultHttpClient();

		CookieStore cookieStore = new BasicCookieStore();

		HttpContext localContext = new BasicHttpContext();

		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		List<Cookie> responsecookie = null;

		JSONObject json = new JSONObject();

		try {
			HttpPost httppost = new HttpPost(requestvo.getRequestUrl());

			if (!requestvo.getRequestHeaders().isEmpty() || null != requestvo.getRequestHeaders()) {

				Set<?> entrySet = requestvo.getRequestHeaders().entrySet();
				for (Iterator<?> itor = entrySet.iterator(); itor.hasNext();) {
					Entry<?, ?> entry = (Entry<?, ?>) itor.next();
					httppost.setHeader(entry.getKey().toString(), entry.getValue().toString());
				}

			}
			if (!requestvo.getRequestParams().isEmpty() || null != requestvo.getRequestParams()) {

				List<NameValuePair> parameters = new ArrayList<NameValuePair>();

				Set<?> set = requestvo.getRequestParams().entrySet();

				Iterator<?> iterator = set.iterator();

				while (iterator.hasNext()) {
					@SuppressWarnings("rawtypes")
					Entry mapentry = (Entry) iterator.next();
					String value = mapentry.getValue().toString();
					if (value.startsWith("[")) {
						value = value.substring(1, value.length() - 1);
					}
					parameters.add(new BasicNameValuePair(mapentry.getKey().toString(), value));
				}
				System.out.println("请求地址：  " + requestvo.getRequestUrl());

				System.out.println("请求参数：  " + parameters);

				System.out.println("请求方式 ：  POST");

				UrlEncodedFormEntity formEntiry = new UrlEncodedFormEntity(parameters, "utf-8");

				httppost.setEntity(formEntiry);
			}

			long startTime = System.currentTimeMillis();
			HttpResponse httpResponse = client.execute(httppost, localContext);
			long endTime = System.currentTimeMillis();
			System.out.println("响应时间：  " + (endTime - startTime) + "ms");

			responsecookie = cookieStore.getCookies();

			responsevo.setCookies(responsecookie);

			int responseCode = httpResponse.getStatusLine().getStatusCode();
			responsevo.setHttpCode(responseCode);
			if (responseCode == 200 || responseCode == 201) {
				HttpEntity entity = httpResponse.getEntity();

				if (entity != null) {

					json = JSON.parseObject(EntityUtils.toString(entity));

					responsevo.setJson(json);

					System.out.println("响应参数 ： " + json);
				} else {
					System.out.println("entity is null");
				}
			} else {
				System.out.println(EntityUtils.toString(httpResponse.getEntity()));
				System.out.println("http POST request return error, error code is " + responseCode);
			}
			httppost.abort();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responsevo;
	}

	private static void addAllHeaders(HttpPost httppost, HashMap<String, String> header) {
		if (!header.isEmpty() || null != header) {
			Set<?> entrySet = header.entrySet();
			for (Iterator<?> itor = entrySet.iterator(); itor.hasNext();) {
				Entry<?, ?> entry = (Entry<?, ?>) itor.next();
				httppost.addHeader(entry.getKey().toString(), entry.getValue().toString());
			}
		}
	}

	private static void setAllBody(HttpPost httpPost, RequestVo requestvo) {
		System.out.println("请求地址: " + requestvo.getRequestUrl());
		System.out.println("请求方式 : POST");
		System.out.println("请求参数 : " + requestvo.getRequestParams());
		System.out.println("请求参数(Json) : " + requestvo.getRequestJsonStr());
		if (!requestvo.getRequestParams().isEmpty() || null != requestvo.getRequestParams()) {
			JSONObject httpbody;
			if (requestvo.getRequestParams().size() == 0) {
				if (requestvo.getRequestJsonStr().length() == 0) {
					httpbody = new JSONObject();
				} else {
					httpbody = JSONObject.parseObject(requestvo.getRequestJsonStr());
				}
			} else {
				String jsonString = JSONObject.toJSONString(requestvo.getRequestParams());
				httpbody = JSON.parseObject(jsonString);
			}
			if (requestvo.getRequestUrl().contains("oss")) {
				httpPost.setEntity(new StringEntity(httpbody.toJSONString(), "utf-8"));
			} else {
				httpPost.setEntity(new StringEntity(httpbody.toJSONString(), "utf-8"));
			}
		}
	}

	public static String setGetRequestParam(RequestVo requestvo, String result) {
		if (requestvo != null) {

			if (!requestvo.getRequestParams().isEmpty()) {
				result += prepareParam(requestvo.getRequestParams());
				requestvo.setRequestUrl(result);
			} else {
				result = result.substring(0, result.length() - 1);
			}
		}
		return result;
	}

	/**
	 * 设置参数URI
	 *
	 * @param //Object object
	 * @return String URI
	 * @throws Exception
	 */
	public static String prepareParam(HashMap<String, Object> hashMap) {
		StringBuffer result = new StringBuffer();
		if (hashMap.isEmpty()) {
			return "";
		} else {
			for (String key : hashMap.keySet()) {
				String value = (String) hashMap.get(key);
				if (result.length() < 1) {
					result.append(key).append("=").append(value);
				} else {
					result.append("&").append(key).append("=").append(value);
				}
			}
		}
		return result.toString();
	}
}
