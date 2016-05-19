package com.mi.game.module.event.data;

import com.mi.core.engine.annotation.XmlTemplate;

/**
 * 每日充值奖励模版
 * 
 * @author 赵鹏翔
 * @time Apr 7, 2015 10:44:35 AM
 */
@XmlTemplate(template = { "com/mi/template/DailyChargePrototype.xml" })
public class DailyPayGiftData extends EventBaseData {

	private int pid; // 模版id
	private String name; // 奖励名称
	private String Reward; // 奖励模版
	private int gold; // 充值金额

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReward() {
		return Reward;
	}

	public void setReward(String reward) {
		Reward = reward;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

}
