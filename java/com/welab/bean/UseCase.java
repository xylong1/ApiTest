package com.welab.bean;


public class UseCase {
	
	//用例名称
	private String param;
	//用例断言
	private String verify;
	//用例描述
	private String Describe;
	//接口地址
	private String url;
	//请求方式
	private String method;
	//保存公共参数
	private String save;
	//设置前置公共参数
	private String preParam;
	//是否执行
	private String run;
	//等待时间
	private String SleepTime;
	
	public String getSleepTime() {
		return SleepTime;
	}
	public void setSleepTime(String sleepTime) {
		SleepTime = sleepTime;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getVerify() {
		return verify;
	}
	public void setVerify(String verify) {
		this.verify = verify;
	}
	public String getDescribe() {
		return Describe;
	}
	public void setDescribe(String describe) {
		Describe = describe;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getSave() {
		return save;
	}
	public void setSave(String save) {
		this.save = save;
	}
	public String getPreParam() {
		return preParam;
	}
	public void setPreParam(String preParam) {
		this.preParam = preParam;
	}
	public String getRun() {
		return run;
	}
	public void setRun(String run) {
		this.run = run;
	}
				

}
