package com.mi.game.module.mainTask.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.mainTask.pojo.MainTaskEntity;

public class MainTaskEntityDAO extends AbstractBaseDAO<MainTaskEntity>{
	private static final MainTaskEntityDAO MAIN_TASK_ENTITY_DAO = new MainTaskEntityDAO();
	private MainTaskEntityDAO(){}
	public static MainTaskEntityDAO getInstance(){
		return MAIN_TASK_ENTITY_DAO;
	}
	
}
