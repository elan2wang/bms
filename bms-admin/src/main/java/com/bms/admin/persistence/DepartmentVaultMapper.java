/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.DepartmentVault;

/**
 * @author AI
 * @create 2013-9-18 下午9:51:45
 * @update TODO
 * 
 * 
 */
public interface DepartmentVaultMapper {
	
	//插入一条DepartmentVault
	public void insertDepartmentVault(DepartmentVault departmentVault);
	
	//修改一条DepartmentVault
	public void updateDepartmentVault(DepartmentVault departmentVault);
	
	//根据v_number修改多条DepartmentVaults
	@SuppressWarnings("rawtypes")
	public void updateDepartmentVaults(Map params);
	
	//按条件返回多条DepartmentVaults
	@SuppressWarnings("rawtypes")
	public List<DepartmentVault> queryDepartmentVaults(Map params);
	
}
