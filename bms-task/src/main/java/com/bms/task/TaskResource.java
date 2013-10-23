/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.bms.admin.service.AccountService;
import com.bms.common.ConfigUtil;
import com.bms.common.Page;
import com.bms.common.consts.MsgType;
import com.bms.common.consts.State;
import com.bms.common.http.ErrorResponse;
import com.bms.common.http.NormalResponse;
import com.bms.common.http.SuccessResponse;
import com.bms.message.Publisher;
import com.bms.message.pojo.BmpMessage;
import com.bms.security.core.SecurityContextHolder;
import com.bms.task.core.ReminderSetter;
import com.bms.task.pojo.Subtask;
import com.bms.task.pojo.SubtaskItem;
import com.bms.task.pojo.Task;
import com.bms.task.service.SubtaskItemService;
import com.bms.task.service.SubtaskService;
import com.bms.task.service.TaskService;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author wangjian
 * @create 2013年8月15日 下午11:57:00
 * @update TODO
 * 
 * 
 */
@Path("tasks")
@Controller
public class TaskResource extends BaseResource{

	@Autowired
	private TaskService taskService;
	@Autowired
	private SubtaskService subtaskService;
	@Autowired
	private SubtaskItemService subtaskItemService;
	@Autowired
	private AccountService accountService;

	@SuppressWarnings("unchecked")
	@GET @Path("/view")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTask(@QueryParam("task_id") Integer task_id) {
		Task task = taskService.queryTask(task_id);
		if (task == null) {
			String request = uriInfo.getPath(); 
			String error_msg = ConfigUtil.getValue("20501");
			ErrorResponse response = new ErrorResponse(request, "20501", error_msg);
			return response.toJson();
		}
		Map<Integer, List<SubtaskItem>> subtaskItemMap = subtaskItemService.queryItems();
		Map<Integer, String> accountMap = getAccountMap(accountService);

		Map<String, Object> taskObj = task.toMap();
		taskObj.put("creator", accountMap.get(task.getCreator())); 
		taskObj.put("last_modify_person", accountMap.get(task.getLast_modify_person()));
		List<Map<String, Object>> subtaskObjList = new ArrayList<Map<String, Object>>();
		taskObj.put("subtasks", subtaskObjList);
		List<Subtask> subtaskList = subtaskService.querySubtasksByTask(task.getId());
		for (Subtask sub : subtaskList) {
			Map<String, Object> subtaskObj = sub.toMap();
			subtaskObjList.add(subtaskObj);

			// owners
			List<Map<String, Object>> ownerObjList = new ArrayList<Map<String, Object>>();
			subtaskObj.put("owners", ownerObjList);
			List<SubtaskItem> itemList = subtaskItemMap.get(sub.getId());
			if (itemList != null) {
				for (SubtaskItem item : itemList) {
					Map<String, Object> itemObj = item.toMap();
					ownerObjList.add(itemObj);
					itemObj.put("owner_name", accountMap.get(item.getOwner()));
				}
			}
		}

		// 生成返回数据
		NormalResponse response = new NormalResponse();
		response.setAttrs(taskObj);
		return response.toJson();
	}

	@SuppressWarnings("unchecked")
	@GET @Path("/current_list")
	@Produces(MediaType.APPLICATION_JSON)
	public String currentList(@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		// 获取参数
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("owner", uid);
		Page page = new Page();
		params.put("page", page);
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);

		// 获取数据
		Map<Integer, String> accountMap = getAccountMap(accountService);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		Map<Integer, List<SubtaskItem>> subtaskItemMap = subtaskItemService.queryItems();
		List<Task> taskList = taskService.queryMyCurrentTasks(params);
		for (Task task : taskList) {
			Map<String, Object> taskObj = task.toMap();
			items.add(taskObj);
			// subtasks
			List<Map<String, Object>> subtaskObjList = new ArrayList<Map<String, Object>>();
			taskObj.put("subtasks", subtaskObjList);
			List<Subtask> subtaskList = subtaskService.querySubtasksByTask(task.getId());
			for (Subtask sub : subtaskList) {
				Map<String, Object> subtaskObj = sub.toMap();
				subtaskObjList.add(subtaskObj);

				// owners
				List<Map<String, Object>> ownerObjList = new ArrayList<Map<String, Object>>();
				subtaskObj.put("owners", ownerObjList);
				List<SubtaskItem> itemList = subtaskItemMap.get(sub.getId());
				if (itemList != null) {
					for (SubtaskItem item : itemList) {
						Map<String, Object> itemObj = new LinkedHashMap<String,Object>();
						ownerObjList.add(itemObj);
						itemObj.put("id", item.getOwner());
						itemObj.put("name", accountMap.get(item.getOwner()));
						itemObj.put("state", item.getState());
					}
				}
			}

		}

