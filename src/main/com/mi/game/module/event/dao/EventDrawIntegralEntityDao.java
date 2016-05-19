package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventDrawIntegralEntity;

public class EventDrawIntegralEntityDao extends AbstractBaseDAO<EventDrawIntegralEntity> {

	private static EventDrawIntegralEntityDao drawIntegralEntityDao = new EventDrawIntegralEntityDao();

	private EventDrawIntegralEntityDao() {

	}

	public static EventDrawIntegralEntityDao getInstance() {
		return drawIntegralEntityDao;
	}

	public EventDrawIntegralEntity getDrawIntegralEntity(String playerID) {
		EventDrawIntegralEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
