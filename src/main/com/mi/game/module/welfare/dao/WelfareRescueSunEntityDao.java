package com.mi.game.module.welfare.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.welfare.pojo.WelfareRescueSunEntity;

public class WelfareRescueSunEntityDao extends AbstractBaseDAO<WelfareRescueSunEntity>{
	private static final WelfareRescueSunEntityDao WELFARE_RESCUE_SUN_ENTITY_DAO= new WelfareRescueSunEntityDao();
	private WelfareRescueSunEntityDao(){
		
	}
	public static WelfareRescueSunEntityDao getInstance(){
		return WELFARE_RESCUE_SUN_ENTITY_DAO;
	}
}
