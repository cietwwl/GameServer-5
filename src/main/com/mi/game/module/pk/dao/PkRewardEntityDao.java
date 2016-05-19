package com.mi.game.module.pk.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.pk.pojo.PkRewardEntity;

public class PkRewardEntityDao extends AbstractBaseDAO<PkRewardEntity> {
	private static PkRewardEntityDao entityDao = new PkRewardEntityDao();

	private PkRewardEntityDao() {

	}

	public static PkRewardEntityDao getInstance() {
		return entityDao;
	}

	public PkRewardEntity getRewardEntity(String playerID) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		return query(queryInfo);
	}

}
