/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.persistence;

import java.util.List;
import java.util.Map;

import com.bms.message.pojo.BmpMessage;

/**
 * @author wangjian
 * @create 2013年8月6日 下午7:08:38
 * @update TODO
 * 
 * 
 */
public interface BmpMessageMapper {
	
	public void insertBmpMessage(BmpMessage bmpMessage);
	
	public void updateBmpMessage(BmpMessage bmpMessage);
	
	public void deleteBmpMessage(Integer id);
	
	@SuppressWarnings("rawtypes")
	public BmpMessage queryBmpMessage(Map params);
	
	@SuppressWarnings("rawtypes")
	public List<BmpMessage> queryBmpMessages(Map params);
	
}
