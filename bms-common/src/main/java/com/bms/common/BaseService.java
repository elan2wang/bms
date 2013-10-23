/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangjian
 * @create 2013年8月22日 下午10:21:35
 * @update TODO
 * 
 * 
 */
public class BaseService {

	private static Logger log = LoggerFactory.getLogger(BaseService.class);
	
	@SuppressWarnings("rawtypes")
	protected List paging(List list, Page page) {
		Integer start = page.getStartIndex();
		Integer itemsPerPage = page.getItemsPerPage();
		log.debug("start="+start+", itemsPerPage="+itemsPerPage);
		Integer end;
		if (start+itemsPerPage-1 > list.size()) {
			end = list.size();
		}  else {
			end = start+itemsPerPage-1;
		}
		page.setCurrentItemCount(end-start+1);
		page.setTotalItems(list.size());
		
		return list.subList(start-1, end);
	}

}
