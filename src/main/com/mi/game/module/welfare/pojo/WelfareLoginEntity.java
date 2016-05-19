package com.mi.game.module.welfare.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.Utilities;

public class WelfareLoginEntity extends BaseEntity {

	private static final long serialVersionUID = -7958033211868466522L;

	// 玩家id
	@Indexed
	private String playerID;
	// 登录次数
	private int loginNum;
	// 已经领取的次数
	private List<Integer> rewardList = new ArrayList<Integer>();
	// 上次领取时间
	private String loginTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(int loginNum) {
		this.loginNum = loginNum;
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
	 * 增加登录次数
	 */
	public void addLoginNum() {
		this.loginNum += 1;
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
		String dateTime = Utilities.getDateTime();
		if (loginTime == null || loginTime.isEmpty()) {
			loginTime = dateTime;
			return false;
		}
		if (!loginTime.equals(dateTime)) {
			loginTime = dateTime;
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("loginNum", loginNum);
		result.put("rewardList", rewardList);
		return result;
	}
	
	

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("playerID", playerID);
		result.put("loginNum", loginNum);
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
