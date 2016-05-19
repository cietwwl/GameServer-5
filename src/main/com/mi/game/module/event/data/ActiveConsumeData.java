package com.mi.game.module.event.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/ActiveConsumePrototype.xml" })
public class ActiveConsumeData extends EventBaseData {
	private int gold;
	private Map<Integer, Integer> rewardValues = new HashMap<Integer, Integer>();
	private String reward;


	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
	
	public int getRewardValue(int key) {
		return rewardValues.get(key);
	}

	public Map<Integer, Integer> getRewardValues() {
		return rewardValues;
	}

	public void setRewardValues(String rewardValues) {		
		if (rewardValues != null && !rewardValues.isEmpty()) {
			String[] arr = rewardValues.split(",");
			this.rewardValues = new HashMap<Integer, Integer>();
			for (int i = 0; i < arr.length; i++) {
				String[] tempArr = arr[i].split("=");
                this.rewardValues.put(Integer.parseInt(tempArr[0]),Integer.parseInt(tempArr[1]));
			}
		}
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
		this.setRewardValues(reward);
	}
}
