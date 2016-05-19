package com.mi.game.module.lead.protocol;

import java.util.List;

import com.mi.game.module.reward.data.GoodsBean;

/**
 * @author 刘凯旋	
 *
 * 2014年9月22日 下午5:30:23
 */
public class ExpResponse {
	private int level;
	private int exp;
	private boolean levelUp;
	private List<GoodsBean> LevelUpList;
	
	public List<GoodsBean> getLevelUpList() {
		return LevelUpList;
	}
	public void setLevelUpList(List<GoodsBean> levelUpList) {
		LevelUpList = levelUpList;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public boolean isLevelUp() {
		return levelUp;
	}
	public void setLevelUp(boolean levelUp) {
		this.levelUp = levelUp;
	}
	
}
