package com.mi.game.module.bag.dao;

import com.mi.core.cache.QueryInfo;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.bag.pojo.BagEntity;

public class BagEntityDAO extends AbstractBaseDAO<BagEntity>{
	private static final BagEntityDAO bagEntityDAO = new BagEntityDAO();
	private BagEntityDAO(){}
	public static BagEntityDAO getInstance(){
		return bagEntityDAO;
	}
	public long getBagCount(){
		QueryInfo queryInfo = new QueryInfo("playerID");
		long total = cache.queryCount(queryInfo,BagEntity.class);
		return total;
	}
}
