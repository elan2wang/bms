/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.common.http;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.bms.common.ConfigUtil;
import com.bms.common.json.JsonFactory;

/**
 * @author wangjian
 * @create 2013年7月31日 上午11:04:41
 * @update TODO
 * 
 * 
 */
public class NormalResponse implements Response{

	private String apiVersion = ConfigUtil.getValue("apiVersion");
	private Map<String, Object> attrs;
	
	public String toJson() {
		String res = null;
		Map<String, Object> attributes = new LinkedHashMap<String, Object>();
		attributes.put("apiVersion", apiVersion);
		attributes.put("data", attrs);
		try {
			res = JsonFactory.toJson(attributes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String getApiVersion() {
		return apiVersion;
	}
	
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	
	public Map<String, Object> getAttrs() {
		return attrs;
	}
	
	public void setAttrs(Map<String, Object> attrs) {
		this.attrs = attrs;
	}

}
