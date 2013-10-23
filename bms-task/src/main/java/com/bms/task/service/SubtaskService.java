/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.common.consts.Periodicity;
import com.bms.common.consts.State;
import com.bms.task.core.Timepair;
import com.bms.task.core.TimepairGenerator;
import com.bms.task.persistence.SubtaskItemMapper;
import com.bms.task.persistence.SubtaskMapper;
import com.bms.task.persistence.TaskMapper;
import com.bms.task.pojo.Subtask;
import com.bms.task.pojo.SubtaskItem;
import com.bms.task.pojo.Task;

/**
 * @author wangjian
 * @create 2013年8月17日 下午7:01:37
 * @update TODO
 * 
 * 
 */
@Service
public class SubtaskService {
	private static Logger log = LoggerFactory.getLogger(SubtaskService.class);

	@Autowired
	private SubtaskMapper subtaskMapper;
	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private SubtaskItemMapper subtaskItemMapper;
	@Autowired
	private TimepairGenerator timepairGenerator;

	@Transactional
	public void addSubtask(Subtask subtask) {
		subtaskMapper.insertSubtask(subtask);
		// insert subtaskItem
		String[] owners = subtask.getOwners().trim().split(",");
		for (String owner : owners) {
			if (owner.equals("")) continue;
			SubtaskItem subtaskItem = new SubtaskItem();
			subtaskItem.setSubtask_id(subtask.getId());
			subtaskItem.setOwner(Integer.parseInt(owner));
			subtaskItem.setState(State.SUBTASK_ITEM_NOT_START);
			subtaskItemMapper.insertSubtaskItem(subtaskItem);
		}
	}
	
	@Transactional
	public void addSubtasks(Subtask subtask, String task_gid, Periodicity periodicity) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("gid", task_gid);
		List<Task> list = taskMapper.queryTasks(params);
		if (list == null || list.size() == 0) return;

		Timestamp start_time = subtask.getStart_time();
		Timestamp end_time = subtask.getEnd_time();
		Timepair pair = new Timepair(start_time, end_time);
		for (Task task : list) {
			Subtask newSubtask = (Subtask) subtask.clone();
			newSubtask.setStart_time(pair.getStart_time());
			newSubtask.setEnd_time(pair.getEnd_time());
			newSubtask.setTask_id(task.getId());
			subtaskMapper.insertSubtask(newSubtask);
			Integer subtask_id = newSubtask.getId();
			
			// insert subtaskItem
			log.debug(newSubtask.getOwners());
			String[] owners = newSubtask.getOwners().trim().split(",");
			for (String owner : owners) {
				if (owner.equals("")) continue;
				SubtaskItem subtaskItem = new SubtaskItem();
				subtaskItem.setSubtask_id(subtask_id);
				subtaskItem.setOwner(Integer.parseInt(owner));
				subtaskItem.setState(State.SUBTASK_ITEM_NOT_START);
				subtaskItemMapper.insertSubtaskItem(subtaskItem);
			}
			// generate next time
			pair = timepairGenerator.generateNextTime(start_time, end_time, periodicity);
			if (pair == null) break;
			start_time = pair.getStart_time();
			end_time = pair.getEnd_time();
		}
	}
	
	@Transactional
	public void updateSubtasks(Subtask subtask, Periodicity periodicity) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("gid", subtask.getGid());
		params.put("begin", new Timestamp(System.currentTimeMillis()));
		List<Subtask> list = subtaskMapper.querySubtasks(params);
		if (list == null || list.size() == 0) return;

		Timestamp start_time = subtask.getStart_time();
		Timestamp end_time = subtask.getEnd_time();
		Timepair pair = new Timepair(start_time, end_time);
		for (Subtask sub : list) {
			sub.setTitle(subtask.getTitle());
			sub.setOwners(subtask.getOwners());
			sub.setStart_time(pair.getStart_time());
			sub.setEnd_time(pair.getEnd_time());
			sub.setLast_modify_person(subtask.getLast_modify_person());
			sub.setLast_modify_time(subtask.getLast_modify_time());
			subtaskMapper.updateSubtask(sub);
			// 删除旧的责任人纪录
			params.clear();
			params.put("subtask_id", sub.getId());
			subtaskItemMapper.deleteSubtaskItems(params);
			// 添加新的责任人纪录
			String[] owners = sub.getOwners().trim().split(",");
			for (String owner : owners) {
				if (owner.equals("")) continue;
				SubtaskItem subtaskItem = new SubtaskItem();
				subtaskItem.setSubtask_id(sub.getId());
				subtaskItem.setOwner(Integer.parseInt(owner));
				subtaskItem.setState(State.SUBTASK_ITEM_NOT_START);
				subtaskItemMapper.insertSubtaskItem(subtaskItem);
			}
			// generate next time
			pair = timepairGenerator.generateNextTime(start_time, end_time, periodicity);
			if (pair == null) break;
			start_time = pair.getStart_time();
			end_time = pair.getEnd_time();
		}
	}
	
	@Transactional
	public void updateSubtask(Subtask subtask) {
		// 更新子任务基本信息
		subtaskMapper.updateSubtask(subtask);
		Integer subtask_id = subtask.getId();
		// 更新责任人纪录
		// 删除旧的责任人纪录
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subtask_id", subtask_id);
		subtaskItemMapper.deleteSubtaskItems(params);
		// 添加新的责任人纪录
		String[] owners = subtask.getOwners().trim().split(",");
		if (owners != null) {
			for (String owner : owners) {
				if (owner.equals("")) continue;
				SubtaskItem subtaskItem = new SubtaskItem();
				subtaskItem.setSubtask_id(subtask_id);
				subtaskItem.setOwner(Integer.parseInt(owner));
				subtaskItem.setState(State.SUBTASK_ITEM_NOT_START);
				subtaskItemMapper.insertSubtaskItem(subtaskItem);
			}
		}
	}
	
	@Transactional
	public void confirmSubtask(Subtask subtask) {
		subtaskMapper.updateSubtask(subtask);
	}
	
	@Transactional
	public void finishSubtask(Subtask subtask) {
		subtaskMapper.updateSubtask(subtask);
	}

	@Transactional
	public void deleteSubtask(Integer subtask_id) {
		subtaskMapper.deleteSubtask(subtask_id);
	}

	@Transactional
	public void deleteSubtasks(String subtask_gid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gid", subtask_gid);
		params.put("begin", new Timestamp(System.currentTimeMillis()));
		subtaskMapper.deleteSubtasks(params);
	}

	public Subtask querySubtaskById(Integer subtask_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", subtask_id);
		return subtaskMapper.querySubtask(params);
	}

	public List<Subtask> querySubtasksByTaskAndOwner(Integer task_id, Integer owner) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("task_id", task_id);
		params.put("owner", owner);
		return subtaskMapper.querySubtasksByTaskAndOwner(params);
	}
	
	public List<Subtask> querySubtasksByTask(Integer task_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("task_id", task_id);
		return subtaskMapper.querySubtasks(params);
	}
	
	@SuppressWarnings("rawtypes")
	public Integer querySubtaskCount(Map params) {
		return subtaskMapper.querySubtaskCount(params);
	}

}
