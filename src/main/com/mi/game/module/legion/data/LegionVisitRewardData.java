package com.mi.game.module.legion.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/LegionVisitRewardPrototype.xml" })
public class LegionVisitRewardData extends LegionBaseData {
	// 建筑需求
	private int bulidLimit;
	// 参拜奖励
	private Map<Integer, Integer> reward;

	public int getBulidLimit() {
		return bulidLimit;
	}

	public void setBulidLimit(int bulidLimit) {
		this.bulidLimit = bulidLimit;
	}

	public Map<Integer, Integer> getReward() {
		return reward;
	}

	public void setReward(String str) {
		this.reward = setMultiple(str);
	}

	public Integer getValue(Integer key) {
		return reward.get(key);
	}

	@Override
	public String toString() {
		return "LegionVisitRewardData [bulidLimit=" + bulidLimit + ", reward=" + reward + "]";
	}

}
