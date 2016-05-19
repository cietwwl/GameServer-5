package com.mi.game.module.dungeon.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.dungeon.pojo.ActLimitRewardMapEntity;

public class ActLimitRewardMapDAO extends AbstractBaseDAO<ActLimitRewardMapEntity>{
	private final static ActLimitRewardMapDAO ACT_LIMIT_REWARD_MAP_DAO = new ActLimitRewardMapDAO();
	private ActLimitRewardMapDAO(){}
	public static ActLimitRewardMapDAO getInstance(){
		return ACT_LIMIT_REWARD_MAP_DAO;
	}
}
