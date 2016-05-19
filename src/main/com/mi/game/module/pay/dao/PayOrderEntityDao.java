package com.mi.game.module.pay.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.pay.pojo.PayOrderEntity;

public class PayOrderEntityDao extends AbstractBaseDAO<PayOrderEntity> {

	private static final PayOrderEntityDao payOrderDao = new PayOrderEntityDao();

	private PayOrderEntityDao() {
	}

	public static PayOrderEntityDao getInstance() {
		return payOrderDao;
	}

	public PayOrderEntity getPayOrderEntity(String orderID) {
		PayOrderEntity payOrderEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("orderID", QueryType.EQUAL, orderID);
		payOrderEntity = this.query(queryInfo);
		return payOrderEntity;
	}

}
