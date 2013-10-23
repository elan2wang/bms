/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.persistence;

import java.util.List;
import java.util.Map;

import com.bms.message.pojo.PushMessage;

/**
 * @author wangjian
 * @create 2013年8月6日 下午7:08:54
 * @update TODO
 * 
 * 
 */
public interface PushMessageMapper {
	
	public void insertPushMessage(PushMessage pushMessage);
	
	public void updatePushMessage(PushMessage pushMessage);
	
	public void deletePushMessage(Integer id);
	
	@SuppressWarnings("rawtypes")
	public PushMessage queryPushMessage(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<PushMessage> queryPushMessages(Map params);
	
}
