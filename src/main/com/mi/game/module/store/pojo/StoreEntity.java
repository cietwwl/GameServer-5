package com.mi.game.module.store.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.Utilities;

public class StoreEntity extends BaseEntity {

	private static final long serialVersionUID = 8005416396020530346L;
	@Indexed
	private String playerID;
	// 已购买物品列表
	private Map<String, Integer> buyItems = new HashMap<String, Integer>();
	// 已购买vip礼包
	private List<Integer> vipGiftList = new ArrayList<Integer>();
	// 商店刷新时间
	private String shopTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public String getShopTime() {
		return shopTime;
	}

	public void setShopTime(String shopTime) {
		this.shopTime = shopTime;
	}

	public Map<String, Integer> getBuyItems() {
		return buyItems;
	}

	public void setBuyItems(Map<String, Integer> buyItems) {
		this.buyItems = buyItems;
	}

	public List<Integer> getVipGiftList() {
		return vipGiftList;
	}

	public void setVipGiftList(List<Integer> vipGiftList) {
		this.vipGiftList = vipGiftList;
	}

	/**
	 * 获取已经购买物品数量
	 * 
	 * @param itemID
	 * @return
	 */
	public int getBuyItemNum(String itemID) {
		int buyNum = 0;
		if (buyItems.containsKey(itemID)) {
			buyNum = buyItems.get(itemID);
		}
		return buyNum;
	}

	/**
	 * 增加物品已购买数量
	 * 
	 * @param itemID
	 */
	public void addBuyItemNum(String itemID,int value) {
		buyItems.put(itemID, getBuyItemNum(itemID) + value);
	}

	/**
	 * 是否刷新商城购买列表
	 * 
	 * @return
	 */
	public boolean isRefresh() {
		String dateTime = Utilities.getDateTime();
		if (shopTime == null || shopTime.isEmpty()) {
			shopTime = dateTime;
			return false;
		}
		if (!shopTime.equals(dateTime)) {
			shopTime = dateTime;
			return false;
		}
		return true;
	}

	/**
	 * 是否购买pid礼包
	 * 
	 * @param pid
	 * @return
	 */
	public boolean isBuyVipGift(Integer pid) {
		return vipGiftList.contains(pid);
	}

	public void addVipGift(Integer pid) {
		if (!vipGiftList.contains(pid)) {
			vipGiftList.add(pid);
		}
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		if (!isRefresh()) {
			buyItems.clear();
		}
		result.put("buyItems", buyItems);
		result.put("vipGiftList", vipGiftList);
		return result;
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return playerID;
	}

	@Override
	public void setKey(Object key) {
		this.playerID = key.toString();
	}

}
