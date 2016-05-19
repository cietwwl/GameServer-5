package com.mi.game.module.vitatly.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.vitatly.pojo.VitatlyEntity;

public class VitatlyDAO extends AbstractBaseDAO<VitatlyEntity>{
	private static final  VitatlyDAO vitatlyDAO = new VitatlyDAO();
	private VitatlyDAO(){}
	
	public static  VitatlyDAO getInstance(){
		return vitatlyDAO;
	}
}

