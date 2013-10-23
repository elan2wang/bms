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
 * @create 2013年8月15日 下午10:48:43
 * @update TODO
 * 
 * 
 */
public class Task implements Serializable, Cloneable {

	private static final long serialVersionUID = -3056356320578725924L;

	private Integer id;
	private String gid;
	private String title;
	private String description;
	private Timestamp start_time;
	private Timestamp end_time;
	private Timestamp finish_time;
	private Timestamp create_time;
	private Integer creator;
	private Integer remind_time;
	private Boolean star;
	private String state;
	private String comment;
	private String periodicity;
	private Timestamp last_modify_time;
	private Integer last_modify_person;
	
	public Task() {
		super();
	}

	public Task(Integer id, String gid, String title, String description,
			Timestamp start_time, Timestamp end_time, Timestamp finish_time,
			Timestamp create_time, Integer creator, Integer remind_time,Boolean star, 
			String state, String comment, String periodicity, Timestamp last_modify_time,
			Integer last_modify_person) {
		super();
		this.id = id;
		this.gid = gid;
		this.title = title;
		this.description = description;
		this.start_time = start_time;
		this.end_time = end_time;
		this.finish_time = finish_time;
		this.create_time = create_time;
		this.creator = creator;
		this.setRemind_time(remind_time);
		this.star = star;
		this.state = state;
		this.comment = comment;
		this.periodicity = periodicity;
		this.last_modify_time = last_modify_time;
		this.last_modify_person = last_modify_person;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Timestamp getFinish_time() {
		return finish_time;
	}

	public void setFinish_time(Timestamp finish_time) {
		this.finish_time = finish_time;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Integer getRemind_time() {
		return remind_time;
	}

	public void setRemind_time(Integer remind_time) {
		this.remind_time = remind_time;
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

	public String getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(String periodicity) {
		this.periodicity = periodicity;
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
		return "Task [id=" + id + ", gid=" + gid + ", title=" + title
				+ ", description=" + description + ", start_time=" + start_time
				+ ", end_time=" + end_time + ", finish_time=" + finish_time
				+ ", create_time=" + create_time + ", creator=" + creator
				+ ", remind_time=" + remind_time + ", star=" + star
				+ ", state=" + state + ", comment=" + comment
				+ ", periodicity=" + periodicity + ", last_modify_time="
				+ last_modify_time + ", last_modify_person="
				+ last_modify_person + "]";
	}

	public Object clone() {
		Task obj = null;
		try {
			obj = (Task) super.clone();
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
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("id", id);
		result.put("gid", gid);
		result.put("title", title);
		result.put("description", description);
		result.put("start_time", start_time);
		result.put("end_time", end_time);
		result.put("finish_time", finish_time);
		result.put("create_time", create_time);
		result.put("creator", creator);
		result.put("remind_time", remind_time);
		result.put("star", star);
		result.put("state", state);
		result.put("comment", comment);
		result.put("periodicity", periodicity);
		result.put("last_modify_time", last_modify_time);
		result.put("last_modify_person", last_modify_person);
		
		return result;
	}
}
