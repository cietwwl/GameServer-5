package com.mi.game.module.event.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventLuckyDrawEntity;

public class EventLuckyDrawEntityDao extends AbstractBaseDAO<EventLuckyDrawEntity> {

	private static EventLuckyDrawEntityDao eventLuckyDrawEntityDao = new EventLuckyDrawEntityDao();

	private EventLuckyDrawEntityDao() {

	}
	public static EventLuckyDrawEntityDao getInstance() {
		return eventLuckyDrawEntityDao;
	}

	public EventLuckyDrawEntity getLuckyDrawEntity(String playerID) {
		EventLuckyDrawEntity luckyDrawEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		luckyDrawEntity = this.query(queryInfo);
		return luckyDrawEntity;
	}
	
	public List<EventLuckyDrawEntity> getAllLuckyDrawEntity() {		
		QueryInfo queryInfo = new QueryInfo();		
		List<EventLuckyDrawEntity> list = this.cache.queryList(queryInfo, EventLuckyDrawEntity.class);
		return list;
	}

}
