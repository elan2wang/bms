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

import com.bms.admin.persistence.AccountRoleMapper;
import com.bms.admin.pojo.AccountRole;

/**
 * @author wangjian
 * @create 2013年8月14日 下午10:57:01
 * @update TODO
 * 
 * 
 */
@Service
public class AccountRoleService {

	@Autowired
	private AccountRoleMapper accountRoleMapper;
	
	@Transactional
	public void assignRole(Integer account_id, List<Integer> roles) {
		// delete first
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("account_id", account_id);
		accountRoleMapper.deleteAccountRoles(params);
		// assign with new roles
		for (Integer role_id : roles) {
			AccountRole accountRole = new AccountRole(account_id, role_id);
			accountRoleMapper.insertAccountRole(accountRole);
		}
	}

	public List<AccountRole> queryAccountRoles(Integer account_id) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("account_id", account_id);
		return accountRoleMapper.queryAccountRoles(params);
	}
}
