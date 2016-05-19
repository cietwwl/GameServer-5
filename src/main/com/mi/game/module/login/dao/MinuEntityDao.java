package com.mi.game.module.login.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.login.pojo.MinuEntity;

public class MinuEntityDao extends AbstractBaseDAO<MinuEntity> {
	private static MinuEntityDao minuEntityDao = new MinuEntityDao();

	private MinuEntityDao() {

	}

	public static MinuEntityDao getInstance() {
		return minuEntityDao;
	}

	public MinuEntity getMinuEntity(String minu_uid) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("minu_uid", QueryType.EQUAL, minu_uid);
		MinuEntity entity = this.query(queryInfo);
		return entity;
	}

}
