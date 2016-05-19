package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

public class EventUniversalVerfareEntity extends BaseEntity {
	
	private static final long serialVersionUID = 52873515841626343L;

	@Indexed
	private String playerID;
	// 普通用户已领取列表
	private List<Integer> rewardList = new ArrayList<Integer>();
	//VIP用户已领取列表
	private List<Integer> rewardVipList = new ArrayList<Integer>();
	
	// 开始时间
	private long startTime;
	// 结束时间
	private long endTime;
	
	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();		
		result.put("startTime", startTime);
		result.put("endTime", endTime);
		if(rewardList!=null&&!rewardList.isEmpty()){
			result.put("rewardList", rewardList);
		}else{
			result.put("rewardList", new ArrayList<Integer>());
		}
		if(rewardVipList!=null&&!rewardVipList.isEmpty()){
			result.put("rewardVipList", rewardVipList);
		}else{
			result.put("rewardVipList", new ArrayList<Integer>());
		}
		return result;
	}
	
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

	public List<Integer> getRewardVipList() {
		return rewardVipList;
	}

	public void setRewardVipList(List<Integer> rewardVipList) {
		this.rewardVipList = rewardVipList;
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
	
	public void addReward(int rewardID) {
		if (!isReward(rewardID)) {
			rewardList.add(rewardID);
		}
	}

	public boolean isReward(int rewardID) {
		return rewardList.contains(rewardID);
	}
	
	public void addVipReward(int rewardID) {
		if (!isVipReward(rewardID)) {
			rewardVipList.add(rewardID);
		}
	}

	public boolean isVipReward(int rewardID) {
		return rewardVipList.contains(rewardID);
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
