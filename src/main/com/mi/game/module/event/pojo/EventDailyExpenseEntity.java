package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

/**
 * 每日消费实体
 * 
 * @author Administrator
 *
 */
public class EventDailyExpenseEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4360352746932216614L;
	private String expenseID; // 主键
	@Indexed(value = IndexDirection.DESC, unique = false)
	private String playerID;
	// 消费总数
	private int expenseTotal;
	// 开始时间
	private long startTime;
	// 结束时间
	private long endTime;
	// 已领取列表
	private List<Integer> rewardList = new ArrayList<Integer>();
	@Indexed(value = IndexDirection.DESC, unique = false)
	private String day; // 消费日期,格式:yyyyMMdd

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
		return expenseID;
	}

	@Override
	public String getKeyName() {
		return "expenseID";
	}

	@Override
	public void setKey(Object key) {
		this.expenseID = key.toString();
	}

	public String getExpenseID() {
		return expenseID;
	}

	public void setExpenseID(String expenseID) {
		this.expenseID = expenseID;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

}
