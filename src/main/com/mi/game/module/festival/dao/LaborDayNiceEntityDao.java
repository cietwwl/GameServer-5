package com.mi.game.module.festival.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.festival.pojo.LaborDayNiceEntity;

public class LaborDayNiceEntityDao extends AbstractBaseDAO<LaborDayNiceEntity> {

	private static LaborDayNiceEntityDao firecrackerEntityDao = new LaborDayNiceEntityDao();

	private LaborDayNiceEntityDao() {

	}

	public static LaborDayNiceEntityDao getInstance() {
		return firecrackerEntityDao;
	}

	public LaborDayNiceEntity getNewYearFirecrackerEntity(String playerID) {
		LaborDayNiceEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
