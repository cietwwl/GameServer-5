package com.mi.game.module.pk.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

@XmlTemplate(template = { "com/mi/template/TournamentShopPrototype.xml" })
public class PkRewardShopData extends BaseTemplate {
	private String itemID; // 兑换物品信息101441=1 物品id,数量
	private String cost; // 消耗物品信息100059=1000 物品id,数量
	/**
	 * 次数限制信息 times="1=1" times="2=1" times="3=1" 等号前边的123含义 Week
	 * =1,day=2，only=3
	 */
	private String times;

	private int level;

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
