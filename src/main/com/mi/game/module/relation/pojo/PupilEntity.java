package com.mi.game.module.relation.pojo;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.relation.define.RelationConstants;

public class PupilEntity extends BaseEntity {

	private static final long serialVersionUID = -2084163542142309289L;

	private String playerID;
	// 拜师开关
	private boolean isOpen;
	// 师傅id
	private String masterID;
	// 礼包领取
	private boolean isReward;
	// 首冲返利
	private boolean isPay;
	// 申请列表
	private List<String> applys = new ArrayList<String>();
	// 惩罚开始时间
	private long lastTime;
	// 创建时间
	private String dateTime;

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getMasterID() {
		return masterID;
	}

	public void setMasterID(String masterID) {
		this.masterID = masterID;
	}

	public boolean isReward() {
		return isReward;
	}

	public void setReward(boolean isReward) {
		this.isReward = isReward;
	}

	public boolean isPay() {
		return isPay;
	}

	public void setPay(boolean isPay) {
		this.isPay = isPay;
	}

	public List<String> getApplys() {
		return applys;
	}

	public void setApplys(List<String> applys) {
		this.applys = applys;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * 是否有师傅
	 * 
	 * @return
	 */
	public boolean isHasMaster() {
		if (masterID == null || masterID.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * 拜师冷却时间
	 * 
	 * @return
	 */
	public long getCoolingTime() {
		long coolingTime = RelationConstants.RELATION_COOLING_TIME * 3600 * 1000;
		coolingTime -= System.currentTimeMillis() - coolingTime;
		coolingTime = coolingTime > 0 ? coolingTime : 0;
		return coolingTime;
	}

	/**
	 * 申请列表是否已满
	 * 
	 * @return
	 */
	public boolean applyIsFull() {
		if (applys.size() == RelationConstants.RELATION_APPLY_MAXNUM) {
			return true;
		}
		return false;
	}

	/**
	 * 申请列表是否已存在
	 * 
	 * @param pupilID
	 * @return
	 */
	public boolean isHasApply(String masterID) {
		return applys.contains(masterID);
	}

	/**
	 * 申请列表增加
	 * 
	 * @param pupilID
	 */
	public void addApplys(String masterID) {
		if (!isHasApply(masterID)) {
			applys.add(masterID);
		}
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
