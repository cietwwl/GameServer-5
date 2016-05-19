package com.mi.game.module.festival.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mi.game.module.festival.data.FestivalActiveData;

public class FestivalUtils {

	/**
	 * 是否活动时间
	 * 
	 * @param times
	 * @param regx
	 * @return
	 */
	public static boolean isFestivalTime(FestivalActiveData festivalData, String regx) {
		String start = festivalData.getStartTime();
		String end = festivalData.getEndTime();
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
