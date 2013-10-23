/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.persistence;

import java.util.List;
import java.util.Map;

import com.bms.task.pojo.Subtask;

/**
 * @author wangjian
 * @create 2013年8月15日 下午11:55:24
 * @update TODO
 * 
 * 
 */
public interface SubtaskMapper {

	public void insertSubtask(Subtask subtask);
	
	public void updateSubtask(Subtask subtask);
	
	public void deleteSubtask(Integer id);
	
	@SuppressWarnings("rawtypes")
	public void deleteSubtasks(Map params);
	
	@SuppressWarnings("rawtypes")
	public Subtask querySubtask(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Subtask> querySubtasks(Map params);

	@SuppressWarnings("rawtypes")
	public List<Subtask> querySubtasksByTaskAndOwner(Map params);
	
	@SuppressWarnings("rawtypes")
	public Integer querySubtaskCount(Map params);
	
}
