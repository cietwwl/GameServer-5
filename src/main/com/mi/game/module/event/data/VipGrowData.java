package com.mi.game.module.event.data;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/VIPGrowthPrototype.xml" })
public class VipGrowData extends EventBaseData {

	private int level;
	private int vipLimit;
	private Map<Integer, Integer> itemID = new HashMap<Integer, Integer>();

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVipLimit() {
		return vipLimit;
	}

	public void setVipLimit(int vipLimit) {
		this.vipLimit = vipLimit;
	}

	public Map<Integer, Integer> getItemID() {
		return itemID;
	}

	public void setItemID(String str) {
		this.itemID = setMultiple(str);
	}

	public Integer getValue(Integer key) {
		return itemID.get(key);
	}

}
