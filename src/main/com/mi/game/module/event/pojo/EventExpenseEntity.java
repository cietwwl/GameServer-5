package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;

/**
 * 消费送礼实体
 * 
 * @author Administrator
 *
 */
public class EventExpenseEntity extends BaseEntity {
	private static final long serialVersionUID = -6658307895801155319L;
	@Indexed
	private String playerID;
	// 消费总数
	private int expenseTotal;
	// 开始时间
	private long startTime;
	// 结束时间
	private long endTime;
	// 已领取列表
	private List<Integer> rewardList = new ArrayList<Integer>();

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getExpenseTotal() {
		return expenseTotal;
	}

	public void setExpenseTotal(int expenseTotal) {
		this.expenseTotal = expenseTotal;
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

	/**
	 * 增加消费金额
	 * 
	 * @param expenseNum
	 */
	public void addExpenseTotal(int expenseNum) {
		this.expenseTotal += expenseNum;
	}

	/**
	 * 增加领取记录
	 * 
	 * @param rewardID
	 */
	public void addReward(int rewardID) {
		if (!isReward(rewardID)) {
			rewardList.add(rewardID);
		}
	}

	/**
	 * 是否已领取 rewardID
	 * 
	 * @param rewardID
	 * @return
	 */
	public boolean isReward(int rewardID) {
		return rewardList.contains(rewardID);
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("expenseTotal", expenseTotal);
		result.put("rewardList", rewardList);
		result.put("startTime", startTime);
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
