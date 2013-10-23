/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

import java.io.Serializable;

/**
 * @author wangjian
 * @create 2013年8月1日 上午9:59:30
 * @update TODO
 * 
 * 
 */
public class SecurityContext implements Serializable{

	private static final long serialVersionUID = 769502677595645574L;
	
	private AuthenticationToken authenticationToken;
	
	public boolean equals(Object obj) {
        if (obj instanceof SecurityContext) {
        	SecurityContext test = (SecurityContext) obj;

            if ((this.getAuthenticationToken() == null) && (test.getAuthenticationToken() == null)) {
                return true;
            }

            if ((this.getAuthenticationToken() != null) && (test.getAuthenticationToken() != null)
                && this.getAuthenticationToken().equals(test.getAuthenticationToken())) {
                return true;
            }
        }

        return false;
    }

    public AuthenticationToken getAuthenticationToken() {
    	return authenticationToken;
    }

    public void setAuthenticationToken(AuthenticationToken authenticationToken) {
    	this.authenticationToken = authenticationToken;
    }

}
