package com.mi.game.module.dungeon.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.dungeon.pojo.DungeonActMapEntity;

public class DungeonActMapEntityDAO extends AbstractBaseDAO<DungeonActMapEntity>{
	
	private static final DungeonActMapEntityDAO dungeonActMapEntityDAO = new DungeonActMapEntityDAO();
	
	private DungeonActMapEntityDAO(){}
	
	public static DungeonActMapEntityDAO getInstance(){
		return dungeonActMapEntityDAO;
	}

}
