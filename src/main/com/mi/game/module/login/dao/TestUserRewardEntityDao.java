package com.mi.game.module.login.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.login.pojo.TestUserRewardEntity;

public class TestUserRewardEntityDao extends AbstractBaseDAO<TestUserRewardEntity> {
	private static TestUserRewardEntityDao minuEntityDao = new TestUserRewardEntityDao();

	private TestUserRewardEntityDao() {

	}

	public static TestUserRewardEntityDao getInstance() {
		return minuEntityDao;
	}

	public TestUserRewardEntity getTestUserRewardEntity(String uid) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("uid", QueryType.EQUAL, uid);
		TestUserRewardEntity entity = this.query(queryInfo);
		return entity;
	}

}
