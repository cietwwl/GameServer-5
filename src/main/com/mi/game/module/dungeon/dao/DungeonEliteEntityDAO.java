package com.mi.game.module.dungeon.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.dungeon.pojo.DungeonEliteEntity;

public class DungeonEliteEntityDAO extends AbstractBaseDAO<DungeonEliteEntity>{
	private static final DungeonEliteEntityDAO dao = new DungeonEliteEntityDAO();
	private DungeonEliteEntityDAO(){}
	public static DungeonEliteEntityDAO getInstance(){
		return dao;
	}
}
