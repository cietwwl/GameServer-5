package com.mi.game.module.worldBoss.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.worldBoss.pojo.PlayerBossEntity;
import com.mi.game.module.worldBoss.pojo.WorldBossEntity;

public class WorldBossEntityDAO extends AbstractBaseDAO<WorldBossEntity>{
	private final static WorldBossEntityDAO WORLD_BOSS_ENTITY_DAO = new WorldBossEntityDAO();
	private WorldBossEntityDAO(){}
	public static WorldBossEntityDAO getInstance(){
		return WORLD_BOSS_ENTITY_DAO;
	}
	public List<PlayerBossEntity> getTopTenList( ){
		QueryInfo queryInfo = new QueryInfo("-damage");
		queryInfo.addQueryCondition("damage", QueryType.GREATERTHAN,0);
		queryInfo.setSize(10);
		List<PlayerBossEntity> heroList = cache.queryPage(queryInfo, PlayerBossEntity.class);
		return heroList;
	}
	
	public long getWorldBossPlayerTotal(){
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("damage", QueryType.GREATERTHAN, 0);
		long total = cache.queryCount(queryInfo,PlayerBossEntity.class);
		return total;
	}
}
