package com.mi.game.module.manual.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.manual.pojo.HeroManualsEntity;

public class HeroManualDAO extends AbstractBaseDAO<HeroManualsEntity>{
	private static final HeroManualDAO HEROMANUAL_DAO = new HeroManualDAO();
	private HeroManualDAO(){}
	public static HeroManualDAO getInstance(){
		return HEROMANUAL_DAO;
	}
}
