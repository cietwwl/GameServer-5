package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.EventShopEntity;

public class EventShopEntityDao extends AbstractBaseDAO<EventShopEntity> {

	private static EventShopEntityDao eventShopEntityDao = new EventShopEntityDao();

	private EventShopEntityDao() {

	}

	public static EventShopEntityDao getInstance() {
		return eventShopEntityDao;
	}

	public EventShopEntity getShopEntity(String playerID) {
		EventShopEntity shopEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		shopEntity = this.query(queryInfo);
		return shopEntity;
	}

}
