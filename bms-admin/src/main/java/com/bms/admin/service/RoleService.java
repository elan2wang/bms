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

import com.bms.admin.persistence.RoleMapper;
import com.bms.admin.pojo.Role;
import com.bms.common.BaseService;
import com.bms.common.Page;

/**
 * @author wangjian
 * @create 2013年8月15日 下午1:27:04
 * @update TODO
 * 
 * 
 */
@Service
public class RoleService extends BaseService{

	@Autowired
	private RoleMapper roleMapper;
	
	@Transactional
	public void insertRole(Role role) {
		roleMapper.insertRole(role);
	}
	
	@Transactional
	public void updateRole(Role role) {
		roleMapper.updateRole(role);
	}
	
	@Transactional
	public void deleteRole(Integer role_id) {
		roleMapper.deleteRole(role_id);
	}
	
	public Role queryRole(Integer role_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role_id", role_id);
		return roleMapper.queryRole(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Role> queryRoles(Map params) {
		List<Role> list = roleMapper.queryRoles(params);
		Page page = (Page) params.get("page");
		return paging(list, page);
	}
	
	public List<Role> queryRolesByLevel(Integer level) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("role_level", level);
		return roleMapper.queryRoles(params);
	}
}
