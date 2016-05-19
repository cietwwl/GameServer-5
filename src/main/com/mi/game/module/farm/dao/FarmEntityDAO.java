package com.mi.game.module.farm.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.farm.pojo.FarmEntity;

public class FarmEntityDAO extends AbstractBaseDAO<FarmEntity>{
	private static final FarmEntityDAO FARM_ENTITY_DAO = new FarmEntityDAO();
	FarmEntityDAO(){}
	public static FarmEntityDAO getInstance(){
		return FARM_ENTITY_DAO;
	}
}
