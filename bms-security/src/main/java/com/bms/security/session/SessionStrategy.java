/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.session;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bms.security.core.AuthenticationToken;

/**
 * @author wangjian
 * @create 2013年8月1日 下午2:07:03
 * @update TODO
 * 
 * 
 */
@Component
public class SessionStrategy {

	private static Logger log = LoggerFactory.getLogger(SessionStrategy.class);
	
	private SessionRegistry sessionRegistry;
	private int maximumSessions = 1;
	
	public SessionStrategy() {
		this.sessionRegistry = SessionRegistry.getInstance();
	}
	
	public void onAuthentication(AuthenticationToken token,
			HttpServletRequest request, HttpServletResponse response) {
		checkAuthenticationAllowed(token, request);
		log.debug("authentication token : " + token.getUsername());
		sessionRegistry.registerNewSession(request.getSession().getId(), token.getUsername());
	}
	
	private void checkAuthenticationAllowed(AuthenticationToken token, HttpServletRequest request) {
		
		List<SessionInfo> sessions = sessionRegistry.getAllSessions(token.getUsername(), false);
		
		int sessionCount = sessions.size();
		log.debug(token.getUsername()+" already have "+sessionCount+" user online");
		int allowedSessions = maximumSessions;
		
		if (sessionCount < allowedSessions) {
			log.debug("sessionCount="+sessionCount+" haven't exceed allowed count="+allowedSessions);
			// they haven't got too many login sessions running at present
			return;
		}
		if (allowedSessions == -1) {
			log.debug("we permit unlimited logins");
			// we permit unlimited logins
			return;
		}
		
		if (sessionCount == allowedSessions) {
			log.debug("session count equal to allowedSessions");
			HttpSession session = request.getSession(false);
			if (session != null) {
				for (SessionInfo si : sessions) {
					if (si.getSessionId().equals(session.getId())) {
						log.debug("current session is already exist");
						return;
					}
				}
			}
		}
		// expire the least recently used session
		allowableSessionExceed(sessions, allowedSessions, sessionRegistry);
	}
	
	private void allowableSessionExceed(List<SessionInfo> sessions, int allowableSessions,
			SessionRegistry sessionRegistry) {
		SessionInfo leastRecentlyUsed = null;
		for (SessionInfo si : sessions) {
			if((leastRecentlyUsed == null) || si.getLastRequest().before(leastRecentlyUsed.getLastRequest())) {
				leastRecentlyUsed = si;
			}
		}
		log.debug("sessionid : " + leastRecentlyUsed.getSessionId() + "will be expired now");
		leastRecentlyUsed.expireNow();
	}
	
	public void setMaximumSessions(int maximumSessions) {
		this.maximumSessions = maximumSessions;
	}
}
