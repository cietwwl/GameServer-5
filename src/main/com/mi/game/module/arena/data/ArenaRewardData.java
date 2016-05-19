package com.mi.game.module.arena.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;
@XmlTemplate(template = {"com/mi/template/ArenaRewardPrototype.xml"})
public class ArenaRewardData extends BaseTemplate{
	private long rank;
	private int coinReward;
	private int reputationReward;
	
	public long getRank() {
		return rank;
	}
	public void setRank(long rank) {
		this.rank = rank;
	}
	public int getCoinReward() {
		return coinReward;
	}
	public void setCoinReward(int coinReward) {
		this.coinReward = coinReward;
	}
	public int getReputationReward() {
		return reputationReward;
	}
	public void setReputationReward(int reputationReward) {
		this.reputationReward = reputationReward;
	}
}
