package com.mi.game.module.relation.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.relation.pojo.MasterEntity;

public class MasterEntityDao extends AbstractBaseDAO<MasterEntity> {

	private static final MasterEntityDao masterDao = new MasterEntityDao();

	private MasterEntityDao() {
	}

	public static MasterEntityDao getInstance() {
		return masterDao;
	}

	public MasterEntity getMasterEntity(String playerID) {
		MasterEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
