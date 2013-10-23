/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.bms.common.ConfigUtil;
import com.bms.common.http.ErrorResponse;

/**
 * @author wangjian
 * @create 2013年8月2日 上午11:35:42
 * @update TODO
 * 
 * 
 */
@Component
public class ExceptionHandler {

	public void handle(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uri = request.getRequestURI();
		ErrorResponse res = null;
		
		if (e instanceof BadPrincipalsException) {
			res = new ErrorResponse(uri, "10004", ConfigUtil.getValue("10004"));
		}
		if (e instanceof BadCredentialsException) {
			res = new ErrorResponse(uri, "10005", ConfigUtil.getValue("10005"));
		}
		if (e instanceof NullAuthenticationException) {
			res = new ErrorResponse(uri, "10006", ConfigUtil.getValue("10006"));
		}
		if (e instanceof AccessDeniedException) {
			res = new ErrorResponse(uri, "10007", ConfigUtil.getValue("10007"));
		}
		
		response.setContentType("application/json;charset=UTF-8");
		response.getOutputStream().write(res.toJson().getBytes());
		return;
	}
	
}
