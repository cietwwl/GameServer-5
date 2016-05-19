package com.mi.game.module.store.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.store.pojo.StoreEntity;

public class StoreEntityDao extends AbstractBaseDAO<StoreEntity> {
	private static final StoreEntityDao storeDao = new StoreEntityDao();

	private StoreEntityDao() {
	}

	public static StoreEntityDao getInstance() {
		return storeDao;
	}

	public StoreEntity getStoreEntity(String playerID) {
		StoreEntity storeEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		storeEntity = this.query(queryInfo);
		return storeEntity;
	}
}
