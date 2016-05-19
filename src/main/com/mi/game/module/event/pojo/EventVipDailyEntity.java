package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.Utilities;

public class EventVipDailyEntity extends BaseEntity {
	private static final long serialVersionUID = 8847956227568818875L;
	@Indexed
	private String playerID;
	private String dateTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * 是否领取
	 * 
	 * @return
	 */
	public boolean isReward() {
		String nowTime = Utilities.getDateTime();
		if (dateTime == null || dateTime.isEmpty()) {
			dateTime = nowTime;
			return false;
		}
		if (!nowTime.equals(dateTime)) {
			dateTime = nowTime;
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("isReward", isReward());
		return resultMap;
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
