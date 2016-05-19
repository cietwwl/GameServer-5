package com.mi.game.module.event.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

/**
 * 每日充值奖励
 * 
 * @author 赵鹏翔
 * @time Apr 8, 2015 2:07:29 PM
 */
@XmlTemplate(template = { "com/mi/template/DailyChargePrototype.xml" })
public class ChargeDayGiftData extends EventBaseData {

	private int gold;
	private Map<Integer, Integer> reward = new HashMap<Integer, Integer>();

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
		this.reward = setMultiple(str);
	}

	public int getRewardValue(int key) {
		return reward.get(key);
	}

}
