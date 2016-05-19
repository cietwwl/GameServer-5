package com.mi.game.module.legion.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/LegionShopPrototype.xml" })
public class LegionShopData extends LegionBaseData {

	// 道具ID
	private Map<Integer, Integer> itemID;
	// 解锁条件
	private int unlockLimit;
	// 价格
	private Map<Integer, Integer> price;
	// 每日限购买次数
	private int dayLimit;

	public int getPriceValue(int key) {
		return price.get(key);
	}

	public int getItemValue(int key) {
		return itemID.get(key);
	}

	public Map<Integer, Integer> getItemID() {
		return itemID;
	}

	public void setItemID(String str) {
		this.itemID = setMultiple(str);
	}

	public int getUnlockLimit() {
		return unlockLimit;
	}

	public void setUnlockLimit(int unlockLimit) {
		this.unlockLimit = unlockLimit;
	}

	public Map<Integer, Integer> getPrice() {
		return price;
	}

	public void setPrice(String str) {
		this.price = setMultiple(str);
	}

	public int getDayLimit() {
		return dayLimit;
	}

	public void setDayLimit(int dayLimit) {
		this.dayLimit = dayLimit;
	}

	public Integer getValue(Integer key) {
		return itemID.get(key);
	}

	@Override
	public String toString() {
		return "LegionShopData [itemID=" + itemID + ", unlockLimit=" + unlockLimit + ", price=" + price + ", dayLimit=" + dayLimit + "]";
	}

}
