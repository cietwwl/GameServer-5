package com.mi.game.module.relation.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.relation.pojo.PupilEntity;

public class PupilEntityDao extends AbstractBaseDAO<PupilEntity> {
	private static final PupilEntityDao pupilDao = new PupilEntityDao();

	private PupilEntityDao() {
	}

	public static PupilEntityDao getInstance() {
		return pupilDao;
	}

	public PupilEntity getPupilEntity(String playerID) {
		PupilEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		entity = this.query(queryInfo);
		return entity;
	}
}
