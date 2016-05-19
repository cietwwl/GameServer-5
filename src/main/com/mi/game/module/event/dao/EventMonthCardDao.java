package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventMonthCardEntity;

public class EventMonthCardDao extends AbstractBaseDAO<EventMonthCardEntity> {

	private static EventMonthCardDao eventMonthCardEntityDao = new EventMonthCardDao();

	private EventMonthCardDao() {

	}

	public static EventMonthCardDao getInstance() {
		return eventMonthCardEntityDao;
	}

	public EventMonthCardEntity getMonthCardEntity(String playerID) {
		EventMonthCardEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
