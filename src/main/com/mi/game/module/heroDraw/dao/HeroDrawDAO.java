package com.mi.game.module.heroDraw.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.heroDraw.pojo.HeroDrawEntity;

public class HeroDrawDAO extends AbstractBaseDAO<HeroDrawEntity>{
	private static final HeroDrawDAO heroDrawDAO  =  new HeroDrawDAO();
	private HeroDrawDAO(){}
	public static HeroDrawDAO getInstance(){
		return heroDrawDAO;
	}
}
