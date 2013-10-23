/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.AccountDepartment;

/**
 * @author wangjian
 * @create 2013年8月14日 下午11:05:06
 * @update TODO
 * 
 * 
 */
public interface AccountDepartmentMapper {

	public void insertAccountDepartment(AccountDepartment accountDepartment);
	
	@SuppressWarnings("rawtypes")
	public void deleteAccountDepartments(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<AccountDepartment> queryAccountDepartments(Map params);
	
}
