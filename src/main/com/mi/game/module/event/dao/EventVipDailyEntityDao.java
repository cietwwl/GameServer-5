package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventVipDailyEntity;

public class EventVipDailyEntityDao extends AbstractBaseDAO<EventVipDailyEntity> {

	private static EventVipDailyEntityDao eventVipDailyEntityDao = new EventVipDailyEntityDao();

	private EventVipDailyEntityDao() {

	}

	public static EventVipDailyEntityDao getInstance() {
		return eventVipDailyEntityDao;
	}

	public EventVipDailyEntity getVipDailyEntity(String playerID) {
		EventVipDailyEntity vipDailyEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		vipDailyEntity = this.query(queryInfo);
		return vipDailyEntity;
	}

}
