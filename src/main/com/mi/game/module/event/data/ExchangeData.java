package com.mi.game.module.event.data;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/ExchangePrototype.xml" })
public class ExchangeData extends EventBaseData {
	// 兑换次数
	private int dailyTime;
	// 兑换价格
	private int gold;
	// 原料1
	private int itemID;
	// 原料2
	private int rawID;
	// 原料3
	private int artifactsID;
	// 原料4
	private int artifactID;
	// 兑换物品
	private int result;

	public int getDailyTime() {
		return dailyTime;
	}

	public void setDailyTime(int dailyTime) {
		this.dailyTime = dailyTime;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getRawID() {
		return rawID;
	}

	public void setRawID(int rawID) {
		this.rawID = rawID;
	}

	public int getArtifactsID() {
		return artifactsID;
	}

	public void setArtifactsID(int artifactsID) {
		this.artifactsID = artifactsID;
	}

	public int getArtifactID() {
		return artifactID;
	}

	public void setArtifactID(int artifactID) {
		this.artifactID = artifactID;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
