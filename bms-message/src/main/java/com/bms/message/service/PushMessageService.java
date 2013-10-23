/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.common.BaseService;
import com.bms.common.Page;
import com.bms.message.persistence.PushMessageMapper;
import com.bms.message.pojo.PushMessage;

/**
 * @author wangjian
 * @create 2013年8月9日 下午2:45:55
 * @update TODO
 * 
 * 
 */
@Service
public class PushMessageService extends BaseService{

	@Autowired
	private PushMessageMapper pushMessageMapper;
	
	@Transactional
	public void insertPushMessage(PushMessage pushMessage) {
		pushMessageMapper.insertPushMessage(pushMessage);
	}
	
	@Transactional
	public void updatePushMessage(PushMessage pushMessage) {
		pushMessageMapper.updatePushMessage(pushMessage);
	}
	
	@Transactional
	public void deletePushMessage(Integer id) {
		pushMessageMapper.deletePushMessage(id);
	}

	public PushMessage queryPushMessage(Integer id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		
		return pushMessageMapper.queryPushMessage(params);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<PushMessage> queryPushMessages(Map params) {
		List<PushMessage> list = pushMessageMapper.queryPushMessages(params);
		Page page = (Page) params.get("page");
		
		return paging(list, page);
	}
	
	public List<PushMessage> queryPushMessages(String state, Integer dst_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("state", state);
		params.put("dst_id", dst_id);
		
		return pushMessageMapper.queryPushMessages(params);
	}
}
