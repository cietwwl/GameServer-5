package com.mi.game.module.festival.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.festival.pojo.NewYearPayMoreEntity;

public class NewYearPayMoreEntityDao extends AbstractBaseDAO<NewYearPayMoreEntity> {

	private static NewYearPayMoreEntityDao payMoreEntityDao = new NewYearPayMoreEntityDao();

	private NewYearPayMoreEntityDao() {

	}

	public static NewYearPayMoreEntityDao getInstance() {
		return payMoreEntityDao;
	}

	public NewYearPayMoreEntity getNewYearPayMoreEntity(String playerID) {
		NewYearPayMoreEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
