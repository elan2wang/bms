/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.pojo;

import java.io.Serializable;

/**
 * @author wangjian
 * @create 2013年8月14日 下午11:06:27
 * @update TODO
 * 
 * 
 */
public class AccountDepartment implements Serializable {

	private static final long serialVersionUID = -6203012641882198368L;

	private Integer id;
	private Integer account_id;
	private Integer dep_id;
	
	public AccountDepartment() {
		super();
	}

	public AccountDepartment(Integer account_id, Integer dep_id) {
		super();
		this.account_id = account_id;
		this.dep_id = dep_id;
	}

	public AccountDepartment(Integer id, Integer account_id, Integer dep_id) {
		super();
		this.id = id;
		this.account_id = account_id;
		this.dep_id = dep_id;
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

	public Integer getDep_id() {
		return dep_id;
	}

	public void setDep_id(Integer dep_id) {
		this.dep_id = dep_id;
	}

	@Override
	public String toString() {
		return "AccountModule [id=" + id + ", account_id=" + account_id
				+ ", dep_id=" + dep_id + "]";
	}
	
}
