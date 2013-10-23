/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author wangjian
 * @create 2013年8月1日 上午10:00:13
 * @update TODO
 * 
 * 
 */
public class AuthenticationToken implements Serializable {
	
	private static final long serialVersionUID = -2033617545841264626L;

	private final Integer uid;
	private final String username;
	private final String password;
	private final List<Integer> roles;
	private boolean isAuthenticated;
	
	public AuthenticationToken(String username, String password) {
		this.uid = null;
		this.username = username;
		this.password = password;
		this.roles = new ArrayList<Integer>();
		isAuthenticated = false;
	}
	
	public AuthenticationToken(Integer uid, String username, String password, 
			List<Integer> roles) {
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.roles = roles;
		isAuthenticated = true;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}

	public Integer getUid() {
		return uid;
	}
	
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<Integer> getRoles() {
		return roles;
	}
	
}
