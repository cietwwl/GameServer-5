package com.mi.game.module.event.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/ConsumeGiftPrototype.xml" })
public class ConsumeGiftData extends EventBaseData {
	private int gold;
	private Map<Integer, Integer> reward = new HashMap<Integer, Integer>();
	private String rewardValues;

	public String getRewardValues() {
		return rewardValues;
	}

	public void setRewardValues(String rewardValues) {
		this.rewardValues = rewardValues;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public Map<Integer, Integer> getReward() {
		return reward;
	}

	public void setReward(String str) {
		this.rewardValues=str;
		this.reward = setMultiple(str);
	}

	public int getRewardValue(int key) {
		return reward.get(key);
	}
}
