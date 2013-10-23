/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.Authority;
import com.bms.admin.pojo.AuthorityResource;
import com.bms.admin.pojo.Resource;

/**
 * @author wangjian
 * @create 2013年8月15日 上午10:46:50
 * @update TODO
 * 
 * 
 */
public interface AuthorityResourceMapper {
	
	public void insertAuthorityResource(AuthorityResource authorityResource);
	
	@SuppressWarnings("rawtypes")
	public void deleteAuthorityResources(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<AuthorityResource> queryAuthorityResources(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Resource> queryResourcesByAuth(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Authority> queryAuthoritiesByRes(Map params);

}
