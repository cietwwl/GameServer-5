package com.mi.game.module.dailyLogin.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.Utilities;

public class DailyLoginEntity extends BaseEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6403940190263411726L;
	// 玩家id
	@Indexed
	private String playerID;
	// 已经领取的次数
	private List<Integer> rewardList = new ArrayList<Integer>();
	// 上次登录时间
	private String loginTime;
	// 活动开始时间
	private Long beginTime;
	// 活动结束时间
	private Long endTime;
	private String day; // 日期

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
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

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	/**
	 * 添加奖励iD
	 * 
	 * @param level
	 */
	public void addRewardID(int rewardID) {
		if (!rewardList.contains(rewardID)) {
			rewardList.add(rewardID);
		}
	}

	/**
	 * 检查是否领取
	 * 
	 * @param level
	 * @return
	 */
	public boolean isGetReward(int rewardID) {
		return rewardList.contains(rewardID);
	}

	/**
	 * 检查当天是否登录
	 * 
	 * @return
	 */
	public boolean isLogin(boolean sign) {
		String dateTime = Utilities.getDateTime("yyyyMMdd");
		if (loginTime == null || loginTime.isEmpty()) { // 没有登录过
			loginTime = dateTime;
			return false;
		}
		if (!loginTime.equals(dateTime)) { // 上一次登录时间不等于今天
			loginTime = dateTime;
			return false;
		}
		return true;
	}

	/**
	 * 检查是否是连续登录
	 * 
	 * @param
	 * @return
	 */
	public boolean isContinuity() {
		String yesterday = Utilities.getYesterDay("yyyyMMdd");
		if (!loginTime.equals(yesterday)) {
			return false;
		}
		return true;
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
		result.put("loginTime", loginTime);
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
