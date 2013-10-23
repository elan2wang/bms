/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.admin.persistence;

import java.util.List;
import java.util.Map;

import com.bms.admin.pojo.Module;

/**
 * @author wangjian
 * @create 2013年7月23日 下午11:28:02
 * @update TODO
 * 
 * 
 */
public interface ModuleMapper {
	
	public void insertModule(Module module);
	
	public void updateModule(Module module);
	
	public void deleteModule(Integer mod_id);
	
	@SuppressWarnings("rawtypes")
	public Module queryModule(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Module> queryModules(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Module> querySubModules(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Module> queryMenuModules(Map params);
}
