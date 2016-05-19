package com.mi.game.module.arena.data;

import com.mi.core.engine.annotation.XmlTemplate;
import com.mi.core.template.BaseTemplate;

/**
 * 竞技排行奖励
 * 
 * @author 赵鹏翔
 * @time Apr 11, 2015 3:59:16 PM
 */
@XmlTemplate(template = { "com/mi/template/ActiveRankRewardPrototype.xml" })
public class RankRewardData extends BaseTemplate {
	private int pid;
	private String name;
	private String itemID; // itemID="10174=100,10171=10,10177=10" 格式
	private long lowRank; // lowRank="2" highRank="5" 表示2-5名奖励
	private long highRank;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public long getLowRank() {
		return lowRank;
	}

	public void setLowRank(long lowRank) {
		this.lowRank = lowRank;
	}

	public long getHighRank() {
		return highRank;
	}

	public void setHighRank(long highRank) {
		this.highRank = highRank;
	}

}
