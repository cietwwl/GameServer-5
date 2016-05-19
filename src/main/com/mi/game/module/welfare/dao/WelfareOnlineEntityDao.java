package com.mi.game.module.welfare.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.welfare.pojo.WelfareOnlineEntity;

public class WelfareOnlineEntityDao extends AbstractBaseDAO<WelfareOnlineEntity> {

	private static WelfareOnlineEntityDao eventOnlineEntityDao = new WelfareOnlineEntityDao();

	private WelfareOnlineEntityDao() {

	}

	public static WelfareOnlineEntityDao getInstance() {
		return eventOnlineEntityDao;
	}

	public WelfareOnlineEntity getOnlineEntity(String playerID) {
		WelfareOnlineEntity eventOnlineEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		eventOnlineEntity = this.query(queryInfo);
		return eventOnlineEntity;
	}

}
