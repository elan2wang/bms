/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangjian
 * @create 2013年7月30日 下午2:33:35
 * @update TODO
 * 
 * 
 */
public class Account implements Serializable{

	private static final long serialVersionUID = -6961988904632764220L;
	
	private Integer account_id;
	private String username;
	private String password;
	private String email;
	private String mobile;
	private Integer dep_id;
	private String last_login_ip;	
	private Integer creator;
	private Integer last_modify_person;
	private Timestamp register_time;
	private Timestamp last_login_time;
	private Timestamp last_modify_time;
	private Boolean account_enable;
	
	public Account() {
		super();
	}

	public Account(Integer account_id, String username, String password,
			String email, String mobile, Integer dep_id,
			String last_login_ip, Integer creator, Integer last_modify_person,
			Timestamp register_time, Timestamp last_login_time,
			Timestamp last_modify_time, Boolean account_enable) {
		super();
		this.account_id = account_id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.mobile = mobile;
		this.dep_id = dep_id;
		this.last_login_ip = last_login_ip;
		this.creator = creator;
		this.last_modify_person = last_modify_person;
		this.register_time = register_time;
		this.last_login_time = last_login_time;
		this.last_modify_time = last_modify_time;
		this.account_enable = account_enable;
	}

	public Integer getAccount_id() {
		return account_id;
	}

	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public Integer getDep_id() {
		return dep_id;
	}

	public void setDep_id(Integer dep_id) {
		this.dep_id = dep_id;
	}

	public String getLast_login_ip() {
		return last_login_ip;
	}

	public void setLast_login_ip(String last_login_ip) {
		this.last_login_ip = last_login_ip;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Integer getLast_modify_person() {
		return last_modify_person;
	}

	public void setLast_modify_person(Integer last_modify_person) {
		this.last_modify_person = last_modify_person;
	}

	public Timestamp getRegister_time() {
		return register_time;
	}

	public void setRegister_time(Timestamp register_time) {
		this.register_time = register_time;
	}

	public Timestamp getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(Timestamp last_login_time) {
		this.last_login_time = last_login_time;
	}

	public Timestamp getLast_modify_time() {
		return last_modify_time;
	}

	public void setLast_modify_time(Timestamp last_modify_time) {
		this.last_modify_time = last_modify_time;
	}

	public Boolean getAccount_enable() {
		return account_enable;
	}

	public void setAccount_enable(Boolean account_enable) {
		this.account_enable = account_enable;
	}

	@Override
	public String toString() {
		return "Account [account_id=" + account_id + ", username=" + username
				+ ", password=" + password + ", email=" + email + ", mobile="
				+ mobile + ", dep_id=" + dep_id + ", last_login_ip="
				+ last_login_ip + ", creator=" + creator
				+ ", last_modify_person=" + last_modify_person
				+ ", register_time=" + register_time + ", last_login_time="
				+ last_login_time + ", last_modify_time=" + last_modify_time
				+ ", account_enable=" + account_enable + "]";
	}

	@SuppressWarnings("rawtypes")
	public Map toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("account_id", account_id);
		map.put("username", username);
		map.put("email", email);
		map.put("mobile", mobile);
		map.put("dep_id", dep_id);
		map.put("last_login_ip", last_login_ip);
		map.put("creator", creator);
		map.put("last_modify_person", last_modify_person);
		map.put("last_modify_time", last_modify_time);
		map.put("account_enable", account_enable);
		return map;
	}
}
