/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author wangjian
 * @create 2013年7月31日 下午10:16:23
 * @update TODO
 * 
 * 
 */
public class SessionRegistry {

	private static Logger log = LoggerFactory.getLogger(SessionRegistry.class);
	
	/** <principal:Object,SessionIdSet> */
	private final ConcurrentMap<Object, Set<String>> principals = new ConcurrentHashMap<Object, Set<String>>();
	/** <sessionId:Object,SessionInfo> */
	private final ConcurrentMap<Object, SessionInfo> sessionIds = new ConcurrentHashMap<Object, SessionInfo>();
	
	/**
	 * get SessionRegistry singleton
	 */
	private static SessionRegistry sessionRegistry;
	private SessionRegistry() { }
	
	public static SessionRegistry getInstance() {
		if (sessionRegistry == null) {
			sessionRegistry = new SessionRegistry();
		}
		return sessionRegistry;
	}
	
	/**
	 * retrieve all the principals
	 *
	 * @return List<Object> a <code>List</code> of principals
	 */
	public List<Object> getAllPrincipals() {
		return new ArrayList<Object>(principals.keySet());
	}
	
	public List<SessionInfo> getAllSessions(Object principal, boolean includeExpiredSessions) {
		final Set<String> sessionsUsedByPrincipal = principals.get(principal);
		
		if (sessionsUsedByPrincipal == null) {
			log.debug(principal + " have no user online");
			return Collections.emptyList();
		}
		
		List<SessionInfo> list = new ArrayList<SessionInfo>(sessionsUsedByPrincipal.size());
		log.debug("sessioninfo used by "+principal+" is "+list.size());
		for (String sessionId : sessionsUsedByPrincipal) {
			SessionInfo sessionInformation = getSessionInfo(sessionId);
			if (sessionInformation == null) {
				log.debug("sessionid "+sessionId+" info is empty");
				continue;
			}
			
			if (includeExpiredSessions || !sessionInformation.isExpired()) {
				log.debug("session added");
				list.add(sessionInformation);
			}
		}
		return list;
	}
	
	public SessionInfo getSessionInfo(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");

        return sessionIds.get(sessionId);
    }

	public void registerNewSession(String sessionId, Object principal) {
		if(getSessionInfo(sessionId) != null) {
			removeSessionInformation(sessionId);
		}
		
		sessionIds.put(sessionId, new SessionInfo(principal, sessionId, new Date()));
		Set<String> sessionsUsedByPrincipal = principals.get(principal);
		
		if (sessionsUsedByPrincipal == null) {
			sessionsUsedByPrincipal = new CopyOnWriteArraySet<String>();
			Set<String> prevSessionsUsedByPrincipal = principals.putIfAbsent(principal, sessionsUsedByPrincipal);
			if (prevSessionsUsedByPrincipal != null) {
                sessionsUsedByPrincipal = prevSessionsUsedByPrincipal;
            }
		}
		
		sessionsUsedByPrincipal.add(sessionId);
	}
	
	public void removeSessionInformation(String sessionId) {
		Assert.hasText(sessionId, "SessionId required as per interface contract");

        SessionInfo info = getSessionInfo(sessionId);

        if (info == null) {
            return;
        }
        
        sessionIds.remove(sessionId);
        
        Set<String> sessionsUsedByPrincipal = principals.get(info.getPrincipal());
        if (sessionsUsedByPrincipal == null) {
            return;
        }
        sessionsUsedByPrincipal.remove(sessionId);
        if(sessionsUsedByPrincipal.isEmpty()) {
        	principals.remove(info.getPrincipal());
        }
	}
	
	public void refreshLastRequest(String sessionId) {
        Assert.hasText(sessionId, "SessionId required as per interface contract");

        SessionInfo info = getSessionInfo(sessionId);

        if (info != null) {
            info.refreshLastRequest();
        }
    }
}
