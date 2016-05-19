package com.mi.game.module.login.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.login.pojo.PlayerStatusEntity;

public class PlayerStatuesEntityDAO extends AbstractBaseDAO<PlayerStatusEntity>{
	private static final PlayerStatuesEntityDAO PLAYER_STATUES_ENTITY_DAO = new PlayerStatuesEntityDAO();
	private PlayerStatuesEntityDAO(){}
	public static PlayerStatuesEntityDAO getInstance(){
		return PLAYER_STATUES_ENTITY_DAO;
	}
}
