/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bms.admin.persistence.AuthorityResourceMapper;
import com.bms.admin.pojo.Authority;
import com.bms.admin.pojo.AuthorityResource;
import com.bms.admin.pojo.Resource;
import com.bms.common.BaseService;
import com.bms.common.Page;

/**
 * @author wangjian
 * @create 2013年8月22日 下午4:35:15
 * @update TODO
 * 
 * 
 */
@Service
public class AuthorityResourceService extends BaseService {

	@Autowired
	private AuthorityResourceMapper authorityResourceMapper;

	public void assignResources(Integer auth_id, List<Integer> resources) {
		// delete first
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("auth_id", auth_id);
		authorityResourceMapper.deleteAuthorityResources(params);
		// assign with new resources
		for (Integer res_id : resources) {
			AuthorityResource authRes = new AuthorityResource(auth_id, res_id);
			authorityResourceMapper.insertAuthorityResource(authRes);;
		}
	}
	
	public List<AuthorityResource> queryAuthorityResourcesByAuth(Integer auth_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("auth_id", auth_id);
		return authorityResourceMapper.queryAuthorityResources(params);
	}
	
	public List<AuthorityResource> queryAuthorityResourcesByRes(Integer res_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("res_id", res_id);
		return authorityResourceMapper.queryAuthorityResources(params);
	}
	
	@SuppressWarnings("unchecked")
	public List<Resource> queryResourcesByAuth(Integer auth_id, Page page) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("auth_id", auth_id);
		List<Resource> list = authorityResourceMapper.queryResourcesByAuth(params);
		
		if (page == null) return list;
		else return paging(list, page);
	}
	
	@SuppressWarnings("unchecked")
	public List<Authority> queryAuthoritiesByRes(Integer res_id, Page page) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("res_id", res_id);
		List<Authority> list = authorityResourceMapper.queryAuthoritiesByRes(params);
		
		if (page == null) return list;
		else return paging(list, page);
	}

}
