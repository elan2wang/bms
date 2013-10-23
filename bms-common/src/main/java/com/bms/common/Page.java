/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.common;

import java.io.Serializable;

/**
 * @author wangjian
 * @create 2013年8月17日 上午10:29:16
 * @update TODO
 * 
 * 
 */
public class Page implements Serializable{

	private static final long serialVersionUID = -6927490839198113549L;
	public static final Integer defaultStart = 1;
	public static final Integer defaultItemsPerPage = 10;
	
	private Integer startIndex;
	private Integer itemsPerPage;
	private Integer currentItemCount;
	private Integer totalItems;
	
	public Page() {
		super();
		this.startIndex = defaultStart;
		this.itemsPerPage = defaultItemsPerPage;
	}

	public Page(Integer itemsPerPage, Integer startIndex) {
		this.itemsPerPage = itemsPerPage;
		this.startIndex = startIndex;
	}
	
	public Integer getCurrentItemCount() {
		return currentItemCount;
	}

	public void setCurrentItemCount(Integer currentItemCount) {
		this.currentItemCount = currentItemCount;
	}

	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

}
