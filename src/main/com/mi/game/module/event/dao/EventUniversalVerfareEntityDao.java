package com.mi.game.module.event.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventUniversalVerfareEntity;

public class EventUniversalVerfareEntityDao extends AbstractBaseDAO<EventUniversalVerfareEntity> {

	private static EventUniversalVerfareEntityDao eventUniversalVerfareEntityDao = new EventUniversalVerfareEntityDao();

	private EventUniversalVerfareEntityDao() {

	}

	public static EventUniversalVerfareEntityDao getInstance() {
		return eventUniversalVerfareEntityDao;
	}

	public EventUniversalVerfareEntity getUniversalVerfareEntity(String playerID) {
		EventUniversalVerfareEntity universalVerfareEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		universalVerfareEntity = this.query(queryInfo);
		return universalVerfareEntity;
	}
	
	public List<EventUniversalVerfareEntity> getAllUniversalVerfareEntity() {		
		QueryInfo queryInfo = new QueryInfo();		
		List<EventUniversalVerfareEntity> list = this.cache.queryList(queryInfo, EventUniversalVerfareEntity.class);
		return list;
	}

}
