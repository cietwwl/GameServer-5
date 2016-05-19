package com.mi.game.module.worldBoss.pojo;

import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class WorldBossEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8472781533436405951L;
	@Indexed(value=IndexDirection.ASC, unique=true)
	private String bossID;
	private long bossHp;
	private long startTime;
	private long endTime;
	private int level;
	private int state;  //0未开启1正在攻打2结算中
	private List<String> topNameList;
	private String killName;
	private int bossLevelID;
	private String killID;
	private boolean settle;       //是否结算
	private long settleTime ; 
	
	
	public long getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(long settleTime) {
		this.settleTime = settleTime;
	}

	public boolean isSettle() {
		return settle;
	}
	
	public void setSettle(boolean settle) {
		this.settle = settle;
	}
	
	public String getKillID() {
		return killID;
	}
	
	public void setKillID(String killID) {
		this.killID = killID;
	}
	
	public int getBossLevelID() {
		return bossLevelID;
	}

	public void setBossLevelID(int bossLevelID) {
		this.bossLevelID = bossLevelID;
	}

	public List<String> getTopNameList() {
		return topNameList;
	}

	public void setTopNameList(List<String> topNameList) {
		this.topNameList = topNameList;
	}

	public String getKillName() {
		return killName;
	}

	public void setKillName(String killName) {
		this.killName = killName;
	}


	public long getBossHp() {
		return bossHp;
	}

	public void setBossHp(long bossHp) {
		this.bossHp = bossHp;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public Object getKey() {
		// TODO 自动生成的方法存根
		return bossID;
	}

	@Override
	public String getKeyName() {
		// TODO 自动生成的方法存根
		return "bossID";
	}

	@Override
	public void setKey(Object key) {
		// TODO 自动生成的方法存根
		bossID = key.toString();
	}

}
