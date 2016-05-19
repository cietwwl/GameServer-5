package com.mi.game.module.legion.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.mi.core.pojo.BaseEntity;
import com.mi.game.util.Utilities;

@Entity(noClassnameStored = true)
public class LegionMemberEntity extends BaseEntity {
	private static final long serialVersionUID = -5225332760824166828L;
	// 玩家id
	@Indexed
	private String playerID;
	// 贡献
	private long devote;
	// 总贡献
	private long maxDevote;
	// 建设
	private String build;
	// 建设时间
	private String buildTime;
	// 商店已购买 pid,num
	private Map<String, Integer> buyItem = new HashMap<String, Integer>();
	// 珍品已购买 pid,num
	private Map<String, Integer> gemItem = new HashMap<String, Integer>();
	// 购买珍品时间
	private String gemTime;
	// 购买道具时间
	private String buyTime;
	// 参拜次数
	private int visit;
	// 参拜时间
	private String visitTime;
	// 切磋次数
	private int compare;
	// 切磋时间
	private String compareTime;
	// 退出,解散,踢出军团时间
	private long lastTime;
	// 申请加入军团列表
	private List<String> applyList = new ArrayList<String>();;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public long getDevote() {
		return devote;
	}

	public void setDevote(long devote) {
		this.devote = devote;
	}

	public long getMaxDevote() {
		return maxDevote;
	}

	public void setMaxDevote(long maxDevote) {
		this.maxDevote = maxDevote;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(String buildTime) {
		this.buildTime = buildTime;
	}

	public Map<String, Integer> getBuyItem() {
		return buyItem;
	}

	public void setBuyItem(Map<String, Integer> buyItem) {
		this.buyItem = buyItem;
	}

	public Map<String, Integer> getGemItem() {
		return gemItem;
	}

	public void setGemItem(Map<String, Integer> gemItem) {
		this.gemItem = gemItem;
	}

	public String getGemTime() {
		return gemTime;
	}

	public void setGemTime(String gemTime) {
		this.gemTime = gemTime;
	}

	public String getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}

	public int getVisit() {
		return visit;
	}

	public void setVisit(int visit) {
		this.visit = visit;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

	public int getCompare() {
		return compare;
	}

	public void setCompare(int compare) {
		this.compare = compare;
	}

	public String getCompareTime() {
		return compareTime;
	}

	public void setCompareTime(String compareTime) {
		this.compareTime = compareTime;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public List<String> getApplyList() {
		return applyList;
	}

	public void setApplyList(List<String> applyList) {
		this.applyList = applyList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 检查是否可以购买军团道具
	 * 
	 * @param itemID
	 * @param num
	 * @return
	 */
	public boolean isCanBuy(String itemID, int limitNum) {
		if (!buyItem.containsKey(itemID)) {
			return false;
		}
		// 已购买数量
		int value = buyItem.get(itemID);
		if (value < limitNum) {
			return false;
		}
		return true;
	}

	/**
	 * 增加军团道具购买记录
	 * 
	 * @param itemID
	 */
	public void addBuyItem(String itemID) {
		if (!buyItem.containsKey(itemID)) {
			buyItem.put(itemID, 1);
		} else {
			int value = buyItem.get(itemID);
			buyItem.put(itemID, value + 1);
		}
	}

	/**
	 * 检查是否可以购买珍品
	 * 
	 * @param itemID
	 * @return
	 */
	public boolean isGemCanBuy(String itemID) {
		return gemItem.containsKey(itemID);
	}

	/**
	 * 增加军团珍品购买记录
	 * 
	 * @param itemID
	 */
	public void addGemItem(String itemID) {
		gemItem.put(itemID, 1);
	}

	public void initBuyItem() {
		buyItem.clear();
	}

	public void initGemItem() {
		gemItem.clear();
	}

	public boolean isBuildRefresh(boolean sign) {
		if (!sign) {
			return false;
		}
		String dateTime = Utilities.getDateTime();
		if (buildTime == null || buildTime.isEmpty()) {
			buildTime = dateTime;
			return false;
		}
		if (!buildTime.equals(dateTime)) {
			buildTime = dateTime;
			return false;
		}
		return true;
	}

	public boolean isBuyRefresh(boolean sign) {
		if (!sign) {
			return false;
		}
		String dateTime = Utilities.getDateTime();
		if (buyTime == null || buyTime.isEmpty()) {
			buyTime = dateTime;
			return false;
		}
		if (!buyTime.equals(dateTime)) {
			buyTime = dateTime;
			return false;
		}
		return true;
	}

	public boolean isGemRefresh(boolean sign) {
		if (!sign) {
			return false;
		}
		String dateTime = Utilities.getDateTime();
		if (gemTime == null || gemTime.isEmpty()) {
			gemTime = dateTime;
			return false;
		}
		if (!gemTime.equals(dateTime)) {
			gemTime = dateTime;
			return false;
		}
		return true;
	}

	public boolean isVisitRefresh(boolean sign) {
		if (!sign) {
			return false;
		}
		String dateTime = Utilities.getDateTime();
		if (visitTime == null || visitTime.isEmpty()) {
			visitTime = dateTime;
			return false;
		}
		if (!visitTime.equals(dateTime)) {
			visitTime = dateTime;
			return false;
		}
		return true;
	}

	public boolean isCompareRefresh(boolean sign) {
		if (!sign) {
			return false;
		}
		String dateTime = Utilities.getDateTime();
		if (compareTime == null || compareTime.isEmpty()) {
			compareTime = dateTime;
			return false;
		}
		if (!compareTime.equals(dateTime)) {
			compareTime = dateTime;
			return false;
		}
		return true;
	}

	/**
	 * 增加个人贡献
	 * 
	 * @param currency
	 */
	public void addDevote(int currency) {
		devote += currency;
		maxDevote += currency;
	}

	/**
	 * 减少个人贡献
	 * 
	 * @param currency
	 */
	public void consumeDevote(int currency) {
		devote -= currency;
	}

	/**
	 * 增加申请军团
	 * 
	 * @param legionID
	 */
	public void addApplyList(String legionID) {
		if (!this.applyList.contains(legionID)) {
			this.applyList.add(legionID);
		}
	}

	/**
	 * 删除申请军团
	 * 
	 * @param legionID
	 */
	public void delApplyList(String legionID) {
		if (this.applyList.contains(legionID)) {
			this.applyList.remove(legionID);
		}
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("playerID", playerID);
		result.put("devote", devote);
		result.put("maxDevote", maxDevote);
		result.put("build", build);
		result.put("buildTime", buildTime);
		result.put("visit", visit);
		result.put("visitTime", visitTime);
		return result;
	}

	public void delApplyAll() {
		this.applyList.clear();
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
		this.playerID = key.toString();
	}

}
