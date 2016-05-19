package com.mi.game.module.welfare.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.welfare.pojo.WelfareMonthEntity;

public class WelfareMonthEntityDao extends AbstractBaseDAO<WelfareMonthEntity> {

	private static WelfareMonthEntityDao eventSignEntityDao = new WelfareMonthEntityDao();

	private WelfareMonthEntityDao() {

	}

	public static WelfareMonthEntityDao getInstance() {
		return eventSignEntityDao;
	}

	public WelfareMonthEntity getMonthEntity(String playerID) {
		WelfareMonthEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", playerID);
		entity = this.query(queryInfo);
		return entity;
	}

}
