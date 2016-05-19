package com.mi.game.module.hero.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.hero.pojo.HeroSkinEntity;

public class HeroSkinEntityDAO extends AbstractBaseDAO<HeroSkinEntity>{
	private final static HeroSkinEntityDAO HEROS_SKIN_ENTITY_DAO = new HeroSkinEntityDAO();
	private HeroSkinEntityDAO(){}
	public static HeroSkinEntityDAO getInstance(){
		return HEROS_SKIN_ENTITY_DAO;
	}
	
}
