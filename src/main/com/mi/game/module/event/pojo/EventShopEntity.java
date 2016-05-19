package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.core.util.DateTimeUtil;
import com.mi.game.module.event.data.MysteryItemData;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.module.event.util.EventUtils;
import com.mi.game.util.Utilities;

public class EventShopEntity extends BaseEntity {
	private static final long serialVersionUID = 8847956227568818875L;
	@Indexed
	private String playerID;
	// 神秘商店商品列表
	private List<MysteryItem> sellItems = new ArrayList<MysteryItem>();
	// 神秘商店购买列表
	private List<Integer> buyItems = new ArrayList<Integer>();
	// 神秘商店刷新次数
	private int refresh;
	// 神秘商店每日自动刷新时间
	private String shopTime;
	// 最后一次刷新时间
	private long lastRefreshTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public List<MysteryItem> getSellItems() {
		return sellItems;
	}

	public void setSellItems(List<MysteryItem> sellItems) {
		this.sellItems = sellItems;
	}

	public List<Integer> getBuyItems() {
		return buyItems;
	}

	public void setBuyItems(List<Integer> buyItems) {
		this.buyItems = buyItems;
	}

	public int getRefresh() {
		return refresh;
	}

	public void setRefresh(int refresh) {
		this.refresh = refresh;
	}

	public String getShopTime() {
		return shopTime;
	}

	public void setShopTime(String shopTime) {
		this.shopTime = shopTime;
	}

	public void reduceRefresh() {
		refresh -= 1;
	}

	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	public void setLastRefreshTime() {
		long hourTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) * DateTimeUtil.ONE_HOUR_TIME_MS;
		hourTime += EventUtils.getDayStartTime() - 1000;
		this.lastRefreshTime = hourTime;
	}

	/**
	 * 根据物品id 获取商店物品
	 * 
	 * @param itemID
	 * @return
	 */
	public MysteryItem getShopItemByID(int itemID) {
		for (MysteryItem item : sellItems) {
			if (item.getItemID() == itemID) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 检查神秘商店是否购买过
	 * 
	 * @param itemID
	 * @return
	 */
	public boolean isBuyShopItem(int itemID) {
		return buyItems.contains(itemID);
	}

	/**
	 * 获取神秘商店刷新倒计时
	 * 
	 * @return
	 */
	public long getShopRefreshTime() {
		long diffTime = 0;
		// 计算倒计时
		diffTime = EventUtils.diff2Hour();
		return diffTime;
	}

	/**
	 * 增加神秘商店购买记录
	 * 
	 * @param itemID
	 */
	public void addBuyShopItem(Integer itemID) {
		buyItems.add(itemID);
	}

	/*
	 * 今天是否刷新神秘商店
	 */
	public boolean isShop() {
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
	 * 检查商店是否有物品
	 * 
	 * @param itemIntID
	 * @return
	 */
	public boolean isHasShopItem(int itemIntID) {
		for (MysteryItem item : sellItems) {
			if (item.getItemID() == itemIntID) {
				return true;
			}
		}
		return false;
	}

	// 随机获取物品
	private MysteryItem getRandomItem(MysteryItemData mysteryItem) {
		int max = mysteryItem.getMax();
		int random = Utilities.getRandomInt(max);
		int weight = 0;
		MysteryItem result = new MysteryItem();
		for (ActiveItem item : mysteryItem.getItemList()) {
			weight = item.getWeight();
			if (weight >= random) {
				result.setItemID(item.getItemID());
				result.setItemNum(item.getAmount());
				int coinType = item.getKeys(item.getPrice()).get(0);
				result.setCoinType(coinType);
				result.setCoinValue(item.getPriceValue(coinType));
				break;
			}
		}
		return result;
	}

	/**
	 * 初始化神秘商店
	 * 
	 * @param mysteryItem
	 */
	public void initShopItems(MysteryItemData mysteryItem) {
		sellItems.clear();
		buyItems.clear();
		for (int i = 0; i < EventConstans.MYSTERY_SHOP_NUM; i++) {
			MysteryItem item = getRandomItem(mysteryItem);
			while (hasItem(item)) {
				item = getRandomItem(mysteryItem);
			}
			if (EventConstans.MYSTERY_SHOP_NUM > sellItems.size()) {
				sellItems.add(item);
			}
		}
	}

	private boolean hasItem(MysteryItem item) {
		boolean has = false;
		for (MysteryItem tempItem : sellItems) {
			if (tempItem.getItemID() == item.getItemID()) {
				has = true;
				break;
			}
		}
		return has;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sellItems", getItemsMap(sellItems));
		result.put("buyItems", buyItems);
		result.put("refresh", refresh);
		result.put("cdTime", getShopRefreshTime());
		return result;
	}

	private List<Map<String, Integer>> getItemsMap(List<MysteryItem> itemList) {
		List<Map<String, Integer>> listMap = new ArrayList<Map<String, Integer>>();
		for (MysteryItem item : itemList) {
			Map<String, Integer> temp = new HashMap<String, Integer>();
			temp.put("itemID", item.getItemID());
			temp.put("itemNum", item.getItemNum());
			temp.put("coinType", item.getCoinType());
			temp.put("coinValue", item.getCoinValue());
			listMap.add(temp);
		}
		return listMap;
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
