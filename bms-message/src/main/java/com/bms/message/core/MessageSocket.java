/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.core;

import java.io.IOException;
import java.util.List;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bms.common.AppContext;
import com.bms.common.consts.State;
import com.bms.message.pojo.PushMessage;
import com.bms.message.service.PushMessageService;

/**
 * @author wangjian
 * @create 2013年8月8日 上午11:34:53
 * @update TODO
 * 
 * 
 */
public class MessageSocket implements WebSocketListener{

	private static Logger log = LoggerFactory.getLogger(MessageSocket.class);

	private Object principal;
	private Session session;
	private ClientRegistry clientRegistry;
	private PushMessageService pushMessageService;
		
	public MessageSocket() {
		super();
		clientRegistry = ClientRegistry.getInstance();
		pushMessageService = (PushMessageService) AppContext.getBean("pushMessageService");
	}
	
	public MessageSocket(Object principal) {
		super();
		this.principal = principal;
		clientRegistry = ClientRegistry.getInstance();
		pushMessageService = (PushMessageService) AppContext.getBean("pushMessageService");
	}
	
	@Override
	public void onWebSocketConnect(Session session) {
		log.debug(session.getRemoteAddress().toString()+" opened");
		this.session = session;
		try {
			clientRegistry.registerNewClient(principal, session);
			// get unread messages and send to this principal
			List<PushMessage> unreadMessages = 
					pushMessageService.queryPushMessages(State.PUSH_MESSAGE_NEW, (Integer) principal);
			MessagePublisher.sendMessages(session.getRemote(), unreadMessages);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void onWebSocketClose(int arg0, String arg1) {
		log.debug(session.getRemoteAddress().toString()+" closed");
		try {
			clientRegistry.removeClient(principal);
			session.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onWebSocketText(String message) {
		if (session != null && session.isOpen()) {
			if (message.equals("ping")) {
				session.getRemote().sendStringByFuture("pong");
			} else {
				log.error("receive unexpected message["+message+"]");
			}
        }
	}

	@Override
	public void onWebSocketBinary(byte[] arg0, int arg1, int arg2) {
		log.error("receive binary message from "+session.getRemoteAddress().toString());
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		cause.printStackTrace();
	}
	
}
