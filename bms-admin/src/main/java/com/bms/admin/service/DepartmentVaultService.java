/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.admin.persistence.DepartmentVaultMapper;
import com.bms.admin.pojo.DepartmentVault;

/**
 * @author AI
 * @create 2013-9-18 下午9:56:33
 * @update TODO
 * 
 * 
 */
@Service
public class DepartmentVaultService {

	@Autowired
	DepartmentVaultMapper departmentVaultMapper;
	
	//按部门编号返回DepartmentVaults
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<DepartmentVault> queryDepartmentVaultByDep_Id(Integer dep_id){
		Map params = new HashMap();
		params.put("dep_id", dep_id);
		return departmentVaultMapper.queryDepartmentVaults(params);
	}
	
	//按部门编号返回历史DepartmentVaults
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<DepartmentVault> queryHistoryDepartmentVaultByDep_Id(Integer dep_id){
		Map params = new HashMap();
		params.put("dep_id", dep_id);
		params.put("state", true);
		return departmentVaultMapper.queryDepartmentVaults(params);
	}
	
	@Transactional
	public void assignVaults(Integer dep_id, List<String> new_v_numbers){
		//先获取该部门的所有账户信息
		List<DepartmentVault> allDepartmentVaults = queryDepartmentVaultByDep_Id(dep_id);
		for(DepartmentVault departmentVault : allDepartmentVaults){
			//如果是正在用的账户并且没有出现在最新的账户中，则将其置为“历史账户”
			if((departmentVault.getState() == false) && (new_v_numbers.contains(departmentVault.getV_number()) == false)){
				departmentVault.setEnd_time(new Timestamp(System.currentTimeMillis()));
				departmentVault.setState(true);
				departmentVaultMapper.updateDepartmentVault(departmentVault);
			}
		}
		//如果该部门的账户信息中存在和最新账户相同的历史账户，则将原账户信息置为“当前账户”；否则新建一条记录
		for(String v_number : new_v_numbers){
			if(!v_number.equals("")){//如果前端没有选中任何选项，则split的结果为""，传递过来的new_v_number也为空
				Boolean exist = false;
				for(DepartmentVault departmentVault : allDepartmentVaults){
					if(departmentVault.getV_number().equals(v_number)){
						exist = true;
						if(departmentVault.getState() == true){
							departmentVault.setEnd_time(departmentVault.getStart_time());
							departmentVault.setState(false);
							departmentVaultMapper.updateDepartmentVault(departmentVault);
						}
						break;
					}
				}
				if(exist == false){
					DepartmentVault dep_vault = new DepartmentVault(dep_id, v_number);
					departmentVaultMapper.insertDepartmentVault(dep_vault);
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public void setDepartmentVaultHistory(String v_number){
		Map params = new HashMap();
		params.put("v_number", v_number);
		params.put("end_time", new Timestamp(System.currentTimeMillis()));
		params.put("state", true);
		departmentVaultMapper.updateDepartmentVaults(params);
	}

}
