/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangjian
 * @create 2013年8月6日 下午6:30:16
 * @update TODO
 * 
 * 
 */
public class PushMessage implements Serializable{

	private static final long serialVersionUID = 6789018914762481743L;

	private Integer id;
	private Integer msg_id;
	private String msg_type;
	private String msg_content;
	private Integer dst_id;
	private String state;
	private String link;
	private Timestamp create_time;
	private Timestamp receive_time;
	private Timestamp read_time;
	
	public PushMessage() {
		super();
	}

	public PushMessage(Integer id, Integer msg_id, String msg_type,
			String msg_content, Integer dst_id, String state, String link,
			Timestamp create_time, Timestamp receive_time, Timestamp read_time) {
		super();
		this.id = id;
		this.msg_id = msg_id;
		this.msg_type = msg_type;
		this.msg_content = msg_content;
		this.dst_id = dst_id;
		this.state = state;
		this.link = link;
		this.create_time = create_time;
		this.receive_time = receive_time;
		this.read_time = read_time;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMsg_id() {
		return msg_id;
	}
	
	public void setMsg_id(Integer msg_id) {
		this.msg_id = msg_id;
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

	public Integer getDst_id() {
		return dst_id;
	}

	public void setDst_id(Integer dst_id) {
		this.dst_id = dst_id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Timestamp getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}

	public Timestamp getReceive_time() {
		return receive_time;
	}

	public void setReceive_time(Timestamp receive_time) {
		this.receive_time = receive_time;
	}

	public Timestamp getRead_time() {
		return read_time;
	}

	public void setRead_time(Timestamp read_time) {
		this.read_time = read_time;
	}

	@Override
	public String toString() {
		return "PushMessage [id=" + id + ", msg_id=" + msg_id + ", msg_type=" + msg_type
				+ ", msg_content=" + msg_content + ", dst_id=" + dst_id
				+ ", state=" + state + ", link=" + link + ", receive_time="
				+ receive_time + ", read_time=" + read_time + "]";
	}
	
	@SuppressWarnings("rawtypes")
	public Map toMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		map.put("id", id);
		map.put("msg_id", msg_id);
		map.put("msg_type", msg_type);
		map.put("msg_content", msg_content);
		map.put("dst_id", dst_id);
		map.put("state", state);
		map.put("link", link);
		map.put("create_time", create_time);
		map.put("receive_time", receive_time);
		map.put("read_time", read_time);
		
		return map;
	}
}
