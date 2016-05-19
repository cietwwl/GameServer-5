package com.mi.game.module.hero.dao;

import com.mi.game.module.hero.pojo.HeroTroopsEntity;
import com.mi.core.dao.AbstractBaseDAO;

public class HeroTroopsDAO extends AbstractBaseDAO<HeroTroopsEntity>{
	private final static HeroTroopsDAO heroTroopsDAO = new HeroTroopsDAO();
	private HeroTroopsDAO(){}
	public static HeroTroopsDAO getInstance(){
		return heroTroopsDAO;
	}
}
