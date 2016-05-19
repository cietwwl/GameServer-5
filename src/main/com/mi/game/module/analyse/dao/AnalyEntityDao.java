package com.mi.game.module.analyse.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.analyse.pojo.AnalyEntity;

public class AnalyEntityDao extends AbstractBaseDAO<AnalyEntity> {

	private static AnalyEntityDao analyDao = new AnalyEntityDao();

	private AnalyEntityDao() {

	}

	public static AnalyEntityDao getInstance() {
		return analyDao;
	}

	public AnalyEntity getAnalyEntity(String player_id) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("player_id", QueryType.EQUAL, player_id);
		AnalyEntity entity = this.query(queryInfo);
		return entity;
	}

	public List<AnalyEntity> getAllAnalyEntity() {
		QueryInfo queryInfo = new QueryInfo("last_login_time");
		return this.queryList(queryInfo);
	}

}
