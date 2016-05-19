package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.Utilities;

/**
 * 充值抽奖实体
 * 
 * @author Administrator
 *
 */
public class EventDrawPayEntity extends BaseEntity {
	private static final long serialVersionUID = -6658307895801155319L;
	@Indexed
	private String playerID;
	// 充值总数
	private int payTotal;
	// 已抽奖列表
	private List<Integer> rewardList = new ArrayList<Integer>();
	// 开始时间
	private long startTime;
	// 结束时间
	private long endTime;
	// 时间
	private String dateTime;
	// 奖励状态 0初始状态,1有首充奖励,2已领取奖励
	private int state;

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

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	/**
	 * 增加消费金额
	 * 
	 * @param expenseNum
	 */
	public void addPayTotal(int payNum) {
		this.payTotal += payNum;
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

	/**
	 * 是否已刷新
	 * 
	 * @return
	 */
	public boolean isRefresh() {
		String nowTime = Utilities.getDateTime();
		if (dateTime == null || dateTime.isEmpty()) {
			dateTime = nowTime;
			return false;
		}
		if (!nowTime.equals(dateTime)) {
			dateTime = nowTime;
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("payTotal", payTotal);
		result.put("state", state);
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
