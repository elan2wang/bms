/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.core;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bms.common.AppContext;
import com.bms.common.ConfigUtil;
import com.bms.common.consts.MsgType;
import com.bms.message.Publisher;
import com.bms.message.pojo.BmpMessage;
import com.bms.task.pojo.SubtaskItem;
import com.bms.task.pojo.Task;
import com.bms.task.service.SubtaskItemService;
import com.bms.task.service.TaskService;

/**
 * @author wangjian
 * @create 2013年9月5日 下午10:47:29
 * @update TODO
 * 
 */
public class TaskReminderJob implements Job {
	private static Logger log = LoggerFactory.getLogger(TaskReminderJob.class);

	private TaskService taskService;
	private SubtaskItemService subtaskItemService;
	
	public TaskReminderJob() {
		taskService = (TaskService) AppContext.getBean("taskService");
		subtaskItemService = (SubtaskItemService) AppContext.getBean("subtaskItemService");
	}
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String task_gid = (String) context.getJobDetail().getJobDataMap().get("task_gid");
		Integer creator = context.getJobDetail().getJobDataMap().getInt("creator");
		Integer remind_time = context.getJobDetail().getJobDataMap().getInt("remind_time");
		
		log.debug("task_gid:"+task_gid);
		log.debug("remind_time:"+remind_time);
		
		// set parameter to retrieve Task
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gid", task_gid);
		Calendar now = Calendar.getInstance();
		now.setTime(new Timestamp(System.currentTimeMillis()));
		now.add(Calendar.MINUTE, remind_time);
		params.put("start_time", now.getTime());
		Task task = taskService.queryTask(params);
		if (task == null) {
			log.info("No task found, No reminder");
			return;
		}
		
		// get message destination
		List<SubtaskItem> items = subtaskItemService.queryItemsByTask(task.getId());
		if (items != null && items.size() != 0) {
			StringBuilder sb = new StringBuilder();
			boolean containCreator = false;
			for (SubtaskItem item : items) {
				if (creator == item.getOwner()) containCreator = true;
				sb.append(item.getOwner()+",");
			}
			if (!containCreator) sb.append(creator+",");
			if (sb.length() > 0) {
				sb = sb.deleteCharAt(sb.length()-1);
			}
			log.debug("dst_list:" + sb.toString());
			
			String msg_content = String.format(ConfigUtil.getValue("task_reminder_msg"), 
					task.getTitle(), timeExpressionTransfer(task.getRemind_time()));
			
			BmpMessage msg = new BmpMessage();
			msg.setDst_list(sb.toString());
			msg.setMsg_content(msg_content);
			msg.setMsg_type(MsgType.task_msg.toString());
			msg.setTrigger_time(new Timestamp(System.currentTimeMillis()));
			msg.setTrigger_event("task reminder job");
			
			Publisher.publish(msg);
			log.debug("Task reminder message send successfully");
		} else {
			log.error("This task haven't assign owners, No reminder");
		}
	}

	private String timeExpressionTransfer(Integer minutes) {
		if (minutes == 15) return "15分钟";
		if (minutes == 30) return "30分钟";
		if (minutes == 60) return "1个小时";
		if (minutes == 1440) return "1天";
		
		return "";
	}
}
