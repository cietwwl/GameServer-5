package com.mi.game.module.event.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventFortunaGiftsEntity;

public class EventFortunaGiftsEntityDao extends AbstractBaseDAO<EventFortunaGiftsEntity> {

	private static EventFortunaGiftsEntityDao eventFortunaGiftsEntityDao = new EventFortunaGiftsEntityDao();

	private EventFortunaGiftsEntityDao() {

	}

	public static EventFortunaGiftsEntityDao getInstance() {
		return eventFortunaGiftsEntityDao;
	}

	public EventFortunaGiftsEntity getFortunaGiftsEntity(String playerID) {
		EventFortunaGiftsEntity fortunaGiftsEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		fortunaGiftsEntity = this.query(queryInfo);
		return fortunaGiftsEntity;
	}
	
	public List<EventFortunaGiftsEntity> getAllFortunaGiftsEntity() {		
		QueryInfo queryInfo = new QueryInfo();		
		List<EventFortunaGiftsEntity> list = this.cache.queryList(queryInfo, EventFortunaGiftsEntity.class);
		return list;
	}

}
