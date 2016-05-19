package com.mi.game.module.vip.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.vip.pojo.VipEntity;

public class VipEnitiyDao extends AbstractBaseDAO<VipEntity> {

	private static final VipEnitiyDao vipDao = new VipEnitiyDao();

	private VipEnitiyDao() {

	}

	public static VipEnitiyDao getInstance() {
		return vipDao;
	}

	public VipEntity getVipEntity(String playerID) {
		VipEntity vipEntity = null;
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		vipEntity = this.query(queryInfo);
		return vipEntity;
	}
}
