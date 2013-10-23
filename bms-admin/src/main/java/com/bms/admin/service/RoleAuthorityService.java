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

import com.bms.admin.persistence.RoleAuthorityMapper;
import com.bms.admin.pojo.RoleAuthority;

/**
 * @author wangjian
 * @create 2013年8月15日 下午12:13:10
 * @update TODO
 * 
 * 
 */
@Service
public class RoleAuthorityService {

	@Autowired
	private RoleAuthorityMapper roleAuthorityMapper;
	
	@Transactional
	public void deleteAuthorities(Integer role_id, Integer mod_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role_id", role_id);
		params.put("mod_id", mod_id);
		roleAuthorityMapper.deleteRoleAuthorities(params);
	}
	
	@Transactional
	public void assignAuthorities(Integer role_id, Integer mod_id, List<Integer> authorities) {
		// delete first
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role_id", role_id);
		params.put("mod_id", mod_id);
		roleAuthorityMapper.deleteRoleAuthorities(params);
		// assign with new authorities
		for (Integer auth_id : authorities) {
			RoleAuthority roleAuthority = new RoleAuthority(role_id, auth_id);
			roleAuthorityMapper.insertRoleAuthority(roleAuthority);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public List<RoleAuthority> queryRoleAuthorities(Map params) {
		return roleAuthorityMapper.queryRoleAuthorities(params);
	}

}
