package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class EventLuckyDrawEntity extends BaseEntity {
		
	private static final long serialVersionUID = 7502864053111234493L;
	private long startTime;// 开始时间	
	private long endTime;// 结束时间
	private int  overplus;//免费抽奖剩余次数
	private int  drawNum;//付费抽奖次数
	
	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("startTime", startTime);
		response.put("endTime", endTime);
		response.put("overplus", overplus);
		response.put("drawNum", drawNum);		
		return response;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getOverplus() {
		return overplus;
	}

	public void setOverplus(int overplus) {
		this.overplus = overplus;
	}

	public int getDrawNum() {
		return drawNum;
	}

	public void setDrawNum(int drawNum) {
		this.drawNum = drawNum;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	@Indexed
	private String playerID;
	
	@Override
	public Object getKey() {
		// TODO Auto-generated method stub
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO Auto-generated method stub
		return playerID;
	}

	@Override
	public void setKey(Object key) {
		// TODO Auto-generated method stub
		this.playerID = key.toString();
	}

}
