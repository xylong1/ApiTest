package com.len.bean;

import java.util.HashMap;

/**
 * @author Len.Xie
 *
 */
public class RequestVo {

	private String _requesturl = "";
	private HashMap<String, Object> _requestparams = new HashMap<String, Object>();
	private HashMap<String, String> _requestheaders = new HashMap<String, String>();
	private String _requestJsonStr;
	private String _requestSet = "";

	public String getRequestUrl() {
		return _requesturl;
	}

	public void setRequestUrl(String requesturl) {
		this._requesturl = requesturl;
	}

	public HashMap<String, Object> getRequestParams() {
		return _requestparams;
	}

	public String getRequestJsonStr() {
		return _requestJsonStr;
	}

	public void setRequestJsonStr(String _requestJsonStr) {
		this._requestJsonStr = _requestJsonStr;
	}

	public void setRequestParams(HashMap<String, Object> params) {
		this._requestparams = params;
	}

	public HashMap<String, String> getRequestHeaders() {
		return _requestheaders;
	}

	public void setRquestHeaders(HashMap<String, String> _requestheaders) {
		this._requestheaders = _requestheaders;
	}

	public void setRequestVo(HashMap<String, String> headers, HashMap<String, Object> params, String requesturl) {
		setRquestHeaders(headers);
		setRequestParams(params);
		setRequestUrl(requesturl);
	}

	public void setRequestVo(HashMap<String, String> headers, String requestJsonStr, String requesturl) {
		setRquestHeaders(headers);
		setRequestJsonStr(requestJsonStr);
		setRequestUrl(requesturl);
	}

	public String getRequestSet() {
		return _requestSet;
	}

	public void setRequestSet(String requestSet) {
		this._requestSet = requestSet;
	}

}
