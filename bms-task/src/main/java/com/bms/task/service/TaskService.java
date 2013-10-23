/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.common.BaseService;
import com.bms.common.Page;
import com.bms.common.consts.Periodicity;
import com.bms.task.core.Timepair;
import com.bms.task.core.TimepairGenerator;
import com.bms.task.persistence.SubtaskItemMapper;
import com.bms.task.persistence.TaskMapper;
import com.bms.task.pojo.Task;

/**
 * @author wangjian
 * @create 2013年8月15日 下午11:56:21
 * @update TODO
 * 
 * 
 */
@Service
public class TaskService extends BaseService{
	
	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private SubtaskItemMapper subtaskItemMapper;
	@Autowired
	private TimepairGenerator timepairGenerator;
	
	@Transactional
	public void addTasks(Task task, Timestamp expire_time) {
		// no repeat
		if (task.getPeriodicity().equals("oncely")) {
			taskMapper.insertTask(task);
			return;
		}
		// with repeat
		List<Timepair> list = timepairGenerator.generateTimePair(task.getStart_time(),
				task.getEnd_time(), expire_time, Periodicity.valueOf(task.getPeriodicity()));
		if (list == null || list.size() == 0) return;
		for (Timepair pair : list) {
			Task newTask = (Task) task.clone();
			newTask.setStart_time(pair.getStart_time());
			newTask.setEnd_time(pair.getEnd_time());
			taskMapper.insertTask(newTask);
		}
	}
	
	@Transactional
	public void updateTask(Task task) {
		taskMapper.updateTask(task);
	}
	
	@Transactional
	public void updateTasks(Task task) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("task", task);
		params.put("begin", new Timestamp(System.currentTimeMillis()));
		taskMapper.updateTasks(params);
	}
	
	@Transactional
	public void deleteTask(Integer task_id) {
		taskMapper.deleteTask(task_id);
	}
	
	@Transactional
	public void deleteTasks(String task_gid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gid", task_gid);
		params.put("begin", new Timestamp(System.currentTimeMillis()));
		taskMapper.deleteTasks(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Task> queryMyCurrentTasks(Map params) {
		Timestamp current_time = new Timestamp(System.currentTimeMillis());
		params.put("current_time", current_time);
		params.put("not_finished", true);
		List<Task> list = taskMapper.queryMyTasks(params);
		Page page = (Page) params.get("page");
		
		return paging(list, page);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Task> queryMyAllTasks(Map params) {
		List<Task> list = taskMapper.queryMyTasks(params);
		Page page = (Page) params.get("page");
		
		return paging(list, page);
	}
	
	public Task queryTask(Integer task_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", task_id);
		return taskMapper.queryTask(params);
	}
	
	@SuppressWarnings("rawtypes")
	public Task queryTask(Map params) {
		return taskMapper.queryTask(params);
	}
	
	public List<Task> queryTasks(String task_gid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gid", task_gid);
		
		return taskMapper.queryTasks(params);
	}
}
