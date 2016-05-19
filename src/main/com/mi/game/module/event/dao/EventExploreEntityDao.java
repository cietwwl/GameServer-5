package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventExploreEntity;

public class EventExploreEntityDao extends AbstractBaseDAO<EventExploreEntity> {

	private static final EventExploreEntityDao exploreDao = new EventExploreEntityDao();

	private EventExploreEntityDao() {

	}

	public static EventExploreEntityDao getInstance() {
		return exploreDao;
	}

	public EventExploreEntity getExploreEntity(String playerID) {
		EventExploreEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
