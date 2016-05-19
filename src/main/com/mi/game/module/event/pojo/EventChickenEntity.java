package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.Utilities;

public class EventChickenEntity extends BaseEntity {

	private static final long serialVersionUID = -7958033211868466522L;

	// 玩家id
	@Indexed
	private String playerID;
	// 第一次
	private boolean first;
	// 第二次
	private boolean second;
	// 时间
	private String timeDate;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public String getTimeDate() {
		return timeDate;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean isSecond() {
		return second;
	}

	public void setSecond(boolean second) {
		this.second = second;
	}

	public void setTimeDate(String timeDate) {
		this.timeDate = timeDate;
	}

	public boolean isChicken() {
		String nowTime = Utilities.getDateTime();
		if (timeDate == null || timeDate.isEmpty()) {
			timeDate = nowTime;
			return false;
		}
		if (!timeDate.equals(nowTime)) {
			timeDate = nowTime;
			return false;
		}
		return true;
	}
	
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("first", first);
		result.put("second", second);
		return result;
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
