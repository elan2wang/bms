/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.persistence;

import java.util.List;
import java.util.Map;

import com.bms.task.pojo.SubtaskItem;

/**
 * @author wangjian
 * @create 2013年8月15日 下午11:55:37
 * @update TODO
 * 
 * 
 */
public interface SubtaskItemMapper {
	
	public void insertSubtaskItem(SubtaskItem subtaskItem);
	
	public void updateSubtaskItem(SubtaskItem subtaskItem);
	
	public void deleteSubtaskItem(Integer id);
	
	@SuppressWarnings("rawtypes")
	public void deleteSubtaskItems(Map params);
	
	@SuppressWarnings("rawtypes")
	public SubtaskItem querySubtaskItem(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<SubtaskItem> querySubtaskItemsBySubtask(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<SubtaskItem> querySubtaskItemsByTask(Map params);
	
	@SuppressWarnings("rawtypes")
	public Integer querySubtaskItemCount(Map params);
}
