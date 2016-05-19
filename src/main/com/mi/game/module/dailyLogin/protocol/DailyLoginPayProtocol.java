package com.mi.game.module.dailyLogin.protocol;

import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.dailyLogin.data.DailyLoginRewardData;
import com.mi.game.module.reward.data.GoodsBean;

/**
 * 每日登录充值活动
 * 
 * @author 赵鹏翔
 * @time Apr 8, 2015 11:22:34 AM
 */
public class DailyLoginPayProtocol extends BaseProtocol {

	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;
	private Integer loginNum; // 连续登录天数
	private List<Integer> loginDrawList; // 登录签到已领取奖励列表
	private List<DailyLoginRewardData> rewardList; // 奖励模版
	private List<Integer> payDrawList; // 登录充值已领取奖励列表
	private int todayReward; // 今天奖励模版
	private int payNum; // 今日充值金额

	public int getPayNum() {
		return payNum;
	}

	public void setPayNum(int payNum) {
		this.payNum = payNum;
	}

	public int getTodayReward() {
		return todayReward;
	}

	public void setTodayReward(int todayReward) {
		this.todayReward = todayReward;
	}

	public List<Integer> getPayDrawList() {
		return payDrawList;
	}

	public void setPayDrawList(List<Integer> payDrawList) {
		this.payDrawList = payDrawList;
	}


	public List<Integer> getLoginDrawList() {
		return loginDrawList;
	}

	public void setLoginDrawList(List<Integer> loginDrawList) {
		this.loginDrawList = loginDrawList;
	}

	public Integer getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(Integer loginNum) {
		this.loginNum = loginNum;
	}


	public List<DailyLoginRewardData> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<DailyLoginRewardData> rewardList) {
		this.rewardList = rewardList;
	}

	// @Override
	// public Map<String, Object> responseMap() {
	// Map<String, Object> response = new HashMap<String, Object>();
	// if (itemMap != null) {
	// response.put("itemMap", itemMap);
	// }
	// if (showMap != null) {
	// response.put("showMap", showMap);
	// }
	// return response;
	// }

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}

}
