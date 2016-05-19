package com.mi.game.module.dungeon.dao;

import java.util.List;

import com.mi.core.cache.QueryInfo;
import com.mi.core.cache.QueryType;
import com.mi.core.cache.bean.QueryBean;
import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.dungeon.pojo.DungeonMapEntity;

public class DungeonMapEntityDAO extends AbstractBaseDAO<DungeonMapEntity>{
	private static final DungeonMapEntityDAO dungeonMapEntity = new DungeonMapEntityDAO();
	private DungeonMapEntityDAO(){}
	
	public static  DungeonMapEntityDAO getInstance(){
		return dungeonMapEntity;
	}
	
	
	public List<DungeonMapEntity> getTop50List(){
		QueryInfo queryInfo = new QueryInfo("-starNum,lastAddStarlNumTime");
		QueryBean queryBean = new QueryBean("starNum", QueryType.GREATERTHAN, 0);
		queryInfo.addQueryBean(queryBean);
		queryInfo.setSize(50);
		List<DungeonMapEntity> dungeonList = cache.queryPage(queryInfo, DungeonMapEntity.class);
		return dungeonList;
	}
}
