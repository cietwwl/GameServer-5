package com.mi.game.module.festival.dao;

import com.mi.core.dao.AbstractBaseDAO;
import com.mi.game.module.festival.pojo.ValentineEntity;

public class ValentineEntityDao extends AbstractBaseDAO<ValentineEntity>{
	private static final ValentineEntityDao newYearFirecrackerEntityDao = new ValentineEntityDao();
	
	private ValentineEntityDao(){}
	
	public static ValentineEntityDao getInstance(){
		return newYearFirecrackerEntityDao;
	}
}
