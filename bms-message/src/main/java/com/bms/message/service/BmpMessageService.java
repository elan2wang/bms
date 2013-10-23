/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.message.persistence.BmpMessageMapper;
import com.bms.message.pojo.BmpMessage;

/**
 * @author wangjian
 * @create 2013年8月9日 下午2:45:38
 * @update TODO
 * 
 * 
 */
@Service
public class BmpMessageService {

	@Autowired
	private BmpMessageMapper bmpMessageMapper;
	
	@Transactional
	public void insertBmpMessage(BmpMessage bmpMessage) {
		bmpMessageMapper.insertBmpMessage(bmpMessage);
	}
	
	@Transactional
	public void updateBmpMessage(BmpMessage bmpMessage) {
		bmpMessageMapper.updateBmpMessage(bmpMessage);
	}
	
	@Transactional
	public void deleteBmpMessage(Integer id) {
		bmpMessageMapper.deleteBmpMessage(id);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BmpMessage queryBmpMessageById(Integer id) {
		Map params = new HashMap();
		params.put("id", id);
		
		return bmpMessageMapper.queryBmpMessage(params);
	}

}
