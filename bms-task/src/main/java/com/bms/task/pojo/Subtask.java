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
 * @create 2013年8月15日 下午10:48:57
 * @update TODO
 * 
 * 
 */
public class Subtask implements Serializable, Cloneable{

	private static final long serialVersionUID = 905847519525948400L;
	
	private Integer id;
	private Integer task_id;
	private String gid;
	private String title;
	private String owners;
	private String description;
	private Timestamp start_time;
	private Timestamp end_time;
	private Timestamp create_time;
	private Timestamp finish_time;
	private Boolean star;
	private String state;
	private String comment;
	private Timestamp last_modify_time;
	private Integer last_modify_person;
	
	public Subtask() {
		super();
	}

	public Subtask(Integer id, Integer task_id, String gid, String title, String owners,
			String description, Timestamp start_time, Timestamp end_time,
			Timestamp create_time, Timestamp finish_time, Boolean star,
			String state, String comment, Timestamp last_modify_time,
			Integer last_modify_person) {
		super();
		this.id = id;
		this.task_id = task_id;
		this.gid = gid;
		this.title = title;
		this.owners = owners;
		this.description = description;
		this.start_time = start_time;
		this.end_time = end_time;
		this.create_time = create_time;
		this.finish_time = finish_time;
		this.star = star;
		this.state = state;
		this.comment = comment;
		this.last_modify_time = last_modify_time;
		this.last_modify_person = last_modify_person;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTask_id() {
		return task_id;
	}

	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOwners() {
		return owners;
	}

	public void setOwners(String owners) {
		this.owners = owners;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(Timestamp finish_time) {
		this.finish_time = finish_time;
	}

	public Boolean getStar() {
		return star;
	}

	public void setStar(Boolean star) {
		this.star = star;
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

	public Timestamp getLast_modify_time() {
		return last_modify_time;
	}

	public void setLast_modify_time(Timestamp last_modify_time) {
		this.last_modify_time = last_modify_time;
	}

	public Integer getLast_modify_person() {
		return last_modify_person;
	}

	public void setLast_modify_person(Integer last_modify_person) {
		this.last_modify_person = last_modify_person;
	}
	
	@Override
	public String toString() {
		return "Subtask [id=" + id + ", task_id=" + task_id + ", gid=" + gid
				+ ", title=" + title + ", owners=" + owners + ", description="
				+ description + ", start_time=" + start_time + ", end_time="
				+ end_time + ", create_time=" + create_time + ", finish_time="
				+ finish_time + ", star=" + star + ", state=" + state
				+ ", comment=" + comment + ", last_modify_time="
				+ last_modify_time + ", last_modify_person="
				+ last_modify_person + "]";
	}

	public Object clone() {
		Subtask obj = null;
		try {
			obj = (Subtask) super.clone();
			if (start_time != null)
				obj.start_time = new Timestamp(start_time.getTime());
			if (end_time != null)
				obj.end_time = new Timestamp(end_time.getTime());
			if (finish_time != null) 
				obj.finish_time = new Timestamp(finish_time.getTime());
			if (create_time != null)
				obj.create_time = new Timestamp(create_time.getTime());
			if (last_modify_time != null)
				obj.last_modify_time = new Timestamp(last_modify_time.getTime());
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	@SuppressWarnings("rawtypes")
	public Map toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("id", id);
		map.put("task_id", task_id);
		map.put("gid", gid);
		map.put("title", title);
		map.put("description", description);
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		map.put("create_time", create_time);
		map.put("finish_time", finish_time);
		map.put("star", star);
		map.put("state", state);
		map.put("last_modify_person", last_modify_person);
		map.put("last_modify_time", last_modify_time);
		map.put("owners", owners);
		
		return map;
	}
}
