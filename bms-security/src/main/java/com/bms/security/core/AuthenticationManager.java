/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.security.core;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bms.common.Crypto;
import com.bms.security.exception.AuthenticationException;
import com.bms.security.exception.BadCredentialsException;
import com.bms.security.exception.BadPrincipalsException;
import com.bms.security.service.SecurityService;
import com.bms.security.session.SessionStrategy;

/**
 * @author wangjian
 * @create 2013年8月1日 下午7:41:29
 * @update TODO
 * 
 * 
 */
@Component
public class AuthenticationManager {

	private static Logger log = LoggerFactory.getLogger(AuthenticationManager.class);
	
	@Autowired
	private SecurityService securityService;
	@Autowired
	private SessionStrategy sessionStrategy;
	
	public AuthenticationToken authenticate(AuthenticationToken token, HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException{
		String username = token.getUsername();
		String password = token.getPassword();
		
		log.info("begin to authenticated");
		
		Account account = securityService.queryAccountByUsername(username);
		// if the username is not exist
		if (account == null) {
			throw new BadPrincipalsException("the username ["+username+"] does not exist");
		}
		// if the password is not correct
		if (!account.getPassword().equals(Crypto.MD5Encrypt(password))) {
			throw new BadCredentialsException("password is not correct for the user ["+username+"]");
		}
		Integer uid = account.getAccount_id();
		List<Integer> roles = securityService.queryAccountRoles(uid);
		AuthenticationToken newToken = new AuthenticationToken(uid, username, password, roles);
		
		// do session strategy
		sessionStrategy.onAuthentication(newToken, request, response);
		
		return newToken;
	}
}
