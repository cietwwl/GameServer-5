package com.mi.game.module.event.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.event.pojo.WarGodRankEntity;

public class WarGodEntityDao extends AbstractBaseDAO<WarGodRankEntity>{
	private static final WarGodEntityDao WAR_GOD_ENTITY_DAO = new WarGodEntityDao();
	private  WarGodEntityDao(){}
	public static WarGodEntityDao getInstance(){
		return WAR_GOD_ENTITY_DAO;
	}
}
