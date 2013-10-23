/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.AccountRole;

/**
 * @author wangjian
 * @create 2013年8月14日 下午10:52:39
 * @update TODO
 * 
 * 
 */
public interface AccountRoleMapper {
	
	public void insertAccountRole(AccountRole accountRole);
	
	@SuppressWarnings("rawtypes")
	public void deleteAccountRoles(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<AccountRole> queryAccountRoles(Map params);
	
}
