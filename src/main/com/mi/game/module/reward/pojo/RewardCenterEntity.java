package com.mi.game.module.reward.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;
import com.mi.core.pojo.BaseEntity;
@Entity(noClassnameStored = true)
public class RewardCenterEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5955414194436721394L;
	@Indexed(value=IndexDirection.ASC, unique=true)
	private String playerID;
	private List<Reward> rewardList ;
	private long rewardID;
	
	@Override
	public Map<String,Object> responseMap(){
		Map<String,Object> data = new HashMap<>();
		data.put("rewardList", rewardList);
		return data;
	}

	public long getCount(){
		rewardID ++;
		return rewardID;
	}
	
	public long getRewardID() {
		return rewardID;
	}

	public void setRewardID(long rewardID) {
		this.rewardID = rewardID;
	}



	public List<Reward> getRewardList() {
		if(rewardList == null)
			rewardList = new ArrayList<>();
		return rewardList;
	}

	public void setRewardList(List<Reward> rewardList) {
		this.rewardList = rewardList;
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
