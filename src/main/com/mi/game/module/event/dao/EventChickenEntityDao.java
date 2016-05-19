package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventChickenEntity;

public class EventChickenEntityDao extends AbstractBaseDAO<EventChickenEntity> {

	private static final EventChickenEntityDao chickenDao = new EventChickenEntityDao();

	private EventChickenEntityDao() {

	}

	public static EventChickenEntityDao getInstance() {
		return chickenDao;
	}

	public EventChickenEntity getChickenEntity(String playerID) {
		EventChickenEntity chickenEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		chickenEntity = this.query(queryInfo);
		return chickenEntity;
	}

}
