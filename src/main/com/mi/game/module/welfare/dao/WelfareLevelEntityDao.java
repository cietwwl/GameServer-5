package com.mi.game.module.welfare.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.welfare.pojo.WelfareLevelEntity;

public class WelfareLevelEntityDao extends AbstractBaseDAO<WelfareLevelEntity> {

	private static WelfareLevelEntityDao eventLevelEntityDao = new WelfareLevelEntityDao();

	private WelfareLevelEntityDao() {

	}

	public static WelfareLevelEntityDao getInstance() {
		return eventLevelEntityDao;
	}

	public WelfareLevelEntity getLevelEntity(String playerID) {
		WelfareLevelEntity eventLevelEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		eventLevelEntity = this.query(queryInfo);
		return eventLevelEntity;
	}

}
