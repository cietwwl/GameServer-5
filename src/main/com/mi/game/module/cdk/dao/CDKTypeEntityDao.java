package com.mi.game.module.cdk.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.cdk.pojo.CDKTypeEntity;

public class CDKTypeEntityDao extends AbstractBaseDAO<CDKTypeEntity> {

	private static CDKTypeEntityDao entityDao = new CDKTypeEntityDao();

	private CDKTypeEntityDao() {

	}

	public static CDKTypeEntityDao getInstance() {
		return entityDao;
	}

	public List<CDKTypeEntity> getTypeEntitys() {
		QueryInfo queryInfo = new QueryInfo();
		return this.cache.queryList(queryInfo, CDKTypeEntity.class);
	}

}
