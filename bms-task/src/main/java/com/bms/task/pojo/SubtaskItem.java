/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangjian
 * @create 2013年8月15日 下午11:39:09
 * @update TODO
 * 
 * 
 */
public class SubtaskItem implements Serializable{

	private static final long serialVersionUID = -5800867156063040976L;
	
	private Integer id;
	private Integer subtask_id;
	private Integer owner;
	private Timestamp confirm_time;
	private Timestamp finish_time;
	private String state;
	private String comment;
	
	public SubtaskItem() {
		super();
	}

	public SubtaskItem(Integer id, Integer subtask_id, Integer owner,
			Timestamp confirm_time, Timestamp finish_time, String state,
			String comment) {
		super();
		this.id = id;
		this.subtask_id = subtask_id;
		this.owner = owner;
		this.confirm_time = confirm_time;
		this.finish_time = finish_time;
		this.state = state;
		this.comment = comment;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSubtask_id() {
		return subtask_id;
	}

	public void setSubtask_id(Integer subtask_id) {
		this.subtask_id = subtask_id;
	}

	public Integer getOwner() {
		return owner;
	}

	public void setOwner(Integer owner) {
		this.owner = owner;
	}

	public Timestamp getConfirm_time() {
		return confirm_time;
	}

	public void setConfirm_time(Timestamp confirm_time) {
		this.confirm_time = confirm_time;
	}

	public Timestamp getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(Timestamp finish_time) {
		this.finish_time = finish_time;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "SubtaskItem [id=" + id + ", subtask_id=" + subtask_id
				+ ", owner=" + owner + ", confirm_time=" + confirm_time
				+ ", finish_time=" + finish_time + ", state=" + state
				+ ", comment=" + comment + "]";
	}
	
	@SuppressWarnings("rawtypes")
	public Map toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		map.put("id", id);
		map.put("subtask_id", subtask_id);
		map.put("owner", owner);
		map.put("confirm_time", confirm_time);
		map.put("finish_time", finish_time);
		map.put("state", state);
		map.put("comment", comment);
		
		return map;
	}
	
}
