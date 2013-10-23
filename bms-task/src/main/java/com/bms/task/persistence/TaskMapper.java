/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.persistence;

import java.util.List;
import java.util.Map;

import com.bms.task.pojo.Task;

/**
 * @author wangjian
 * @create 2013年8月15日 下午11:55:11
 * @update TODO
 * 
 * 
 */
public interface TaskMapper {
	
	public void insertTask(Task task);
	
	public void updateTask(Task task);
	
	@SuppressWarnings("rawtypes")
	public void updateTasks(Map params);
	
	public void deleteTask(Integer id);
	
	@SuppressWarnings("rawtypes")
	public void deleteTasks(Map params);
	
	@SuppressWarnings("rawtypes")
	public Task queryTask(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Task> queryTasks(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<Task> queryMyTasks(Map params);
	
}
