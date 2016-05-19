package com.mi.game.module.login.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.login.pojo.NewPlayerEntity;

public class NewPlayerEntityDAO extends AbstractBaseDAO<NewPlayerEntity>{
	private final static NewPlayerEntityDAO newPlayerEntityDAO = new NewPlayerEntityDAO();
	private NewPlayerEntityDAO(){}
	public static   NewPlayerEntityDAO getInstance(){
		return newPlayerEntityDAO;
	}
}
