package com.mi.game.module.event.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.mi.core.util.DateTimeUtil;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.pojo.EventConfigEntity;
import com.mi.game.util.Utilities;

public class EventUtils {

	public static int diffHour(String str) {
		long nowTime = System.currentTimeMillis();
		Date date = strToDate(str, EventConstans.YMD);
		long diff = nowTime - date.getTime();
		return (int) (diff / 3600 / 1000) / 2;
	}

	/**
	 * 获取刷新倒计时后倒计时
	 * 
	 * @param dateTime
	 * @return
	 */
	public static long diff2Hour() {
		Calendar calendar = Calendar.getInstance();
		long diffTime = 0;
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		String regx = "yyyy-MM-dd HH";
		// 双数
		if (hour % 2 == 0) {
			calendar.add(Calendar.HOUR_OF_DAY, 2);
			String tempStr = dateToStr(calendar.getTime(), regx);
			Date tempDate = strToDate(tempStr, regx);
			diffTime = tempDate.getTime() - System.currentTimeMillis();
		} else {
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			String tempStr = dateToStr(calendar.getTime(), regx);
			Date tempDate = strToDate(tempStr, regx);
			diffTime = tempDate.getTime() - System.currentTimeMillis();
		}
		return diffTime / 1000;
	}

	/**
	 * 获取当天毫秒数
	 * 
	 * @return
	 */
	public static long getDayStartTime() {
		String dateStr = Utilities.getDateTime(EventConstans.YMD);
		long timeStart = DateTimeUtil.getDate(dateStr).getTime();
		return timeStart;
	}

	/**
	 * 获取两个字符串日期相差天数
	 * 
	 * @param dateTime
	 * @param nowTime
	 * @return
	 */
	public static int diffTime(String nowTime, String dateTime) {
		Date date1 = strToDate(nowTime, EventConstans.YMD);
		Date date2 = strToDate(dateTime, EventConstans.YMD);
		long diffTime = date1.getTime() / 1000 - date2.getTime() / 1000;
		int diffDay = (int) (diffTime / (3600 * 24));
		return diffDay;
	}

	/**
	 * 检查是否吃烧鸡时间
	 * 
	 * @param chickenTimes
	 * @return
	 */
	public static boolean isChickenTime(String[] chickenTimes) {
		for (String chickenTime : chickenTimes) {
			String[] times = chickenTime.split("-");
			int start = Integer.parseInt(times[0]);
			int end = Integer.parseInt(times[1]);
			int now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			// 是否活动时间
			if (now >= start && now < end) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据活动时间判断是否开启
	 * 
	 * @param index
	 * @return
	 */
	public static boolean isChickenTime(int index) {
		String time = EventConstans.CHICKEN_TIME[index];
		String[] times = time.split("-");
		int start = Integer.parseInt(times[0]);
		int end = Integer.parseInt(times[1]);
		int now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		// 是否活动时间
		if (now >= start && now <= end) {
			return true;
		}
		return false;
	}

	/**
	 * 活动是否结束
	 * 
	 * @param eventInfo
	 * @return
	 */
	public static boolean eventIsEnd(EventConfigEntity eventInfo) {
		if (eventInfo == null) {
			return false;
		}
		// 判断活动是否结束
		long endTime = EventUtils.strToDate(eventInfo.getEndTime(), EventConstans.YMDHMS).getTime();
		long nowTime = System.currentTimeMillis();
		if (nowTime > endTime) {
			return true;
		}
		return false;
	}

	/**
	 * 是否活动时间
	 * 
	 * @param times
	 * @param regx
	 * @return
	 */
	public static boolean isEventTime(EventConfigEntity eventInfo, String regx) {
		if(eventInfo == null){
			return false;
		}
		String start = eventInfo.getStartTime();
		String end = eventInfo.getEndTime();
		if (start == null || end == null || start.isEmpty() || end.isEmpty()) {
			return false;
		}
		Date startTime = strToDate(start, regx);
		Date endTime = strToDate(end, regx);
		Date nowTime = new Date();
		if (nowTime.before(startTime) || nowTime.after(endTime)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断昨天是否活动时间
	 * 
	 * @param times
	 * @param regx
	 * @return
	 */
	public static boolean isEventTimeYesterday(EventConfigEntity eventInfo,
			String regx) {
		String start = eventInfo.getStartTime();
		String end = eventInfo.getEndTime();
		if (start == null || end == null || start.isEmpty() || end.isEmpty()) {
			return false;
		}
		Date startTime = strToDate(start, regx);
		Date endTime = strToDate(end, regx);
		Date nowTime = new Date();

		SimpleDateFormat dft = new SimpleDateFormat(regx);
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
		Date yesDate = null;
		try {
			yesDate = dft.parse(dft.format(date.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (yesDate.before(startTime) || yesDate.after(endTime)) {
			return false;
		}
		return true;
	}


	public static Date strToDate(String str, String regx) {
		SimpleDateFormat sdf = new SimpleDateFormat(regx);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String dateToStr(Date date, String regx) {
		SimpleDateFormat sdf = new SimpleDateFormat(regx);
		return sdf.format(date);
	}

}
