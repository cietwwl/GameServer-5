package com.mi.game.module.vip.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.vip.define.VipConstants;
import com.mi.game.util.Utilities;

public class VipEntity extends BaseEntity {
	private static final long serialVersionUID = -3286146785099210142L;
	// 玩家id
	@Indexed
	private String playerID;
	// vip等级
	private int vipLevel;
	// 已使用特权
	private Map<String, Integer> permission = new HashMap<String, Integer>();
	// vip特权获取时间
	private String vipTime;

	public String getPlayerID() {
		return playerID;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Map<String, Integer> getPermission() {
		return permission;
	}

	public void setPermission(Map<String, Integer> permission) {
		this.permission = permission;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public String getVipTime() {
		return vipTime;
	}

	public void setVipTime(String vipTime) {
		this.vipTime = vipTime;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("vipLevel", vipLevel);
		result.put("permission", filterMap(permission));
		return result;
	}

	private Map<String, Integer> filterMap(Map<String, Integer> map) {
		for (String filter : VipConstants.VIP_FILTER) {
			if (map.containsKey(filter)) {
				map.remove(filter);
			}
		}
		return map;
	}

	/*
	 * 初始化每日vip特权
	 */
	public void initPermission() {
		permission.clear();
	}

	/**
	 * 判断当日特权是否已经重置
	 */
	public boolean isPermission() {
		String nowTime = Utilities.getDateTime();
		if (vipTime == null || vipTime.isEmpty()) {
			vipTime = nowTime;
			return false;
		}
		if (!vipTime.equals(nowTime)) {
			vipTime = nowTime;
			return false;
		}
		return true;
	}

	public List<Integer> getKeys(Map<Integer, Integer> map) {
		List<Integer> list = new ArrayList<Integer>();
		Set<Entry<Integer, Integer>> set = map.entrySet();
		for (Entry<Integer, Integer> entry : set) {
			list.add(entry.getKey());
		}
		return list;
	}

	/**
	 * 获取特权属性
	 * 
	 * @param key
	 * @return
	 */
	public Integer getValue(int key) {
		return permission.get(key);
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return playerID;
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

}
