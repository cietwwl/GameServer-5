package com.mi.game.module.welfare.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/LevelRewardPrototype.xml" })
public class LevelRewardData extends WelfareBaseData {
	// 等级
	private int level;
	// 奖励列表
	private Map<Integer, Integer> rewardMap;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
