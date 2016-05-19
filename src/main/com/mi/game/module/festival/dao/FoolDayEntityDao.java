package com.mi.game.module.festival.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.festival.pojo.FoolDayEntity;

public class FoolDayEntityDao extends AbstractBaseDAO<FoolDayEntity>{
	private static final FoolDayEntityDao FOOL_DAY_ENTITY_DAO = new FoolDayEntityDao();
	private FoolDayEntityDao(){
		
	}
	
	public static FoolDayEntityDao getInstance(){
		return FOOL_DAY_ENTITY_DAO;
	}
}
