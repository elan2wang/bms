/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.bms.common.http.NormalResponse;

/**
 * @author wangjian
 * @create 2013年8月4日 下午10:16:11
 * @update TODO
 * 
 * 
 */
@Component
public class LogoutSuccessHandler {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		Map<String, Object> attrs = new LinkedHashMap();
		attrs.put("result_code", 10000);
		attrs.put("result_msg", "logout successfully");
		NormalResponse res = new NormalResponse();
		res.setAttrs(attrs);
		
		response.setContentType("application/json;charset=UTF-8");
		response.getOutputStream().write(res.toJson().getBytes());
		return;
	}
}
