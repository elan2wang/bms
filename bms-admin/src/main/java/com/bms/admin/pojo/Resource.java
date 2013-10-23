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
 * @create 2013年7月30日 下午2:32:53
 * @update TODO
 * 
 * 
 */
public class Resource implements Serializable{

	private static final long serialVersionUID = 8892468477522295506L;
	
	private Integer res_id;
	private String res_link;
	private String res_type;
	private String res_description;
	private Boolean res_enable;
	private Integer mod_id;
	
	public Resource() {
		super();
	}

	public Resource(Integer res_id, String res_link, String res_type,
			String res_description, Boolean res_enable, Integer mod_id) {
		super();
		this.res_id = res_id;
		this.res_link = res_link;
		this.res_type = res_type;
		this.res_description = res_description;
		this.res_enable = res_enable;
		this.mod_id = mod_id;
	}

	public Integer getRes_id() {
		return res_id;
	}

	public void setRes_id(Integer res_id) {
		this.res_id = res_id;
	}

	public String getRes_link() {
		return res_link;
	}

	public void setRes_link(String res_link) {
		this.res_link = res_link;
	}

	public String getRes_type() {
		return res_type;
	}

	public void setRes_type(String res_type) {
		this.res_type = res_type;
	}

	public String getRes_description() {
		return res_description;
	}

	public void setRes_description(String res_description) {
		this.res_description = res_description;
	}

	public Boolean getRes_enable() {
		return res_enable;
	}

	public void setRes_enable(Boolean res_enable) {
		this.res_enable = res_enable;
	}

	public Integer getMod_id() {
		return mod_id;
	}

	public void setMod_id(Integer mod_id) {
		this.mod_id = mod_id;
	}

	@Override
	public String toString() {
		return "Resource [res_id=" + res_id + ", res_link=" + res_link
				+ ", res_type=" + res_type + ", res_description="
				+ res_description + ", res_enable=" + res_enable + ", mod_id="
				+ mod_id + "]";
	}

	@SuppressWarnings("rawtypes")
	public Map toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		map.put("res_id", res_id);
		map.put("res_link", res_link);
		map.put("res_type", res_type);
		map.put("res_description", res_description);
		map.put("res_enable", res_enable);
		map.put("mod_id", mod_id);
		
		return map;
	}
}
