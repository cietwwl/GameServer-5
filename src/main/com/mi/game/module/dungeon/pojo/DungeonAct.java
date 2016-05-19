package com.mi.game.module.dungeon.pojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DungeonAct {
	private int templateID;                                 //大关的ID
	private Map<Integer,ActReward> rewardlist;           //是否领取奖励\
	private int actPoint;
	private boolean pass;
	
	public Map<String ,Object> responseMap(){
		Map<String,Object> data = new HashMap<>();
		data.put("templateID", templateID);
		Collection<ActReward> collections = new ArrayList<ActReward>();
		for(ActReward actRward : rewardlist.values()){
			collections.add(actRward);
		}
		data.put("rewardState", collections);
		data.put("actPoint", actPoint);
		return data;
	}
	
	
	
	public boolean isPass() {
		return pass;
	}
	public void setPass(boolean pass) {
		this.pass = pass;
	}
	public int getActPoint() {
		return actPoint;
	}
	public void setActPoint(int actPoint) {
		this.actPoint = actPoint;
	}
	public int getTemplateID() {
		return templateID;
	}
	public void setTemplateID(int templateID) {
		this.templateID = templateID;
	}
	
	public Map<Integer, ActReward> getRewardlist() {
		if(rewardlist == null){
			rewardlist = new HashMap<Integer, ActReward>();
		}
		return rewardlist;
	}
	public void setRewardlist(Map<Integer, ActReward> rewardlist) {
		this.rewardlist = rewardlist;
	}

	
}
