/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.admin.persistence.ResourceMapper;
import com.bms.admin.pojo.Resource;
import com.bms.common.BaseService;
import com.bms.common.Page;

/**
 * @author wangjian
 * @create 2013年8月22日 下午1:53:28
 * @update TODO
 * 
 * 
 */
@Service
public class ResourceService extends BaseService{

	@Autowired
	private ResourceMapper resourceMapper;
	
	@Transactional
	public void insertResource(Resource res) {
		resourceMapper.insertResource(res);
	}
	
	@Transactional
	public void updateResource(Resource res) {
		resourceMapper.updateResource(res);
	}
	
	@Transactional
	public void deleteResource(Integer res_id) {
		resourceMapper.deleteResource(res_id);
	}
	
	public Resource queryResourceById(Integer res_id) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("res_id", res_id);
		
		return resourceMapper.queryResource(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Resource> queryResources(Map params) {
		List<Resource> list = resourceMapper.queryResources(params);
		Page page = (Page) params.get("page");		
		return paging(list, page);
	}

}
