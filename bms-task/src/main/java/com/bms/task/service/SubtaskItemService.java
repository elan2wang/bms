/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.common.consts.State;
import com.bms.task.persistence.SubtaskItemMapper;
import com.bms.task.pojo.SubtaskItem;

/**
 * @author wangjian
 * @create 2013年8月20日 下午7:25:14
 * @update TODO
 * 
 * 
 */
@Service
public class SubtaskItemService {

	@Autowired
	private SubtaskItemMapper subtaskItemMapper;
	
	@Transactional
	public void confirmSubtaskItems(Integer task_id, Integer owner) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("task_id", task_id);
		params.put("owner", owner);
		List<SubtaskItem> list = subtaskItemMapper.querySubtaskItemsByTask(params);
		
		for (SubtaskItem item : list) {
			item.setConfirm_time(new Timestamp(System.currentTimeMillis()));
			item.setState(State.SUBTASK_ITEM_CONFIRMED);
			subtaskItemMapper.updateSubtaskItem(item);
		}
	}
	
	@Transactional 
	public void finishSubtaskItem(Integer subtask_id, Integer owner, String comment) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subtask_id", subtask_id);
		params.put("owner", owner);
		SubtaskItem item = subtaskItemMapper.querySubtaskItem(params);
		item.setComment(comment);
		item.setState(State.SUBTASK_ITEM_FINISHED);
		item.setFinish_time(new Timestamp(System.currentTimeMillis()));
		subtaskItemMapper.updateSubtaskItem(item);
	}
	
	public List<SubtaskItem> queryItemsBySubtask(Integer subtask_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subtask_id", subtask_id);
		return subtaskItemMapper.querySubtaskItemsBySubtask(params);
	}

	public List<SubtaskItem> queryItemsByTask(Integer task_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("task_id", task_id);
		return subtaskItemMapper.querySubtaskItemsByTask(params);
	}
	
	public Map<Integer, List<SubtaskItem>> queryItems() {
		Map<Integer, List<SubtaskItem>> map = new HashMap<Integer, List<SubtaskItem>>();
		List<SubtaskItem> items = subtaskItemMapper.querySubtaskItemsBySubtask(new HashMap<String, Object>());
		for (SubtaskItem item : items) {
			if (map.containsKey(item.getSubtask_id())) {
				map.get(item.getSubtask_id()).add(item);
			} else {
				List<SubtaskItem> list = new ArrayList<SubtaskItem>();
				list.add(item);
				map.put(item.getSubtask_id(), list);
			}
		}	
		return map;
	}
	

	@SuppressWarnings("rawtypes")
	public Integer querySubtaskItemCount(Map params) {
		return subtaskItemMapper.querySubtaskItemCount(params);
	}
}
