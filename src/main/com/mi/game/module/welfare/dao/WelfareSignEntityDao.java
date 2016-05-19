package com.mi.game.module.welfare.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.welfare.pojo.WelfareSignEntity;

public class WelfareSignEntityDao extends AbstractBaseDAO<WelfareSignEntity> {

	private static WelfareSignEntityDao eventSignEntityDao = new WelfareSignEntityDao();

	private WelfareSignEntityDao() {

	}

	public static WelfareSignEntityDao getInstance() {
		return eventSignEntityDao;
	}

	public WelfareSignEntity getSignEntity(String playerID) {
		WelfareSignEntity eventSignEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		eventSignEntity = this.query(queryInfo);
		return eventSignEntity;
	}

}
