/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.persistence;

import java.util.List;
import java.util.Map;

import com.bms.security.core.Account;

/**
 * @author wangjian
 * @create 2013年7月30日 下午2:17:23
 * @update TODO
 * 
 * 
 */
public interface SecurityMapper {
	
	public List<String> queryPublicResources();

	public List<String> queryNonPublicResources();
	
	@SuppressWarnings("rawtypes")
	public Account queryAccountByUsername(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Integer> queryAccountRoles(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Integer> queryNeededRoles(Map params);
	
	@SuppressWarnings("rawtypes")
	public void updateLastLoginInfo(Map params);
}
