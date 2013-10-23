/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.core;

import java.util.Map;

import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * @author wangjian
 * @create 2013年8月8日 下午2:45:07
 * @update TODO
 * 
 * 
 */
public class MessageSocketCreator implements WebSocketCreator{

	@Override
	public Object createWebSocket(UpgradeRequest req, UpgradeResponse resp) {
		Map<String, String[]> params = req.getParameterMap();
		Integer principal = Integer.parseInt(params.get("account")[0]);
		return new MessageSocket(principal);

	}
}
