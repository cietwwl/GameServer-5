package com.mi.game.module.tower.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;

@Entity(noClassnameStored = true)
public class TowerEntity extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6013316643542051783L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String playerID;
	private int maxLevel; // 最大层数
	private int nowLevel; // 当前层数
	private int passLevel; // 通过层数
	private int heartNum; // 挑战心数
	private int freeResetNum; // 免费重置次数
	private long lastUpdateTime; // 最后更新时间
	private int payResetNum; // 付费更新次数
	private int maxPayResetNum; // 最大付费更新次数
	private long overTime; // 扫荡结束时间
	private boolean lock; // 最大层数锁定
	private boolean clear; // 是否扫荡
	private int heartBuyNum; // 挑战心数购买次数
	private int maxHeartBuyNum; // 最大可购买挑战心数
	private long lastAddTowerTime; // 最后添加塔层数的时间
	private List<HideInfo> hideList; // 隐藏关列表
	private long hideCounter; // 隐藏关ID

	@Override
	public Map<String, Object> responseMap() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("maxLevel", maxLevel);
		data.put("nowLevel", nowLevel);
		data.put("passLevel", passLevel);
		data.put("heartNum", heartNum);
		data.put("freeResetNum", freeResetNum);
		data.put("lastUpdateTime", lastUpdateTime);
		data.put("payResetNum", payResetNum);
		data.put("maxPayResetNum", maxPayResetNum);
		data.put("overTime", overTime);
		data.put("lock", lock);
		data.put("clear", clear);
		data.put("heartBuyNum", heartBuyNum);
		data.put("maxHeartBuyNum",maxHeartBuyNum);
		data.put("hideList", hideList);
		return data;
	}

	@Override
	public Map<String, Object> responseMap(int t) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("playerID", playerID);
		data.put("maxLevel", maxLevel);
		data.put("nowLevel", nowLevel);
		data.put("passLevel", passLevel);
		data.put("heartNum", heartNum);
		data.put("freeResetNum", freeResetNum);
		data.put("lastUpdateTime", lastUpdateTime);
		data.put("payResetNum", payResetNum);
		data.put("maxPayResetNum", maxPayResetNum);
		data.put("overTime", overTime);
		data.put("lock", lock);
		data.put("clear", clear);
		data.put("heartBuyNum", heartBuyNum);
		data.put("hideList", hideList);
		return data;
	}

	public long addHideCounter() {
		hideCounter += 1;
		return hideCounter;
	}

	public long getHideCounter() {
		return hideCounter;
	}

	public void setHideCounter(long hideCounter) {
		this.hideCounter = hideCounter;
	}

	public List<HideInfo> getHideList() {
		if (hideList == null) {
			hideList = new ArrayList<>();
		}
		return hideList;
	}

	public void setHideList(List<HideInfo> hideList) {
		this.hideList = hideList;
	}

	public long getLastAddTowerTime() {
		return lastAddTowerTime;
	}

	public void setLastAddTowerTime(long lastAddTowerTime) {
		this.lastAddTowerTime = lastAddTowerTime;
	}

	public int getHeartBuyNum() {
		return heartBuyNum;
	}

	public void setHeartBuyNum(int heartBuyNum) {
		this.heartBuyNum = heartBuyNum;
	}

	public boolean isClear() {
		return clear;
	}

	public void setClear(boolean clear) {
		this.clear = clear;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}

	public long getOverTime() {
		return overTime;
	}

	public void setOverTime(long overTime) {
		this.overTime = overTime;
	}

	public int getFreeResetNum() {
		return freeResetNum;
	}

	public void setFreeResetNum(int freeResetNum) {
		this.freeResetNum = freeResetNum;
	}

	public int getPayResetNum() {
		return payResetNum;
	}

	public void setPayResetNum(int payResetNum) {
		this.payResetNum = payResetNum;
	}

	public int getMaxPayResetNum() {
		return maxPayResetNum;
	}

	public void setMaxPayResetNum(int maxPayResetNum) {
		this.maxPayResetNum = maxPayResetNum;
	}

	public int getPassLevel() {
		return passLevel;
	}

	public void setPassLevel(int passLevel) {
		this.passLevel = passLevel;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getNowLevel() {
		return nowLevel;
	}

	public void setNowLevel(int nowLevel) {
		this.nowLevel = nowLevel;
	}

	public int getHeartNum() {
		return heartNum;
	}

	public void setHeartNum(int heartNum) {
		this.heartNum = heartNum;
	}

	public int getMaxHeartBuyNum() {
		return maxHeartBuyNum;
	}

	public void setMaxHeartBuyNum(int maxHeartBuyNum) {
		this.maxHeartBuyNum = maxHeartBuyNum;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return playerID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "playerID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		playerID = key.toString();
	}

}
