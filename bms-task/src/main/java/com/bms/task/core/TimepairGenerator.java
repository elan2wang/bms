/**
 * 
 * Copyright 2013.  All rights reserved. 
 * 
 */
package com.bms.task.core;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bms.common.consts.Periodicity;

/**
 * @author wangjian
 * @create 2013年8月17日 下午12:38:01
 * @update TODO
 * 
 * 
 */
@Service
public class TimepairGenerator {

	public List<Timepair> generateTimePair(Timestamp start_time, Timestamp end_time, 
			Timestamp expire_time, Periodicity periodicity) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		Calendar expire = Calendar.getInstance();
		
		start.setTime(start_time);
		end.setTime(end_time);
		expire.setTime(expire_time);
		
		switch(periodicity) {
		case oncely:
			return generateOncely(start, end, expire);
		case daily:
			return generateDaily(start, end, expire);
		case weekly:
			return generateWeekly(start, end, expire);
		case monthly:
			return generateMonthly(start, end, expire);
		case yearly:
			return generateYearly(start, end, expire);
		}
		return null;
	}

	public Timepair generateNextTime(Timestamp start_time, Timestamp end_time, 
			Periodicity periodicity) {
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		start.setTime(start_time);
		end.setTime(end_time);
		Timepair pair = new Timepair();
		
		switch(periodicity) {
		case oncely:
			return null;
		case daily:
			start.add(Calendar.DAY_OF_YEAR, 1);
			end.add(Calendar.DAY_OF_YEAR, 1);
			break;
		case weekly:
			start.add(Calendar.WEEK_OF_YEAR, 1);
			end.add(Calendar.WEEK_OF_YEAR, 1);
			break;
		case monthly:
			start.add(Calendar.MONTH, 1);
			end.add(Calendar.MONTH, 1);
			break;
		case yearly:
			start.add(Calendar.YEAR, 1);
			end.add(Calendar.YEAR, 1);
			break;
		}

		pair.setStart_time(new Timestamp(start.getTime().getTime()));
		pair.setEnd_time(new Timestamp(end.getTime().getTime()));
		return pair;
	}
	
	private List<Timepair> generateOncely(Calendar start, Calendar end, 
			Calendar expire) {
		List<Timepair> timePairList = new ArrayList<Timepair>();
		Timestamp start_time = new Timestamp(start.getTime().getTime());
		Timestamp end_time = new Timestamp(end.getTime().getTime());

		Timepair pair = new Timepair();
		pair.setStart_time(start_time);
		pair.setEnd_time(end_time);
		timePairList.add(pair);
		return timePairList;
	}

	private List<Timepair> generateDaily(Calendar start, Calendar end, 
			Calendar expire) {
		List<Timepair> timePairList = new ArrayList<Timepair>();

		do {
			Timepair pair = new Timepair();
			Timestamp start_time = new Timestamp(start.getTime().getTime());
			Timestamp end_time = new Timestamp(end.getTime().getTime());
			
			pair.setStart_time(start_time);
			pair.setEnd_time(end_time);
			timePairList.add(pair);
			start.add(Calendar.DAY_OF_YEAR, 1);
			end.add(Calendar.DAY_OF_YEAR, 1);
		} while (start.before(expire));

		return timePairList;
	}

	private List<Timepair> generateWeekly(Calendar start, Calendar end, 
			Calendar expire) {
		List<Timepair> timePairList = new ArrayList<Timepair>();

		do {
			Timepair pair = new Timepair();
			Timestamp start_time = new Timestamp(start.getTime().getTime());
			Timestamp end_time = new Timestamp(end.getTime().getTime());
			
			pair.setStart_time(start_time);
			pair.setEnd_time(end_time);
			timePairList.add(pair);
			start.add(Calendar.WEEK_OF_YEAR, 1);
			end.add(Calendar.WEEK_OF_YEAR, 1);
		} while (start.before(expire));

		return timePairList;
	}

	private List<Timepair> generateMonthly(Calendar start, Calendar end, 
			Calendar expire) {
		List<Timepair> timePairList = new ArrayList<Timepair>();

		do {
			Timepair pair = new Timepair();
			Timestamp start_time = new Timestamp(start.getTime().getTime());
			Timestamp end_time = new Timestamp(end.getTime().getTime());
			
			pair.setStart_time(start_time);
			pair.setEnd_time(end_time);
			timePairList.add(pair);
			start.add(Calendar.MONTH, 1);
			end.add(Calendar.MONTH, 1);
		} while (start.before(expire));

		return timePairList;
	}

	private List<Timepair> generateYearly(Calendar start, Calendar end, 
			Calendar expire) {
		List<Timepair> timePairList = new ArrayList<Timepair>();
		do {
			Timepair pair = new Timepair();
			Timestamp start_time = new Timestamp(start.getTime().getTime());
			Timestamp end_time = new Timestamp(end.getTime().getTime());
			
			pair.setStart_time(start_time);
			pair.setEnd_time(end_time);
			timePairList.add(pair);
			start.add(Calendar.YEAR, 1);
			end.add(Calendar.YEAR, 1);
		} while (start.before(expire));

		return timePairList;
	}

	public static void main(String[] args) {
		Timestamp start = Timestamp.valueOf("2013-08-17 15:10:00");
		Timestamp end = Timestamp.valueOf("2013-08-17 17:10:00");
		Timestamp expire = Timestamp.valueOf("2013-11-17 15:10:00");
		TimepairGenerator generator = new TimepairGenerator();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(start.getTime())+ " 到 " + 
				df.format(end.getTime()) +" 结束 " + df.format(expire.getTime())+"\n");
		
		List<Timepair> list = generator.generateTimePair(start, end, expire, Periodicity.monthly);
	
		for (Timepair pair : list) {
			Timestamp start_time = pair.getStart_time();
			Timestamp end_time = pair.getEnd_time();
			System.out.println(start_time + " 到 " + end_time);
		}
	}
}
