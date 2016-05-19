package com.mi.game.module.limitShop.protocol;

import java.util.List;
import java.util.Map;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.limitShop.data.LimitShopData;
import com.mi.game.module.reward.data.GoodsBean;

/**
 * 限时抢购活动
 * 
 * @author 赵鹏翔
 * @time Apr 8, 2015 11:22:34 AM
 */
public class LimitShopProtocol extends BaseProtocol {

	// 限时抢购商品列表
	private List<LimitShopData> shopList;
	private Map<String, Object> itemMap;
	private List<GoodsBean> showMap;
	private List<Integer> buyList; // 当天已经购买物品pid列表

	public List<Integer> getBuyList() {
		return buyList;
	}

	public void setBuyList(List<Integer> buyList) {
		this.buyList = buyList;
	}

	public List<LimitShopData> getShopList() {
		return shopList;
	}

	public List<GoodsBean> getShowMap() {
		return showMap;
	}

	public void setShowMap(List<GoodsBean> showMap) {
		this.showMap = showMap;
	}

	public void setShopList(List<LimitShopData> shopList) {
		this.shopList = shopList;
	}

	public Map<String, Object> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, Object> itemMap) {
		this.itemMap = itemMap;
	}
}
