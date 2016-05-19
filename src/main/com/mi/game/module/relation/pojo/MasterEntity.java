package com.mi.game.module.relation.pojo;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.pojo.BaseEntity;
import com.mi.game.module.relation.define.RelationConstants;

public class MasterEntity extends BaseEntity {

	private static final long serialVersionUID = 8403357027719463659L;

	private String playerID;
	// 收徒开关
	private boolean isOpen;
	// 徒弟列表
	private List<String> pupils = new ArrayList<String>();
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

	public List<String> getPupils() {
		return pupils;
	}

	public void setPupils(List<String> pupils) {
		this.pupils = pupils;
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
	 * 获取收徒冷却时间
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
	 * 申请拜师列表是否已满
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
	public boolean isHasApply(String pupilID) {
		return applys.contains(pupilID);
	}

	/**
	 * 申请列表增加
	 * 
	 * @param pupilID
	 */
	public void addApplys(String pupilID) {
		if (!isHasApply(pupilID)) {
			applys.add(pupilID);
		}
	}

	/**
	 * 徒弟列表是否已满
	 * 
	 * @return
	 */
	public boolean pupilIsFull() {
		if (pupils.size() == 100) {
			return true;
		}
		return false;
	}

	/**
	 * 徒弟是否已存在
	 * 
	 * @param pupilID
	 * @return
	 */
	public boolean isHasPupil(String pupilID) {
		return pupils.contains(pupilID);
	}

	/**
	 * 增加徒弟
	 * 
	 * @param pupilID
	 */
	public void addPupil(String pupilID) {
		if (!isHasPupil(pupilID)) {
			pupils.add(pupilID);
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
