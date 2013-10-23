/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.Resource;

/**
 * @author wangjian
 * @create 2013年8月22日 下午1:46:46
 * @update TODO
 * 
 * 
 */
public interface ResourceMapper {

	public void insertResource(Resource res);
	
	public void updateResource(Resource res);
	
	public void deleteResource(Integer res_id);
	
	@SuppressWarnings("rawtypes")
	public Resource queryResource(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Resource> queryResources(Map params);
	
}
