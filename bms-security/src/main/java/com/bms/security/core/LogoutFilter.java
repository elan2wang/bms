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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bms.common.AppContext;

/**
 * @author wangjian
 * @create 2013年8月3日 下午4:23:40
 * @update TODO
 * 
 * 
 */
public class LogoutFilter implements Filter{

	private static Logger log = LoggerFactory.getLogger(LogoutFilter.class);
	private LogoutSuccessHandler logoutSuccessHandler;

	private String logoutUrl = "/1/logout";	
	
	public void init(FilterConfig filterConfig) throws ServletException {
		logoutSuccessHandler = (LogoutSuccessHandler) AppContext.getBean("logoutSuccessHandler");
		if (filterConfig.getInitParameter("logoutUrl") != null) {
			logoutUrl = filterConfig.getInitParameter("logoutUrl");
		}
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		log.debug("enter into LogoutFilter ===================================");
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;

		if (requireLogout(request, response)) {
			// invalidate session
			HttpSession session = request.getSession(false);
			if (session != null) {
				log.debug("Invalidating session: " + session.getId());
	            session.invalidate();
	        }
			// clear SecurityContextHolder
			SecurityContextHolder.clearContext();
			
			// clear Cookies
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				log.debug(cookie.getName());
	            cookie.setMaxAge(0);
	            response.addCookie(cookie);
			}

			logoutSuccessHandler.onLogoutSuccess(request, response);
		}
	}

	public void destroy() {
		
	}
	
	private boolean requireLogout(HttpServletRequest request, HttpServletResponse response) {
		String uri = request.getRequestURI();
		log.debug("the request uri: "+uri);
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
            return uri.endsWith(logoutUrl);
        }
        
        return uri.endsWith(request.getContextPath()+logoutUrl);
	}
}
