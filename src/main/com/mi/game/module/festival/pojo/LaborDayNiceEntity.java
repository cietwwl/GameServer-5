package com.mi.game.module.festival.pojo;

import java.util.HashMap;
import java.util.Map;

import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.Utilities;

/**
 * 五一节点赞
 *
 */
public class LaborDayNiceEntity extends BaseEntity {

	private static final long serialVersionUID = 2100409991358906769L;

	private String playerID;
	// 免费红鞭炮
	private int freeRed;
	// 红鞭炮最大购买数量
	private int maxRed;
	// 免费金鞭炮
	private int freeGold;
	// 金鞭炮最大数
	private int maxGold;
	// 时间标记
	private String dateTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public int getFreeRed() {
		return freeRed;
	}

	public void setFreeRed(int freeRed) {
		this.freeRed = freeRed;
	}

	public int getMaxRed() {
		return maxRed;
	}

	public void setMaxRed(int maxRed) {
		this.maxRed = maxRed;
	}

	public int getFreeGold() {
		return freeGold;
	}

	public void setFreeGold(int freeGold) {
		this.freeGold = freeGold;
	}

	public int getMaxGold() {
		return maxGold;
	}

	public void setMaxGold(int maxGold) {
		this.maxGold = maxGold;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * 是否刷新
	 * 
	 * @return
	 */
	public boolean isRefresh() {
		String nowTime = Utilities.getDateTime();
		if (dateTime == null || dateTime.isEmpty()) {
			dateTime = nowTime;
			return false;
		}
		if (!nowTime.equals(dateTime)) {
			dateTime = nowTime;
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("freeRed", freeRed);
		result.put("maxRed", maxRed);
		result.put("freeGold", freeGold);
		result.put("maxGold", maxGold);
		return result;
	}

	@Override
	public Object getKey() {
		return playerID;
	}

	@Override
	public String getKeyName() {
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		playerID = key.toString();
	}

}
