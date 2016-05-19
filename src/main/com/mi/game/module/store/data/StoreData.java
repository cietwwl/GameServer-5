package com.mi.game.module.store.data;

import com.mi.core.engine.annotation.XmlTemplate;

@XmlTemplate(template = { "com/mi/template/ItemShopPrototype.xml" })
public class StoreData extends StoreBaseData {
	// 物品id
	private int itemID;
	// 货币类型
	private int currencyType;
	// 价格
	private int price;
	// 递增价格
	private int pricePlus;
	// vip等级
	private int[] vipLimit;

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(int currencyType) {
		this.currencyType = currencyType;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPricePlus() {
		return pricePlus;
	}

	public void setPricePlus(int pricePlus) {
		this.pricePlus = pricePlus;
	}

	public int[] getVipLimit() {
		return vipLimit;
	}

	public void setVipLimit(String str) {
		if (str != null && !str.isEmpty()) {
			String[] arr = str.split(",");
			vipLimit = new int[arr.length];
			for (int i = 0; i < arr.length; i++) {
				vipLimit[i] = Integer.parseInt(arr[i]);
			}
		}
	}

}
