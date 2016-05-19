package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventVipGrowEntity;

public class EventVipGrowEntityDao extends AbstractBaseDAO<EventVipGrowEntity> {

	private static EventVipGrowEntityDao eventVipGrowEntityDao = new EventVipGrowEntityDao();

	private EventVipGrowEntityDao() {

	}

	public static EventVipGrowEntityDao getInstance() {
		return eventVipGrowEntityDao;
	}

	public EventVipGrowEntity getVipGrowEntity(String playerID) {
		EventVipGrowEntity vipGrowEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		vipGrowEntity = this.query(queryInfo);
		return vipGrowEntity;
	}

}
