/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangjian
 * @create 2013年8月8日 上午11:31:45
 * @update TODO
 * 
 * 
 */
public class MessageServlet extends WebSocketServlet {
	private static final long serialVersionUID = 7372502483496994891L;
	private static Logger log = LoggerFactory.getLogger(MessageServlet.class);
		
	@Override
	public void configure(WebSocketServletFactory factory) {
		// 设置Socket的IdleTimeout为1分钟
		factory.getPolicy().setIdleTimeout(1000*60);
		factory.setCreator(new MessageSocketCreator());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		log.debug(request.getRequestURL().toString());
		// All other processing
		super.service(request,response);
	}
	
}