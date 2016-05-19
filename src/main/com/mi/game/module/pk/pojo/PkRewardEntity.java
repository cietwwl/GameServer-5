package com.mi.game.module.pk.pojo;


import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;


/**
 * 比武玩家荣誉值
 * 
 * @author 赵鹏翔
 * @time Mar 27, 2015 12:35:03 PM
 */
@Entity(noClassnameStored = true)
public class PkRewardEntity extends BaseEntity {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7015375076128900225L;
	@Indexed(value = IndexDirection.ASC, unique = true)
	private String rewardID;
	private String playerID; // 玩家ID
	private int reward; // 当前荣誉值

	@Override
	public Object getKey() {
		return rewardID;
	}

	public String getPlayerID() {
		return playerID;
	}

	public void setPlayerID(String playerID) {
		this.playerID = playerID;
	}

	@Override
	public String getKeyName() {
		return "rewardID";
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	@Override
	public void setKey(Object key) {
		rewardID = key.toString();
	}

	public String getRewardID() {
		return rewardID;
	}

	public void setRewardID(String rewardID) {
		this.rewardID = rewardID;
	}


}
