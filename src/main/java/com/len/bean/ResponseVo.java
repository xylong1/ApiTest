package com.len.bean;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.cookie.Cookie;

import java.util.List;

/**
 * @author len.xie
 *
 */
public class ResponseVo {

	private JSONObject json = null;
	private String responseStr = "";
	private int httpCode = 0;

	private List<Cookie> cookies;

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public List<Cookie> getCookies() {
		return cookies;
	}

	public void setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}

	public String getResponseStr() {
		return responseStr;
	}

	public void setResponseStr(String responseStr) {
		this.responseStr = responseStr;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int _httpcode) {
		this.httpCode = _httpcode;
	}

	public String getCookiesToString() {
		StringBuffer result = new StringBuffer();
		for (Cookie c : cookies) {
			// System.out.println("cookie begin***\n" + c +
			// "\n cookie end");
			result.append(c.getName() + "=" + c.getValue() + "; ");
		}
		result.deleteCharAt(result.length() - 2);
		return result.toString();
	}
}
