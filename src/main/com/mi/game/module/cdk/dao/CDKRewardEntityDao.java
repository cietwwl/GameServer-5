package com.mi.game.module.cdk.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.cdk.pojo.CDKRewardEntity;

public class CDKRewardEntityDao extends AbstractBaseDAO<CDKRewardEntity> {

	private static CDKRewardEntityDao entityDao = new CDKRewardEntityDao();

	private CDKRewardEntityDao() {

	}

	public static CDKRewardEntityDao getInstance() {
		return entityDao;
	}

	public List<CDKRewardEntity> getRewardEntitys() {
		QueryInfo queryInfo = new QueryInfo();
		return this.cache.queryList(queryInfo, CDKRewardEntity.class);
	}

}
