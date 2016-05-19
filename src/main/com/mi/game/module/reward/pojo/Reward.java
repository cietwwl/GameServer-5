package com.mi.game.module.reward.pojo;

import java.util.List;

import com.mi.game.module.reward.data.GoodsBean;

public class Reward {
	private long time;
	private List<GoodsBean> goodsList;
	private int type;
	private long rewardID;
	
	public long getRewardID() {
		return rewardID;
	}
	public void setRewardID(long rewardID) {
		this.rewardID = rewardID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public List<GoodsBean> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsBean> goodsList) {
		this.goodsList = goodsList;
	}

	
	
}
