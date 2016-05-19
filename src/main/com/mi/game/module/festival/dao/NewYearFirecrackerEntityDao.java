package com.mi.game.module.festival.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.festival.pojo.NewYearFirecrackerEntity;

public class NewYearFirecrackerEntityDao extends AbstractBaseDAO<NewYearFirecrackerEntity> {

	private static NewYearFirecrackerEntityDao firecrackerEntityDao = new NewYearFirecrackerEntityDao();

	private NewYearFirecrackerEntityDao() {

	}

	public static NewYearFirecrackerEntityDao getInstance() {
		return firecrackerEntityDao;
	}

	public NewYearFirecrackerEntity getNewYearFirecrackerEntity(String playerID) {
		NewYearFirecrackerEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
