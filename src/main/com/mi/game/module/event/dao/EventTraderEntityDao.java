package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventTraderEntity;

public class EventTraderEntityDao extends AbstractBaseDAO<EventTraderEntity> {

	private static EventTraderEntityDao eventTraderEntityDao = new EventTraderEntityDao();

	private EventTraderEntityDao() {

	}

	public static EventTraderEntityDao getInstance() {
		return eventTraderEntityDao;
	}

	public EventTraderEntity getTraderEntity(String playerID) {
		EventTraderEntity traderEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		traderEntity = this.query(queryInfo);
		return traderEntity;
	}

}
