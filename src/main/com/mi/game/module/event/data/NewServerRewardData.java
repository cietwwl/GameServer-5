package com.mi.game.module.event.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/NewServerActiveRewardPrototype.xml" })
public class NewServerRewardData extends EventBaseData {

	private String name;

	private int levelRequest;

	private Map<Integer, Integer> reward = new HashMap<Integer, Integer>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<Integer, Integer> getReward() {
		return reward;
	}

	public void setReward(String str) {
		reward = setMultiple(str);
	}

	public int getLevelRequest() {
		return levelRequest;
	}

	public void setLevelRequest(int levelRequest) {
		this.levelRequest = levelRequest;
	}

}
