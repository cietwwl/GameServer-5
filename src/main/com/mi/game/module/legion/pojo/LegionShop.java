package com.mi.game.module.legion.pojo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mi.game.module.legion.data.LegionDropData;
import com.mi.game.module.legion.define.LegionConstans;
import com.mi.game.util.Utilities;

public class LegionShop extends LegionBase {

	private List<LegionShopItem> goods = new ArrayList<LegionShopItem>();

	public List<LegionShopItem> getGoods() {
		return goods;
	}

	// 珍品刷新时间
	private String flushTime;

	public void setGoods(List<LegionShopItem> goods) {
		this.goods = goods;
	}

	public String getFlushTime() {
		return flushTime;
	}

	public void setFlushTime(String flushTime) {
		this.flushTime = flushTime;
	}

	/**
	 * // 检查珍品是否还有购买次数
	 * 
	 * @param itemID
	 * @return
	 */
	public boolean isHaveItem(int itemID) {
		for (LegionShopItem item : goods) {
			if (itemID == item.getItemID()) {
				if (item.getRemainNum() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * // 减少珍品购买次数
	 * 
	 * @param itemID
	 */
	public void reduceItemNum(int itemID) {
		for (LegionShopItem item : goods) {
			if (itemID == item.getItemID()) {
				if (item.getRemainNum() > 0) {
					item.setRemainNum(item.getRemainNum() - 1);
					return;
				}
			}
		}
	}

	/**
	 * // 根据物品id获取珍品信息
	 * 
	 * @param itemID
	 * @return
	 */
	public LegionShopItem getShopItem(int itemID) {
		for (LegionShopItem item : goods) {
			if (itemID == item.getItemID()) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 珍品刷新刷新
	 * 
	 * @return
	 */
	public boolean isGoodFlush() {
		String dateTime = Utilities.getDateTime();
		if (flushTime == null || flushTime.isEmpty()) {
			flushTime = dateTime;
			return false;
		}
		if (!flushTime.equals(dateTime)) {
			flushTime = dateTime;
			return false;
		}
		return true;
	}

	/**
	 * 初始化珍品商品
	 * 
	 * @param dropData
	 */
	public void initGoods(LegionDropData dropData) {
		// 清空googs
		goods.clear();
		for (int i = 0; i < LegionConstans.LEGION_DROPTYPENUM; i++) {
			LegionShopItem item = getRandomItem(dropData);
			while (isHaveItem(item.getItemID())) {
				item = getRandomItem(dropData);
			}
			if (LegionConstans.LEGION_DROPTYPENUM > goods.size()) {
				goods.add(item);
			}
		}
	}

	/**
	 * 获取珍品刷新剩余时间
	 * 
	 * @return
	 */
	public long getDiffTime() {
		String nowDay = Utilities.getDateTime();
		String nowTime = "10:00:00";
		long diffTime = Utilities.getDiffTime(nowDay, nowTime, 0);
		// 判断当前时间是否超过十点
		if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) >= LegionConstans.LEGION_GOODS_FLUSHTIME) {
			diffTime = Utilities.getDiffTime(nowDay, nowTime, 1);
		}
		return diffTime;
	}

	private LegionShopItem getRandomItem(LegionDropData dropData) {
		int max = dropData.getMax();
		int random = Utilities.getRandomInt(max);
		int weight = 0;
		// 珍品数据
		LegionShopItem result = null;
		for (DropItem item : dropData.getDropItems()) {
			weight = item.getWeight();
			if (weight >= random) {
				result = new LegionShopItem();
				result.setItemID(item.getItemID());
				result.setItemNum(item.getAmount());
				result.setRemainNum(item.getMaxSale());
				int coinType = item.getKeys(item.getPrice()).get(0);
				result.setCoinType(coinType);
				result.setCoinValue(item.getPriceValue(coinType));
				break;
			}
		}
		if (result == null) {
			return getRandomItem(dropData);
		}
		return result;
	}

	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("currentPid", getCurrentPid());
		result.put("collingTime", getDiffTime());
		result.put("goods", goodsResponseMap());
		return result;
	}

	public List<Map<String, Object>> goodsResponseMap() {
		List<Map<String, Object>> goodsList = new ArrayList<Map<String, Object>>();
		for (LegionShopItem item : goods) {
			goodsList.add(item.responseMap());
		}
		return goodsList;
	}
}
