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

import com.bms.admin.persistence.AccountDepartmentMapper;
import com.bms.admin.pojo.AccountDepartment;

/**
 * @author wangjian
 * @create 2013年8月14日 下午11:10:14
 * @update TODO
 * 
 * 
 */
@Service
public class AccountDepartmentService {

	@Autowired
	private AccountDepartmentMapper accountDepartmentMapper;
	
	public void assignDepartment(Integer account_id, List<Integer> deps) {
		// delete first
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("account_id", account_id);
		accountDepartmentMapper.deleteAccountDepartments(params);
		// assign with new departments
		for (Integer dep_id : deps) {
			AccountDepartment accountDepartment = new AccountDepartment(account_id, dep_id);
			accountDepartmentMapper.insertAccountDepartment(accountDepartment);
		}
	}
	
	public List<AccountDepartment> queryAccountDepartments(Integer account_id) {
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("account_id", account_id);
		return accountDepartmentMapper.queryAccountDepartments(params);
	}
	
}
