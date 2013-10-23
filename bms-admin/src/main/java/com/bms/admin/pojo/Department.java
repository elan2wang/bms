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
 * @create 2013年8月21日 上午10:17:11
 * @update TODO
 * 
 * 
 */
public class Department implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 3320118454768607917L;

	private Integer dep_id;
	private Integer dep_level;
	private String dep_name;
	private String address;
	
	public Department() {
		super();
	}

	public Department(Integer dep_id, Integer dep_level, String dep_name,
			String address) {
		super();
		this.dep_id = dep_id;
		this.dep_level = dep_level;
		this.dep_name = dep_name;
		this.address = address;
	}

	
	
	public Department(Integer dep_level, String dep_name, String address) {
		super();
		this.dep_level = dep_level;
		this.dep_name = dep_name;
		this.address = address;
	}

	public Integer getDep_id() {
		return dep_id;
	}

	public void setDep_id(Integer dep_id) {
		this.dep_id = dep_id;
	}

	public Integer getDep_level() {
		return dep_level;
	}

	public void setDep_level(Integer dep_level) {
		this.dep_level = dep_level;
	}

	public String getDep_name() {
		return dep_name;
	}

	public void setDep_name(String dep_name) {
		this.dep_name = dep_name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Department [dep_id=" + dep_id + ", dep_level=" + dep_level
				+ ", dep_name=" + dep_name + ", address=" + address + "]";
	}

	public Object clone() {
		Department obj = null;
		try {
			obj = (Department) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	@SuppressWarnings("rawtypes")
	public Map toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("dep_id", dep_id);
		map.put("dep_level", dep_level);
		map.put("dep_name", dep_name);
		map.put("address", address);
		
		return map;
	}
}
