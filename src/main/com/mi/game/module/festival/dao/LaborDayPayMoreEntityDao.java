package com.mi.game.module.festival.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.festival.pojo.LaborDayPayMoreEntity;

public class LaborDayPayMoreEntityDao extends AbstractBaseDAO<LaborDayPayMoreEntity> {

	private static LaborDayPayMoreEntityDao payMoreEntityDao = new LaborDayPayMoreEntityDao();

	private LaborDayPayMoreEntityDao() {

	}

	public static LaborDayPayMoreEntityDao getInstance() {
		return payMoreEntityDao;
	}

	public LaborDayPayMoreEntity getNewYearPayMoreEntity(String playerID) {
		LaborDayPayMoreEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
