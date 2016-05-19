package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.util.DateTimeUtil;

/**
 * 月卡实体
 * 
 */
public class EventMonthCardEntity extends BaseEntity {

	private static final long serialVersionUID = 4574291456323747404L;
	@Indexed
	private String playerID; // 玩家id
	private long receiveTime; // 领取时间
	private long startTime; // 月卡开始时间
	private long expireTime; // 月卡过期时间

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		// 默认值
		response.put("days", 0);
		response.put("stat", 0);
		response.put("receive", 0);
		int days = DateTimeUtil.getDiffDay(expireTime, System.currentTimeMillis());
		if (expireTime > System.currentTimeMillis()) { // 如果月卡有效
			response.put("days", days + 1);
			response.put("stat", 1); // 状态 1购买了月卡，0没有购买月卡
			if (DateTimeUtil.isSameDay(receiveTime, System.currentTimeMillis())) { // 如果今天领取过了
				response.put("receive", 1);
			}
		}
		return response;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		return super.responseMap(t);
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

}
