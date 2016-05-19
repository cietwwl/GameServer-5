package com.mi.game.util;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mi.core.util.DateTimeUtil;
import com.mi.game.module.reward.data.GoodsBean;

public class Utilities {
	public static Random random = null;

	/**
	 * 计算sign值
	 */
	public static String makeCode(String str) {
		String md = encrypt(str);
		String a = md.substring(24, 26);
		String b = md.substring(26, 28);
		String c = md.substring(28, 30);
		String d = md.substring(30, 32);
		int aa = Integer.valueOf(a, 16) % 10;
		int bb = Integer.valueOf(b, 16) % 10;
		int cc = Integer.valueOf(c, 16) % 10;
		int dd = Integer.valueOf(d, 16) % 10;
		StringBuilder sb = new StringBuilder();
		sb.append(aa).append(bb).append(cc).append(dd);
		return sb.toString();
	}

	public static String encrypt(String str) {
		if (str == null || str.length() == 0)
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte hash[] = md.digest();
			for (int i = 0; i < hash.length; i++)
				if ((255 & hash[i]) < 16)
					hexString.append((new StringBuilder("0")).append(Integer.toHexString(255 & hash[i])).toString());
				else
					hexString.append(Integer.toHexString(255 & hash[i]));

		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("Can not find such a algorithm!");
		}
		return hexString.toString();
	}

	public static Map<Integer, Integer> getPropMap(String str) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		String[] strArray = str.split(";");
		if (!strArray[0].equals("") && strArray != null) {
			if (strArray.length != 0) {
				for (int i = 0; i < strArray.length; i++) {
					String[] perArray = strArray[i].split("=");
					map.put(Integer.parseInt(perArray[0]), Integer.parseInt(perArray[1]));
				}
			}
		}
		return map;
	}

	static {
		random = new Random(DateTimeUtil.getMillTime());
	}

	public static String MD5Encode(String origin) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(origin.getBytes("utf8"));
			byte[] result = md.digest();
			for (int i = 0; i < result.length; i++) {
				// int val = result[i] & 0xff;
				// sb.append(Integer.toHexString(val));
				int val = (result[i] & 0x000000ff) | 0xffffff00;
				sb.append(Integer.toHexString(val).substring(6));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static Map<String, String> param2Map(String str) {
		Map<String, String> map = new HashMap<String, String>();
		String[] strArray = str.split(";");
		if (!strArray[0].equals("") && strArray != null) {
			if (strArray.length != 0) {
				for (int i = 0; i < strArray.length; i++) {
					String[] perArray = strArray[i].split("=");
					map.put(perArray[0], perArray[1]);
				}
			}
		}
		return map;
	}

	public static int getMonsterType(int pid) {
		int type = 0;
		type = (int) Math.floor(pid / 1000) * 1000;
		return type;
	}

	public static int getCurrentTime() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static List<Integer> getRandomList(int size, int n) {
		List<Integer> list = new ArrayList<Integer>();
		int num = 0;
		for (int i = 0; i < size; i++) {
			do {
				// 如果产生的数相同继续循环
				num = random.nextInt(size);

			} while (list.contains(num));
			list.add(num);
			if (list.size() >= n) {
				break;
			}
		}
		return list;
	}

	public static double getrandomDouble() {
		return random.nextDouble();
	}

	public static int getRandomInt(int count) {
		return random.nextInt(count);
	}

	public static long getRandomLong() {
		return random.nextLong();
	}

	public static int getMainID(int id) {
		int mainID = (int) Math.floor(id / 1000000);
		return mainID;
	}

	public static int getKindID(int id) {
		int kindID = (int) Math.floor(id / 1000);
		return kindID;
	}

	public static boolean compareSameDate(long time) {
		long timeMillis = System.currentTimeMillis();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String sp_time = sf.format(time);
		String current_time = sf.format(timeMillis);

		if (!sp_time.equals(current_time)) {
			return false;
		}
		return true;
	}

	public static List<GoodsBean> getGoodList(Map<Integer, GoodsBean> goodsList) {
		List<GoodsBean> beanList = new ArrayList<>();
		for (GoodsBean bean : goodsList.values()) {
			beanList.add(bean);
		}
		return beanList;
	}

	/**
	 * 检查字符串长度是否合法,中文,占2个长度
	 * 
	 * @param s
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean checkLenght(String s, int min, int max) {
		int length = length(s);
		if (length < min) {
			return false;
		}
		if (length > max) {
			return false;
		}
		return true;
	}

	/**
	 * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
	 * 
	 * @param String
	 *            s 需要得到长度的字符串
	 * @return int 得到的字符串长度
	 */
	private static int length(String s) {
		if (s == null)
			return 0;
		char[] c = s.toCharArray();
		int len = 0;
		for (int i = 0; i < c.length; i++) {
			len++;
			if (!isLetter(c[i])) {
				len++;
			}
		}
		return len;
	}

	private static boolean isLetter(char c) {
		int k = 0x80;
		return c / k == 0 ? true : false;
	}

	/**
	 * 获取yyyy-MM-dd 格式日起字符串
	 * 
	 * @return
	 */
	public static String getDateTime() {
		String regx = "yyyy-MM-dd";
		return getDateTime(regx);
	}

	/**
	 * 获取regx格式日起字符串
	 * 
	 * @return
	 */
	public static String getDateTime(String regx) {
		SimpleDateFormat sdf = new SimpleDateFormat(regx);
		return sdf.format(new Date());
	}

	/**
	 * 获取时间差
	 * 
	 * @param str
	 * @return
	 */
	public static long getDiffTime(String str, String str2, int diffDay) {
		long difftime = 0;
		String time = str + " " + str2;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date srcTime = sdf.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(srcTime);
			calendar.add(Calendar.DAY_OF_MONTH, diffDay);
			Date resultTime = calendar.getTime();
			difftime = resultTime.getTime() - System.currentTimeMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return difftime;
	}
	
	public static double doubleAdd(double a , double b){
		BigDecimal decilmanA =  new BigDecimal(a + "");
		BigDecimal decilmanB =  new BigDecimal(b + "");
		return decilmanA.add(decilmanB).doubleValue();
	}
	
	public static double doubleSubtract(double a , double b){
		BigDecimal decilmanA =  new BigDecimal(a + "");
		BigDecimal decilmanB =  new BigDecimal(b + "");
		return decilmanA.subtract(decilmanB).doubleValue();
	}
	
	public static double doubleMultiply(double a , double b){
		BigDecimal decilmanA =  new BigDecimal(a + "");
		BigDecimal decilmanB =  new BigDecimal(b + "");
		return decilmanA.multiply(decilmanB).doubleValue();
	}
	
	public static double doubleDivide(double a , double b){
		BigDecimal decilmanA =  new BigDecimal(a + "");
		BigDecimal decilmanB =  new BigDecimal(b + "");
		return decilmanA.divide(decilmanB).doubleValue();
	}
	
	
	

	public static void test() {
		String str = "2013-10-30 12:00:00";
		Date oldDate = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			oldDate = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar c1 = Calendar.getInstance();
		c1.setTime(oldDate);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, c1.get(Calendar.SECOND));
	}

	public static void test1() {

	}

	public void test(int a, int b) {

	}

	public static boolean isNpc(String ID) {
		if (ID.length() < 4) {
			return false;
		}
		String temp = ID.substring(0, 3);
		if (temp.equals("npc")) {
			return true;
		}
		return false;
	}

	public static String getUniqueKey(long time, String playerID) {
		String key = time + playerID;
		String uniqueKey = MD5Encode(key);
		int index = getRandomInt(25);
		return uniqueKey.substring(index, index + 8);
	}

	public static int getRandom(int a, int b) {
		a++;
		if (a != b) {
			System.out.println(a);
			getRandom(a, b);
		}
		return a;
	}
	
	/**
	 * 验证用户名(英文或数字,6-12位)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 * */
	public static boolean IsUserName(String userName){
		String regex = "^[a-zA-Z0-9]{4,12}$";
		return match(regex,userName);
	}
	
	/** 获取下一个整点时间 */
	public static Date getOclock(){
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.HOUR_OF_DAY,+1);
		ca.set(Calendar.SECOND,0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.MILLISECOND,0);
		Date date  = ca.getTime();
		return date;
	}
	

	/**
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	
	public static void main(String[] args) {
		System.out.println("xxxmtez0a".hashCode());
		System.out.println("xxxmtexna".hashCode());
	}

	/**
	 * 随机指定范围内N个不重复的数
	 * 
	 * @param min
	 *            指定范围最小值
	 * @param max
	 *            指定范围最大值
	 * @param n
	 *            随机数个数
	 */
	public static int[] randomCommon(int min, int max, int n) {
		if (n > (max - min + 1) || max < min) {
			return null;
		}
		int[] result = new int[n];
		int count = 0;
		while (count < n) {
			int num = (int) (Math.random() * (max - min)) + min;
			boolean flag = true;
			for (int j = 0; j < n; j++) {
				if (num == result[j]) {
					flag = false;
					break;
				}
			}
			if (flag) {
				result[count] = num;
				count++;
			}
		}
		return result;
	}

	/**
	 * 根据时间获取是一年中的第几周
	 * 
	 * @param date
	 * @return 第几周(String类型)
	 */
	public static String getWeekNumOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.setTime(date);
		return calendar.get(Calendar.WEEK_OF_YEAR) + "";
	}

	/**
	 * 得到当前周的日期(周一到周六)
	 * 
	 * @param pattern
	 *            日期格式yyyyMMdd
	 * @param dayNum
	 *            获得的天数(1-7)
	 * @return
	 */
	public static List<String> getDayOfWeek(String pattern, int dayNum) {
		List<String> daysList = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar c = Calendar.getInstance();
		// 今天是一周中的第几天
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		if (c.getFirstDayOfWeek() == Calendar.SUNDAY) {
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
		// 计算一周开始的日期
		c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

		for (int i = 1; i <= 7; i++) {
			c.add(Calendar.DAY_OF_MONTH, 1);
			daysList.add(sdf.format(c.getTime()));
		}
		return daysList;
	}

	/**
	 * 判断当前日期是不是周六
	 * 
	 * @return true:是;false:不是
	 */
	public static boolean todayIsSaturday() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			return true;
		}
		return false;
	}

	/**
	 * 得到昨天的日期
	 * 
	 * @param formater
	 *            日期格式
	 * @return
	 */
	public static String getYesterDay(String formater) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat(formater).format(cal
				.getTime());
		return yesterday;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(String startTime, String endTime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginDate = sdf.parse(startTime);
		Date endDate = sdf.parse(endTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(endDate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetweenForStr(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

}
