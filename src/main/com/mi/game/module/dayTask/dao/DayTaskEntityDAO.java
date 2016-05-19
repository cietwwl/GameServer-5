package com.mi.game.module.dayTask.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.dayTask.pojo.DayTaskEntity;

public class DayTaskEntityDAO extends AbstractBaseDAO<DayTaskEntity>{
	private final static DayTaskEntityDAO DAY_TASK_ENTITY_DAO = new DayTaskEntityDAO();
	DayTaskEntityDAO(){}
	public static DayTaskEntityDAO getInstance(){
		return DAY_TASK_ENTITY_DAO;
	}
}
