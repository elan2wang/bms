/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.pojo;

import java.io.Serializable;

/**
 * @author wangjian
 * @create 2013年8月15日 上午10:27:04
 * @update TODO
 * 
 * 
 */
public class AuthorityResource implements Serializable{

	private static final long serialVersionUID = -5658932247479046033L;

	private Integer auth_id;
	private Integer res_id;
	
	public AuthorityResource() {
		super();
	}

	public AuthorityResource(Integer auth_id, Integer res_id) {
		super();
		this.auth_id = auth_id;
		this.res_id = res_id;
	}

	public Integer getAuth_id() {
		return auth_id;
	}

	public void setAuth_id(Integer auth_id) {
		this.auth_id = auth_id;
	}

	public Integer getRes_id() {
		return res_id;
	}

	public void setRes_id(Integer res_id) {
		this.res_id = res_id;
	}

	@Override
	public String toString() {
		return "AuthorityResource [auth_id=" + auth_id + ", res_id=" + res_id + "]";
	}
	
}
