/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.RoleModule;

/**
 * @author wangjian
 * @create 2013年8月15日 上午10:34:21
 * @update TODO
 * 
 * 
 */
public interface RoleModuleMapper {

	public void insertRoleModule(RoleModule roleModule);
	
	@SuppressWarnings("rawtypes")
	public RoleModule queryRoleModule(Map params);
	
	@SuppressWarnings("rawtypes")
	public void deleteRoleModules(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<RoleModule> queryRoleModules(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<RoleModule> queryRoleModulesByFather(Map params);
}
