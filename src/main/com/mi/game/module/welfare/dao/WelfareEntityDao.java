package com.mi.game.module.welfare.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.welfare.pojo.WelfareEntity;

public class WelfareEntityDao extends AbstractBaseDAO<WelfareEntity> {

	private static WelfareEntityDao eventSignEntityDao = new WelfareEntityDao();

	private WelfareEntityDao() {

	}

	public static WelfareEntityDao getInstance() {
		return eventSignEntityDao;
	}

	public WelfareEntity getWelfareEntity(String playerID) {
		WelfareEntity welfareEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		welfareEntity = this.query(queryInfo);
		return welfareEntity;
	}

}
