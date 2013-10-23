/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bms.common.AppContext;
import com.bms.security.exception.AuthenticationException;
import com.bms.security.exception.ExceptionHandler;

/**
 * @author wangjian
 * @create 2013年8月4日 下午2:43:17
 * @update TODO
 * 
 * 
 */
public class LoginFilter implements Filter{

	private static Logger log = LoggerFactory.getLogger(LoginFilter.class);

	private AuthenticationManager authenticationManager;
	private LoginSuccessHandler loginSuccessHandler;
	private ExceptionHandler exceptionHandler;

	private String loginUrl = "/1/login";
	private String principal_name = "username";
	private String credential_name = "password";
		
	public void init(FilterConfig filterConfig) throws ServletException {
		authenticationManager = (AuthenticationManager) AppContext.getBean("authenticationManager");
		loginSuccessHandler = (LoginSuccessHandler) AppContext.getBean("loginSuccessHandler");
		exceptionHandler = (ExceptionHandler)AppContext.getBean("exceptionHandler");

		if (filterConfig.getInitParameter("loginUrl") != null) {
			loginUrl = filterConfig.getInitParameter("loginUrl");
		}
		if (filterConfig.getInitParameter("principal_name") != null) {
			principal_name = filterConfig.getInitParameter("principal_name");
		}
		if (filterConfig.getInitParameter("credential_name") != null) {
			credential_name = filterConfig.getInitParameter("credential_name");
		}
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		log.debug("enter into LoginFilter ===============================");
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		
		if (requireLogin(request, response)) {
			String username = request.getParameter(principal_name);
			String password = request.getParameter(credential_name);
			
			AuthenticationToken token = new AuthenticationToken(username, password);
			try {
				token = authenticationManager.authenticate(token, request, response);
			} catch (AuthenticationException e) {
				exceptionHandler.handle(e, request, response);
				return;
			}
			SecurityContextHolder.getContext().setAuthenticationToken(token);
			loginSuccessHandler.onLoginSuccess(request, response, token);
		}
		
		chain.doFilter(request, response);
	}

	public void destroy() {
	}

	private boolean requireLogin(HttpServletRequest request, HttpServletResponse response) {
		String uri = request.getRequestURI();
        int pathParamIndex = uri.indexOf(';');

        if (pathParamIndex > 0) {
            // strip everything from the first semi-colon
            uri = uri.substring(0, pathParamIndex);
        }

        int queryParamIndex = uri.indexOf('?');

        if (queryParamIndex > 0) {
            // strip everything from the first question mark
            uri = uri.substring(0, queryParamIndex);
        }

        if ("".equals(request.getContextPath())) {
            return uri.endsWith(loginUrl);
        }
        
        return uri.endsWith(request.getContextPath()+loginUrl);
	}

}
