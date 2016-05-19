package com.mi.game.module.relation.pojo;

import com.mi.core.pojo.BaseEntity;

public class RelationEntity extends BaseEntity {

	private static final long serialVersionUID = 2238475916372297640L;

	// 关系id
	private String relationID;
	// 师傅id
	private String masterID;
	// 徒弟id
	private String pupilID;
	// 返利额
	private int rewardGold;
	// 返利时间
	private long dateTime;

	public String getRelationID() {
		return relationID;
	}

	public void setRelationID(String relationID) {
		this.relationID = relationID;
	}

	public String getMasterID() {
		return masterID;
	}

	public void setMasterID(String masterID) {
		this.masterID = masterID;
	}

	public String getPupilID() {
		return pupilID;
	}

	public void setPupilID(String pupilID) {
		this.pupilID = pupilID;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public int getRewardGold() {
		return rewardGold;
	}

	public void setRewardGold(int rewardGold) {
		this.rewardGold = rewardGold;
	}

	@Override
	public Object getKey() {
		return relationID;
	}

	@Override
	public String getKeyName() {
		return relationID;
	}

	@Override
	public void setKey(Object key) {
		this.relationID = key.toString();
	}

}
