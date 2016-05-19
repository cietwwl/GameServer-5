package com.mi.game.module.welfare.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class WelfareLevelEntity extends BaseEntity {

	private static final long serialVersionUID = -7958033211868466522L;

	// 玩家id
	@Indexed
	private String playerID;
	// 已领取奖励的等级
	private List<Integer> rewardList= new ArrayList<Integer>();
	// 上次领取时间
	private String rewardTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public List<Integer> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<Integer> rewardList) {
		this.rewardList = rewardList;
	}

	public String getRewardTime() {
		return rewardTime;
	}

	public void setRewardTime(String rewardTime) {
		this.rewardTime = rewardTime;
	}

	public boolean isGetReward(int rewardIntID) {
		return rewardList.contains(rewardIntID);
	}

	public void addRewardLevel(int rewardIntID) {
		if (!rewardList.contains(rewardIntID)) {
			rewardList.add(rewardIntID);
		}
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rewardList", rewardList);
		return result;
	}
	
	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("playerID", playerID);
		result.put("rewardList", rewardList);
		result.put("rewardTime", rewardTime);
		return result;
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

}
