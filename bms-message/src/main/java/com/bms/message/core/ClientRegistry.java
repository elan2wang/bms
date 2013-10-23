/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.core;

import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author wangjian
 * @create 2013年8月8日 下午12:55:39
 * @update TODO
 * 
 * 
 */
public class ClientRegistry {

	private static Logger log = LoggerFactory.getLogger(ClientRegistry.class);
	private final ConcurrentMap<Object, Session> clients = new ConcurrentHashMap<Object, Session>();
	
	private static ClientRegistry socketClientRegistry;
	
	private ClientRegistry() { }
	
	public static ClientRegistry getInstance() {
		if (socketClientRegistry == null) {
			socketClientRegistry = new ClientRegistry();
		}
		return socketClientRegistry;
	}

	public void registerNewClient(Object principal, Session session) throws IOException {
		Session client = getClient(principal);
		if (client != null) {
			log.debug("old client for user: "+principal+" will destroy");
			client.close();
			clients.remove(principal);
		}
		clients.put(principal, session);
		log.debug("new client opened for user: "+principal);
	}
		
	public Session getClient(Object principal) {
		Session session = clients.get(principal);
		if (session != null && session.isOpen()) {
			return session;
		} else {
			return null;
		}
	}
	
	public void removeClient(Object principal) {
		if (clients.containsKey(principal)) {
			clients.remove(principal);
		}
	}
}
