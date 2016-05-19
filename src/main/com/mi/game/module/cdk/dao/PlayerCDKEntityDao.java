package com.mi.game.module.cdk.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.cdk.pojo.PlayerCDKEntity;

public class PlayerCDKEntityDao extends AbstractBaseDAO<PlayerCDKEntity> {

	private static PlayerCDKEntityDao entityDao = new PlayerCDKEntityDao();

	private PlayerCDKEntityDao() {

	}

	public static PlayerCDKEntityDao getInstance() {
		return entityDao;
	}

	public PlayerCDKEntity getPlayerCDKEntity(String playerID) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("playerID", QueryType.EQUAL, playerID);
		PlayerCDKEntity entity = this.query(queryInfo);
		return entity;
	}

}
