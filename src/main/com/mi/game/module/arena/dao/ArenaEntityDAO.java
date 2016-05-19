package com.mi.game.module.arena.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.arena.pojo.ArenaEntity;

public class ArenaEntityDAO extends AbstractBaseDAO<ArenaEntity>{
	private final static ArenaEntityDAO ARENA_ENTITY_DAO = new ArenaEntityDAO();
	private ArenaEntityDAO(){}
	public static ArenaEntityDAO getInstance(){
		return ARENA_ENTITY_DAO;
	} 
	
	public long getCount(){
		QueryInfo queryInfo = new QueryInfo();
		return this.queryCount(queryInfo);
	}
	
	public ArenaEntity getEntityByRank(long rank){
		QueryInfo queryInfo = new QueryInfo();
		QueryBean queryBean = new QueryBean("rank", QueryType.EQUAL, rank);
		queryInfo.addQueryBean(queryBean);
		ArenaEntity entity = this.query(queryInfo);
		return entity;
	}
	
	public List<ArenaEntity> getTopList(){
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("rank",QueryType.LESSTHAN, 11);
		List<ArenaEntity> list = this.queryList(queryInfo);
		return list;
	}
	
	public long getMaxRank(){
		QueryInfo queryInfo = new QueryInfo(1,1,"-rank");
		List<ArenaEntity> list = this.queryList(queryInfo);
		ArenaEntity arenaEntity = list.get(0);
		if(arenaEntity != null){
			return arenaEntity.getRank();
		}
		return 0;
	}

	/**
	 * 查询排行榜前200名
	 * 
	 * @return
	 */
	public List<ArenaEntity> getTopTwoHundredList() {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addQueryCondition("rank", QueryType.LESSTHAN, 201);
		queryInfo.setOrder("rank");
		List<ArenaEntity> list = this.queryList(queryInfo);
		return list;
	}

}
