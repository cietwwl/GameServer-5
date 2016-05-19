package com.mi.game.module.welfare.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/SigninRewardPrototype.xml" })
public class SigninRewardData extends WelfareBaseData {
	// 签到天数
	private int days;
	// 奖励列表
	private Map<Integer, Integer> rewardMap;

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

	public Integer getRewardValue(int key) {
		return rewardMap.get(key);
	}

}
