package com.mi.game.module.reward.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.reward.pojo.RewardCenterEntity;

public class RewardDAO extends AbstractBaseDAO<RewardCenterEntity>{

	private final static RewardDAO REWARD_DAO = new RewardDAO();
	private RewardDAO(){}
	public static  RewardDAO getInstance(){
		return REWARD_DAO;
	}
}
