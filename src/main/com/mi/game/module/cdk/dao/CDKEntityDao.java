package com.mi.game.module.cdk.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.cdk.pojo.CDKEntity;

public class CDKEntityDao extends AbstractBaseDAO<CDKEntity> {

	private static CDKEntityDao entityDao = new CDKEntityDao();

	private CDKEntityDao() {

	}

	public static CDKEntityDao getInstance() {
		return entityDao;
	}

	/**
	 * 根据cdk获取cdk实体
	 * @param cdk
	 * @return
	 */
	public CDKEntity getCdkEntityByCdk(String cdk) {
		QueryInfo query = new QueryInfo();
		query.addQueryCondition("cdk", cdk);
		return query(query);
	}

}
