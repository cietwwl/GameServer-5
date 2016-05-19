package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

/**
 * 充值送礼实体
 * 
 * @author Administrator
 *
 */
public class EventPayEntity extends BaseEntity {

	private static final long serialVersionUID = 2136206624141542327L;
	@Indexed
	private String playerID;
	// 充值累计
	private int payTotal;
	// 已领取列表
	private List<Integer> rewardList = new ArrayList<Integer>();
	// 开始时间
	private long startTime;
	// 结束时间
	private long endTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(int payTotal) {
		this.payTotal = payTotal;
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

	public List<Integer> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<Integer> rewardList) {
		this.rewardList = rewardList;
	}

	public void addPayTotal(int payNum) {
		payTotal += payNum;
	}

	public void addReward(int rewardID) {
		if (!isReward(rewardID)) {
			rewardList.add(rewardID);
		}
	}

	public boolean isReward(int rewardID) {
		return rewardList.contains(rewardID);
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("payTotal", payTotal);
		result.put("rewardList", rewardList);
		result.put("startTime",startTime);
		result.put("endTime", endTime);
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
