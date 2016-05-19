package com.mi.game.module.welfare.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.event.util.EventUtils;
import com.mi.game.module.welfare.define.WelfareConstants;
import com.mi.game.util.Utilities;

public class WelfareSignEntity extends BaseEntity {

	private static final long serialVersionUID = -7958033211868466522L;

	// 玩家id
	@Indexed
	private String playerID;
	// 连续签到次数
	private int signNum;
	// 已领取列表
	private List<Integer> rewardList = new ArrayList<Integer>();
	// 上次签到时间
	private String signTime;
	// 登录时间
	private String loginTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getSignNum() {
		return signNum;
	}

	public void setSignNum(int signNum) {
		this.signNum = signNum;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
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
	 * 判断是否已经领取过
	 * 
	 * @param rewardID
	 * @return
	 */
	public boolean isReward(int rewardID) {
		return rewardList.contains(rewardID);
	}

	/**
	 * 增加已领取 ID
	 * 
	 * @param rewardID
	 */
	public void addRewardID(int rewardID) {
		if (!isReward(rewardID)) {
			rewardList.add(rewardID);
		}
	}

	/**
	 * 增加签到次数
	 */
	public void addSignNum(boolean sign) {
		if (!sign) {
			return;
		}
		if (signNum < WelfareConstants.SIGN_MAXNUM) {
			signNum += 1;
		}
	}

	/**
	 * 检查第七天是否签过
	 * 
	 * @return
	 */
	public boolean isSeven(boolean sign) {
		if (!sign) {
			return false;
		}
		String dateTime = Utilities.getDateTime();
		if (signTime == null || signTime.isEmpty()) {
			signTime = dateTime;
			return false;
		}
		if (!signTime.equals(dateTime)) {
			signTime = dateTime;
			return false;
		}
		return true;
	}

	/**
	 * 检测是否连续签到
	 * 
	 * @return
	 */
	public boolean isContinuous(boolean sign) {
		if (!sign) {
			return false;
		}
		if (loginTime == null || loginTime.isEmpty()) {
			return true;
		}
		int diffTime = EventUtils.diffTime(Utilities.getDateTime(), loginTime);
		if (diffTime == 1 || diffTime == -1 || diffTime == 0) {
			return true;
		}
		return false;
	}

	public void refreshReward(boolean sign) {
		if (!sign) {
			return;
		}
		rewardList.clear();
		signNum = 1;
		loginTime = Utilities.getDateTime();
	}

	/**
	 * 检查当天是否已经登录
	 */
	public boolean isLogin(boolean sign) {
		if (!sign) {
			return false;
		}
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
		result.put("signNum", signNum);
		result.put("rewardList", rewardList);
		result.put("isSeven", isSeven(true));
		return result;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("playerID", playerID);
		result.put("signNum", signNum);
		result.put("loginTime", loginTime);
		result.put("signTime", loginTime);
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
