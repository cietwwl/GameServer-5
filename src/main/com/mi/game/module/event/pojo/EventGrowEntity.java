package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.List;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class EventGrowEntity extends BaseEntity {
	private static final long serialVersionUID = 8847956227568818875L;
	@Indexed
	private String playerID;
	// 已经领取成长列表
	private List<Integer> growList = new ArrayList<Integer>();
	// 操作时间
	private String growTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public List<Integer> getGrowList() {
		return growList;
	}

	public void setGrowList(List<Integer> growList) {
		this.growList = growList;
	}

	public String getGrowTime() {
		return growTime;
	}

	public void setGrowTime(String growTime) {
		this.growTime = growTime;
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
