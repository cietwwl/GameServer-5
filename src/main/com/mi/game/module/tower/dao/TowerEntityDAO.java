package com.mi.game.module.tower.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.tower.pojo.TowerEntity;

public class TowerEntityDAO extends AbstractBaseDAO<TowerEntity>{
	private static final TowerEntityDAO TOWER_ENTITY_DAO = new TowerEntityDAO();
	private TowerEntityDAO(){}
	public static TowerEntityDAO getInstance(){
		return TOWER_ENTITY_DAO;
	}
	
	public List<TowerEntity> getTopList(){
		QueryInfo queryInfo = new QueryInfo("-passLevel,lastAddTowerTime");
		QueryBean queryBean = new QueryBean("passLevel", QueryType.GREATERTHAN, 0);
		queryInfo.addQueryBean(queryBean);
		queryInfo.setSize(50);
		List<TowerEntity> towerList = cache.queryPage(queryInfo, TowerEntity.class);
		return towerList;
	}
}
