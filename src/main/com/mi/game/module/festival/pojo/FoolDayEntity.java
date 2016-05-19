package com.mi.game.module.festival.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;

public class FoolDayEntity extends BaseEntity{

	private static final long serialVersionUID = -1186621943886491754L;
	private String playerID;
	private long goldNum;
	private long allGold;
	private long drawGold;
	
	public long getDrawGold() {
		return drawGold;
	}

	public void setDrawGold(long drawGold) {
		this.drawGold = drawGold;
	}

	public long getAllGold() {
		return allGold;
	}

	public void setAllGold(long allGold) {
		this.allGold = allGold;
	}

	public long getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(long goldNum) {
		this.goldNum = goldNum;
	}

	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("goldNum", goldNum);
		data.put("allGold", allGold);
		data.put("drawGold", drawGold);
		return data;
	}
	
	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		playerID = key.toString();
	}
}
