package com.mi.game.module.welfare.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/OnlineTimeRewardPrototype.xml" })
public class OnlineTimeRewardData extends WelfareBaseData {
	// 在线时长
	private int seconds;
	// 奖励列表
	private Map<Integer, Integer> rewardMap;

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
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
