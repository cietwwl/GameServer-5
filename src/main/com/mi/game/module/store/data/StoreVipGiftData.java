package com.mi.game.module.store.data;

import java.util.Map;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/VIPPackagePrototype.xml" })
public class StoreVipGiftData extends StoreBaseData {
	// vip等级
	private int vipLevel;
	// 礼包价格
	private int price;
	// 礼包物品
	private Map<Integer, Integer> itemID;

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Map<Integer, Integer> getItemID() {
		return itemID;
	}

	public void setItemID(String str) {
		this.itemID = setMultiple(str);
	}

	public Integer getItemValue(int key) {
		return itemID.get(key);
	}

}
