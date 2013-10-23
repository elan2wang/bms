/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.Department;

/**
 * @author wangjian
 * @create 2013年8月21日 上午10:24:50
 * @update TODO
 * 
 * 
 */
public interface DepartmentMapper {

	@SuppressWarnings("rawtypes")
	public Department queryDepartment(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Department> queryDepartments(Map params);
	
	public void addDepartment(Department department);
	
	public void deleteDepartment(Integer dep_id);
	
	public void updateDepartment(Department department);
	
	@SuppressWarnings("rawtypes")
	public List<Department> queryDepartmentsByCondition(Map params);
	
	@SuppressWarnings("rawtypes")
	public Integer getDepartmentsCountByCondition(Map params);
}
