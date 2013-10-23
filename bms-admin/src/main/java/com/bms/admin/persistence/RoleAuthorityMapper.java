/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.RoleAuthority;

/**
 * @author wangjian
 * @create 2013年8月15日 上午10:36:23
 * @update TODO
 * 
 * 
 */
public interface RoleAuthorityMapper {

	public void insertRoleAuthority(RoleAuthority roleAuthority);
	
	@SuppressWarnings("rawtypes")
	public void deleteRoleAuthorities(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<RoleAuthority> queryRoleAuthorities(Map params);
	
}
