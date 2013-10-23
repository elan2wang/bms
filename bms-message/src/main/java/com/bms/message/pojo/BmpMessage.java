/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author wangjian
 * @create 2013年8月6日 下午6:30:08
 * @update TODO
 * 
 * 
 */
public class BmpMessage implements Serializable{

	private static final long serialVersionUID = 8126171190365535060L;
	
	private Integer id;
	private String msg_type;
	private String msg_content;
	private String trigger_event;
	private String link;
	private String dst_list;
	private Timestamp trigger_time;
	
	public BmpMessage() {
		super();
		this.trigger_time = new Timestamp(System.currentTimeMillis());
	}
	
	public BmpMessage(String msg_type, String msg_content, String trigger_event, 
			String link, String dst_list) {
		super();
		this.msg_type = msg_type;
		this.msg_content = msg_content;
		this.trigger_time = new Timestamp(System.currentTimeMillis());
		this.trigger_event = trigger_event;
		this.link = link;
		this.dst_list = dst_list;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getMsg_type() {
		return msg_type;
	}
	
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	
	public String getMsg_content() {
		return msg_content;
	}
	
	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}
	
	public Timestamp getTrigger_time() {
		return trigger_time;
	}
	
	public void setTrigger_time(Timestamp trigger_time) {
		this.trigger_time = trigger_time;
	}
	
	public String getTrigger_event() {
		return trigger_event;
	}
	
	public void setTrigger_event(String trigger_event) {
		this.trigger_event = trigger_event;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getDst_list() {
		return dst_list;
	}
	
	public void setDst_list(String dst_list) {
		this.dst_list = dst_list;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", msg_type=" + msg_type
				+ ", msg_content=" + msg_content + ", trigger_time="
				+ trigger_time + ", trigger_event=" + trigger_event + ", link="
				+ link + ", dst_list=" + dst_list + "]";
	}
	
}
