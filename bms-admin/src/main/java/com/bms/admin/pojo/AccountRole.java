/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.pojo;

import java.io.Serializable;

/**
 * @author wangjian
 * @create 2013年8月14日 下午10:53:28
 * @update TODO
 * 
 * 
 */
public class AccountRole implements Serializable {

	private static final long serialVersionUID = 1812082412127454088L;

	private Integer id;
	private Integer account_id;
	private Integer role_id;
	
	public AccountRole() {
		super();
	}

	public AccountRole(Integer account_id, Integer role_id) {
		super();
		this.account_id = account_id;
		this.role_id = role_id;
	}
	
	public AccountRole(Integer id, Integer account_id, Integer role_id) {
		super();
		this.id = id;
		this.account_id = account_id;
		this.role_id = role_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccount_id() {
		return account_id;
	}

	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	@Override
	public String toString() {
		return "AccountRole [id=" + id + ", account_id=" + account_id
				+ ", role_id=" + role_id + "]";
	}
	
}
