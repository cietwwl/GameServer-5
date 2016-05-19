package com.mi.game.module.welfare.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.welfare.pojo.WelfareLoginEntity;

public class WelfareLoginEntityDao extends AbstractBaseDAO<WelfareLoginEntity> {

	private static WelfareLoginEntityDao eventSignEntityDao = new WelfareLoginEntityDao();

	private WelfareLoginEntityDao() {

	}

	public static WelfareLoginEntityDao getInstance() {
		return eventSignEntityDao;
	}

	public WelfareLoginEntity getLoginEntity(String playerID) {
		WelfareLoginEntity eventLoginEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		eventLoginEntity = this.query(queryInfo);
		return eventLoginEntity;
	}

}
