/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.session;

import java.io.Serializable;
import java.util.Date;

import org.springframework.util.Assert;


/**
 * @author wangjian
 * @create 2013年7月31日 下午9:57:33
 * @update TODO
 * 
 * 
 */
public class SessionInfo implements Serializable{

	private static final long serialVersionUID = -2106374138781597547L;
	
	private Date lastRequest;
	private final String sessionId;
	private final Object principal;
	private boolean expired = false;
	
	public SessionInfo(Object principal, String sessionId, Date lastRequest) {
        Assert.notNull(principal, "Principal required");
        Assert.hasText(sessionId, "SessionId required");
        Assert.notNull(lastRequest, "LastRequest required");
        this.sessionId = sessionId;
        this.principal = principal;
        this.lastRequest = lastRequest;
    }

	public void expireNow() {
        this.expired = true;
    }
	
	public Date getLastRequest() {
		return lastRequest;
	}

	public boolean isExpired() {
		return expired;
	}

	public String getSessionId() {
		return sessionId;
	}

	public Object getPrincipal() {
		return principal;
	}

	public void refreshLastRequest() {
        this.lastRequest = new Date();
    }
   
}
