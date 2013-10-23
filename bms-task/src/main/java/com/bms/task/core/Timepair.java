/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.core;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author wangjian
 * @create 2013年8月17日 下午3:42:05
 * @update TODO
 * 
 * 
 */
public class Timepair implements Serializable{

	private static final long serialVersionUID = -2540008692812821012L;

	private Timestamp start_time;
	private Timestamp end_time;
	
	public Timepair() {
		super();
	}
	
	public Timepair(Timestamp start_time, Timestamp end_time) {
		super();
		this.start_time = start_time;
		this.end_time = end_time;
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
}
