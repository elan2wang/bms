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

/**
 * @author wangjian
 * @create 2013年8月3日 下午11:00:57
 * @update TODO
 * 
 * 
 */
public class SecurityContextPersistenceFilter implements Filter{

	private static Logger log = LoggerFactory.getLogger(SecurityContextPersistenceFilter.class);
	private SecurityContextRepository repo;
    
	public void init(FilterConfig filterConfig) throws ServletException {
		this.repo = (SecurityContextRepository)
				AppContext.getBean("securityContextRepository");
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		log.debug("enter into SecurityContextPersistenceFilter =============================");
		
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        log.debug("the request url : " + request.getRequestURL());
        
        HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
        SecurityContext contextBeforeChainExecution = repo.loadContext(holder);
        try {
        	SecurityContextHolder.setContext(contextBeforeChainExecution);
        	chain.doFilter(holder.getRequest(), holder.getResponse());
        } finally {
        	SecurityContext contextAfterChainExecution = SecurityContextHolder.getContext();
        	SecurityContextHolder.clearContext();
        	repo.saveContext(contextAfterChainExecution, holder.getRequest(), holder.getResponse());
        }
        
	}

	public void destroy() {
		this.repo = null;
	}

}
