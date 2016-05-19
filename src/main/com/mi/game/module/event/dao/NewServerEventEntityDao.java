package com.mi.game.module.event.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.NewServerEventEntity;

public class NewServerEventEntityDao extends AbstractBaseDAO<NewServerEventEntity> {

	private static final NewServerEventEntityDao newServerDao = new NewServerEventEntityDao();

	private NewServerEventEntityDao() {

	}

	public static NewServerEventEntityDao getInstance() {
		return newServerDao;
	}

	public NewServerEventEntity getNewServerEventEntity(String pid) {
		NewServerEventEntity entity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("pid", pid);
		entity = this.query(queryInfo);
		return entity;
	}

}
