/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author AI
 * @create 2013-9-18 下午3:51:54
 * @update TODO
 * 
 * 
 */
public class DepartmentVault implements Serializable {

	private static final long serialVersionUID = -4409298123486070018L;

	private Integer dep_id;
	private String v_number;
	private Timestamp start_time;
	private Timestamp end_time;
	private Boolean state;
	
	public DepartmentVault() {
		super();
	}

	public DepartmentVault(Integer dep_id, String v_number,
			Timestamp start_time, Timestamp end_time, Boolean state) {
		super();
		this.dep_id = dep_id;
		this.v_number = v_number;
		this.start_time = start_time;
		this.end_time = end_time;
		this.state = state;
	}

	public DepartmentVault(Integer dep_id, String v_number) {
		super();
		this.dep_id = dep_id;
		this.v_number = v_number;
		this.start_time = new Timestamp(System.currentTimeMillis());;
		this.end_time = this.start_time;
		this.state = false;
	}
	
	public Integer getDep_id() {
		return dep_id;
	}

	public void setDep_id(Integer dep_id) {
		this.dep_id = dep_id;
	}

	public String getV_number() {
		return v_number;
	}

	public void setV_number(String v_number) {
		this.v_number = v_number;
	}

	public Timestamp getStart_time() {
		return start_time;
	}

	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}

	public Timestamp getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Timestamp end_time) {
		this.end_time = end_time;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "DepartmentVault [dep_id=" + dep_id + ", v_number=" + v_number
				+ ", start_time=" + start_time + ", end_time=" + end_time
				+ ", state=" + state + "]";
	}
	
	

	
}
