/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.Authority;

/**
 * @author wangjian
 * @create 2013年8月22日 上午9:55:17
 * @update TODO
 * 
 * 
 */
public interface AuthorityMapper {

	public void insertAuthority(Authority auth);
	
	public void updateAuthority(Authority auth);
	
	public void deleteAuthority(Integer auth_id);
	
	@SuppressWarnings("rawtypes")
	public Authority queryAuthority(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Authority> queryAuthorities(Map params);
}
