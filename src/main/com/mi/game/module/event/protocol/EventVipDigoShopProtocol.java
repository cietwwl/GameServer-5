package com.mi.game.module.event.protocol;

import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.event.data.VipDigoShopData;

/**
 * vip折扣店
 * 
 * @author 赵鹏翔
 * @time Apr 7, 2015 10:51:11 AM
 */
public class EventVipDigoShopProtocol extends BaseProtocol {
	// 折扣店商品列表
	private List<VipDigoShopData> shopList;
	private Map<String, Object> itemMap;
	private List<Integer> historyList; // 购买历史

	public List<Integer> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<Integer> historyList) {
		this.historyList = historyList;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}

	public List<VipDigoShopData> getShopList() {
		return shopList;
	}

	public void setShopList(List<VipDigoShopData> shopList) {
		this.shopList = shopList;
	}
	

}
