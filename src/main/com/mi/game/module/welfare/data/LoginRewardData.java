package com.mi.game.module.welfare.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/LoginRewardPrototype.xml" })
public class LoginRewardData extends WelfareBaseData {
	// 登录天数
	private int days;
	// 奖励列表
	private Map<Integer, Integer> rewardMap;

	private int payTotal;

	public int getDays() {
		return days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public Map<Integer, Integer> getReward() {
		return rewardMap;
	}

	public void setReward(String str) {
		this.rewardMap = setMultiple(str);
	}

	public int getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(int payTotal) {
		this.payTotal = payTotal;
	}

	public Integer getRewardValue(int key) {
		return rewardMap.get(key);
	}

}
