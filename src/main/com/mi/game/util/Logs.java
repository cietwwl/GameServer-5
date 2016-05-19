package com.mi.game.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class Logs {

	private static String[] params = { "ts", "puid", "device_id", "player_id", "scene", "level", "stay", "action", "v1", "v2", "v3", "v4", "ip", "osversion",
			"phonetype", "imei", "mac", "store", "server", "os", "gameid" };

	private Logs() {

	}

	public static final Log logger = LogFactory.getLog(Logs.class);

	public static Logger pay = Logger.getLogger("pay");

	private static Logger stats = Logger.getLogger("stats");

	/**
	 * 统计日志记录
	 * 
	 * @param ts
	 *            动作发生的时间 . 格式. 2014-07-10 06:55:00
	 * @param puid
	 *            用户id .使用平台id
	 * @param device_id
	 *            设备唯一id
	 * @param player_id
	 *            角色id. 游戏内生成的id
	 * @param scene
	 *            场景 .目前没有实际意义
	 * @param level
	 *            级别
	 * @param stay
	 *            注册天数
	 * @param action
	 *            动作
	 * @param v1
	 *            参数（若无，可空）
	 * @param v2
	 *            参数（若无，可空）
	 * @param v3
	 *            参数（若无，可空）
	 * @param v4
	 *            参数（若无，可空）
	 * @param ip
	 *            ip地址
	 * @param osversion
	 *            操作系统版本 ,为了区分IOS和安卓系统，可以加前缀为IOS_或者Android_
	 *            ，比如IOS_7.0.4、Android_4.1.2
	 * @param phonetype
	 *            设备型号, 苹果设备比如：iPhone5.2、iPad2.5；安卓设备比如：hct15_gb2、LePhone
	 * @param imei
	 *            设备imei ,安卓系统取设备imei；IOS系统本字段留空
	 * @param mac
	 *            设备mac地址 ,安卓系统及IOS7以下取mac地址；IOS7.0(含)以上取不到mac，则本字段留空
	 * @param store
	 *            渠道名称, 比如qh360
	 * @param server
	 *            分服标示,玩家所在服，例如:区服1就记为1即可；这个取值是现在运维分配的sever_index，具有唯一性。
	 *            如果能够保证唯一性，可以用serverid
	 * @param os
	 *            操作系统类型,取值如：ios、android、wp
	 * @param gameid
	 *            游戏唯一标识，如梦想海贼王记为：mxhzw
	 */
	public static void analy(String ts, String puid, String device_id, String player_id, String scene, int level, int stay, String action, JSONObject v1, JSONObject v2,
			JSONObject v3, JSONObject v4, String ip, String osversion, String phonetype, String imei, String mac, String store, String server, String os, String gameid) {
		JSONObject json = new JSONObject();
		json.put(params[0], ts);
		json.put(params[1], puid);
		json.put(params[2], device_id);
		json.put(params[3], player_id);
		json.put(params[4], scene);
		json.put(params[5], level);
		json.put(params[6], stay);
		json.put(params[7], action);
		if (v1 == null) {
			json.put(params[8], "");
		} else {
			json.put(params[8], v1);
		}
		if (v2 == null) {
			json.put(params[9], "");
		} else {
			json.put(params[9], v2);
		}
		if (v3 == null) {
			json.put(params[10], "");
		} else {
			json.put(params[10], v3);
		}
		if (v4 == null) {
			json.put(params[11], "");
		} else {
			json.put(params[11], v4);
		}
		json.put(params[12], ip);
		json.put(params[13], osversion);
		json.put(params[14], phonetype);
		json.put(params[15], imei);
		json.put(params[16], mac);
		json.put(params[17], store);
		json.put(params[18], server);
		json.put(params[19], os);
		json.put(params[20], gameid);
		stats.info(json.toString());
	}

}