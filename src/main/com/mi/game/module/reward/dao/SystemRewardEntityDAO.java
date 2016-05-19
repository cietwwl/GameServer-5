package com.mi.game.module.reward.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.reward.pojo.SystemRewardEntity;

public class SystemRewardEntityDAO extends AbstractBaseDAO<SystemRewardEntity> {
	private static final SystemRewardEntityDAO systemRewardEntityDAO = new SystemRewardEntityDAO();

	private SystemRewardEntityDAO() {
	}

	public static SystemRewardEntityDAO getInstance() {
		return systemRewardEntityDAO;
	}

	public SystemRewardEntity getsSystemRewardEntityByKey(String rewardKey) {
		QueryInfo queryInfo = new QueryInfo();
		QueryBean queryBean = new QueryBean("rewardKey", QueryType.EQUAL, rewardKey);
		queryInfo.addQueryBean(queryBean);
		return this.query(queryInfo);
	}

	public List<SystemRewardEntity> getSystemRewardEntityList() {
		QueryInfo queryInfo = new QueryInfo();
		return this.cache.queryList(queryInfo, SystemRewardEntity.class);
	}
}
