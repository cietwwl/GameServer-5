package com.mi.game.module.pay.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.pay.pojo.PayEntity;

public class PayEntityDao extends AbstractBaseDAO<PayEntity> {
	private static final PayEntityDao payDao = new PayEntityDao();

	private PayEntityDao() {
	}

	public static PayEntityDao getInstance() {
		return payDao;
	}

	public PayEntity getPayEntity(String playerID) {
		PayEntity payEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		payEntity = this.query(queryInfo);
		return payEntity;
	}
	
	public long getPayEntityCount(){
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("payTotal", QueryType.GREATERTHAN, 0);
		long total = cache.queryCount(queryInfo, PayEntity.class);
		return total;
	}
}
