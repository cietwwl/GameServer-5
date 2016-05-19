package com.mi.game.module.worldBoss.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.worldBoss.pojo.TopTenListEntity;

public class TopTenListEntityDAO extends AbstractBaseDAO<TopTenListEntity>{
	private static final TopTenListEntityDAO topTenListEntityDAO = new TopTenListEntityDAO();
	TopTenListEntityDAO(){}
	public static TopTenListEntityDAO getInstance(){
		return topTenListEntityDAO;
	}
}
