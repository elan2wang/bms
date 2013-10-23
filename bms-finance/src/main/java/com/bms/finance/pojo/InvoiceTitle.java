/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.finance.pojo;

import java.io.Serializable;

/**
 * @author asus
 * @create 2013年9月22日 上午11:55:04
 * @update TODO
 * 
 * 
 */
public class InvoiceTitle implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String title;
	private Boolean enable;
	
	public InvoiceTitle() {
		super();
	}

	public InvoiceTitle(Integer id, String title, Boolean enable) {
		super();
		this.id = id;
		this.title = title;
		this.enable = enable;
	}
	
	public InvoiceTitle(String title, Boolean enable) {
		super();
		this.title = title;
		this.enable = enable;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "InvoiceTitle [id=" + id + ", title=" + title + ", enable="
				+ enable + "]";
	}

}
