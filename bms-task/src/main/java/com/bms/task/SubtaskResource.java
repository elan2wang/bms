/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.common.ConfigUtil;
import com.bms.common.consts.MsgType;
import com.bms.common.consts.Periodicity;
import com.bms.common.consts.State;
import com.bms.common.http.ErrorResponse;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;
import com.bms.message.Publisher;
import com.bms.message.pojo.BmpMessage;
import com.bms.security.core.SecurityContextHolder;
import com.bms.task.pojo.Subtask;
import com.bms.task.service.SubtaskService;

/**
 * @author wangjian
 * @create 2013年8月17日 下午7:00:57
 * @update TODO
 * 
 * 
 */
@Path("/subtasks")
@Controller
public class SubtaskResource {
	private static Logger log = LoggerFactory.getLogger(SubtaskResource.class);
	@Autowired
	private SubtaskService subtaskService;

	@Context
	UriInfo uriInfo;

	@SuppressWarnings("unchecked")
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addSubtasks(@FormParam("title") String title, @FormParam("type") String type,
			@FormParam("start_time") String start_time, @FormParam("task_id") Integer task_id,
			@FormParam("end_time") String end_time, @FormParam("task_gid") String task_gid,
			@FormParam("owners") String owners,@FormParam("periodicity") String periodicity) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		String username = SecurityContextHolder.getContext().getAuthenticationToken().getUsername();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		// 获取表单参数
		Subtask subtask = new Subtask();
		subtask.setTitle(title);
		subtask.setStart_time(Timestamp.valueOf(start_time));
		subtask.setEnd_time(Timestamp.valueOf(end_time));
		subtask.setCreate_time(currentTime);
		subtask.setOwners(owners);// multiple owner id join with ',', eg: 1,2,3
		subtask.setStar(false);
		subtask.setState(State.SUBTASK_NOT_START);
		subtask.setGid(UUID.randomUUID().toString());
		subtask.setLast_modify_person(uid);
		subtask.setLast_modify_time(currentTime);
		
