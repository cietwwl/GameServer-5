package com.mi.game.module.event.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/VIPDailyRewardPrototype.xml" })
public class VipDailyData extends EventBaseData {

	// vip等级
	private int level;
	// 礼包内容
	private Map<Integer, Integer> itemID = new HashMap<Integer, Integer>();

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Map<Integer, Integer> getItemID() {
		return itemID;
	}

	public void setItemID(String str) {
		this.itemID = setMultiple(str);
	}

	public Integer getValue(int key) {
		return itemID.get(key);
	}

}
