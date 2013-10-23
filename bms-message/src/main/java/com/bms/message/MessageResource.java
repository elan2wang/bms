/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.message;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.security.core.SecurityContextHolder;
import com.bms.common.ConfigUtil;
import com.bms.common.Page;
import com.bms.common.consts.State;
import com.bms.common.http.ErrorResponse;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;
import com.bms.message.pojo.PushMessage;
import com.bms.message.service.PushMessageService;

/**
 * @author wangjian
 * @create 2013年8月12日 下午2:28:36
 * @update TODO
 * 
 * 
 */
@Path("/messages")
@Controller
public class MessageResource extends BaseResource{

	@Autowired
	private PushMessageService pushMessageService;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String list(@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage,
			@QueryParam("state") String state, @QueryParam("msg_type") String msg_type) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		Map<String, Object> params = new HashMap<String, Object>();
		Page page = new Page();
		params.put("page", page);
		params.put("dst_id", uid);
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);
		if (state != null && !state.equals("")) params.put("state", state);
		if (msg_type != null && !msg_type.equals("")) params.put("msg_type", msg_type);

		List<PushMessage> list = pushMessageService.queryPushMessages(params);
		List<Map<String, Object>> msgObjList = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (PushMessage msg : list) {
				Map<String, Object> msgObj = msg.toMap();
				msgObjList.add(msgObj);
			}
		}

		Map<String, Object> attrs = generateQueryResult(page, msgObjList);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);

		return response.toJson();
	}

	@SuppressWarnings("unchecked")
	@Path("/view")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getMessage(@QueryParam("msg_id") Integer msg_id) {
		PushMessage msg = pushMessageService.queryPushMessage(msg_id);
		if (msg == null) {
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20601");
			ErrorResponse response = new ErrorResponse(request, "20601", error_msg);
			return response.toJson();
		}

		NormalResponse response = new NormalResponse();
		response.setAttrs(msg.toMap());
		return response.toJson();
	}

	@Path("/read")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String readMessage(@FormParam("msg_id") Integer msg_id) {
		PushMessage msg = pushMessageService.queryPushMessage(msg_id);
		if (msg == null) {
			String request = uriInfo.getRequestUri().toString();
			String error_msg = ConfigUtil.getValue("20601");
			ErrorResponse response = new ErrorResponse(request, "20601", error_msg);
			return response.toJson();
		}
		msg.setState(State.PUSH_MESSAGE_READ);
		msg.setRead_time(new Timestamp(System.currentTimeMillis()));
		
		pushMessageService.updatePushMessage(msg);
		
		SuccessResponse response = new SuccessResponse(10000, "Read Message Successfully");
		return response.toJson();
	}

	@Path("/publish")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String publishMessage() {
		return null;
	}

}
