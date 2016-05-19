package com.mi.game.module.dungeon.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.dungeon.pojo.DungeonActiveEntity;

public class DungeonActiveDAO extends AbstractBaseDAO<DungeonActiveEntity>{
	private static final DungeonActiveDAO dungeonActiveDAO = new DungeonActiveDAO();
	private DungeonActiveDAO(){}
	public static DungeonActiveDAO getInstance(){
		return dungeonActiveDAO;
	}
}
