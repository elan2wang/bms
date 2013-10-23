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
import org.springframework.transaction.annotation.Transactional;

import com.bms.admin.persistence.AuthorityMapper;
import com.bms.admin.pojo.Authority;
import com.bms.common.BaseService;
import com.bms.common.Page;

/**
 * @author wangjian
 * @create 2013年8月22日 上午10:04:21
 * @update TODO
 * 
 * 
 */
@Service
public class AuthorityService extends BaseService{

	@Autowired
	private AuthorityMapper authorityMapper;
	
	@Transactional
	public void insertAuthority(Authority auth) {
		authorityMapper.insertAuthority(auth);
	}
	
	@Transactional
	public void updateAuthority(Authority auth) {
		authorityMapper.updateAuthority(auth);
	}
	
	@Transactional
	public void deleteAuthority(Integer auth_id) {
		authorityMapper.deleteAuthority(auth_id);
	}
	
	public Authority queryAuthorityById(Integer auth_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("auth_id", auth_id);
		return authorityMapper.queryAuthority(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Authority> queryAuthorities(Map params) {
		List<Authority> list = authorityMapper.queryAuthorities(params);
		Page page = (Page) params.get("page");
		return paging(list, page);
	}
	
}
