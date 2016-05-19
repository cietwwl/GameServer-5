package com.mi.game.module.legion.pojo;

import java.util.HashMap;
import java.util.Map;

public class LegionShopItem {
	// 商品id
	private int itemID;
	// 商品数量
	private int itemNum;
	// 剩余销售数量
	private int remainNum;
	// 货币数量
	private int coinValue;
	// 货币类型
	private int coinType;

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

	public int getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(int remainNum) {
		this.remainNum = remainNum;
	}

	public int getCoinValue() {
		return coinValue;
	}

	public void setCoinValue(int coinValue) {
		this.coinValue = coinValue;
	}

	public int getCoinType() {
		return coinType;
	}

	public void setCoinType(int coinType) {
		this.coinType = coinType;
	}

	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("itemID", itemID);
		result.put("itemNum", itemNum);
		result.put("coinType", coinType);
		result.put("coinValue", coinValue);
		result.put("remainNum", remainNum);
		return result;
	}

}
