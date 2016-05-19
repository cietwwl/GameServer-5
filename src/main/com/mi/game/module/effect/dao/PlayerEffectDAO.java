package com.mi.game.module.effect.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.effect.pojo.PlayerEffectEntity;

public class PlayerEffectDAO extends AbstractBaseDAO<PlayerEffectEntity>{
	private static final PlayerEffectDAO playerEffectDAO = new PlayerEffectDAO();
	public static PlayerEffectDAO getInstance(){
		return playerEffectDAO;
	}
}
