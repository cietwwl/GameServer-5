package com.mi.game.module.reward.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.reward.pojo.SystemGetListEntity;

public class SystemGetListEntityDAO extends AbstractBaseDAO<SystemGetListEntity>{
	private static final SystemGetListEntityDAO  SYSTEM_GET_LIST_ENTITY_DAO = new SystemGetListEntityDAO();
	SystemGetListEntityDAO(){}
	public static SystemGetListEntityDAO getInstance(){
		return SYSTEM_GET_LIST_ENTITY_DAO;
	}
 }
