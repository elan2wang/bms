/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.session;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangjian
 * @create 2013年8月3日 下午6:47:46
 * @update TODO
 * 
 * 
 */
public class SessionListener implements HttpSessionListener {

	private static Logger log = LoggerFactory.getLogger(SessionListener.class);
	private SessionRegistry sessionRegistry = SessionRegistry.getInstance();
	
	public void sessionCreated(HttpSessionEvent se) {
		String sessionid = se.getSession().getId();
		log.info("new session created, sessionid : "+sessionid);
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		String sessionId = se.getSession().getId();
		sessionRegistry.removeSessionInformation(sessionId);
	}
	
}
