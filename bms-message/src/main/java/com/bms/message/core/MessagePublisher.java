/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.core;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bms.common.AppContext;
import com.bms.common.http.NormalResponse;
import com.bms.message.pojo.PushMessage;
import com.bms.message.service.PushMessageService;

/**
 * @author wangjian
 * @create 2013年8月12日 上午11:28:25
 * @update TODO
 * 
 * 
 */
public class MessagePublisher {

	private static Logger log = LoggerFactory.getLogger(MessagePublisher.class);
	private static PushMessageService pushMessageService;
	
	static {
		pushMessageService = (PushMessageService) AppContext.getBean("pushMessageService");
	}
	
	@SuppressWarnings("unchecked")
	public static void sendMessages(RemoteEndpoint remote, List<PushMessage> messages) {
		// wrap the response data
		NormalResponse response = new NormalResponse();
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		attrs.put("msg_count", messages.size());
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		for (PushMessage msg : messages) {
			Map<String, Object> item = msg.toMap();
			items.add(item);
		}
		attrs.put("items", items);
		response.setAttrs(attrs);

		Future<Void> fut = null;
		try {
			fut = remote.sendStringByFuture(response.toJson());
			// wait 10s for sending 
			fut.get(10, TimeUnit.SECONDS);
			// update receive time
			for (PushMessage msg : messages) {
				if (msg.getReceive_time() == null) {
					msg.setReceive_time(new Timestamp(System.currentTimeMillis()));
					pushMessageService.updatePushMessage(msg);
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			// send failed
			log.error("sending pushMessage failed. " + response.toJson());
			e.printStackTrace();
		} catch (TimeoutException e) {
			log.error("sending pushMessage timeout. " + response.toJson());
			e.printStackTrace();
			if (fut != null) {
				fut.cancel(true);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void sendMessage(RemoteEndpoint remote, PushMessage pushMsg) {
		// wrap the response data
		NormalResponse response = new NormalResponse();
		Map<String, Object> attrs = new LinkedHashMap<String, Object>();
		attrs.put("msg_count", 1);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = pushMsg.toMap();
		items.add(item);
		attrs.put("items", items);
		response.setAttrs(attrs);

		Future<Void> fut = null;
		try {
			fut = remote.sendStringByFuture(response.toJson());
			// wait 10s for sending 
			fut.get(10, TimeUnit.SECONDS);
			// update receive time
			if (pushMsg.getReceive_time() == null) {
				pushMsg.setReceive_time(new Timestamp(System.currentTimeMillis()));
				pushMessageService.updatePushMessage(pushMsg);
			}
		} catch (InterruptedException | ExecutionException e) {
			// send failed
			log.error("sending pushMessage failed. " + response.toJson());
			e.printStackTrace();
		} catch (TimeoutException e) {
			log.error("sending pushMessage timeout. " + response.toJson());
			e.printStackTrace();
			if (fut != null) {
				fut.cancel(true);
			}
		}
	}
}
