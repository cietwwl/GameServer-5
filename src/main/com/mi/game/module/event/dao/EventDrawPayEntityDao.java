package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventDrawPayEntity;

public class EventDrawPayEntityDao extends AbstractBaseDAO<EventDrawPayEntity> {

	private static EventDrawPayEntityDao eventDrawEntityDao = new EventDrawPayEntityDao();

	private EventDrawPayEntityDao() {

	}

	public static EventDrawPayEntityDao getInstance() {
		return eventDrawEntityDao;
	}

	public EventDrawPayEntity getDrawPayEntity(String playerID) {
		EventDrawPayEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
