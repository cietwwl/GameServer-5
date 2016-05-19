package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventExchangeEntity;

public class EventExchangeEntityDao extends AbstractBaseDAO<EventExchangeEntity> {

	private static EventExchangeEntityDao exchangeEntityDao = new EventExchangeEntityDao();

	private EventExchangeEntityDao() {

	}

	public static EventExchangeEntityDao getInstance() {
		return exchangeEntityDao;
	}

	public EventExchangeEntity getExchangeEntity(String playerID) {
		EventExchangeEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
