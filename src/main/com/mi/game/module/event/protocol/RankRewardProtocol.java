package com.mi.game.module.event.protocol;

import java.util.List;

import com.mi.core.protocol.BaseProtocol;
import com.mi.game.module.arena.data.RankRewardData;

/**
 * 竞技排行
 * 
 * @author 赵鹏翔
 * @time Apr 11, 2015 4:21:26 PM
 */
public class RankRewardProtocol extends BaseProtocol {
	private List<RankRewardData> rewardList; // 排行奖励模版

	public List<RankRewardData> getRewardList() {
		return rewardList;
	}

	public void setRewardList(List<RankRewardData> rewardList) {
		this.rewardList = rewardList;
	}
}
