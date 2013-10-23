/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.pojo;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangjian
 * @create 2013年7月30日 下午2:33:09
 * @update TODO
 * 
 * 
 */
public class Authority implements Serializable{

	private static final long serialVersionUID = 315008277699052962L;
	
	private Integer auth_id;
	private Integer mod_id;
	private String auth_name;
	private String auth_description;
	private String auth_type;
	private Boolean auth_enable;
	
	public Authority() {
		super();
	}
	
	public Authority(Integer auth_id, Integer mod_id, String auth_name,
			String auth_description, String auth_type, Boolean auth_enable) {
		super();
		this.auth_id = auth_id;
		this.mod_id = mod_id;
		this.auth_name = auth_name;
		this.auth_description = auth_description;
		this.auth_type = auth_type;
		this.auth_enable = auth_enable;
	}

	public Integer getAuth_id() {
		return auth_id;
	}

	public void setAuth_id(Integer auth_id) {
		this.auth_id = auth_id;
	}

	public Integer getMod_id() {
		return mod_id;
	}

	public void setMod_id(Integer mod_id) {
		this.mod_id = mod_id;
	}

	public String getAuth_name() {
		return auth_name;
	}

	public void setAuth_name(String auth_name) {
		this.auth_name = auth_name;
	}

	public String getAuth_description() {
		return auth_description;
	}

	public void setAuth_description(String auth_description) {
		this.auth_description = auth_description;
	}

	public String getAuth_type() {
		return auth_type;
	}

	public void setAuth_type(String auth_type) {
		this.auth_type = auth_type;
	}

	public Boolean getAuth_enable() {
		return auth_enable;
	}

	public void setAuth_enable(Boolean auth_enable) {
		this.auth_enable = auth_enable;
	}

	@Override
	public String toString() {
		return "Authority [auth_id=" + auth_id + ", mod_id=" + mod_id
				+ ", auth_name=" + auth_name + ", auth_description="
				+ auth_description + ", auth_type=" + auth_type
				+ ", auth_enable=" + auth_enable + "]";
	}
	
	@SuppressWarnings("rawtypes")
	public Map toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		map.put("auth_id", auth_id);
		map.put("mod_id", mod_id);
		map.put("auth_name", auth_name);
		map.put("auth_description", auth_description);
		map.put("auth_type", auth_type);
		map.put("auth_enable", auth_enable);
		
		return map;
	}
}
