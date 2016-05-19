package com.mi.game.module.hero.dao;

import java.util.ArrayList;
import java.util.List;

import com.mi.core.cache.CacheFactory;
import com.mi.core.cache.ICache;
import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.hero.pojo.HeroEntity;

public class HeroDAO extends AbstractBaseDAO<HeroEntity> {
	private final static HeroDAO heroDAO = new HeroDAO();
	static ICache cache = CacheFactory.getICache();
	
	private HeroDAO(){}

	public static HeroDAO getInstance() {
		return heroDAO;
	}

	public List<HeroEntity> getEntityInList(List<Object> list) {
		QueryInfo queryInfo = new QueryInfo();
		queryInfo.addMultipleQueryCondition("playerID", QueryType.IN, list);
		List<HeroEntity> heroList = cache.queryList(queryInfo, HeroEntity.class);
		return heroList;
	}

	/**
	 * 获取经验值最高的size人
	 * 
	 * @return
	 */
	public List<String> getExpRankList(int size) {
		List<String> players = new ArrayList<String>();
		QueryInfo queryInfo = new QueryInfo("-heroMap.10431.exp");
		queryInfo.setSize(size);
		List<HeroEntity> heroList = cache.queryPage(queryInfo, HeroEntity.class);
		for (HeroEntity hero : heroList) {
			players.add(hero.getKey().toString());
		}
		return players;
	}

}
