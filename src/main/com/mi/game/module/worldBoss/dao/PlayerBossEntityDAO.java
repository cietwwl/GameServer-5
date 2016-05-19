package com.mi.game.module.worldBoss.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.worldBoss.pojo.PlayerBossEntity;

public class PlayerBossEntityDAO extends AbstractBaseDAO<PlayerBossEntity>{
	
	private final static PlayerBossEntityDAO bossEntityDAO = new PlayerBossEntityDAO();
	private PlayerBossEntityDAO(){}
	public static PlayerBossEntityDAO getInstance(){
		return bossEntityDAO;
	}



	
}
