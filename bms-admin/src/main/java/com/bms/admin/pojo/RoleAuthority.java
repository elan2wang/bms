/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.pojo;

import java.io.Serializable;

/**
 * @author wangjian
 * @create 2013年8月15日 上午10:19:05
 * @update TODO
 * 
 * 
 */
public class RoleAuthority implements Serializable{

	private static final long serialVersionUID = -5277005590243285694L;

	private Integer id;
	private Integer role_id;
	private Integer auth_id;
	
	public RoleAuthority() {
		super();
	}

	public RoleAuthority(Integer role_id, Integer auth_id) {
		super();
		this.role_id = role_id;
		this.auth_id = auth_id;
	}
	
	public RoleAuthority(Integer id, Integer role_id, Integer auth_id) {
		super();
		this.id = id;
		this.role_id = role_id;
		this.auth_id = auth_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public Integer getAuth_id() {
		return auth_id;
	}

	public void setAuth_id(Integer auth_id) {
		this.auth_id = auth_id;
	}

	@Override
	public String toString() {
		return "RoleAuthority [id=" + id + ", role_id=" + role_id
				+ ", auth_id=" + auth_id + "]";
	}

}
