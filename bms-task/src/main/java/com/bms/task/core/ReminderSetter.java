/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.core;

import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bms.common.AppContext;
import com.bms.common.consts.Periodicity;
import com.bms.task.pojo.Task;

/**
 * @author wangjian
 * @create 2013年9月8日 下午3:49:44
 * @update TODO
 * 
 * 
 */
public class ReminderSetter {
	private static Logger log = LoggerFactory.getLogger(ReminderSetter.class);
	
	private static StdScheduler taskScheduler = 
			(StdScheduler) AppContext.getBean("taskScheduler");
	
	/**
	 * Add new Job and Trigger into the scheduler 
	 *
	 * @param TODO
	 * @return void
	 */
	public static void addReminder(Task task, Timestamp expire_time) 
			throws ParseException, SchedulerException {

		JobDetail jobDetail = newJob(TaskReminderJob.class)
				.withIdentity("reminder_for_tasks_"+task.getGid(), "reminderJob")
				.usingJobData("task_gid", task.getGid())
				.usingJobData("remind_time", task.getRemind_time())
				.build();

		Calendar start = Calendar.getInstance();
		start.setTime(task.getStart_time());
		start.add(Calendar.MINUTE, -task.getRemind_time());
		
		Trigger trigger = null;
		if (task.getPeriodicity().equals("oncely")) {
			trigger = newTrigger()
					.withIdentity("trigger_for_tasks_"+task.getGid(), "reminderTrigger")
					.withSchedule(simpleSchedule().withRepeatCount(0)
							.withMisfireHandlingInstructionFireNow())
					.forJob(jobDetail)
					.startAt(start.getTime())
					.build();
		} else {
			trigger = newTrigger()
					.withIdentity("trigger_for_tasks_"+task.getGid(), "reminderTrigger")
					.withSchedule(getSchedule(task))
					.forJob(jobDetail)
					.endAt(expire_time)
					.build();
		}
		
		taskScheduler.scheduleJob(jobDetail, trigger);	
	}

	/**
	 * Create a CronScheduleBuilder with a cron-expression
	 *
	 * @return the new CronScheduleBuilder
     * @see CronExpression
	 */
	private static CronScheduleBuilder getSchedule(Task task) {
		CronScheduleBuilder cronSchedule = null;

		Calendar start = Calendar.getInstance();
		start.setTime(task.getStart_time());
		start.add(Calendar.MINUTE, -task.getRemind_time());
		log.debug(start.getTime().toString());
		
		switch(Periodicity.valueOf(task.getPeriodicity())) {
		case daily:
			cronSchedule = dailyAtHourAndMinute(
					start.get(Calendar.HOUR_OF_DAY),
					start.get(Calendar.MINUTE))
					.withMisfireHandlingInstructionFireAndProceed();
			break;
		case weekly:
			cronSchedule = weeklyOnDayAndHourAndMinute(
					start.get(Calendar.DAY_OF_WEEK),
					start.get(Calendar.HOUR_OF_DAY),
					start.get(Calendar.MINUTE))
					.withMisfireHandlingInstructionFireAndProceed();
			break;
		case monthly:
			cronSchedule = monthlyOnDayAndHourAndMinute(
					start.get(Calendar.DAY_OF_MONTH),
					start.get(Calendar.HOUR_OF_DAY),
					start.get(Calendar.MINUTE))
					.withMisfireHandlingInstructionFireAndProceed();
			break;
		case yearly:
			cronSchedule = yearlyOnDayAndHourAndMinute(
					start.get(Calendar.MONTH),
					start.get(Calendar.DAY_OF_MONTH),
					start.get(Calendar.HOUR_OF_DAY),
					start.get(Calendar.MINUTE))
					.withMisfireHandlingInstructionFireAndProceed();
			break;
		default:
			break;

		}
		return cronSchedule;
	}
	
	/**
     * Create a CronScheduleBuilder with a cron-expression that sets the
     * schedule to fire one per year on the given day of month of the 
     * given month at the given time (hour and minute).
     * 
     * @param month
     *            the month to fire
     * @param dayOfMonth
     *            the day of the month to fire
     * @param hour
     *            the hour of day to fire
     * @param minute
     *            the minute of the given hour to fire
     * @return the new CronScheduleBuilder
     * @see CronExpression
     */
	private static CronScheduleBuilder yearlyOnDayAndHourAndMinute(int month, int dayOfMonth, int hour, int minute) {
		CronScheduleBuilder cronSchedule = null;
		DateBuilder.validateMonth(month);
		DateBuilder.validateDayOfMonth(dayOfMonth);
        DateBuilder.validateHour(hour);
        DateBuilder.validateMinute(minute);

        String cronExpression = String.format("0 %d %d %d %d ?", minute, hour, dayOfMonth, month);

        cronSchedule = cronSchedule(cronExpression);
        
        return cronSchedule;
    }
}
