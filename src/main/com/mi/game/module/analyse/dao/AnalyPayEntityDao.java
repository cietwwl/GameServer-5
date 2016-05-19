package com.mi.game.module.analyse.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.analyse.pojo.AnalyPayEntity;

public class AnalyPayEntityDao extends AbstractBaseDAO<AnalyPayEntity> {

	private static AnalyPayEntityDao analyPayDao = new AnalyPayEntityDao();

	private AnalyPayEntityDao() {

	}

	public static AnalyPayEntityDao getInstance() {
		return analyPayDao;
	}

	public AnalyPayEntity getAnalyPayEntity(String payno) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("payno", payno);
		AnalyPayEntity entity = this.query(queryInfo);
		return entity;
	}

	public List<AnalyPayEntity> getAllAnalyPayEntity() {
		QueryInfo queryInfo = new QueryInfo("paytime");
		return this.queryList(queryInfo);
	}

}