		if (type.equals("all")) {
			log.debug("type=all");
			Periodicity p = Periodicity.valueOf(periodicity);
			log.debug("task_gid="+task_gid+"\nperiodicity="+p);
			subtaskService.addSubtasks(subtask, task_gid, p);

		} else if (type.equals("one")) {
			log.debug("type=one");
			subtask.setTask_id(task_id);
			subtaskService.addSubtask(subtask);
			
		} else {
			String request = uriInfo.getRequestUri().toString(); 
			String error_msg = String.format(ConfigUtil.getValue("10011"), "type", "one or all", type);
			ErrorResponse response = new ErrorResponse(request, "10011", error_msg);
			return response.toJson();
		}
		// 发送消息
		BmpMessage message = new BmpMessage();
		message.setDst_list(owners);
		message.setMsg_content("您有新任务["+title+"]");
		message.setMsg_type(MsgType.task_msg.toString());
		message.setTrigger_time(new Timestamp(System.currentTimeMillis()));
		message.setTrigger_event(uriInfo.getRequestUri().toString());
		Publisher.publish(message);
		// 返回数据
		NormalResponse response = new NormalResponse();
		Map<String, Object> attrs = subtask.toMap();
		attrs.put("last_modify_person", username);
		response.setAttrs(attrs);
		return response.toJson();
	}

	@SuppressWarnings("unchecked")
	@Path("/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateSubtask(@FormParam("subtask_id") Integer subtask_id, 
			@FormParam("subtask_gid") String subtask_gid, @FormParam("type") String type,
			@FormParam("title") String title, @FormParam("start_time") String start_time,
			@FormParam("end_time") String end_time, @FormParam("owners") String owners,
			@FormParam("periodicity") String periodicity) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		String username = SecurityContextHolder.getContext().getAuthenticationToken().getUsername();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Subtask subtask = null;

		if (type.equals("one")) {
			subtask = subtaskService.querySubtaskById(subtask_id);
			List<String> allOwners = Arrays.asList((subtask.getOwners() + "," + owners).split(","));
			List<String> oldOwners = Arrays.asList(subtask.getOwners().split(","));
			List<String> newOwners = Arrays.asList(owners.split(","));
			if (allOwners == null) allOwners = new ArrayList<String>();
			if (oldOwners == null) oldOwners = new ArrayList<String>();
			if (newOwners == null) newOwners = new ArrayList<String>();
			subtask.setTitle(title);
			subtask.setStart_time(Timestamp.valueOf(start_time));
			subtask.setEnd_time(Timestamp.valueOf(end_time));
			subtask.setOwners(owners);// multiple owner id join with ',', eg: 1,2,3
			subtask.setLast_modify_person(uid);
			subtask.setLast_modify_time(currentTime);
			subtaskService.updateSubtask(subtask);

			// 发送消息
			StringBuilder newAddOwners = new StringBuilder();
			StringBuilder deletedOwners = new StringBuilder();
			StringBuilder nonChangeOwners = new StringBuilder();
			for (String owner : allOwners) {
				if (oldOwners.contains(owner) && newOwners.contains(owner)) {
					nonChangeOwners.append(owner+",");
				} else if (oldOwners.contains(owner) && !newOwners.contains(owner)) {
					deletedOwners.append(owner+",");
				} else if (!oldOwners.contains(owner) && newOwners.contains(owner)) {
					newAddOwners.append(owner+",");
				}
			}
			if (deletedOwners.length() > 0) {
				BmpMessage msg = new BmpMessage();
				msg.setDst_list(deletedOwners.deleteCharAt(deletedOwners.length()-1).toString());
				String content = subtask.getTitle();
				msg.setMsg_content("您的任务["+content+"]已经被撤销了");
				msg.setMsg_type(MsgType.task_msg.toString());
				msg.setTrigger_time(new Timestamp(System.currentTimeMillis()));
				msg.setTrigger_event(uriInfo.getRequestUri().toString());
				Publisher.publish(msg);
			}
			if (newAddOwners.length() > 0) {
				BmpMessage msg = new BmpMessage();
				msg.setDst_list(newAddOwners.deleteCharAt(newAddOwners.length()-1).toString());
				String content = subtask.getTitle();
				msg.setMsg_content("您有新任务["+content+"]");
				msg.setMsg_type(MsgType.task_msg.toString());
				msg.setTrigger_time(new Timestamp(System.currentTimeMillis()));
				msg.setTrigger_event(uriInfo.getRequestUri().toString());
				Publisher.publish(msg);
			}
			if (nonChangeOwners.length() > 0) {
				BmpMessage msg = new BmpMessage();
				msg.setDst_list(nonChangeOwners.deleteCharAt(nonChangeOwners.length()-1).toString());
				String content = subtask.getTitle();
				msg.setMsg_content("您有新任务["+content+"]");
				msg.setMsg_type(MsgType.task_msg.toString());
				msg.setTrigger_time(new Timestamp(System.currentTimeMillis()));
				msg.setTrigger_event(uriInfo.getRequestUri().toString());
				Publisher.publish(msg);
			}
		} else if (type.equals("all")) {
			subtask = new Subtask();
			subtask.setTitle(title);
			subtask.setStart_time(Timestamp.valueOf(start_time));
			subtask.setEnd_time(Timestamp.valueOf(end_time));
			subtask.setCreate_time(currentTime);
			subtask.setOwners(owners);// multiple owner id join with ',', eg: 1,2,3
			subtask.setStar(false);
			subtask.setState(State.SUBTASK_NOT_START);
			subtask.setGid(subtask_gid);
			subtask.setLast_modify_person(uid);
			subtask.setLast_modify_time(currentTime);

			Periodicity p = Periodicity.valueOf(periodicity);
			subtaskService.updateSubtasks(subtask, p);
		} else {
			String request = uriInfo.getRequestUri().toString(); 
			String error_msg = String.format(ConfigUtil.getValue("10011"), "type", "one or all", type);
			ErrorResponse response = new ErrorResponse(request, "10011", error_msg);
			return response.toJson();
		}

		// 返回数据
		NormalResponse response = new NormalResponse();
		Map<String, Object> attrs = subtask.toMap();
		attrs.put("last_modify_person", username);
		response.setAttrs(attrs);
		return response.toJson();
	}


	@Path("/delete")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String deleteSubtask(@FormParam("subtask_gid") String gid, @FormParam("subtask_id") Integer id,
			@FormParam("type") String type) {
		if (type.equals("one")) {
			subtaskService.deleteSubtask(id);
			SuccessResponse response = new SuccessResponse(10000, "Delete Subtask Successfully");
			return response.toJson();
		} else if (type.equals("all")){
			subtaskService.deleteSubtasks(gid);
			SuccessResponse response = new SuccessResponse(10000, "Delete Subtasks Successfully");
			return response.toJson();
		} else {
			String request = uriInfo.getRequestUri().toString(); 
			String error_msg = String.format(ConfigUtil.getValue("10011"), "type", "one or all", type);
			ErrorResponse response = new ErrorResponse(request, "10011", error_msg);
			return response.toJson();
		}
	}

}
