package com.mi.game.module.talisman.dao;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.talisman.pojo.TalismanShard;

public class TalismanShardDAO extends AbstractBaseDAO<TalismanShard>{
	private static final TalismanShardDAO TALISMAN_SHARD1DAO = new TalismanShardDAO();
	private TalismanShardDAO(){}
	
	public static TalismanShardDAO getInstance(){
		return TALISMAN_SHARD1DAO;
	}
	
	public List<TalismanShard> getTalismanShardList(String playerID){
		QueryInfo queryInfo = new QueryInfo();
		QueryBean queryBean = new QueryBean("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryBean(queryBean);
		List<TalismanShard> list = this.queryList(queryInfo);
		return list;
	}
	
	public TalismanShard getTalismanShard(String playerID, int shardID){
		QueryInfo queryInfo = new QueryInfo();
		QueryBean queryBean = new QueryBean("playerID", QueryType.EQUAL, playerID);
		queryInfo.addQueryBean(queryBean);
		QueryBean queryBean2 = new QueryBean("shardID",QueryType.EQUAL,shardID);
		queryInfo.addQueryBean(queryBean2);
		TalismanShard talismanShard1 = this.query(queryInfo);
		return talismanShard1;
	}
	
	public List<TalismanShard> searchTalismanShard(String playerID,int shardID, int level){
		if(level < 12){
			return new ArrayList<>();
		}
		int minLevel = level - 15;
		int maxLevel = level + 15;
		if(minLevel < 1){
			minLevel = 0;
		}
		if(minLevel < 12){
			minLevel = 12;
		}
		QueryInfo queryInfo = new QueryInfo();
		QueryBean qb1 = new QueryBean("shardID",QueryType.EQUAL,shardID);
		QueryBean qb2 = new QueryBean("level",QueryType.GREATERTHAN_OR_EQUAL,minLevel);
		QueryBean qb3 = new QueryBean("level",QueryType.LESSTHAN_OR_EQUAL,maxLevel);
		//QueryBean qb4 = new QueryBean("playerID",QueryType.NOT_EQUAL,playerID);
		queryInfo.addQueryBean(qb1);
		queryInfo.addQueryBean(qb2);
		queryInfo.addQueryBean(qb3);
		//queryInfo.addQueryBean(qb4);
		List<TalismanShard> list = this.queryList(queryInfo);
		return list;
	}
}
