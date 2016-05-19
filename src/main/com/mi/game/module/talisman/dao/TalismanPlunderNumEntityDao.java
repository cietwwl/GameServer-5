package com.mi.game.module.talisman.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.talisman.pojo.PlunderNumEntity;

public class TalismanPlunderNumEntityDao extends AbstractBaseDAO<PlunderNumEntity>{
	private static final TalismanPlunderNumEntityDao TALISMAN_PLUNDER_NUM_ENTITY_DAO = new TalismanPlunderNumEntityDao(); 
	private TalismanPlunderNumEntityDao(){}
	public static TalismanPlunderNumEntityDao getInstance(){
		return TALISMAN_PLUNDER_NUM_ENTITY_DAO;
	}
}