		// 生成返回数据
		Map<String, Object> attrs = generateQueryResult(page, items);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}

	@SuppressWarnings("unchecked")
	@GET @Path("/all_list")
	@Produces(MediaType.APPLICATION_JSON)
	public String allList(@QueryParam("start") Integer start, @QueryParam("itemsPerPage") Integer itemsPerPage,
			@QueryParam("state") String state, @QueryParam("creator") Integer creator, 
			@QueryParam("filter_title") String filter_title,
			@QueryParam("filter_start_time") String filter_start_time,
			@QueryParam("filter_end_time") String filter_end_time) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		// 获取参数
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("owner", uid);
		Page page = new Page();
		params.put("page", page);
		if (start != null) page.setStartIndex(start);
		if (itemsPerPage != null) page.setItemsPerPage(itemsPerPage);
		if (state != null) params.put("state", state);
		if (creator != null) params.put("creator", creator);
		if (filter_title != null && !filter_title.equals("")) params.put("filter_title", filter_title);
		if (filter_start_time != null && !filter_start_time.equals("")) params.put("filter_start_time",
				Timestamp.valueOf(filter_start_time));
		if (filter_end_time != null && !filter_end_time.equals("")) params.put("filter_end_time",
				Timestamp.valueOf(filter_end_time));
		
		// 获取数据
		Map<Integer, String> accountMap = getAccountMap(accountService);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		List<Task> taskList = taskService.queryMyAllTasks(params);
		for (Task task : taskList) {
			Map<String, Object> taskObj = task.toMap();
			items.add(taskObj);
			// modify some fields
			taskObj.put("creator_username", accountMap.get(task.getCreator()));
			taskObj.put("last_modify_person", accountMap.get(task.getLast_modify_person()));
		}

		// 生成返回数据
		Map<String, Object> attrs = generateQueryResult(page, items);
		NormalResponse response = new NormalResponse();
		response.setAttrs(attrs);
		return response.toJson();
	}

	@SuppressWarnings("unchecked")
	@Path("/add")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addTask(@FormParam("title") String title, @FormParam("start_time") String start_time,
			@FormParam("end_time") String end_time, @FormParam("expire_time") String expire_time,
			@FormParam("remind_time") Integer remind_time, @FormParam("periodicity") String periodicity,
			@FormParam("description") String description) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		String username = SecurityContextHolder.getContext().getAuthenticationToken().getUsername();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		String gid = UUID.randomUUID().toString();

		Task task = new Task();
		task.setCreator(uid);
		task.setTitle(title);
		task.setStart_time(Timestamp.valueOf(start_time));
		task.setEnd_time(Timestamp.valueOf(end_time));
		task.setCreate_time(currentTime);
		task.setRemind_time(remind_time);
		task.setDescription(description);
		task.setStar(false);
		task.setState(State.TASK_NOT_START);
		task.setPeriodicity(periodicity);
		task.setGid(gid);
		task.setLast_modify_person(uid);
		task.setLast_modify_time(currentTime);

		Timestamp expire = null;
		if (!periodicity.equals("oncely")) {
			expire = Timestamp.valueOf(expire_time);
		}
		taskService.addTasks(task, expire);

		try {
			ReminderSetter.addReminder(task, expire);
		} catch (ParseException | SchedulerException e) {
			e.printStackTrace();
		}
		
		NormalResponse response = new NormalResponse();
		Map<String, Object> attrs = task.toMap();
		attrs.put("creator", username);
		attrs.put("last_modify_person", username);
		response.setAttrs(attrs);

		return response.toJson();
	}

	@SuppressWarnings("unchecked")
	@Path("/update")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String updateTask(@FormParam("task_id") Integer task_id, @FormParam("task_gid") String task_gid,
			@FormParam("title") String title, @FormParam("start_time") String start_time,
			@FormParam("end_time") String end_time, @FormParam("remind_time") Integer remind_time,
			@FormParam("description") String description, @FormParam("periodicity") String periodicity,
			@FormParam("expire_time") String expire_time, @FormParam("type") String type) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		String username = SecurityContextHolder.getContext().getAuthenticationToken().getUsername();
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Task task = null;
		
		if (type.equals("all")) {
			task = new Task();
			task.setCreator(uid);
			task.setTitle(title);
			task.setStart_time(Timestamp.valueOf(start_time));
			task.setEnd_time(Timestamp.valueOf(end_time));
			task.setCreate_time(currentTime);
			task.setRemind_time(remind_time);
			task.setDescription(description);
			task.setStar(false);
			task.setState(State.TASK_NOT_START);
			task.setPeriodicity(periodicity);
			task.setGid(task_gid);
			task.setLast_modify_person(uid);
			task.setLast_modify_time(currentTime);

			taskService.updateTasks(task);
			
		} else if (type.equals("one")) {
			task = taskService.queryTask(task_id);
			task.setTitle(title);
			task.setStart_time(Timestamp.valueOf(start_time));
			task.setEnd_time(Timestamp.valueOf(end_time));
			task.setRemind_time(remind_time);
			task.setDescription(description);
			task.setLast_modify_person(uid);
			task.setLast_modify_time(currentTime);
			taskService.updateTask(task);
			
		} else {
			String request = uriInfo.getRequestUri().toString(); 
			String error_msg = String.format(ConfigUtil.getValue("10011"), "type", "one or all", type);
			ErrorResponse response = new ErrorResponse(request, "10011", error_msg);
			return response.toJson();
		}
		
		NormalResponse response = new NormalResponse();
		Map<String, Object> attrs = task.toMap();
		attrs.put("creator", username);
		attrs.put("last_modify_person", username);
		response.setAttrs(attrs);

		return response.toJson();
	}
	
	@Path("/confirm")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String confirmTask(@FormParam("task_id") Integer task_id) {
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		subtaskItemService.confirmSubtaskItems(task_id, uid);

		// set subtask state to ONGOING if necessary
		List<Subtask> subtaskList = subtaskService.querySubtasksByTaskAndOwner(task_id, uid);
		for (Subtask sub : subtaskList) {
			if (sub.getState().equals(State.SUBTASK_NOT_START)){
				sub.setState(State.SUBTASK_ONGOING);
				subtaskService.confirmSubtask(sub);
			}
		}

		// set task state to ONGOING if necessary
		Task task = taskService.queryTask(task_id);
		if (task.getState().equals(State.TASK_NOT_START)) {
			task.setState(State.TASK_ONGOING);
			taskService.updateTask(task);
		}

		SuccessResponse response = new SuccessResponse(10000, "Confirm Task Successfully");
		return response.toJson();
	}

	@Path("/finish")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String finishTask(@FormParam("subtask_id") Integer subtask_id, @FormParam("comment") String comment) {
		// set subtaskItem state to FINISHED
		Integer uid = SecurityContextHolder.getContext().getAuthenticationToken().getUid();
		String username = SecurityContextHolder.getContext().getAuthenticationToken().getUsername();
		
		subtaskItemService.finishSubtaskItem(subtask_id, uid, comment);

		// set subtask state to FINISHED if necessary

		Subtask subtask = subtaskService.querySubtaskById(subtask_id);;
		Task task = taskService.queryTask(subtask.getTask_id());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("subtask_id", subtask_id);
		params.put("state", State.SUBTASK_ITEM_FINISHED);
		Integer unfinished_item_count = subtaskItemService.querySubtaskItemCount(params);
		if (unfinished_item_count == 0) {
			subtask.setState(State.SUBTASK_FINISHED);
			subtask.setFinish_time(new Timestamp(System.currentTimeMillis()));
			subtaskService.finishSubtask(subtask);

			// set task state to FINISHED if necessary
			params.clear();
			params.put("task_id", subtask.getTask_id());
			params.put("state", State.SUBTASK_FINISHED);
			Integer unfinished_subtask_count = subtaskService.querySubtaskCount(params);
			if (unfinished_subtask_count == 0) {
				task.setState(State.TASK_FINISHED);
				task.setFinish_time(new Timestamp(System.currentTimeMillis()));
				taskService.updateTask(task);
			}

		}
		// 发消息给所有其它任务成员以及任务创建者
		List<SubtaskItem> items = subtaskItemService.queryItemsByTask(subtask.getTask_id());
		if (items != null) {
			StringBuilder dsts = new StringBuilder();
			boolean containCreator = false;
			for (SubtaskItem item : items) {
				if (task.getCreator() == item.getOwner()) containCreator = true;
				dsts.append(item.getOwner()+",");
			}
			if (!containCreator) dsts.append(task.getCreator()+",");
			if (dsts.length() > 0) {
				dsts = dsts.deleteCharAt(dsts.length()-1);
			}
			BmpMessage message = new BmpMessage();
			message.setDst_list(dsts.toString());
			message.setMsg_content(username+"已经完成了["+task.getTitle()+"]");
			message.setMsg_type(MsgType.task_msg.toString());
			message.setTrigger_event(uriInfo.getRequestUri().toString());
			Publisher.publish(message);
		}
		

		SuccessResponse response = new SuccessResponse(10000, "Finish Task Successfully");
		return response.toJson();
	}

	@Path("/delete")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String deleteTask(@FormParam("task_id") Integer id, 
			@FormParam("task_gid") String gid, @FormParam("type") String type) {
		if (type.equals("one")) {
			taskService.deleteTask(id);
			SuccessResponse response = new SuccessResponse(10000, "Delete Task Successfully");
			return response.toJson();
		} else if (type.equals("all")) {
			taskService.deleteTasks(gid);
			SuccessResponse response = new SuccessResponse(10000, "Delete Tasks Successfully");
			return response.toJson();
		} else {
			String request = uriInfo.getRequestUri().toString(); 
			String error_msg = String.format(ConfigUtil.getValue("10011"), "type", "one or all", type);
			ErrorResponse response = new ErrorResponse(request, "10011", error_msg);
			return response.toJson();
		}
	}

}
