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

import com.bms.admin.persistence.ModuleMapper;
import com.bms.admin.pojo.Module;
import com.bms.common.BaseService;
import com.bms.common.Page;

/**
 * @author wangjian
 * @create 2013年7月23日 下午11:39:00
 * @update TODO
 * 
 * 
 */
@Service
public class ModuleService extends BaseService{
	
	@Autowired
	private ModuleMapper moduleMapper;
	
	@Transactional
	public void insertModule(Module module) {
		moduleMapper.insertModule(module);
	}
	
	@Transactional
	public void updateModule(Module module) {
		moduleMapper.updateModule(module);
	}
	
	@Transactional
	public void deleteModule(Integer mod_id) {
		moduleMapper.deleteModule(mod_id);
	}
	
	public Module queryModuleById(Integer mod_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mod_id", mod_id);
		return moduleMapper.queryModule(params);
	}
	
	public List<Module> querySubModules(Integer father_mod, List<Integer> roles) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("father_mod", father_mod);
		params.put("roles", roles);
		return moduleMapper.querySubModules(params);
	}
	
	public List<Module> queryMenuModules(Integer mod_level, List<Integer> roles) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mod_level", mod_level);
		params.put("roles", roles);
		return moduleMapper.queryMenuModules(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Module> queryModules(Map params) {
		List<Module> list = moduleMapper.queryModules(params);
		Page page = (Page) params.get("page");
		if (page == null) {
			return list;
		}
		return paging(list, page);
	}
}
