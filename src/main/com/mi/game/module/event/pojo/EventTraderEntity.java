package com.mi.game.module.event.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.event.data.MysteryItemData;
import com.mi.game.module.event.define.EventConstans;
import com.mi.game.util.Utilities;

public class EventTraderEntity extends BaseEntity {
	private static final long serialVersionUID = 8847956227568818875L;
	@Indexed
	private String playerID;
	// 神秘商人商品列表
	private List<MysteryItem> sellItems = new ArrayList<MysteryItem>();
	// 神秘商人购买列表
	private List<Integer> buyItems = new ArrayList<Integer>();
	// 神秘商人每日自动刷新时间
	private String traderTime;
	// 神秘商人出现时间
	private long showTime;
	// 神秘商人存在时长
	private long existTime;

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

	public String getTraderTime() {
		return traderTime;
	}

	public void setTraderTime(String traderTime) {
		this.traderTime = traderTime;
	}

	public long getShowTime() {
		return showTime;
	}

	public void setShowTime(long showTime) {
		this.showTime = showTime;
	}

	public long getExistTime() {
		if (existTime == -1) {
			return -1;
		}
		// 出现时间+存在时间-当前时间-
		long diffTime = (showTime + existTime - System.currentTimeMillis());
		// 计算存在时间
		diffTime = diffTime > 0 ? diffTime : 0;
		return diffTime;
	}

	public void setExistTime(long existTime) {
		this.existTime = existTime;
	}

	public void addExistTime(long time) {
		if (existTime == -1) {
			return;
		}
		existTime = getExistTime() + time;
		// 重置出现时间
		showTime = System.currentTimeMillis();
	}

	/**
	 * 根据物品id 获取商人物品
	 * 
	 * @param itemID
	 * @return
	 */
	public MysteryItem getTraderItemByID(int itemID) {
		for (MysteryItem item : sellItems) {
			if (item.getItemID() == itemID) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 检查神秘商人是否购买过
	 * 
	 * @param itemID
	 * @return
	 */
	public boolean isBuyTraderItem(int itemID) {
		return buyItems.contains(itemID);
	}

	/**
	 * 增加神秘商人购买记录
	 * 
	 * @param itemID
	 */
	public void addTraderItem(Integer itemID) {
		buyItems.add(itemID);
	}

	/**
	 * 检查商人是否有物品
	 * 
	 * @param itemIntID
	 * @return
	 */
	public boolean isHasTraderItem(int itemIntID) {
		for (MysteryItem item : sellItems) {
			if (item.getItemID() == itemIntID) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 今天是否刷新神秘商人
	 * 
	 * @return
	 */
	public boolean isTrader() {
		String dateTime = Utilities.getDateTime();
		if (traderTime == null || traderTime.isEmpty()) {
			traderTime = dateTime;
			return false;
		}
		if (!traderTime.equals(dateTime)) {
			traderTime = dateTime;
			return false;
		}
		return true;
	}

	// 随机获取物品
	private MysteryItem getRandomItem(MysteryItemData mysteryItem) {
		int max = mysteryItem.getMax();
		int random = Utilities.getRandomInt(max);
		int weight = 0;
		MysteryItem result = null;
		for (ActiveItem item : mysteryItem.getItemList()) {
			weight = item.getWeight();
			if (weight >= random) {
				result = new MysteryItem();
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
	 * 初始化神秘商人
	 * 
	 * @param mysteryItem
	 */
	public void initTraderItems(MysteryItemData mysteryItem) {
		sellItems.clear();
		buyItems.clear();
		for (int i = 0; i < EventConstans.MYSTERY_TRADER_NUM; i++) {
			MysteryItem item = getRandomItem(mysteryItem);
			while (hasItem(item)) {
				item = getRandomItem(mysteryItem);
			}
			if (EventConstans.MYSTERY_TRADER_NUM > sellItems.size()) {
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
		result.put("refresh", -1);
		if (existTime == -1) {
			result.put("cdTime", -1);
		} else {
			result.put("cdTime", getExistTime() / 1000);
		}
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
