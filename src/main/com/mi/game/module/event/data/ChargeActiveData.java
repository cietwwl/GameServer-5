package com.mi.game.module.event.data;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/ChargeActivePrototype.xml" })
public class ChargeActiveData extends EventBaseData {

	private int dropID;
	private int gold;
	private String desc;
	private String reward;
	private String spriteName;

	public int getDropID() {
		return dropID;
	}

	public void setDropID(int dropID) {
		this.dropID = dropID;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getSpriteName() {
		return spriteName;
	}

	public void setSpriteName(String spriteName) {
		this.spriteName = spriteName;
	}
}
