package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class EventVipGrowEntity extends BaseEntity {
	private static final long serialVersionUID = 8847956227568818875L;
	@Indexed
	private String playerID;
	private List<Integer> rewardList = new ArrayList<Integer>();
	private String dateTime;

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
	public boolean isReward(Integer rewardID) {
		return rewardList.contains(rewardID);
	}

	public void addRewardID(Integer rewardID) {
		if (!rewardList.contains(rewardID)) {
			rewardList.add(rewardID);
		}
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("rewardList", rewardList);
		resultMap.put("isBuy", true);
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
