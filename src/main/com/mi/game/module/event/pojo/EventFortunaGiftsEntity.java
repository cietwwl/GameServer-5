package com.mi.game.module.event.pojo;

import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.event.define.EventConstans;

public class EventFortunaGiftsEntity extends BaseEntity {

	
	private static final long serialVersionUID = -2414372940613293609L;

	@Indexed
	private String playerID;
		
	private long startTime;// 开始时间	
	private long endTime;// 结束时间	
	private long receiveTime;//下次领取时间
	private int num;//元宝领取次数		
	private String lowPrice;
	private String highPrice;
	private int gold;			

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("startTime", startTime);
		response.put("endTime", endTime);
		response.put("receiveTime", receiveTime);
		response.put("num", num);
		response.put("lowPrice", lowPrice);
		response.put("highPrice", highPrice);
		return response;
	}
	
	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
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

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	public String getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}

	public String getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

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
