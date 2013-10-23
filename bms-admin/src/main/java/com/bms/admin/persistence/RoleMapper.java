/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.Role;

/**
 * @author wangjian
 * @create 2013年8月15日 下午1:27:56
 * @update TODO
 * 
 * 
 */
public interface RoleMapper {

	public void insertRole(Role role);
	
	public void updateRole(Role role);
	
	public void deleteRole(Integer role_id);
	
	@SuppressWarnings("rawtypes")
	public Role queryRole(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Role> queryRoles(Map params);
	
}
