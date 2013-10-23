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
import com.bms.security.SecuritySupport;
import com.bms.security.exception.AccessDeniedException;
import com.bms.security.exception.AuthenticationException;
import com.bms.security.exception.ExceptionHandler;
import com.bms.security.exception.NullAuthenticationException;
import com.bms.security.session.SessionRegistry;

/**
 * @author wangjian
 * @create 2013年8月1日 下午8:03:12
 * @update TODO
 * 
 * 
 */
public class SecurityControlFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(SecurityControlFilter.class);

	private AuthenticationManager authenticationManager; 
	private AccessDecisionManager accessDecisionManager;
	private ExceptionHandler exceptionHandler;
	private SessionRegistry sessionRegistry;

	public void init(FilterConfig filterConfig) throws ServletException {
		authenticationManager = (AuthenticationManager) AppContext.getBean("authenticationManager");
		accessDecisionManager = (AccessDecisionManager) AppContext.getBean("accessDecisionManager");
		exceptionHandler = (ExceptionHandler) AppContext.getBean("exceptionHandler");
		sessionRegistry = SessionRegistry.getInstance();
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		log.debug("enter into SecurityControlFilter ============================");
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		String uri = request.getRequestURI();
		// if the request resource is public
		if (SecuritySupport.isPublic(uri)) {
			log.debug("public resource -- authentication not attempt");
			chain.doFilter(request, response);
			return;
		}
		
		// attempt to authenticate
		AuthenticationToken authenticated = null;
		try {
			authenticated = authenticateIfRequired(request, response);
		} catch (AuthenticationException e) {
			exceptionHandler.handle(e, request, response);
			return;
		}

		// attempt to make access decision
		try {
			accessDecisionManager.decide(authenticated, request.getRequestURI());
			log.debug("authorization successful");
		} catch (AccessDeniedException e) {
			exceptionHandler.handle(e, request, response);
			return; 
		}
		
		// refresh last request
		sessionRegistry.refreshLastRequest(request.getSession().getId());
		
		// filter chain
		chain.doFilter(request, response);
	}

	private AuthenticationToken authenticateIfRequired(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {		
		AuthenticationToken token = SecurityContextHolder.getContext().getAuthenticationToken();

		if (token == null) {
			log.debug("authenticationToken is null");
			throw new NullAuthenticationException("you haven't login or your session is timeout");
		}

		if (token.isAuthenticated()) {
			log.debug("previously authenticated: "+token);
			return token;
		}

		token = authenticationManager.authenticate(token, request, response);

		SecurityContextHolder.getContext().setAuthenticationToken(token);

		return token;
	}

	public void destroy() { }

}
