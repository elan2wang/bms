/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bms.security.service.SecurityService;

/**
 * @author wangjian
 * @create 2013年8月4日 下午10:51:14
 * @update TODO
 * 
 * 
 */
@Component
public class LoginSuccessHandler {

	@Autowired
	private SecurityService securityService;
	
	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			AuthenticationToken token) throws IOException {
		String username = token.getUsername();
		
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		securityService.updateLastLoginInfo(username, ip);
	}

}
