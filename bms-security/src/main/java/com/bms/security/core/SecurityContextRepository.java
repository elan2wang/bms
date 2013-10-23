/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bms.security.session.SessionInfo;
import com.bms.security.session.SessionRegistry;

/**
 * @author wangjian
 * @create 2013年8月3日 下午10:16:08
 * @update TODO
 * 
 * 
 */
@Component
public class SecurityContextRepository {

	private static Logger log = LoggerFactory.getLogger(SecurityContextRepository.class);
	
	private final String securityContextKey = "SECURITY_CONTEXT";
	private SessionRegistry sessionRegistry = SessionRegistry.getInstance();
	
	/**
	 * Load SecurityContext from session
	 * 
	 */
	public SecurityContext loadContext(
			HttpRequestResponseHolder requestResponseHolder) {
		HttpServletRequest request = requestResponseHolder.getRequest();
        HttpSession session = request.getSession(false);
        
        // if the session already expired
        if (session != null) {
        	SessionInfo sessionInfo = sessionRegistry.getSessionInfo(session.getId());
            if (sessionInfo != null && sessionInfo.isExpired()) {
            	log.debug("sessionid "+session.getId()+" will be expired immediately");
            	session.invalidate();
            	return generateNewContext();
            }
        }
        
        // if the session haven't expired
        SecurityContext context = readSecurityContextFromSession(session);
        
        if (context == null) {
        	generateNewContext();
        }
        
        return context;
	}

	/**
	 * Save SecurityContext to session
	 * 
	 */
	public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
		AuthenticationToken token = context.getAuthenticationToken();
		HttpSession session = request.getSession(false);

		if (token == null) {
			if (session != null) {
				session.removeAttribute(securityContextKey);
			}
			return;
		}
		
		if (session != null) {
			session.setAttribute(securityContextKey, context);
		}
	}
	
	public boolean containsContext(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		
		if (session == null) {
			return false;
		}
		
		return session.getAttribute(securityContextKey) != null;
		
	}
	
	/**
	 * Read SecurityContext from session
	 *
	 * @param session a <code>HttpSession</code> from which to read SecurityContext
	 * @return SecurityContext
	 */
	private SecurityContext readSecurityContextFromSession(HttpSession session) {
		if (session == null) {
			return null;
		}
		
		Object contextFromSession = session.getAttribute(securityContextKey);
		
		if (contextFromSession == null) {
			return null;
		}
		
		if (!(contextFromSession instanceof SecurityContext)) {
			return null;
		}
		
		return (SecurityContext) contextFromSession;
	}
	
	private SecurityContext generateNewContext() {
		return SecurityContextHolder.createEmptyContext();
	}
}
