/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.common.http;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bms.common.json.JsonFactory;

/**
 * @author wangjian
 * @create 2013年7月31日 上午11:04:28
 * @update TODO
 * 
 * 
 */
public class ErrorResponse implements Response{
	
	private String request;
	private String error_code;
	private String error_msg;
	
	public ErrorResponse() {}
	
	public ErrorResponse(String request, String error_code, String error_msg) {
		this.request = request;
		this.error_code = error_code;
		this.error_msg = error_msg;
	}
	
	public String toJson() {
		String res = null;
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		attrs.put("request", request);
		attrs.put("error_code", error_code);
		attrs.put("error_msg", error_msg);
		try {
			res = JsonFactory.toJson(attrs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	
}
