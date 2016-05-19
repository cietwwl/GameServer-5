package com.mi.game.module.event.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventPayEntity;

public class EventPayEntityDao extends AbstractBaseDAO<EventPayEntity> {

	private static EventPayEntityDao eventPayEntityDao = new EventPayEntityDao();

	private EventPayEntityDao() {

	}

	public static EventPayEntityDao getInstance() {
		return eventPayEntityDao;
	}

	public EventPayEntity getEventPayEntity(String playerID) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		return this.query(queryInfo);
	}

	public List<EventPayEntity> getPayRank() {
		QueryInfo queryInfo = new QueryInfo(1, 1000, "-payTotal");
		return queryPage(queryInfo);
	}

}
