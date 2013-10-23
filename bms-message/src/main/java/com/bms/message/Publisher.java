/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message;


import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bms.common.AppContext;
import com.bms.common.consts.State;
import com.bms.message.core.ClientRegistry;
import com.bms.message.core.MessagePublisher;
import com.bms.message.pojo.BmpMessage;
import com.bms.message.pojo.PushMessage;
import com.bms.message.service.BmpMessageService;
import com.bms.message.service.PushMessageService;

/**
 * @author wangjian
 * @create 2013年8月8日 下午6:01:59
 * @update TODO
 * 
 * 
 */
public class Publisher {

	private static Logger log = LoggerFactory.getLogger(Publisher.class);
	private static ClientRegistry clientRegistry;
	private static BmpMessageService bmpMessageService;
	private static PushMessageService pushMessageService;
	
	static {
		clientRegistry = ClientRegistry.getInstance();
		bmpMessageService = (BmpMessageService) AppContext.getBean("bmpMessageService");
		pushMessageService = (PushMessageService) AppContext.getBean("pushMessageService");
	}

	public static void publish(BmpMessage bmpMessage) {
		// save bmpMessage
		bmpMessageService.insertBmpMessage(bmpMessage);
		// generate pushMessage
		String[] dst_list = bmpMessage.getDst_list().split(",");
		for (String dst : dst_list) {
			if (dst.equals(""))continue;
			Integer dst_id = Integer.parseInt(dst);
			PushMessage pushMsg = new PushMessage();
			pushMsg.setCreate_time(bmpMessage.getTrigger_time());
			pushMsg.setMsg_id(bmpMessage.getId());
			pushMsg.setDst_id(dst_id);
			pushMsg.setLink(bmpMessage.getLink());
			pushMsg.setMsg_content(bmpMessage.getMsg_content());
			pushMsg.setMsg_type(bmpMessage.getMsg_type());
			pushMsg.setState(State.PUSH_MESSAGE_NEW);
			
			// save pushMessage
			pushMessageService.insertPushMessage(pushMsg);
			// publish pushMessage
			Session session = clientRegistry.getClient(dst_id);
			if (session != null) {
				RemoteEndpoint remote = session.getRemote();
				MessagePublisher.sendMessage(remote, pushMsg);
			} else {
				log.info("account " + dst + " is not online");
			}
		}

	}
}
