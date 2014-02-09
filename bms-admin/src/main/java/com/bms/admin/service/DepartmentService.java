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

import com.bms.admin.persistence.DepartmentMapper;
import com.bms.admin.pojo.Department;
import com.bms.common.BaseService;
import com.bms.common.Page;

/**
 * @author wangjian
 * @create 2013年8月21日 上午10:28:13
 * @update TODO
 * 
 * 
 */
@Service
public class DepartmentService extends BaseService {

	@Autowired
	private DepartmentMapper departmentMapper;
	
	public Department queryDepartment(Integer dep_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dep_id", dep_id);
		return departmentMapper.queryDepartment(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Department> queryDepartments(Map params) {
		List<Department> list = departmentMapper.queryDepartments(params);
		Page page = (Page) params.get("page");
		if (page == null) {
			return list;
		}
		return paging(list, page);
	}
	
	public List<Department> queryDepartmentsByLevel(Integer level) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dep_level", level);
		return departmentMapper.queryDepartments(params);
	}
	
	public List<Department> queryDepartmentsByCondition(Integer dep_level,
			String dep_name, Page page){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dep_level", dep_level);
		params.put("dep_name", dep_name);
		params.put("startIndex", page.getStartIndex() - 1);
		params.put("currentItemCount", page.getCurrentItemCount());
		return departmentMapper.queryDepartmentsByCondition(params);
	}
	
	public Integer getDepartmentsCountByCondition(Integer dep_level,
			String dep_name){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dep_level", dep_level);
		params.put("dep_name", dep_name);
		return departmentMapper.getDepartmentsCountByCondition(params);
	}
	
	public void addDepartment(Department department){
		departmentMapper.addDepartment(department);
	}
	
	public void updateDepartment(Department department){
		departmentMapper.updateDepartment(department);
	}
	
	public void deleteDepartment(Integer dep_id){
		departmentMapper.deleteDepartment(dep_id);
	}

}
